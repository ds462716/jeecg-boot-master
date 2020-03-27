package org.jeecg.modules.pd.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 首页
 * @Author: mcb
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/IndexChart")
@Slf4j
public class IndexChartController {

    @Autowired
    private IPdPurchaseOrderService pdPurchaseOrderService;
    /*
    * 需要统计的数据
    * 1.总采购量
    * 1.1  今日采购量
    * */
    @GetMapping(value = "/list")
    public Result<?> queryPageAuditList(Map<String,Object> params){
        Map map=new HashMap();
        Double orderCount=0.00;//总采购量
        Double applyCount=0.00;//总申领数量
        Double dosageCount=0.00;//总使用量
        Double stockCount=0.00;//总库存数量
        Double dayOrderNum=0.00; //今日采购量
        Double dayApplyNum=0.00;//今日申领量
        Double dayDosageNum=0.00;//今日使用量
        Double dayStockNum=0.00;//今日入库量
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();



        PdPurchaseOrder purchaseOrder=new  PdPurchaseOrder();
        purchaseOrder.setOrderDate(new Date());
        purchaseOrder.setDepartId(sysUser.getCurrentDepartId());
        purchaseOrder.setDepartParentId(sysUser.getDepartParentId());
        //Map<String,Object> objectMap=pdPurchaseOrderService.queryPurchaseOrderCount(purchaseOrder);
        //orderCount=orderList.size();
        return Result.ok(map);
    }

    /*
    *
    * 采购金额消耗统计
     *根据类别和月份统计
     *
     * 采购数量消耗统计
     * 根据类别统计和月份
     *
     * 2.总申领数量
    * 2.1 今日领用数量
    *
    *
    * 3.总使用量
    * 3.1 今日使用量
    *
    *
    * 4.总库存数量
    *4.1 今日入库数量
    *
    *
    *
    *
    * */

  /*  @GetMapping(value = "/list")
    public Result<?> queryPageList() {
        Map map=new HashMap();
        map.put("orderCount","50001");//总采购量
        map.put("applyCount","20001");//总申领数量
        map.put("dosageCount","30001");//总使用量
        map.put("stockCount","66001");//总库存数量
        return Result.ok(map);
    }*/
}

