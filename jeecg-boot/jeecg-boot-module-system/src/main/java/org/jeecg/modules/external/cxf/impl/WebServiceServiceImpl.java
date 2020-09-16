package org.jeecg.modules.external.cxf.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.cxf.WebServiceService;
import org.jeecg.modules.external.entity.HForcerInfo;
import org.jeecg.modules.external.entity.HForcerRfid;
import org.jeecg.modules.external.entity.HUserFingerFace;
import org.jeecg.modules.external.service.IHForcerRfidService;
import org.jeecg.modules.external.service.IHRfidInfoService;
import org.jeecg.modules.external.service.IHUserFingerFaceService;
import org.jeecg.modules.external.service.IHforcerInfoService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockUniqueCodeService;
import org.jeecg.modules.pd.service.IPdStockRecordService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@WebService(serviceName = "WebServiceService", // 与接口中指定的name一致
        targetNamespace = "http://webservice.business.mixpay.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "org.jeecg.modules.external.cxf.WebServiceService" // 接口地址
)
public class WebServiceServiceImpl implements WebServiceService {
    @Autowired
    private IHforcerInfoService hforcerInfoService;
    @Autowired
    private IHUserFingerFaceService userFingerFaceService;
    @Autowired
    private IHRfidInfoService rfidInfoService;
    @Autowired
    private IHForcerRfidService hForcerRfidService;
    @Autowired
    private IPdStockRecordService pdStockRecordService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IPdProductStockUniqueCodeService pdProductStockUniqueCodeService;

    /**
     * 耗材柜注册接口
     *
     * @param str
     * @return
     */
    @Override
    public String sendConsumablesToSpd(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }

        try {
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String macAddress = MapUtils.getString(map, "macAddress");//mac地址
                String forcerName = MapUtils.getString(map, "forcerName");//柜子名称
                String kfId = MapUtils.getString(map, "kfId");//管理库房
                String sjkfId = MapUtils.getString(map, "sjkfId");//上级库房
                if (StringUtils.isEmpty(macAddress)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "设备地址不能为空");
                    return JSON.toJSONString(retMap);
                }
                if (StringUtils.isEmpty(forcerName)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "设备名称不能为空");
                    return JSON.toJSONString(retMap);
                }
                if (StringUtils.isEmpty(kfId)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "管理库房不能为空");
                    return JSON.toJSONString(retMap);
                }
                if (StringUtils.isEmpty(sjkfId)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "上级库房不能为空");
                    return JSON.toJSONString(retMap);
                }
                //先判断是否已注册；
                boolean state = hforcerInfoService.queryConsumables(macAddress);
                if (!state) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "该设备已经注册");
                    return JSON.toJSONString(retMap);
                }
                hforcerInfoService.saveConsumables(map);
                retMap.put("result", PdConstant.SUCCESS_0);
                retMap.put("message", "设备注册成功");
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "设备注册失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 获取耗材柜信息
     *
     * @param str
     * @return
     */
    @Override
    public String queryForcerInfoList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String macAddress = MapUtils.getString(map, "macAddress");//mac地址
                if (StringUtils.isEmpty(macAddress)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "设备地址不能为空");
                    return JSON.toJSONString(retMap);
                }
                HForcerInfo info = hforcerInfoService.queryForcerList(macAddress);
                if (info == null || info.getId() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "根据设备地址查询不到相应信息！");
                } else {
                    Map<Object, Object> param = new HashMap<>();
                    param.put("id", info.getId());//主键ID
                    param.put("forcerNo", info.getForcerNo());//柜子编号
                    param.put("forcerName", info.getForcerName());//设备名称
                    param.put("createDate", DateUtils.formatDate(info.getCreateTime()));//录入时间
                    param.put("userId", info.getUserId());//用户id
                    param.put("isDisable", info.getIsDisable());
                    //根据库房id获取到库房名称
                    SysDepart sysDepart= pdDepartService.getById(info.getKfId());
                    if (sysDepart != null) {
                        param.put("kfId", sysDepart.getId());
                        param.put("kfName",sysDepart.getDepartName());
                    } else {
                        param.put("kfId", "");
                        param.put("kfName", "");
                    }
                    //根据上级库房id获取到库房名称
                    SysDepart depart= pdDepartService.getById(info.getSjkfId());

                    if (depart != null) {
                        param.put("sjkfId", depart.getId());
                        param.put("sjkfName", depart.getDepartName());
                    } else {
                        param.put("sjkfId", "");
                        param.put("sjkfName", "");
                    }
                    retMap.put("param", param);
                    retMap.put("result",PdConstant.SUCCESS_0);
                    retMap.put("message", "成功");
                }
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("msg", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 获取指纹\人脸信息接口
     *
     * @param str
     * @return
     */
    @Override
    public String queryUserFingerFaceList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }

        try {
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String kfid = MapUtils.getString(map, "kfId");
                String type = MapUtils.getString(map, "type");
                if (StringUtils.isEmpty(type)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "请求参数不能为空！");
                    return JSON.toJSONString(retMap);
                }
                //先根据kfid获取仓库下用户的指纹/人脸信息
                Map<String,Object> parMap = new HashMap<>();
                parMap.put("DEL_FLAG_NORMAL",PdConstant.DEL_FLAG_0);
                parMap.put("currentDepartId",kfid);
                List<SysUser> userList = pdDepartService.findUserList(parMap);
                if (userList == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "获取不到有权限的用户信息");
                    return JSON.toJSONString(retMap);
                }
                //在根据userId查询人脸/指纹信息
                List FingerFaceList = FingerFaceList = new ArrayList<>();
                for (SysUser info : userList) {
                    String userId = info.getId();
                    HUserFingerFace userFingerFace = new HUserFingerFace();
                    userFingerFace.setUserId(userId);
                    userFingerFace.setType(type);
                    List<HUserFingerFace> listInfo = userFingerFaceService.queryHuserFingerFaceList(userFingerFace);
                    for (HUserFingerFace face : listInfo) {
                        Map maps = new HashMap<>();
                        maps.put("userId", face.getUserId());
                        maps.put("image", face.getImage());
                        maps.put("type", face.getType());
                        FingerFaceList.add(maps);
                    }
                }
                retMap.put("param", FingerFaceList);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 注册指纹\人脸信息接口
     *
     * @param str
     * @return
     */
    @Override
    public String sendFngrprtFaceRgst(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }

        try {
            JSONArray arr = JSONObject.parseArray(str);
            if (arr != null && arr.size() > 0) {
                userFingerFaceService.saveList(arr);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }



    /**
     * 耗材柜人员登录接口
     *
     * @param str
     * @return
     */
    @Override
    public String staffLoginRegister(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######耗材柜人员登录:"+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String userName = MapUtils.getString(map, "userNo");
                String passWord = MapUtils.getString(map, "passWord");
                SysUser sysUser = sysUserService.getUserByName(userName);
                if (sysUser == null || sysUser.getPassword() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "用户名或密码错误");
                    return JSON.toJSONString(retMap);
                }
                //校验用户名或密码是否正确
                String userpassword = PasswordUtil.encrypt(userName, passWord, sysUser.getSalt());
                String syspassword = sysUser.getPassword();
                if (!syspassword.equals(userpassword)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "用户名或密码错误");
                    return JSON.toJSONString(retMap);
                } else {
                    //获取关联库房信息
                    Map maps = new HashMap<>();
                    maps.put("userId", sysUser.getId());//用户ID
                    maps.put("userName", sysUser.getUsername());//用户名
                    maps.put("depId", "");//部门ID
                    maps.put("userNo", sysUser.getId());//用户编号
                    List<SysDepart> list = sysUserService.queryUserDeparts(sysUser.getId());
                    List storList = new ArrayList<>();
                    if (list != null && list.size() > 0) {
                        for (SysDepart depart : list) {
                            Map param = new HashMap<>();
                            param.put("kfId", depart.getId());//库房ID
                            param.put("kfName", depart.getDepartName());//库房名称
                            storList.add(param);
                        }
                    }
                    maps.put("kfList", storList);
                    retMap.put("param", maps);
                    retMap.put("result",PdConstant.SUCCESS_0);
                    retMap.put("message", "人员登录成功");
                }
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }

            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "人员登录失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 根据UserID/loginName获取人员信息接口
     *
     * @param str
     * @return
     */
    @Override
    public String queryUserToSpd(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }

        try {
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String userId = MapUtils.getString(map, "userId");
                String loginName = MapUtils.getString(map, "userName");
                if (StringUtils.isEmpty(userId)  && StringUtils.isEmpty(loginName)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "参数异常");
                    return JSON.toJSONString(retMap);
                }
                SysUser user=new SysUser();
                user.setId(userId);
                user.setUsername(loginName);
                SysUser sysUser = sysUserService.getUser(user);
                if (sysUser == null || sysUser.getUsername() == null) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "根据用户id获取不到有效的用户信息");
                    return JSON.toJSONString(retMap);
                }
                Map<Object, Object> param = new HashMap<>();
                param.put("userId", sysUser.getId());//用户ID
                param.put("userName", sysUser.getUsername());//用户名
                param.put("name", sysUser.getRealname());
                param.put("userNo", sysUser.getId());//用户编号
                param.put("depId", "");//部门ID
                SysDepart sysDepart=new SysDepart();
                sysDepart.setParentId(sysUser.getId());
                List<SysDepart> list = sysUserService.queryUserDeparts(sysUser.getId());
                List storList =new ArrayList<>();
                if (list != null && list.size() > 0) {
                    for (SysDepart depart : list) {
                        Map params = new HashMap<>();
                        params.put("kfId", depart.getId());//库房ID
                        params.put("kfName", depart.getDepartName());//库房名称
                        params.put("storeroomType", depart.getDepartType());//库房类型
                        storList.add(params);
                    }
                }
                param.put("kfList", storList);
                retMap.put("param", param);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");

            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }

            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }


    /**
     * 获取仓库列表信息接口
     *
     * @param str
     * @return
     */
    @Override
    public String queryPdStoreroomList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "查询条件为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######获取仓库列表信息接口报文："+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String kfId = MapUtils.getString(map, "kfId");//库房ID
                String kfName = MapUtils.getString(map, "kfName");//库房名称
                SysDepart sysDepart = new SysDepart();
                sysDepart.setId(kfId);
                sysDepart.setDepartName(kfName);
                List<Map<String, Object>> list = pdDepartService.findDepartList(sysDepart);
                retMap.put("param", list);
                retMap.put("result", PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "查询条件不能为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "查询失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 获取库存信息接口
     *
     * @param str
     * @return
     */
    @Override
    public String queryPdProductStockList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }

        try {
            System.out.println("#######获取库存信息接口报文："+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String kfid = MapUtils.getString(map, "kfId");//库房ID
                String productNo = MapUtils.getString(map, "productNo");//产品编号
                String batchNo = MapUtils.getString(map, "batchNo");//产品批次号
                String productVersion = MapUtils.getString(map, "productVersion");//产品型号
                String productSpec = MapUtils.getString(map, "productSpec");//产品规格
                String productName = MapUtils.getString(map, "productName");//产品名称
                String productType = MapUtils.getString(map, "productType");//产品类型
                String recordNo = MapUtils.getString(map, "recordNo");//入库单号
                if (StringUtils.isEmpty(kfid) && StringUtils.isEmpty(recordNo)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "参数不能为空！");
                    return JSON.toJSONString(retMap);
                }
                PdProductStock pdProductStock = new PdProductStock();
                pdProductStock.setDepartId(kfid);//库房id
                pdProductStock.setNumber(productNo);//产品编号
                pdProductStock.setBatchNo(batchNo);//批次号
                pdProductStock.setVersion(productVersion);//产品型号
                pdProductStock.setProductName(productName);//产品名称
                pdProductStock.setSpec(productSpec);//产品规格
                pdProductStock.setProductFlag(productType);//产品类型
                pdProductStock.setRecordNo(recordNo);//入库单号
                List<Map<String, Object>> list = pdProductStockService.queryPdProductStockList(pdProductStock);
                retMap.put("param", list);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }

//*******************************************************************

    /**
     * 条码打印接口
     *
     * @param str
     * @return
     */
    @Override
    public String sendPrintCodeInterface(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######条码打印接口:"+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String rfid = MapUtils.getString(map, "rfId");
                String productId = MapUtils.getString(map, "productId");
                String productNo = MapUtils.getString(map, "productNo");
                String rkmxId = MapUtils.getString(map, "rkmxId");
                String stockId = MapUtils.getString(map, "productStockId");
                String batchNo = MapUtils.getString(map, "batchNo");
                String validDate = MapUtils.getString(map, "validDate");
                //是否在打印标签时加上库存明细ID,
                if (StringUtils.isEmpty(rfid)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "请求参数不能为空！");
                    return JSON.toJSONString(retMap);
                }
                if (StringUtils.isEmpty(productId)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "产品ID不能为空！");
                    return JSON.toJSONString(retMap);
                }
                if (StringUtils.isEmpty(stockId)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "库存明细ID不能为空！");
                    return JSON.toJSONString(retMap);
                }
                /*if (StringUtils.isEmpty(productNo)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "产品编号不能为空！");
                    return JSON.toJSONString(retMap);
                }*/

                /*if (StringUtils.isEmpty(batchNo)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "批次号不能为空！");
                    return JSON.toJSONString(retMap);
                }*/
                rfidInfoService.saveHrfid(map);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            }else{
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 根据rfid标签获取库存信息
     *
     * @param str
     * @return
     */
    @Override
    public String queryHrfidList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "查询条件为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######根据rfid标签获取库存信息接口:"+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                List<Map<String, Object>> list = rfidInfoService.queryHrfidList(map);
                retMap.put("param", list);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "查询条件不能为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "查询失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 保存耗材柜rfid接口标签信息接口
     * @param str
     * @return
     */
    @Override
    public String saveRfidToSpd(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######保存耗材柜rfid接口标签信息接口:"+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                hForcerRfidService.saveList(map);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }

    /**
     * 获取耗材柜rfid标签接口
     *
     * @param str
     * @return
     */
    @Override
    public String queryHforcerRfidList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "查询条件为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######获取耗材柜rfid标签接口:"+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String forcerId = MapUtils.getString(map, "forcerId");//终端设备ID
                String forcerNumber = MapUtils.getString(map, "forcerNumber");//单个柜子标识
                if (StringUtils.isEmpty(forcerId)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "终端设备ID为空！");
                    return JSON.toJSONString(retMap);
                }
                HForcerRfid forcerRfid = new HForcerRfid();
                forcerRfid.setForcerId(forcerId);
                forcerRfid.setForcerNumber(forcerNumber);
                List<Map<String, Object>> list = hForcerRfidService.queryHforcerRfidList(forcerRfid);
                retMap.put("param", list);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "查询条件不能为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "查询失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }







   /* *//**
     * 获取入库单信息接口
     *
     * @param str
     * @return
     *//*
    @Override
    public String queryPdStockList(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "查询条件为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######获取入库单信息接口报文："+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String recordNo = MapUtils.getString(map, "rkdh");//入库单号
                if (StringUtils.isEmpty(recordNo)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "入库单号为空！");
                    return JSON.toJSONString(retMap);
                }
               *//* PdStockRecord stockRecord = new PdStockRecord();
                stockRecord.setRecordNo(recordNo);
                stockRecord.setRecordType(PdConstant.RECODE_TYPE_1);//入库
                stockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);//只查已通过的明细*//*
                //List<Map<String, Object>> list = pdStockRecordService.findOutQueryList(stockRecord);

                PdProductStock productStock=new PdProductStock();
                productStock.setR
                List<Map<String, Object>> list = pdProductStockService.queryPdProductStockList(productStock);
                if (list != null && list.size() > 0) {
                    retMap.put("param", list);
                    retMap.put("result",PdConstant.SUCCESS_0);
                    retMap.put("message", "成功");
                } else {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "获取入库单信息为空");
                }

            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "查询条件不能为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "查询失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }*/

    /**
     * 耗材柜出库单接口
     *
     * @param str
     * @return
     *//*
    @Override
    public String sendOutboundOrderToSpd(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######耗材柜出库单接口报文："+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String outStoreroomId = MapUtils.getString(map, "outStoreroomId");//出库库房ID
                String inStoreroomId = MapUtils.getString(map, "inStoreroomId");//入库库房ID
                String userId = MapUtils.getString(map, "userId");//操作人ID
                if (StringUtils.isEmpty(outStoreroomId) ||
                        StringUtils.isEmpty(inStoreroomId) ||
                        StringUtils.isEmpty(userId)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "出库/入库库房ID或操作人信息不能为空！");
                    return JSON.toJSONString(retMap);
                }
                SysUser  user = sysUserService.getById(userId);
                if (user == null || user.getUsername() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "根据用户id获取不到有效的用户信息");
                    return JSON.toJSONString(retMap);
                }
                PdStockRecord pdStockRecord = new PdStockRecord();
                pdStockRecord.setOutDepartId(outStoreroomId); //出库库房Id
               SysDepart outSysDepart= pdDepartService.getById(outStoreroomId);
                if (outSysDepart == null || outSysDepart.getDepartName() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "根据出库库房Id获取不到有效信息");
                    return JSON.toJSONString(retMap);
                }
                pdStockRecord.setOutType(PdConstant.OUT_TYPE_2);//出库类型  1:科室出库
                pdStockRecord.setSubmitDate(new Date());//出入库时间
                pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);//审核状态
                pdStockRecord.setRemarks("高值耗材柜出/入库单");
                pdStockRecord.setDelFlag(PdConstant.DEL_FLAG_0);
                SysDepart inSysDepart= pdDepartService.getById(inStoreroomId);
                if (inSysDepart == null || inSysDepart.getDepartName() == null) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "根据入库库房Id获取不到有效信息");
                    return JSON.toJSONString(retMap);
                }
                pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2);//出入库类型
                pdStockRecord.setInDepartId(inStoreroomId);
                pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_CK));
                JSONArray orderArr = JSONObject.parseArray(MapUtils.getObject(map, "List").toString());
                List<PdStockRecordDetail> list = JSONArray.parseArray(orderArr.toJSONString(), PdStockRecordDetail.class);
                PdStockRecord pdStockRecords = new PdStockRecord();
                pdStockRecords.setOutDepartId(outStoreroomId); //出库库房Id
                pdStockRecords.setInDepartId(inStoreroomId);
                pdStockRecords.setPdStockRecordDetailList(list);
                // 出库
                pdStockRecordService.addOutForCabinet(pdStockRecord);
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }*/

    /**
     * 耗材柜出库单接口
     *
     * @param str
     * @return
     */
    @Override
    public String sendOutboundOrderToSpd(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######耗材柜出库单接口报文：" + str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String outStoreroomId = MapUtils.getString(map, "outStoreroomId");//出库库房ID
                String inStoreroomId = MapUtils.getString(map, "inStoreroomId");//入库库房ID
                String userId = MapUtils.getString(map, "userId");//操作人ID
                if (StringUtils.isEmpty(outStoreroomId) ||
                        StringUtils.isEmpty(inStoreroomId) ||
                        StringUtils.isEmpty(userId)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "出库/入库库房ID或操作人信息不能为空！");
                    return JSON.toJSONString(retMap);
                }
                SysUser user = sysUserService.getById(userId);
                if (user == null || user.getUsername() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "根据用户id获取不到有效的用户信息");
                    return JSON.toJSONString(retMap);
                }
                SysDepart outSysDepart = pdDepartService.getById(outStoreroomId);
                if (outSysDepart == null || outSysDepart.getDepartName() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "根据出库库房Id获取不到有效信息");
                    return JSON.toJSONString(retMap);
                }
                SysDepart inSysDepart = pdDepartService.getById(inStoreroomId);
                if (inSysDepart == null || inSysDepart.getDepartName() == null) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "根据入库库房Id获取不到有效信息");
                    return JSON.toJSONString(retMap);
                }

                JSONArray orderArr = JSONObject.parseArray(MapUtils.getObject(map, "List").toString());
                List<PdStockRecordDetail> list = JSONArray.parseArray(orderArr.toJSONString(), PdStockRecordDetail.class);
                  if(CollectionUtils.isEmpty(list)){
                      retMap.put("result", PdConstant.FAIL_1);
                      retMap.put("message", "数据异常，产品信息不能为空");
                      return JSON.toJSONString(retMap);
                  }
                PdStockRecord pdStockRecord = new PdStockRecord();
                pdStockRecord.setOutDepartId(outStoreroomId); //出库库房Id
                pdStockRecord.setInDepartId(inStoreroomId);
                pdStockRecord.setPdStockRecordDetailList(list);
                // 出库
                pdStockRecordService.addOutForCabinet(pdStockRecord);

                retMap.put("result", PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            } else {
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            //TODO 日志记录
            return JSON.toJSONString(retMap);
        }
    }


    /**
     * 唯一码清除接口
     *
     * @param str
     * @return
     */
    @Override
    public String deleteRefBerCode(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "推送数据为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######唯一码清除接口:"+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String refBarCode = MapUtils.getString(map, "refBarCode");

                if (StringUtils.isEmpty(refBarCode)) {
                    retMap.put("result", PdConstant.FAIL_1);
                    retMap.put("message", "唯一码不能为空！");
                    return JSON.toJSONString(retMap);
                }
                PdProductStockUniqueCode productStockUniqueCode=pdProductStockUniqueCodeService.getById(refBarCode);
                if(oConvertUtils.isEmpty(productStockUniqueCode)){
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "根据唯一码获取不到数据");
                    return JSON.toJSONString(retMap);
                }else{
                    pdProductStockUniqueCodeService.removeById(refBarCode);
                }
                retMap.put("result",PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
            }else{
                retMap.put("result", PdConstant.FAIL_1);
                retMap.put("message", "推送数据为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("result", PdConstant.FAIL_1);
            retMap.put("message", "失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }
}
