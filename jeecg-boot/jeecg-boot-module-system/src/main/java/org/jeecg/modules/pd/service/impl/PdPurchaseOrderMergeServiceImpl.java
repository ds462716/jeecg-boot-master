package org.jeecg.modules.pd.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdPurchaseDetailMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMergeDetailMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMergeMapper;
import org.jeecg.modules.pd.service.IPdPurchaseOrderMergeService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.util.HttpUtil;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    private PdPurchaseDetailMapper pdPurchaseDetailMapper;
    @Autowired
    private PdPurchaseOrderMergeMapper pdPurchaseOrderMergeMapper;
    @Autowired
    private PdPurchaseOrderMergeDetailMapper pdPurchaseOrderMergeDetailMapper;
    @Autowired
    private IPdPurchaseOrderService pdPurchaseOrderService;
    @Autowired
    private SqlSession sqlSession;
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
        //insert合并明细表并计算出订单总数量
      Double orderCount=  this.insertMergeDetail(orderNos,mergeOrderNo);
        pdPurchaseOrderMerge.setOrderCount(orderCount);
        final String ifPlatform ="1"; //是否有中心平台标识1=没有，0=有
        if("0".equals(ifPlatform)){
            //同步到中心平台2020年3月17日
            PdPurchaseOrder order = new PdPurchaseOrder();
            order.setMergeOrderNo(pdPurchaseOrderMerge.getMergeOrderNo());
            List<PdConsumablesOrder> pdConsumablesOrders = getConsumeOrder(order);
            //有数据再同步
            try{
                if(pdConsumablesOrders!=null && pdConsumablesOrders.size()>0){
                    String jsonPar = JSONArray.toJSONString(pdConsumablesOrders);
                    //接口地址待确认
                    String base_url="http://119.23.56.26:8088/spd-CP/admin";
                    String consumables_order_url="/hys/pdConsumablesOrder/saveConsumablesOrder";
                    JSONObject json = HttpUtil.httpPost(base_url + consumables_order_url, jsonPar);
                    if(json.containsKey("statusCode")){
                        if(PdConstant.SYNC_STATE_SUCCESS.equals(json.get("statusCode"))){
                            //成功
                            pdPurchaseOrderMerge.setSupplierStatus(PdConstant.PURCHASE_ORDER_SUPPLIER_STATUS_1);//已上传
                        }else{
                            //失败
                            pdPurchaseOrderMerge.setSupplierStatus(PdConstant.PURCHASE_ORDER_SUPPLIER_STATUS_0);//待上传
                        }
                    }else{
                        //失败
                        pdPurchaseOrderMerge.setSupplierStatus(PdConstant.PURCHASE_ORDER_SUPPLIER_STATUS_0);//待上传
                    }
                }else{
                    //失败
                    pdPurchaseOrderMerge.setSupplierStatus(PdConstant.PURCHASE_ORDER_SUPPLIER_STATUS_0);//待上传
                }
            }catch(Exception e){
                e.printStackTrace();
                pdPurchaseOrderMerge.setSupplierStatus(PdConstant.PURCHASE_ORDER_SUPPLIER_STATUS_0);//待上传
            }
        }
        pdPurchaseOrderMergeMapper.insert(pdPurchaseOrderMerge);

    }

   public Double insertMergeDetail(String orderNostr,String mergeOrderNo){
       //查询订单下产品信息
       Double orderCount=0.00;//订单总数量
       PdPurchaseDetail ppd = new PdPurchaseDetail();
       List<String> orderNos = Arrays.asList(orderNostr.split(","));
       ppd.setOrderNos(orderNos);
       List<PdPurchaseDetail> ppdList = pdPurchaseDetailMapper.queryPdPurchaseDetail(ppd);
         if(CollectionUtils.isNotEmpty(ppdList)){
             //处理订单下产品
             List<PdPurchaseDetail> finalProdList = dealRepeatProdInfo(ppdList);
             PdPurchaseOrderMergeDetailMapper dao= sqlSession.getMapper(PdPurchaseOrderMergeDetailMapper.class);
             for(PdPurchaseDetail entity:finalProdList) {
                 PdPurchaseOrderMergeDetail  orderMergeDetail =new  PdPurchaseOrderMergeDetail();
                 orderMergeDetail.setProductId(entity.getProductId());
                 orderMergeDetail.setOrderNum(entity.getOrderNum());
                 orderMergeDetail.setMergeOrderNo(mergeOrderNo);
                 List<String> list=entity.getOrderNos();
                 String str= StringUtils.join(list.toArray(), ",");
                 orderMergeDetail.setOrderNo(str);
                 orderMergeDetail.setSupplierId(entity.getSupplierId());
                 orderCount+=entity.getOrderNum();
                BigDecimal purchasePrice=entity.getPurchasePrice();
                 if(ObjectUtils.isNotEmpty(purchasePrice)){
                     orderMergeDetail.setPurchasePrice(purchasePrice);
                     orderMergeDetail.setPrice(purchasePrice.multiply(new BigDecimal(entity.getOrderNum())));
                 }
                 dao.insert(orderMergeDetail);
               //pdPurchaseOrderMergeDetailMapper.insert(orderMergeDetail);
             }
         }
         return orderCount;
   }



    /**
     *  此数据通过定时任务表同步到中心平台
     */
    @Transactional(readOnly = false)
    public List<PdConsumablesOrder> getConsumeOrder(PdPurchaseOrder order) {
        if (null == order)
            return null;
        order.setAuditStatus(PdConstant.AUDIT_STATE_2);
        List<PdPurchaseOrder> poList =pdPurchaseOrderMapper.selectList(order);
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        HashSet<String> oNos = new HashSet<String>();
        for (PdPurchaseOrder pdPurchaseOrder : poList) {
            Map<String, String> tempMap = new HashMap<String, String>();
            if (oNos.contains(pdPurchaseOrder.getMergeOrderNo())) {
                continue;
            }
            String tempOns = "";
            for (PdPurchaseOrder pdOrder : poList) {
                if (pdPurchaseOrder.getMergeOrderNo().equals(pdOrder.getMergeOrderNo())) {
                    tempOns += "," + pdOrder.getOrderNo();//2018年9月26日14:20:44
                    oNos.add(pdPurchaseOrder.getMergeOrderNo());
                }
            }
            tempMap.put("orderNo", pdPurchaseOrder.getMergeOrderNo());
            tempMap.put("purchaseNo", tempOns);
            tempMap.put("orderDate", DateUtils.formatTime(pdPurchaseOrder.getAuditDate()));
            mapList.add(tempMap);
        }

        List<PdConsumablesOrder> pdConsumablesOrderList = new ArrayList<PdConsumablesOrder>();
        Iterator<Map<String, String>> ite = mapList.iterator();
        while (ite.hasNext()) {
            Map<String, String> wgMap = ite.next();
            //订单信息
            PdConsumablesOrder pdConsumablesOrder = new PdConsumablesOrder();
            pdConsumablesOrder.setNumber(wgMap.get("orderNo"));//订单号
            String hospital_number="GZDW";//医院编号
            //pdConsumablesOrder.setHospital(Global.getConfig("HOSPITAL_NUMBER"));
            pdConsumablesOrder.setHospital(hospital_number);
            List<String> qOrderNos = Arrays.asList(wgMap.get("purchaseNo").split(","));
            PdPurchaseOrder po = pdPurchaseOrderMapper.getCountAndMoney(qOrderNos);
            pdConsumablesOrder.setOrderQuantity(po.getTotalNum());//订单总数量
            pdConsumablesOrder.setOrderAmount(po.getTotalPrice());//订单总金额
            pdConsumablesOrder.setOrderDate(wgMap.get("orderDate"));

            //查询订单下产品信息
            PdPurchaseDetail ppd = new PdPurchaseDetail();
            ppd.setOrderNos(qOrderNos);
            List<PdPurchaseDetail> ppdList = pdPurchaseDetailMapper.queryPdPurchaseDetail(ppd);
            //处理订单下产品
            List<PdPurchaseDetail> finalProdList = dealRepeatProdInfo(ppdList);

            List<PdConsumablesOrderDetail> pdConsumablesOrderDetails = new ArrayList<PdConsumablesOrderDetail>();
            for (PdPurchaseDetail pdPurchaseDetail : finalProdList) {

                PdConsumablesOrderDetail pdConsumablesOrderDetail = new PdConsumablesOrderDetail();
                BigDecimal sprice = pdPurchaseDetail.getPurchasePrice();
                if (sprice!=null) {
                    pdConsumablesOrderDetail.setPrice(sprice);
                    pdConsumablesOrderDetail.setAmount(sprice.multiply(new BigDecimal(pdPurchaseDetail.getOrderNum())));
                }
                pdConsumablesOrderDetail.setNumber(pdPurchaseDetail.getNumber());
                pdConsumablesOrderDetail.setOrderQuantity(pdPurchaseDetail.getOrderNum());
                pdConsumablesOrderDetail.setSupplierName(pdPurchaseDetail.getSupplierName());
                pdConsumablesOrderDetails.add(pdConsumablesOrderDetail);
            }
            pdConsumablesOrder.setPdConsumablesOrderDetails(pdConsumablesOrderDetails);
            pdConsumablesOrderList.add(pdConsumablesOrder);
        }

        return pdConsumablesOrderList;
    }

    //重复产品处理
    public List<PdPurchaseDetail> dealRepeatProdInfo(List<PdPurchaseDetail> ppdList ) {
        List<PdPurchaseDetail> newArray = new ArrayList<PdPurchaseDetail>();
        HashSet<String> tempSet = new HashSet<String>();
        for(PdPurchaseDetail detail : ppdList){
            Double total_count = 0.00;
            List<String> orderNos=new ArrayList<String>();
            if(tempSet.contains(detail.getNumber())){
                continue;
            }
            for(PdPurchaseDetail det : ppdList){
                if (detail.getNumber().equals(det.getNumber())) {
                    tempSet.add(det.getNumber());
                    total_count += det.getOrderNum();

                    if(!orderNos.contains(det.getOrderNo())){
                        orderNos.add(det.getOrderNo());
                    }

                }
            }
            detail.setOrderNum(total_count);
            detail.setOrderNos(orderNos);
            newArray.add(detail);
        }
        return newArray;
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
