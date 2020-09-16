package org.jeecg.modules.external.cxf;

import javax.jws.WebParam;
import javax.jws.WebService;
/***
 * 耗材智能柜接口
 * */
@WebService
public interface WebServiceService {
    /**
     * 耗材柜注册接口
     * @param str
     * @return
     */
    public String sendConsumablesToSpd(@WebParam(name = "consumables") String str);
    /**
     * 获取耗材柜信息
     * @param str
     * @return
     */
    public String queryForcerInfoList(@WebParam(name = "forcerInfo") String str);

    /**
     * 获取指纹\人脸信息接口
     * @param str
     * @return
     */
    public String queryUserFingerFaceList(@WebParam(name = "userFingerFace") String str);
    /**
     * 注册指纹\人脸信息接口
     * @param str
     * @return
     */
    public String sendFngrprtFaceRgst(@WebParam(name = "fngrprtFaceRgst") String str);

    /**
     * 条码打印接口
     * @param str
     * @return
     */
    public String sendPrintCodeInterface(@WebParam(name = "printCodeInterface") String str);

    /**
     * 根据rfid标签获取库存信息
     * @param str
     * @return
     */
    public String queryHrfidList(@WebParam(name = "hrfidList") String str);
    /**
     * 保存耗材柜rfid接口标签信息接口
     * @param str
     * @return
     */
    public String saveRfidToSpd(@WebParam(name = "rfidToSpd") String str);

    /**
     * 获取耗材柜rfid标签接口
     * @param str
     * @return
     */
    public String queryHforcerRfidList(@WebParam(name = "hforcerRfidList") String str);
    /**
     * 耗材柜人员登录接口
     * @param str
     * @return
     */
    public String staffLoginRegister(@WebParam(name = "staffLogin") String str);
    /**
     * 根据UserID获取人员信息接口
     * @param str
     * @return
     */
    public String queryUserToSpd(@WebParam(name = "queryUser") String str);

    /**
     * 获取库存信息接口
     * @param str
     * @return
     */
    public String queryPdProductStockList(@WebParam(name = "pdProductStock") String str);

    /**
     * 获取仓库列表信息接口
     * @param str
     * @return
     */
    public String queryPdStoreroomList(@WebParam(name = "pdStoreroomList") String str);

  /*  *//**
     * 获取入库单信息接口
     * @param str
     * @return
     *//*
    public String queryPdStockList(@WebParam(name = "pdStockList") String str);*/

    /**
     * 耗材柜出库单接口
     * @param str
     * @return
     */
    public String sendOutboundOrderToSpd(@WebParam(name = "outboundOrder") String str);


    /**
     * 唯一码清除接口
     * @param str
     * @return
     */
    public String deleteRefBerCode(@WebParam(name = "deleteRefBerCode") String str);

}
