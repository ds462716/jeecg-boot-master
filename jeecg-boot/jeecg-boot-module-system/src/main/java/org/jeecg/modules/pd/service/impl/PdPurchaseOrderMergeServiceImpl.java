package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMergeMapper;
import org.jeecg.modules.pd.service.IPdPurchaseOrderMergeService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description: 合并申购单表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Service
public class PdPurchaseOrderMergeServiceImpl extends ServiceImpl<PdPurchaseOrderMergeMapper, PdPurchaseOrderMerge> implements IPdPurchaseOrderMergeService {
    @Autowired
    private PdPurchaseOrderMapper pdPurchaseOrderMapper;
    @Autowired
    private PdPurchaseOrderMergeMapper pdPurchaseOrderMergeMapper;
    @Autowired
    private IPdPurchaseOrderService pdPurchaseOrderService;

    /**
     * 查询列表
     * @param page
     * @param purchaseOrderMerge
     * @return
     */
    @Override
    public Page<PdPurchaseOrderMerge> selectList(Page<PdPurchaseOrderMerge> page, PdPurchaseOrderMerge purchaseOrderMerge) {
        return page.setRecords(pdPurchaseOrderMergeMapper.selectList(purchaseOrderMerge));
    }

    @Override
    @Transactional(readOnly = false)
    public void saveMergeOrder(String orderNos, LoginUser  sysUser) {
        PdPurchaseOrderMerge pdPurchaseOrderMerge = new PdPurchaseOrderMerge();
        pdPurchaseOrderMerge.setPurchaseDate(new Date());
        pdPurchaseOrderMerge.setMergeBy(sysUser.getId());
        pdPurchaseOrderMerge.setDepartId(sysUser.getCurrentDepartId());
        pdPurchaseOrderMerge.setAuditStatus(PdConstant.AUDIT_STATE_2);
        String mergeOrderNo = UUIDUtil.generateOrderNoByType("HB");
        pdPurchaseOrderMerge.setMergeOrderNo(mergeOrderNo);
        //更新pd_purchase_order表中merge_order_no
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("mergeOrderNo",mergeOrderNo);
        map.put("updateBy", sysUser.getId());
        DateUtils.getDate();
        map.put("updateTime",DateUtils.getDate());
        map.put("orderNos", PdPurchaseOrder.dealStrData(orderNos));
        if(StringUtils.isEmpty(orderNos)){
            throw new NullArgumentException("申购单号为空！！！");
        }
        pdPurchaseOrderMapper.batchUpdateMergeOrderNo(map);
        pdPurchaseOrderMergeMapper.insert(pdPurchaseOrderMerge);

    }


    @Override
    @Transactional(readOnly = false)
    public void  submitOrderInfo(PdPurchaseOrderMerge orderMerge,LoginUser sysUser) {
        String auditStatus= orderMerge.getAuditStatus();//审核状态 2：审核通过
        String orderNos= orderMerge.getOrderNos();
        String oprtSource= orderMerge.getOprtSource();
        String refuseReason= orderMerge.getRefuseReason();
        String submitStatus= orderMerge.getSubmitStatus();
        //批量更新审核状态等信息
       int changeNum = pdPurchaseOrderService.audit(orderNos, auditStatus, refuseReason,submitStatus);
        if (changeNum > 0) {
            if (PdConstant.AUDIT_STATE_2.equals(auditStatus)) { //审核通过
                if ("2".equals(oprtSource)) {//合并提交的请求
                    this.saveMergeOrder(orderNos, sysUser);
                } else if ("1".equals(oprtSource)) {//批量审核
                    String[] strarr = orderNos.split(",");
                    for (String str : strarr) {
                    this.saveMergeOrder(str, sysUser);
                    }
                }
            }
         }

    }
}
