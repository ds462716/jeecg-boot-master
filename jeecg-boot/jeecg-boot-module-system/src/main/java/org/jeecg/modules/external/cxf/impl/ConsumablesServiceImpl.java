package org.jeecg.modules.external.cxf.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.cxf.ConsumablesService;
import org.jeecg.modules.external.entity.HForcerInfo;
import org.jeecg.modules.external.entity.HUserFingerFace;
import org.jeecg.modules.external.service.IHUserFingerFaceService;
import org.jeecg.modules.external.service.IHforcerInfoService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import org.jeecg.modules.pd.entity.PdStockRecord;
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
@WebService(serviceName = "ConsumablesService", // 与接口中指定的name一致
        targetNamespace = "http://webservice.business.mixpay.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "org.jeecg.modules.external.cxf.ConsumablesService" // 接口地址
)
public class ConsumablesServiceImpl implements ConsumablesService {
    @Autowired
    private IHforcerInfoService hforcerInfoService;
    @Autowired
    private IHUserFingerFaceService userFingerFaceService;
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
     * 一体机注册接口
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
     * 获取一体机信息
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
                if (userId == null && loginName== null) {
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
                if (StringUtils.isEmpty(kfid)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "库房ID不能为空！");
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
     * 根据唯一码获取产品信息接口
     *
     * @param str
     * @return
     */
    @Override
    public String sendRefBarCode(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "查询条件为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######根据唯一码获取产品信息接口报文："+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String kfId = MapUtils.getString(map, "kfId");//库房ID
                String uniqueCode = MapUtils.getString(map, "uniqueCode");//唯一码
                if (StringUtils.isEmpty(kfId)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "库房ID不能为空！");
                    return JSON.toJSONString(retMap);
                }
                if (StringUtils.isEmpty(uniqueCode)) {
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "唯一码不能为空！");
                    return JSON.toJSONString(retMap);
                }
                PdProductStock productStock=new PdProductStock();
                //productStock.setProductFlag(PdConstant.PRODUCT_FLAG_1);
                productStock.setProductBarCode(uniqueCode);
                productStock.setDepartId(kfId);
                productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                List<PdProductStock> stock= pdProductStockService.queryUniqueProductStockList(productStock);
                if(CollectionUtils.isEmpty(stock)){
                    retMap.put("result",PdConstant.FAIL_1);
                    retMap.put("message", "未查询到产品对应的库存");
                }else {
                    retMap.put("param", stock);
                    retMap.put("result", PdConstant.SUCCESS_0);
                    retMap.put("message", "成功");
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
    }

    /**
     * 试剂出入库接口
     *
     * @param str
     * @return
     */
    @Override
    public String reagentOutToInRecord(String str) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (str == null || "".equals(str.trim())) {
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "参数为空");
            return JSON.toJSONString(retMap);
        }
        try {
            System.out.println("#######试剂出入库接口报文："+str);
            Map<Object, Object> map = (Map<Object, Object>) JSONObject.parse(str);
            if (map != null && !MapUtils.isEmpty(map)) {
                String outDepartId = MapUtils.getString(map, "ckkf");//出库库房ID
                String inDepartId = MapUtils.getString(map, "rkkf");//入库库房ID
                String operator = MapUtils.getString(map, "czr");//操作人
                String type = MapUtils.getString(map, "type");//操作类型  0:入库；1:出库；
                List<Map<String,Object>> uniqueCodes =(List<Map<String,Object>>) MapUtils.getObject(map, "list");//操作类型  0:入库；1:出库；

//--------------
                Result<PdProductStock> result = new Result<>();
                if(CollectionUtils.isEmpty(uniqueCodes)){
                    retMap.put("result", MessageConstant.ICODE_STATE_500);
                    retMap.put("message", "请扫描正确的条码");
                    return JSON.toJSONString(retMap);
                }
                if(oConvertUtils.isEmpty(inDepartId)){
                    retMap.put("result",MessageConstant.ICODE_STATE_500);
                    retMap.put("message", "出库失败，入库库房为空！");
                    return JSON.toJSONString(retMap);
                }
                List<PdProductStock> stockList = new ArrayList<>();
                for (Map<String,Object> param : uniqueCodes) {
                       String    barcode = MapUtils.getString(param,"uniqueCode");
                    if(StringUtils.isNotBlank(barcode)){
                        LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<PdProductStockUniqueCode>()
                                .eq(PdProductStockUniqueCode::getId, barcode)
                                .eq(PdProductStockUniqueCode::getPrintType, PdConstant.CODE_PRINT_TYPE_1)
                                .eq(PdProductStockUniqueCode::getCodeState,PdConstant.CODE_PRINT_STATE_0)//正常状态不包括已退货和已用完的
                                /*.eq(PdProductStockUniqueCode::getDepartId,sysUser.getCurrentDepartId())*/;//当前科室下的
                        //查询状态是正常状态且是当前科室下的
                        PdProductStockUniqueCode pdProductStockUniqueCode = pdProductStockUniqueCodeService.getOne(query);
                        if(pdProductStockUniqueCode!=null){
                            PdProductStock ps = new PdProductStock();
                            ps.setId(pdProductStockUniqueCode.getProductStockId());
                            ps.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
                            ps.setProductFlag("");//产品类型0耗材1试剂
                            ps.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//状态 未使用
                            ps.setDepartId(outDepartId);
                            // 唯一码查库存
                            List<PdProductStock> pds = pdProductStockService.queryUniqueProductStockList(ps);
                            if(CollectionUtils.isNotEmpty(pds)){
                                ps = pds.get(0);
                                ps.setRefBarCode(barcode);
                                stockList.add(ps);
                            }
                        }
                    }
                }
                if(CollectionUtils.isEmpty(stockList)){
                    retMap.put("result",MessageConstant.ICODE_STATE_500);
                    retMap.put("message", "出库失败，出库明细为空！");
                    return JSON.toJSONString(retMap);
                }
                //自动出库操作
                PdStockRecord  pdStockRecord=new PdStockRecord();
                pdStockRecord.setSubmitBy(operator);      // 提交人
                pdStockRecord.setAuditBy(operator);       // 审核人
                pdStockRecord.setApplyBy(operator);       //领用人
                pdStockRecord.setOutDepartId(outDepartId); // 出库库房id
                pdStockRecord.setInDepartId(inDepartId);
                pdStockRecord.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
                String recordId = pdStockRecordService.addOutForTerminal(pdStockRecord, stockList);
//-------------------
                retMap.put("result", PdConstant.SUCCESS_0);
                retMap.put("message", "成功");
                System.out.println("#######试剂出入库结束");
            } else {
                retMap.put("result",PdConstant.FAIL_1);
                retMap.put("message", "参数不能为空");
            }
            return JSON.toJSONString(retMap);
        } catch (Exception e) {
            System.out.println("#######操作失败，日志：" + e.getMessage());
            e.printStackTrace();
            retMap.put("result",PdConstant.FAIL_1);
            retMap.put("message", "操作失败，日志：" + e.getMessage());
            return JSON.toJSONString(retMap);
        }
    }
}
