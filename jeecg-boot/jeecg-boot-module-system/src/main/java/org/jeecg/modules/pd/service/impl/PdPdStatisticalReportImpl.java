package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdStatisticalReportMapper;
import org.jeecg.modules.pd.service.IPdStatisticalReportService;
import org.jeecg.modules.pd.vo.*;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Description: 空实现
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
@Service
public class PdPdStatisticalReportImpl extends ServiceImpl<PdStatisticalReportMapper, PdStatisticalReport> implements IPdStatisticalReportService {
    @Autowired
    private SysDepartMapper sysDepartMapper;

    /**
     *供应商用量使用统计
     * @param page
     * @param rpSupplierUseReportPage
     * @return
     */
    @Override
    public IPage<RpSupplierUseReportPage> supplierUseReport(Page<RpSupplierUseReportPage> page, RpSupplierUseReportPage rpSupplierUseReportPage) {
        return baseMapper.supplierUseReport(page,rpSupplierUseReportPage);
    }
    /**
     *供应商用量使用统计
     * @param rpSupplierUseReportPage
     * @return
     */
    @Override
    public List<RpSupplierUseReportPage> supplierUseReport(RpSupplierUseReportPage rpSupplierUseReportPage) {
        return baseMapper.supplierUseReport(rpSupplierUseReportPage);
    }

    /**
     * zxh供应商用量统计查询入库明细
     * @param inPageDetail
     * @param inDetail
     * @return
     */
    @Override
    public IPage<PdStockRecordDetail> supplierInDetailReport(Page<PdStockRecordDetail> inPageDetail, PdStockRecordDetail inDetail) {
        return baseMapper.supplierInDetailReport(inPageDetail,inDetail);
    }

    /**
     * zxh用量明细统计报表
     * @param usePageDetail
     * @param rpUseDetailReportPage
     * @return
     */
    @Override
    public IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, RpUseDetailReportPage rpUseDetailReportPage) {
        return baseMapper.rpUseDetailReport(usePageDetail,rpUseDetailReportPage);
    }
    /**
     * zxh退货明细统计报表
     * @param rePageDetail
     * @param rpReDetailReportPage
     * @return
     */
    @Override
    public IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, RpReDetailReportPage rpReDetailReportPage) {
        return baseMapper.rpReDetailReport(rePageDetail,rpReDetailReportPage);
    }
    /**
     * zxh部门用量使用统计
     * @param page
     * @param rpDepartUseReportPage
     * @return
     */
    @Override
    public IPage<RpDepartUseReportPage> departUseReport(Page<RpDepartUseReportPage> page, RpDepartUseReportPage rpDepartUseReportPage) {
        return baseMapper.departUseReport(page,rpDepartUseReportPage);
    }
    /**
     * zxh部门用量使用统计
     * @param rpDepartUseReportPage
     * @return
     */
    @Override
    public List<RpDepartUseReportPage> departUseReport(RpDepartUseReportPage rpDepartUseReportPage) {
        return baseMapper.departUseReport(rpDepartUseReportPage);
    }
    /**
     * zxh部门用量使用明细
     * @param usePageDetail
     * @param rpDepartUseDetailReportPage
     * @return
     */
    @Override
    public IPage<RpDepartUseDetailReportPage> rpDepartUseDetailReport(Page<RpDepartUseDetailReportPage> usePageDetail, RpDepartUseDetailReportPage rpDepartUseDetailReportPage) {
        return baseMapper.rpDepartUseDetailReport(usePageDetail,rpDepartUseDetailReportPage);
    }

    /**
     * 出入库统计报表查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    @Override
    public IPage<RpInAndOutReportPage> rpInAndOutReport(Page<RpInAndOutReportPage> page, RpInAndOutReportPage entity) {
        return baseMapper.rpInAndOutReport(page,entity);
    }

    /**
     * 出入库统计报表查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    @Override
    public List<RpInAndOutReportPage> rpInAndOutReport(RpInAndOutReportPage entity) {
        return baseMapper.rpInAndOutReport(entity);
    }

    /**
     * 出入库统计报表明细查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    @Override
    public IPage<RpInAndOutDetailReportPage> rpInAndOutDetailReport(Page<RpInAndOutDetailReportPage> page, RpInAndOutDetailReportPage entity) {
        return baseMapper.rpInAndOutDetailReport(page,entity);
    }

    /**
     * 出入库统计报表明细查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    @Override
    public List<RpInAndOutDetailReportPage> rpInAndOutDetailReport(RpInAndOutDetailReportPage entity) {
        return baseMapper.rpInAndOutDetailReport(entity);
    }


    /**
     * zxh库存统计报表
     * @param page
     * @param rpDepartStockReportPage
     * @return
     */
    @Override
    public IPage<RpDepartStockReportPage> departStockReport(Page<RpDepartStockReportPage> page, RpDepartStockReportPage rpDepartStockReportPage) {
        return baseMapper.departStockReport(page,rpDepartStockReportPage);
    }
    /**
     * zxh库存统计报表
     * @param rpDepartStockReportPage
     * @return
     */
    @Override
    public List<RpDepartStockReportPage> departStockReport(RpDepartStockReportPage rpDepartStockReportPage) {
        return baseMapper.departStockReport(rpDepartStockReportPage);
    }
    /**
     * zxh库存统计报表
     * @param rpDepartStockDetailReportPage
     * @return
     */
    @Override
    public IPage<RpDepartStockDetailReportPage> rpDepartStockDetailReport(Page<RpDepartStockDetailReportPage> stockPageDetail, RpDepartStockDetailReportPage rpDepartStockDetailReportPage) {
        return baseMapper.rpDepartStockDetailReport(stockPageDetail,rpDepartStockDetailReportPage);
    }
    /**
     * zxh供应商试剂使用报表
     * @param page
     * @param rpSupplierUseReportPage
     * @return
     */
    @Override
    public IPage<RpSupplierUseReportPage> supplierReagentUseReport(Page<RpSupplierUseReportPage> page, RpSupplierUseReportPage rpSupplierUseReportPage) {
        return baseMapper.supplierReagentUseReport(page,rpSupplierUseReportPage);
    }
    /**
     * zxh供应商试剂使用报表详情
     * @param usePageDetail
     * @param rpReagentUseDetailReportPage
     * @return
     */
    @Override
    public IPage<RpReagentUseDetailReportPage> rpReagentUseDetailReport(Page<RpReagentUseDetailReportPage> usePageDetail, RpReagentUseDetailReportPage rpReagentUseDetailReportPage) {
        return baseMapper.rpReagentUseDetailReport(usePageDetail,rpReagentUseDetailReportPage);
    }

    /**
     *  综合统计报表  采购收费对照表数据获取
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public Map<String, Object> queryPurchaseCountView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String,Object> map=new HashMap<String, Object>();
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            purchaseUseReportPage.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        List<RpPurchaseUseReportPage>  list= baseMapper.queryPurchaseCountView(purchaseUseReportPage);
        //数据格式
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","采购金额");
        Map<String,Object> map2=new HashMap<>();
        map2.put("name","收费金额");
        Map<String,Object> map3=new HashMap<>();
        map3.put("name","不可收费金额");
        List<String> str = new ArrayList<>();
        List<Map> list_1=new ArrayList<>();
        List<Double> list3=new ArrayList<>();
        List<Double> list4=new ArrayList<>();
        List<Double> list5=new ArrayList<>();
        for(RpPurchaseUseReportPage info:list){
            String type=info.getType();
            list3.add(info.getY());
            list4.add(info.getX());
            list5.add(info.getS());
            str.add(type);
        }
        map1.put("data",list3);
        map2.put("data",list4);
        map3.put("data",list5);
        list_1.add(map1);
        list_1.add(map2);
        list_1.add(map3);
        map.put("dataSource1",list_1);
        map.put("visitFields1",str);
        return map;
    }

    /**
     *  综合统计报表    全院耗材占比数据查询
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public Map<String, Object> queryConsumptionView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String,Object> resultMap = new HashMap<>();
        List<RpPurchaseUseReportPage> list= baseMapper.queryConsumptionView(purchaseUseReportPage);
        List<String> str = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(RpPurchaseUseReportPage info:list){
                str.add(info.getName());
            }
        }
        resultMap.put("dataSource8",list);
        resultMap.put("visitFields7",str);
        return resultMap;
    }
    /**
     *  综合统计报表    采购收费柱状图(根据科室统计)
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public Map<String, Object> queryDepartPurchaseCountView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String,Object> resultMap = new HashMap<>();
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            purchaseUseReportPage.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        //采购科室金额占比
        List<RpPurchaseUseReportPage> contionList= baseMapper.queryDepartContionView(purchaseUseReportPage);
        List<String> str1 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(contionList)){
        for(RpPurchaseUseReportPage info:contionList){
            str1.add(info.getName());
           }
        }
        resultMap.put("dataSource5",contionList);
        resultMap.put("visitFields5",str1);
        //收费金额占比
        List<RpPurchaseUseReportPage> chargeList= baseMapper.queryDepartChargeView(purchaseUseReportPage);
        List<String> str2 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(chargeList)){
            for(RpPurchaseUseReportPage info:chargeList){
                str2.add(info.getName());
            }
        }
        resultMap.put("dataSource6",chargeList);
        resultMap.put("visitFields6",str2);
        List<RpPurchaseUseReportPage> list= baseMapper.queryDepartPurchaseCountView(purchaseUseReportPage);
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","采购金额");
        Map<String,Object> map2=new HashMap<>();
        map2.put("name","收费金额");
        Map<String,Object> map3=new HashMap<>();
        map3.put("name","不可收费金额");
        List<String> str = new ArrayList<>();
        List<Map> list_1=new ArrayList<>();
        List<Double> list3=new ArrayList<>();
        List<Double> list4=new ArrayList<>();
        List<Double> list5=new ArrayList<>();
        for(RpPurchaseUseReportPage info:list){
            //采购金额
            String type=info.getType();
            list3.add(info.getY());
            list4.add(info.getX());
            list5.add(info.getS());
            str.add(type);
        }
        map1.put("data",list3);
        map2.put("data",list4);
        map3.put("data",list5);
        list_1.add(map1);
        list_1.add(map2);
        list_1.add(map3);
        resultMap.put("dataSource3",list_1);
        resultMap.put("visitFields2",str);
        return resultMap;
    }



    /**
     *  zxh采购环比
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public Map<String, Object> queryPurchaseAmountMomTableView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String,Object> resultMap = new HashMap<>();
        String date_i ="";//当前月的上个月
        String date_ii ="";//当前月的上上个月
        String selectType=purchaseUseReportPage.getSelectType();
        try {
            if(StringUtils.isNotEmpty(purchaseUseReportPage.getYearMonth())) {
                if("1".equals(selectType)){
                    //获得选择月份和对应上个月的值
                    date_i = DateUtils.getLastMonth(0,purchaseUseReportPage.getYearMonth());
                    date_ii = DateUtils.getLastMonth(12,purchaseUseReportPage.getYearMonth());
                }else{
                    date_i = DateUtils.getLastMonth(0,purchaseUseReportPage.getYearMonth());
                    date_ii = DateUtils.getLastMonth(1,purchaseUseReportPage.getYearMonth());
                }

            }else {
                if("1".equals(selectType)) {
                    date_i = DateUtils.getLastMonth(1, null);
                    date_ii = DateUtils.getLastMonth(13, null);
                }else{
                    date_i = DateUtils.getLastMonth(1, null);
                    date_ii = DateUtils.getLastMonth(2, null);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        purchaseUseReportPage.setYearMonth(date_i);
        purchaseUseReportPage.setDepartType("2");
        List<RpPurchaseUseReportPage>  pdPurchaseAmountMomReportPages = baseMapper.queryPurchaseAmountMomTableView(purchaseUseReportPage);
            //获得当前月的上上个月
            purchaseUseReportPage = new RpPurchaseUseReportPage();
             purchaseUseReportPage.setYearMonth(date_ii);
            purchaseUseReportPage.setDepartType("2");
        List<RpPurchaseUseReportPage> pdPurchaseAmountMomReportPages_i = baseMapper.queryPurchaseAmountMomTableView(purchaseUseReportPage);
            //获得横坐标
            List<String> xAxis = new ArrayList<>();
            List<String> legends = new ArrayList<>();
            legends.add(date_i);
            legends.add(date_ii);
        if("0".equals(selectType)) {
            legends.add("环比");
        }else{
            legends.add("同比");
        }
            //取上个月的所有库房
            for(RpPurchaseUseReportPage ps :pdPurchaseAmountMomReportPages){
                xAxis.add(ps.getDepartName());
            }
            //取上上个月的所有库房
            for(RpPurchaseUseReportPage ps_i :pdPurchaseAmountMomReportPages_i){
                if(!xAxis.contains(ps_i.getDepartName())){
                    xAxis.add(ps_i.getDepartName());
                }
            }
            //获得上个月的金额
            List<String> seriesData1 = new ArrayList<>();
            //获得上上个月的金额
            List<String> seriesData2 = new ArrayList<>();
            //获得环比
            List<String> seriesData3 = new ArrayList<>();
            Iterator<String> it = xAxis.iterator();
            while (it.hasNext()) {
                String str = it.next();
                //取得上个月的金额数据
                //记录当前金额
                //上个月金额
                String amout_i = "0";
                String amout_ii = "0";
                String series_hb = "0";
                for(RpPurchaseUseReportPage ps :pdPurchaseAmountMomReportPages){
                    if(ps.getDepartName().equals(str)){
                        amout_i = ps.getAmount();
                        break;
                    }
                }
                //如果当前科室当前月没有则补0
                seriesData1.add(amout_i);
                //取得上上个月的金额数据
                for(RpPurchaseUseReportPage ps_i :pdPurchaseAmountMomReportPages_i){
                    if(ps_i.getDepartName().equals(str)){
                        amout_ii = ps_i.getAmount();
                        break;
                    }
                }
                //如果当前科室当前月没有则补0
                seriesData2.add(amout_ii);
                //获得环比率
                if(!"0".equals(amout_ii) && !"0.00".equals(amout_ii)){
                    BigDecimal bd1=new BigDecimal(amout_i);
                    BigDecimal bd2=new BigDecimal(amout_ii);
                    series_hb = String.format("%.2f", (bd1.doubleValue()-bd2.doubleValue())/bd2.doubleValue()*100);
                }else{
                    if("0".equals(amout_i) || "0.00".equals(amout_i)){
                        series_hb = "0";
                    }else{
                        series_hb = "100";
                    }

                }
                seriesData3.add(series_hb);
            }
            resultMap.put("seriesData1",seriesData1);
            resultMap.put("seriesData2",seriesData2);
            resultMap.put("seriesData3",seriesData3);
            resultMap.put("xAxis",xAxis);
            resultMap.put("legends",legends);
        return resultMap;
    }



    /**
     *  综合统计报表 检验收入金额及采购金额数据获取
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public Map<String, Object> queryItemMoneyCountView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String,Object> map=new HashMap<String, Object>();
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            purchaseUseReportPage.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        List<RpPurchaseUseReportPage>  list= baseMapper.queryItemMoneyCountView(purchaseUseReportPage);
        //数据格式
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","采购金额");
        Map<String,Object> map2=new HashMap<>();
        map2.put("name","检验收入金额");
        List<String> str = new ArrayList<>();
        List<Map> list_1=new ArrayList<>();
        List<Double> list3=new ArrayList<>();
        List<Double> list4=new ArrayList<>();
        for(RpPurchaseUseReportPage info:list){
            String type=info.getType();
            list3.add(info.getY());
            list4.add(info.getX());
            str.add(type);
        }
        map1.put("data",list3);
        map2.put("data",list4);
        list_1.add(map1);
        list_1.add(map2);
        map.put("dataSource",list_1);
        map.put("visitFields",str);
        return map;
    }



    /**
     *  科室采购环比-同比报表
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public IPage<RpPurchaseUseReportPage> queryMoOnMoView(Page<RpPurchaseUseReportPage> page,RpPurchaseUseReportPage purchaseUseReportPage) {
        String date_i = "";//当前月的上个月
        String date_ii = "";//当前月的上上个月
        String selectType = purchaseUseReportPage.getSelectType();
        try {
            if (StringUtils.isNotEmpty(purchaseUseReportPage.getYearMonth())) {
                if ("1".equals(selectType)) {
                    //获得选择月份和对应上个月的值
                    date_i = DateUtils.getLastMonth(0, purchaseUseReportPage.getYearMonth());
                    date_ii = DateUtils.getLastMonth(12, purchaseUseReportPage.getYearMonth());
                } else {
                    date_i = DateUtils.getLastMonth(0, purchaseUseReportPage.getYearMonth());
                    date_ii = DateUtils.getLastMonth(1, purchaseUseReportPage.getYearMonth());
                }
            } else {
                if ("1".equals(selectType)) {
                    date_i = DateUtils.getLastMonth(1, null);
                    date_ii = DateUtils.getLastMonth(13, null);
                } else {
                    date_i = DateUtils.getLastMonth(1, null);
                    date_ii = DateUtils.getLastMonth(2, null);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        purchaseUseReportPage.setLastMonth(date_i);
        purchaseUseReportPage.setTheLastYearMonth(date_ii);
        List<RpPurchaseUseReportPage>  pageList=null;
        IPage<RpPurchaseUseReportPage> pages =null;
        if (page!=null) {
              pages = baseMapper.queryMoOnMoView(page, purchaseUseReportPage);
              pageList=pages.getRecords();
        }else {
            pageList = baseMapper.queryMoOnMoView(purchaseUseReportPage);
            pages.setRecords(pageList);
        }
        if(CollectionUtils.isNotEmpty(pageList)){
            for(RpPurchaseUseReportPage info:pageList){
              Double sPrice=  info.getLastPrice();//上个月采购金额
              Double ssPrice=  info.getTheLastPrice();//上上个月采购金额
                String series_hb="";
                if(ssPrice !=0 && ssPrice !=0.00){
                    series_hb = String.format("%.2f", (sPrice -ssPrice) / ssPrice * 100);
                }else{
                    if(sPrice==0 || sPrice==0.00){
                        series_hb = "0";
                    }else{
                        series_hb = "100";
                    }
                }
                if("1".equals(selectType)){
                    info.setYrOnYr(series_hb+"%");//同比
                }else{
                    info.setMoOnMo(series_hb+"%");//同比
                }
            }
        }
        return pages;
    }


    /**
     *  科室采购环比-同比报表
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public List<RpPurchaseUseReportPage> queryMoOnMoView(RpPurchaseUseReportPage purchaseUseReportPage) {
        String date_i = "";//当前月的上个月
        String date_ii = "";//当前月的上上个月
        String selectType = purchaseUseReportPage.getSelectType();
        try {
            if (StringUtils.isNotEmpty(purchaseUseReportPage.getYearMonth())) {
                if ("1".equals(selectType)) {
                    //获得选择月份和对应上个月的值
                    date_i = DateUtils.getLastMonth(0, purchaseUseReportPage.getYearMonth());
                    date_ii = DateUtils.getLastMonth(12, purchaseUseReportPage.getYearMonth());
                } else {
                    date_i = DateUtils.getLastMonth(0, purchaseUseReportPage.getYearMonth());
                    date_ii = DateUtils.getLastMonth(1, purchaseUseReportPage.getYearMonth());
                }
            } else {
                if ("1".equals(selectType)) {
                    date_i = DateUtils.getLastMonth(1, null);
                    date_ii = DateUtils.getLastMonth(13, null);
                } else {
                    date_i = DateUtils.getLastMonth(1, null);
                    date_ii = DateUtils.getLastMonth(2, null);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        purchaseUseReportPage.setLastMonth(date_i);
        purchaseUseReportPage.setTheLastYearMonth(date_ii);
        List<RpPurchaseUseReportPage>  pageList = baseMapper.queryMoOnMoView(purchaseUseReportPage);
        if(CollectionUtils.isNotEmpty(pageList)){
            for(RpPurchaseUseReportPage info:pageList){
                Double sPrice=  info.getLastPrice();//上个月采购金额
                Double ssPrice=  info.getTheLastPrice();//上上个月采购金额
                String series_hb="";
                if(ssPrice !=0 && ssPrice !=0.00){
                    series_hb = String.format("%.2f", (sPrice -ssPrice) / ssPrice * 100);
                }else{
                    if(sPrice==0 || sPrice==0.00){
                        series_hb = "0";
                    }else{
                        series_hb = "100";
                    }
                }
                if("1".equals(selectType)){
                    info.setYrOnYr(series_hb+"%");//同比
                }else{
                    info.setMoOnMo(series_hb+"%");//同比
                }
            }
        }
        return pageList;
    }



    /**
     *  获取前12个月的年月值
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public List<RpPurchaseUseReportPage> getYearMonth(RpPurchaseUseReportPage purchaseUseReportPage) {
        return baseMapper.getYearMonth(purchaseUseReportPage);
     }
    /**
     *  根据月份获取各个科室的采购金额，收费金额，不可收费金额数据
     * @param purchaseMoneyReportPage
     * @return
     */
    @Override
    public IPage<RpPurchaseMoneyReportPage> queryMonthMoneyList(Page<RpPurchaseMoneyReportPage> page,RpPurchaseMoneyReportPage purchaseMoneyReportPage) {
        return baseMapper.queryMonthMoneyList(page,purchaseMoneyReportPage);
    }


    /**
     *  (不分页)根据月份获取各个科室的采购金额，收费金额，不可收费金额数据
     * @param purchaseMoneyReportPage
     * @return
     */
    @Override
    public List<RpPurchaseMoneyReportPage> queryMonthMoneyList(RpPurchaseMoneyReportPage purchaseMoneyReportPage) {
        return baseMapper.queryMonthMoneyList(purchaseMoneyReportPage);
    }


    /**
     *  科室领用情况统计报表
     * @param departApplyPage
     * @return
     */
    @Override
    public IPage<RpDepartApplyPage> departApplyUseReportPage(Page<RpDepartApplyPage> page,RpDepartApplyPage departApplyPage) {
        return baseMapper.departApplyUseReportPage(page,departApplyPage);
    }

    /**
     *  (不分页)科室领用情况统计报表
     * @param departApplyPage
     * @return
     */
    @Override
    public List<RpDepartApplyPage> departApplyUseReportList(RpDepartApplyPage departApplyPage) {
        return baseMapper.departApplyUseReportPage(departApplyPage);
    }
}
