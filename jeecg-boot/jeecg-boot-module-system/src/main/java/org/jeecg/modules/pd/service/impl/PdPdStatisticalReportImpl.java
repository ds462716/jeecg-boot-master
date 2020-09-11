package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdStatisticalReportMapper;
import org.jeecg.modules.pd.service.IPdStatisticalReportService;
import org.jeecg.modules.pd.vo.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 空实现
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
@Service
public class PdPdStatisticalReportImpl extends ServiceImpl<PdStatisticalReportMapper, PdStatisticalReport> implements IPdStatisticalReportService {

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
        map.put("dataSource1",list_1);
        map.put("visitFields1",str);
        return map;
    }

    /**
     *  综合统计报表    全院耗材占比数据查
     * @param purchaseUseReportPage
     * @return
     */
    @Override
    public List<RpPurchaseUseReportPage> queryConsumptionView(RpPurchaseUseReportPage purchaseUseReportPage) {
        return baseMapper.queryConsumptionView(purchaseUseReportPage);
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
        resultMap.put("dataSource5",contionList);
        //收费金额占比
        List<RpPurchaseUseReportPage> chargeList= baseMapper.queryDepartChargeView(purchaseUseReportPage);
        resultMap.put("dataSource6",chargeList);
        List<RpPurchaseUseReportPage> list= baseMapper.queryDepartPurchaseCountView(purchaseUseReportPage);
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","采购金额");
        Map<String,Object> map2=new HashMap<>();
        map2.put("name","收费金额");
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
        if(!"".equals(purchaseUseReportPage.getYearMonth()) && purchaseUseReportPage.getYearMonth()!=null){
            List<RpPurchaseUseReportPage> pdPurchaseAmountMomReportPages = baseMapper.queryPurchaseAmountMomTableView(purchaseUseReportPage);
        }else{
            //获得当前月的上个月
           // String date_i = DateUtils.getLastMonth(1);
            String date_i = "2020-08";
            purchaseUseReportPage.setYearMonth(date_i);
            List<RpPurchaseUseReportPage> pdPurchaseAmountMomReportPages = baseMapper.queryPurchaseAmountMomTableView(purchaseUseReportPage);
            //获得当前月的上上个月
            purchaseUseReportPage = new RpPurchaseUseReportPage();
           // String date_ii = DateUtils.getLastMonth(2);
            String date_ii = "2020-07";
            purchaseUseReportPage.setYearMonth(date_ii);
            List<RpPurchaseUseReportPage> pdPurchaseAmountMomReportPages_i = baseMapper.queryPurchaseAmountMomTableView(purchaseUseReportPage);
            //获得横坐标
            List<String> xAxis = new ArrayList<>();
            List<String> legends = new ArrayList<>();
            legends.add(date_i);
            legends.add(date_ii);
            legends.add("环比");
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
                String series_hb = "0%";
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
                if(!"0".equals(amout_ii)){
                    BigDecimal bd1=new BigDecimal(amout_i);
                    BigDecimal bd2=new BigDecimal(amout_ii);
                    series_hb = String.format("%.2f", (bd1.doubleValue()-bd2.doubleValue())/bd2.doubleValue()*100);
                }else{
                    series_hb = "100";
                }
                seriesData3.add(series_hb);
            }
            resultMap.put("seriesData1",seriesData1);
            resultMap.put("seriesData2",seriesData2);
            resultMap.put("seriesData3",seriesData3);
            resultMap.put("xAxis",xAxis);
            resultMap.put("legends",legends);
        }
        return resultMap;
    }
}
