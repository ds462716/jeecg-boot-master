package org.jeecg.modules.pd.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.vo.IndexChartPage;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdPurchaseOrderService pdPurchaseOrderService;
    @Autowired
    private IPdApplyOrderService pdApplyOrderService;
    @Autowired
    private IPdProductStockService pdProductStockService;


    /*
     * 需要统计的数据
     * 1.总采购量
     * 1.1  今日采购量
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
     * 3.总使用量
     * 3.1 今日使用量
     *
     * 4.总库存数量
     *4.1 今日入库数量
     */

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
        Double dayRecordNum=0.00;//今日入库量
        List<Map> orderDate=new  ArrayList<Map>();//根据天数统计的采购数量
        List<Map> applyDate=new  ArrayList<Map>();//根据天数统计的申领数量
        List<Map> dosageDate=new  ArrayList<Map>();//根据天数统计的使用数量
        List<Map> recordDate=new  ArrayList<Map>();//根据天数统计的入库数量

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询科室下所有下级科室的ID
        SysDepart depart=new SysDepart();
        List<String> departList=pdDepartService.selectListDepart(depart);
        //采购量统计
        PdPurchaseOrderPage purchaseOrderPage=new  PdPurchaseOrderPage();
        purchaseOrderPage.setOrderDate(new Date());
        purchaseOrderPage.setDepartIdList(departList);
        purchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
        Map<String,Object> orderMap=pdPurchaseOrderService.queryPurchaseOrderCount(purchaseOrderPage);
        orderCount=MapUtils.getDouble(orderMap,"orderCount");
        dayOrderNum=MapUtils.getDouble(orderMap,"dayOrderNum");
        //申领量统计
        PdApplyOrderPage applyOrderPage=new  PdApplyOrderPage();
        applyOrderPage.setApplyDate(new Date());
        applyOrderPage.setDepartIdList(departList);
        applyOrderPage.setDepartParentId(sysUser.getDepartParentId());
        Map<String,Object> applyMap=pdApplyOrderService.queryApplyOrderCount(applyOrderPage);
        applyCount=MapUtils.getDouble(applyMap,"applyCount");
        dayApplyNum=MapUtils.getDouble(applyMap,"dayApplyNum");
        //库存量统计
        PdProductStock stock=new  PdProductStock();
        applyOrderPage.setApplyDate(new Date());
        stock.setDepartIdList(departList);
        stock.setDepartParentId(sysUser.getDepartParentId());
        Map<String,Object> stockMap=pdProductStockService.queryProductStockCount(stock);
        stockCount=MapUtils.getDouble(stockMap,"stockCount");

          //统计入库单数量
        //dayRecordNum=MapUtils.getDouble(stockMap,"dayRecordNum");

         //使用量统计


        map.put("orderCount",orderCount);
        map.put("dayOrderNum",dayOrderNum);
        map.put("applyCount",applyCount);
        map.put("stockCount",stockCount);
        map.put("dayApplyNum",dayApplyNum);
        map.put("dosageCount","30");
        map.put("dayDosageNum","38");
        map.put("dayRecordNum","88");
       // map.put("orderDate",orderDate);
        return Result.ok(map);
    }

    /**
     * 获取采购七天内的日采购数量
     * @param params
     * @return
     */
    @GetMapping(value = "/orderDateList")
    public Result<?> orderDateList(Map<String,Object> params){
        Map map=new HashMap();
        List<HashMap> orderDate=new  ArrayList<HashMap>();//根据天数统计的采购数量
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询科室下所有下级科室的ID
        SysDepart depart=new SysDepart();
        List<String> departList=pdDepartService.selectListDepart(depart);
        PdPurchaseOrderPage purchaseOrderPage=new  PdPurchaseOrderPage();
        //purchaseOrderPage.setDepartIdList(departList);
        //purchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
        //根据日期统计每日的采购量
        orderDate=pdPurchaseOrderService.queryPurchaseOrderDateList(purchaseOrderPage);
        map.put("orderDate",orderDate);
        return Result.ok(map);
    }


    /**
     * 获取七天内的日申领数量
     * @param params
     * @return
     */
    @GetMapping(value = "/applyDateList")
    public Result<?> applyDateList(Map<String,Object> params){
        Map map=new HashMap();
        List<HashMap> applyDate=new  ArrayList<HashMap>();//根据天数统计的申领数量
       // List<Map> dosageDate=new  ArrayList<Map>();//根据天数统计的使用数量
        // List<Map> recordDate=new  ArrayList<Map>();//根据天数统计的入库数量

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询科室下所有下级科室的ID
        SysDepart depart=new SysDepart();
        List<String> departList=pdDepartService.selectListDepart(depart);
        PdApplyOrderPage applyOrderPage=new  PdApplyOrderPage();
        //pdApplyOrderPage.setDepartIdList(departList);
        //pdApplyOrderPage.setDepartParentId(sysUser.getDepartParentId());
        //根据日期统计每日的申领量
        applyDate=pdApplyOrderService.queryApplyOrderDateList(applyOrderPage);
        map.put("applyDate",applyDate);
        return Result.ok(map);
    }


    /**
     * 获取七天内的日耗材使用数量
     * @param params
     * @return
     */
    @GetMapping(value = "/dosageDateList")
    public Result<?> dosageDateList(Map<String,Object> params){
        Map map=new HashMap();
        List<HashMap> dosageDate=new  ArrayList<HashMap>();//根据天数统计的使用数量

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询科室下所有下级科室的ID
        SysDepart depart=new SysDepart();
        List<String> departList=pdDepartService.selectListDepart(depart);
        PdApplyOrderPage applyOrderPage=new  PdApplyOrderPage();
        //pdApplyOrderPage.setDepartIdList(departList);
        //pdApplyOrderPage.setDepartParentId(sysUser.getDepartParentId());
        //根据日期统计每日的申领量
        dosageDate=pdApplyOrderService.queryApplyOrderDateList(applyOrderPage);
        map.put("dosageDate",dosageDate);
        return Result.ok(map);
    }

    /**
     * 获取七天内的日入库数量
     * @param params
     * @return
     */
    @GetMapping(value = "/recordDateList")
    public Result<?> stockDateList(Map<String,Object> params){
        Map map=new HashMap();
         List<HashMap> stockDate=new  ArrayList<HashMap>();//根据天数统计的入库数量

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询科室下所有下级科室的ID
        SysDepart depart=new SysDepart();
        List<String> departList=pdDepartService.selectListDepart(depart);
        PdProductStock productStock=new  PdProductStock();
        //productStock.setDepartIdList(departList);
        //productStock.setDepartParentId(sysUser.getDepartParentId());
        //根据日期统计每日的申领量
        stockDate=pdProductStockService.queryStockDateList(productStock);
        map.put("stockDate",stockDate);
        return Result.ok(map);
    }



    /**
     * 根据类别统计数量和金额
     * @param chartPage
     * @return
     */
    @GetMapping(value = "/orderTotalList")
    public Result<?> orderTotalList(IndexChartPage chartPage, HttpServletRequest req){
        String type=chartPage.getType();
        Map map=new HashMap();
        List<HashMap> orderCountDate=new  ArrayList<HashMap>();//根据类别统计采购数量和金额
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询科室下所有下级科室的ID
        SysDepart depart=new SysDepart();
        List<String> departList=pdDepartService.selectListDepart(depart);
        if("purchase".equals(type)){//采购耗材分类统计
            PdPurchaseOrderPage purchaseOrderPage=new  PdPurchaseOrderPage();
            //purchaseOrderPage.setDepartIdList(departList);
            //purchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
            orderCountDate=pdPurchaseOrderService.queryPurchaseOrderTotalList(purchaseOrderPage);
        }else if("apply".equals(type)){//申领耗材分类统计
            PdApplyOrderPage applyOrderPage=new  PdApplyOrderPage();
            //applyOrderPage.setDepartIdList(departList);
            //applyOrderPage.setDepartParentId(sysUser.getDepartParentId());
            orderCountDate=pdApplyOrderService.queryApplyOrderTotalList(applyOrderPage);
        }else if("dosage".equals(type)){//耗材使用量分类统计


        }else {//库存耗材分类统计
            PdProductStock  stock =new  PdProductStock();
            //stock.setDepartIdList(departList);
            //stock.setDepartParentId(sysUser.getDepartParentId());
            orderCountDate=pdProductStockService.queryStockTotalList(stock);

        }

        map.put("orderCountDate",orderCountDate);
        return Result.ok(map);
    }

}

