package org.jeecg.modules.external.fengcheng.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author jiangxz
 * @description 丰城中医院HIS计费接口
 * @date 2020-5-26
 */
public class HisApiForFCZhongyiUtils {

    private static String HIS_DEFAULT_NAME_SPACE = "http://172.16.1.44:30800/esb-scheduler/api";
    // his收费代码
    private static String HIS_CHARGE_CODE_URL = "/HQXMDM?account=zb&passwd=cnhis.com";
    // his病人
    private static String HIS_PATIENT_URL = "/HQHZXX?account=zb&passwd=cnhis.com";
    // his收费
    private static String HIS_CHARGE_URL = "/ZYSS?account=zb&passwd=cnhis.com";
    // his退费
    private static String HIS_REFUND_URL = "/ZYTF?account=zb&passwd=cnhis.com";

    private static Logger logger = LoggerFactory.getLogger(HisApiForFCZhongyiUtils.class);

    /**
     * 收费接口
     * @param pdDosage
     * @param dosageDetailList
     * @return
     */
    public static JSONObject exeCharge(PdDosage pdDosage, List<PdDosageDetail> dosageDetailList){
        JSONObject returnJson = new JSONObject();
        if(pdDosage == null || StringUtils.isBlank(pdDosage.getVisitNo()) || CollectionUtils.isEmpty(dosageDetailList)){
            returnJson.put("statusCode", "-200");
            returnJson.put("msg", "参数异常");
            logger.error("HIS计费失败，参数异常");
            return returnJson;
        }

        try{
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            JSONArray array = new JSONArray();
            for(PdDosageDetail detail : dosageDetailList){
                JSONObject item = new JSONObject();
                item.put("bby01",detail.getChargeCode()); //收费项目代码
                item.put("vaj25",detail.getDosageCount().toString());//数量
                item.put("vaj38",detail.getAmountMoney()==null?"0":detail.getAmountMoney().toString());//金额
                item.put("bck01d",pdDosage.getOprDeptId());//住院科室编码
                item.put("prodNo",detail.getProductStockId()); // 库存id
                array.add(item);
            }

            JSONObject requestJson = new JSONObject();
            requestJson.put("vaa07",pdDosage.getVisitNo()); //患者流水号
            requestJson.put("vak08",pdDosage.getTotalPrice()==null?"0":pdDosage.getTotalPrice().toString());//应付金额
            requestJson.put("bce02b", sysUser.getUsername()); //开单人(HIS系统工号)
            requestJson.put("Item", array);

            // TODO
            returnJson = HttpUtil.httpPost(HIS_DEFAULT_NAME_SPACE + HIS_CHARGE_URL, requestJson.toJSONString());
//            returnJson = getTestChargeJson(pdDosage,dosageDetailList);

            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS计费失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg","HIS计费失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
                logger.info("HIS计费成功");
            }

        } catch (JSONException ee) {
            ee.printStackTrace();
            returnJson.put("statusCode", "-200");
            returnJson.put("msg","调用HIS收费接口JSON转换返回信息出现错误！");
            logger.error("******调用HIS收费接口JSON转换返回信息出现错误！******");
        }

        return returnJson;
    }

    /**
     * 退费接口
     * @param pdDosage
     * @param dosageDetailList
     * @return
     */
    public static JSONObject exeRefund(PdDosage pdDosage,List<PdDosageDetail> dosageDetailList){
        JSONObject returnJson = new JSONObject();
        if(pdDosage == null || StringUtils.isBlank(pdDosage.getVisitNo()) || CollectionUtils.isEmpty(dosageDetailList)){
            returnJson.put("statusCode", "-200");
            returnJson.put("msg", "参数异常");
            logger.error("HIS退费失败，参数异常");
            return returnJson;
        }

        try{
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            JSONArray array = new JSONArray();
            for(PdDosageDetail detail : dosageDetailList){
                if(oConvertUtils.isEmpty(detail.getHisChargeItemId()) || oConvertUtils.isEmpty(detail.getHisChargeId())){
                    returnJson.put("statusCode", "-200");
                    returnJson.put("msg", "计费ID或计费明细ID为空，请联系管理员！");
                    logger.error("HIS退费失败，计费ID或计费明细ID为空");
                    return returnJson;
                }
                JSONObject item = new JSONObject();
                item.put("vaj01",detail.getHisChargeItemId());
                item.put("bby01",detail.getChargeCode());
                item.put("vaj25",detail.getDosageCount().toString());
                item.put("vaj38",detail.getAmountMoney()==null?"0":detail.getAmountMoney().toString());
                item.put("bck01d",pdDosage.getOprDeptId());
                array.add(item);
            }

            JSONObject requestJson = new JSONObject();
            requestJson.put("vaa07",pdDosage.getVisitNo());
            requestJson.put("vak08",pdDosage.getTotalPrice()==null?"0":pdDosage.getTotalPrice().toString());
            requestJson.put("bce02b", sysUser.getUsername());//开单人(HIS系统工号)
            requestJson.put("vai01", dosageDetailList.get(0).getHisChargeId());
            requestJson.put("Item", array);

            // TODO
            returnJson = HttpUtil.httpPost(HIS_DEFAULT_NAME_SPACE + HIS_REFUND_URL, requestJson.toJSONString());
//            returnJson = getTestRefundJson(pdDosage,dosageDetailList);

            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS退费失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg","HIS退费失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
            }

        } catch (JSONException ee) {
            ee.printStackTrace();
            returnJson.put("statusCode", "-200");
            returnJson.put("msg","调用HIS退费接口JSON转换返回信息出现错误！");
            logger.error("******调用HIS退费接口JSON转换返回信息出现错误！******");
        }


        JSONObject requestJson = new JSONObject();

        return returnJson;
    }


    /**
     * 查询HIS项目收费代码
     * @param SFCODE
     * @param SFNAME
     * @return
     */
    public static JSONObject queryHisChargeCode(String SFCODE,String SFNAME) {
        JSONObject returnJson = new JSONObject();
        JSONObject requestJson = new JSONObject();
        if(oConvertUtils.isEmpty(SFCODE)){
            SFCODE = "";
        }
        if(oConvertUtils.isEmpty(SFNAME)){
            SFNAME = "";
        }
        requestJson.put("SFCODE",SFCODE);
        requestJson.put("SFNAME",SFNAME);

        // TODO
        returnJson = HttpUtil.httpPost(HIS_DEFAULT_NAME_SPACE + HIS_CHARGE_CODE_URL, requestJson.toJSONString());
//        returnJson = getTestChargeCodeJson(); // 测试数据

        if(returnJson == null){
            returnJson = new JSONObject();
            logger.error("HIS查询项目收费代码信息失败，返回null");
            returnJson.put("statusCode", "-200");
            returnJson.put("msg", "HIS查询项目收费代码信息失败，返回null");
            return returnJson;
        }

        if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
            logger.error("HIS查询项目收费代码信息失败，返回msg:"+returnJson.getString("msg"));
            returnJson.put("statusCode", "-200");
            returnJson.put("msg", "HIS查询项目收费代码信息失败，返回msg:"+returnJson.getString("msg"));
        }else{
            returnJson.put("statusCode", PdConstant.SUCCESS_0);
        }
        return returnJson;
    }

    /**
     * 查询HIS病人信息
     * @param hitaionNo
     * @return
     */
    public static JSONObject queryHisPatientInfo(String hitaionNo) {
        JSONObject returnJson = new JSONObject();
        if(StringUtils.isBlank(hitaionNo)){
            returnJson.put("code", "-200");
            returnJson.put("msg","住院号不可为空");
            return returnJson;
        }

        JSONObject requestJson = new JSONObject();
        requestJson.put("hitaionNo",hitaionNo);

        try{
            // TODO
            returnJson = HttpUtil.httpPost(HIS_DEFAULT_NAME_SPACE + HIS_PATIENT_URL, requestJson.toJSONString());
//            returnJson = getTestPatientJson(); // 测试数据

            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS查询病人信息失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg","HIS查询病人信息失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
            }

        } catch (JSONException ee) {
            ee.printStackTrace();
            returnJson.put("statusCode", "-200");
            returnJson.put("msg","调用HIS根据住院号查询病人信息接口JSON转换返回信息出现错误！");
            logger.error("******调用HIS根据住院号查询病人信息接口JSON转换返回信息出现错误！******");
        }
        return returnJson;
    }

    /**
     * 收费测试
     * @return
     */
    public static JSONObject getTestChargeJson(PdDosage pdDosage,List<PdDosageDetail> dosageDetailList){

        JSONArray array = new JSONArray();
        for(PdDosageDetail detail : dosageDetailList){
            JSONObject item = new JSONObject();
            item.put("vaa07",pdDosage.getVisitNo());// 就诊流水号
            item.put("vai01","10325748");// 计费单据id
            item.put("vaj01","33118756");// 计费单据明细ID（退费入参需要）
            item.put("prodNo",detail.getProductId());// 产品ID
            array.add(item);
        }

        JSONObject json = new JSONObject();
        json.put("code","0");
        json.put("msg","成功");
        json.put("data",array);

        return json;
    }

    /**
     * 退费测试
     * @param pdDosage
     * @param dosageDetailList
     * @return
     */
    public static JSONObject getTestRefundJson(PdDosage pdDosage,List<PdDosageDetail> dosageDetailList){

        JSONObject json = new JSONObject();
        json.put("code","0");
        json.put("msg","成功");
        return json;
    }


    /**
     * 收费代码测试
     * @return
     */
    public static JSONObject getTestChargeCodeJson(){
        JSONArray data = new JSONArray();
        JSONObject entity1 = new JSONObject();
        entity1.put("SFCODE","111111");                   //His患者就诊流水号
        entity1.put("SFNO","123123");                        //手术编号（如果没有手术，可为空）
        entity1.put("SFNAME","CT增强1");
//        HisChargeCode entity2 = new HisChargeCode();
        JSONObject entity2 = new JSONObject();
        entity2.put("SFCODE","22222");                   //His患者就诊流水号
        entity2.put("SFNO","1qazxsw2");                        //手术编号（如果没有手术，可为空）
        entity2.put("SFNAME","CT增强2");
        data.add(entity1);
        data.add(entity2);

        JSONObject json = new JSONObject();
        json.put("code","0");
        json.put("msg","成功");
        json.put("data", JSON.toJSONString(data));
        return json;
    }

    /**
     * 病人测试
     * @return
     */
    public static JSONObject getTestPatientJson(){
        JSONArray jsonArray = new JSONArray();
        JSONObject hisPatient = new JSONObject();
        hisPatient.put("vaa07","116326");                   //1His患者就诊流水号
        hisPatient.put("operNo","");                        //1手术编号（如果没有手术，可为空）
        hisPatient.put("blngDptmNo","01000424");            //1所属病区编码
        hisPatient.put("blngDptmName","妇产二科病区");       //1所属病区
        hisPatient.put("blngNo","040004");                  //1所属科室编码
        hisPatient.put("blngName","妇产科");                //1所属科室
        hisPatient.put("type","1");                        //住院标识（1：是  2：否）
        hisPatient.put("projectName","项目名称项目名称项目名称项目名称项目名称");                  //1项目名称
        hisPatient.put("departName","");   //手术或检查项目科室（如果没有手术，则是检查项目科室）
        hisPatient.put("createDate","2018-11-01 10:12:28");//1登记日期
        hisPatient.put("doctorCode","9216");               //1申请医生编码
        hisPatient.put("doctorName","熊清文");             //1申请医生姓名
        hisPatient.put("outpatCode","");                   //1门诊号
        hisPatient.put("sex","女");                        // 1
        hisPatient.put("patientName","陈婧");             //1病人姓名
        hisPatient.put("hitaionNo","201814572");          //1住院号（如果不是住院，可为空）
        hisPatient.put("bedCode","09");                   //1床位号（如果没有，可为空）

//        JSONObject hisPatient2 = new JSONObject();
//        hisPatient2.put("vaa07","1163261");                   //His患者就诊流水号
//        hisPatient2.put("operNo","1");                        //手术编号（如果没有手术，可为空）
//        hisPatient2.put("blngDptmNo","010004241");            //所属病区编码
//        hisPatient2.put("blngDptmName","妇产二科病区1");       //所属病区
//        hisPatient2.put("blngNo","0400041");                  //所属科室编码
//        hisPatient2.put("blngName","妇产科1");                //所属科室
//        hisPatient2.put("type","11");                        //住院标识（1：是  2：否）
//        hisPatient2.put("projectName","1");                  //项目名称
//        hisPatient2.put("departName","1");                   //手术或检查项目科室（如果没有手术，则是检查项目科室）
//        hisPatient2.put("createDate","2018-11-01 10:12:28");//登记日期
//        hisPatient2.put("doctorCode","92161");               //申请医生编码
//        hisPatient2.put("doctorName","熊清文1");             //申请医生姓名
//        hisPatient2.put("outpatCode","1");                   //门诊号
//        hisPatient2.put("sex","女");
//        hisPatient2.put("patientName","陈婧1");             //病人姓名
//        hisPatient2.put("hitaionNo","2018145721");          //住院号（如果不是住院，可为空）
//        hisPatient2.put("bedCode","091");                   //床位号（如果没有，可为空）

        jsonArray.add(hisPatient);
//        jsonArray.add(hisPatient2);

        JSONObject json = new JSONObject();
        json.put("code","0");
        json.put("msg","成功");
        json.put("data",jsonArray);

        return json;
    }

    public static void main(String[] args) {
        JSONObject json = getTestPatientJson();
        System.out.println("json = " + json);
        System.out.println("code = " + json.get("code"));
        System.out.println("msg = " + json.get("msg"));


        JSONArray jsonArray = json.getJSONArray("data");

//        HisPatient bean = (HisPatient) JSONObject.parseObject(jsonArray.getJSONObject(0).toJSONString(), HisPatient.class);

//        List<HisChargeCode> hisChargeCodeList = JSONArray.parseArray(jsonArray.toJSONString(),HisChargeCode.class);
        System.out.println("json = " + json.get("data"));
    }
}
