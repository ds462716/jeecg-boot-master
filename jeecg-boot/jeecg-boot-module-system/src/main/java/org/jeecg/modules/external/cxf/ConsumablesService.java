package org.jeecg.modules.external.cxf;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 市立医院试剂库房一体机相关接口
 */
@WebService
public interface ConsumablesService {
    /**
     * 一体机注册接口
     * @param str
     * @return
     */
    public String sendConsumablesToSpd(@WebParam(name = "consumables") String str);
    /**
     * 获取一体机信息
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
     * 一体机人员登录接口
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


    /**
     * 试剂出入库接口
     * @param str
     * @return
     */
    public String reagentOutToInRecord(@WebParam(name = "outToInRecord") String str);
    /**
     * 根据唯一码获取产品信息
     * @param str
     * @return
     */
    public String sendRefBarCode(@WebParam(name = "queryProduct") String str);

}
