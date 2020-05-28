package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdRejectedDetailMapper;
import org.jeecg.modules.pd.mapper.PdRejectedMapper;
import org.jeecg.modules.pd.mapper.PdSupplierMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Service
public class PdRejectedServiceImpl extends ServiceImpl<PdRejectedMapper, PdRejected> implements IPdRejectedService {

    @Autowired
    private PdRejectedMapper pdRejectedMapper;
    @Autowired
    private PdRejectedDetailMapper pdRejectedDetailMapper;
    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private PdSupplierMapper pdSupplierMapper;
    @Autowired
    private IPdStockLogService pdStockLogService;
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IPdRejectedDetailService pdRejectedDetailService;
    @Autowired
    private IPdProductStockUniqueCodeService pdProductStockUniqueCodeService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(PdRejected pdRejected, List<PdRejectedDetail> pdRejectedDetailList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setRejectedDate(DateUtils.getDate("yyyy-MM-dd"));
        pdRejected.setDepartId(sysUser.getCurrentDepartId());
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        pdRejected.setRejectedType(PdConstant.REJECTED_TYPE_1);//普通码退货
        pdRejected.setId(UUIDUtil.getUuid());//普通码退货

        if(CollectionUtils.isNotEmpty(pdRejectedDetailList)) {
            //总数量
            BigDecimal totalSum = new BigDecimal(0);
            for (PdRejectedDetail detail : pdRejectedDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRejectedId(pdRejected.getId());
                BigDecimal count = new BigDecimal(detail.getRejectedCount());
                totalSum = count.add(totalSum);
            }
            pdRejected.setTotalSum(totalSum.doubleValue());
            pdRejectedMapper.insert(pdRejected);
            pdRejectedDetailService.saveBatch(pdRejectedDetailList);

            // 处理退货库存
            String inStr = pdProductStockTotalService.updateRejectedStock(pdRejected);

            if(!PdConstant.TRUE.equals(inStr)){
                throw new RuntimeException(inStr);
            }

            // 保存退货日志记录
            this.saveStockLog(pdRejected);
        }else{
            throw new RuntimeException("扣减库存失败,没有产品记录");
        }
    }

    /**
     * 唯一码出库
     * @param pdRejected
     * @param pdRejectedDetailList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uniqueSubmit(PdRejected pdRejected, List<PdRejectedDetail> pdRejectedDetailList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setRejectedDate(DateUtils.getDate("yyyy-MM-dd"));
        pdRejected.setDepartId(sysUser.getCurrentDepartId());
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        pdRejected.setRejectedType(PdConstant.REJECTED_TYPE_0);//唯一码退货
        pdRejected.setId(UUIDUtil.getUuid());//普通码退货

        if(CollectionUtils.isNotEmpty(pdRejectedDetailList)) {
            //批量修改条码状态作废条码
            List<PdProductStockUniqueCode> productStockUniqueCodes = new ArrayList<>();
            //总数量
            BigDecimal totalSum = new BigDecimal(0);
            for (PdRejectedDetail detail : pdRejectedDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRejectedId(pdRejected.getId());
                PdProductStockUniqueCode pdProductStockUniqueCode = new PdProductStockUniqueCode();
                pdProductStockUniqueCode.setId(detail.getRefBarCode());
                pdProductStockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_1);//已退货状态
                productStockUniqueCodes.add(pdProductStockUniqueCode);
                BigDecimal count = new BigDecimal(detail.getRejectedCount());
                totalSum = count.add(totalSum);
            }
            pdRejected.setTotalSum(totalSum.doubleValue());
            pdRejectedMapper.insert(pdRejected);
            //批量保存退货详情
            pdRejectedDetailService.saveBatch(pdRejectedDetailList);
            //批量更新条码状态
            pdProductStockUniqueCodeService.updateBatchById(productStockUniqueCodes);
            //合并相同的用量
            List<PdRejectedDetail> newRed = dealRepeatData(pdRejected.getPdRejectedDetailList());
            pdRejected.setPdRejectedDetailList(newRed);
            // 处理退货库存
            String inStr = pdProductStockTotalService.updateRejectedStock(pdRejected);
            if(!PdConstant.TRUE.equals(inStr)){
                throw new RuntimeException(inStr);
            }
            // 保存退货日志记录
            this.saveStockLog(pdRejected);
        }else{
            throw new RuntimeException("扣减库存失败,没有产品记录");
        }
    }

    //合并相同的库存
    public List<PdRejectedDetail> dealRepeatData(final List<PdRejectedDetail> list){
        List<PdRejectedDetail> tempArray = new ArrayList<>();
        Set<String> pids = new HashSet<>();
        if(list != null && list.size() > 0){
            for(PdRejectedDetail temp : list){
                if (temp == null || StringUtils.isEmpty(temp.getProductStockId())) {
                    continue;
                }
                String sb = temp.getProductStockId();
                if(pids.contains(sb)){
                    continue;
                }
                BigDecimal totalCount = new BigDecimal(0);
                for(PdRejectedDetail tp : list){
                    if ( tp != null) {
                        if(sb.equals(tp.getProductStockId())){
                            pids.add(sb);
                            BigDecimal count = new BigDecimal(tp.getRejectedCount());
                            totalCount = count.add(totalCount);
                        }
                    }
                }
                temp.setRejectedCount(totalCount.doubleValue());
                tempArray.add(temp);
            }
        }
        return tempArray;
    }

    @Override
    public List<PdRejected> queryList(PdRejected pdRejected) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        return pdRejectedMapper.selectList(pdRejected);
    }

    @Override
    public IPage<PdRejected> queryList(Page<PdRejected> pageList, PdRejected pdRejected) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        return pdRejectedMapper.selectList(pageList,pdRejected);
    }


    /**
     * 保存出入库记录日志
     * @param pdRejected
     */
    private void saveStockLog(PdRejected pdRejected){
        List<PdStockLog> logList = new ArrayList<PdStockLog>();
        PdStockLog stockLog;
        List<PdRejectedDetail> detail = pdRejected.getPdRejectedDetailList();
        SysDepart sysDepart = sysDepartMapper.selectById(pdRejected.getDepartId());
        PdSupplier pdSupplier =  pdSupplierMapper.selectById(pdRejected.getSupplierId());
        for(PdRejectedDetail psd : detail){
            stockLog = new PdStockLog();
            stockLog.setInvoiceNo(pdRejected.getRejectedNo());
            stockLog.setProductId(psd.getProductId());
            stockLog.setProductBarCode(psd.getProductBarCode());
            stockLog.setBatchNo(psd.getBatchNo());
            stockLog.setProductNum(psd.getRejectedCount());
            stockLog.setExpDate(psd.getExpDate());
            stockLog.setInFrom(sysDepart.getDepartName());
            stockLog.setOutTo(pdSupplier.getName());
            stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_7);
            stockLog.setRecordTime(DateUtils.getDate());
            logList.add(stockLog);
        }
        pdStockLogService.saveBatch(logList);
    }
}
