package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdRejected;
import org.jeecg.modules.pd.entity.PdRejectedDetail;
import org.jeecg.modules.pd.entity.PdStockLog;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.mapper.PdRejectedDetailMapper;
import org.jeecg.modules.pd.mapper.PdRejectedMapper;
import org.jeecg.modules.pd.mapper.PdSupplierMapper;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdRejectedService;
import org.jeecg.modules.pd.service.IPdStockLogService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(PdRejected pdRejected, List<PdRejectedDetail> pdRejectedDetailList) {
        Map<String,String> result = new HashMap<>();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setRejectedDate(DateUtils.getDate("yyyy-MM-dd"));
        pdRejected.setDepartId(sysUser.getCurrentDepartId());
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        pdRejectedMapper.insert(pdRejected);

        if(CollectionUtils.isNotEmpty(pdRejectedDetailList)) {
            for (PdRejectedDetail detail : pdRejectedDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRejectedId(pdRejected.getId());
                detail.setDelFlag(PdConstant.DEL_FLAG_0);
                pdRejectedDetailMapper.insert(detail);
            }
        }

        // 处理退货库存
        String inStr = pdProductStockTotalService.updateRejectedStock(pdRejected);

        if(!PdConstant.TRUE.equals(inStr)){
            throw new RuntimeException(inStr);
        }

        // 保存退货日志记录
        this.saveStockLog(pdRejected);
    }

    @Override
    public List<PdRejected> queryList(PdRejected pdRejected) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        return pdRejectedMapper.selectList(pdRejected);
    }

    @Override
    public Page<PdRejected> queryList(Page<PdRejected> pageList, PdRejected pdRejected) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        return pageList.setRecords(pdRejectedMapper.selectList(pdRejected));
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
