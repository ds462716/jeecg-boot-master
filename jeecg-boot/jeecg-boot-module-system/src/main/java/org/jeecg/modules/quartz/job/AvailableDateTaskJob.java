package org.jeecg.modules.quartz.job;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.entity.PdVender;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.jeecg.modules.pd.service.IPdVenderService;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 生产厂家及供应商证照有效期到期定时任务
 *
 * @Author Scott
 */
@Slf4j
public class AvailableDateTaskJob implements Job {

    @Autowired
    private IPdVenderService pdVenderService;
    @Autowired
    private IPdSupplierService pdSupplierService;
    @Autowired
    private PushMsgUtil pushMsgUtil;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysAnnouncementSendService announcementSendService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        /**
         * 获取厂家证件信息及供应商证件信息，判断是否超过有效期；
         * 判断证件有效期是否大于当前日期，如果大于当前日期，直接设置为已过期
         * 获取静态字典表配置有效期天数信息，
         * 如果不大于当前日期，那么加上设置的有效期天数，在判断是否大于当前日期，如果大于，则设置为即将过期
         * 根据有效期天数信息判断信息表中的证件有效期是否是已过期，或者是即将过期，如果是已过期或者是即将过期，则更新基本信息表中的有效期标识字段；
         */
        log.info("生产厂家及供应商证照有效期到期定时任务进来了，时间:" + DateUtils.getTimestamp());
        Date date = DateUtils.getDate();//当前日期
        Integer remind = 0;//设定的有效期限   默认 0；
        String day = "7";
        if (!StringUtils.isEmpty(day)) {
            remind = Integer.valueOf(day);
        }
        //获取生产厂家信息数据
        PdVender vender = new PdVender();
        List<PdVender> venderList = pdVenderService.selectAllList(vender);
        if (venderList != null && venderList.size() > 0) {
            StringBuffer verderName1=new StringBuffer();
            StringBuffer verderName2=new StringBuffer();
            for (PdVender pdVender : venderList) {
                String flag="0";//所有证照过期标识
                Date afterMonthDate = null;
                String valType = "0";// 0：未过期  1：即将过期    2：已过期
                Date licenceDate = pdVender.getLicenceDate0();//获取第一组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    //如果有效期不等于当前日期，并且当前日期比证件有效期小，则过期了
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity0(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate1();//获取第二组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity1(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate2();//获取第三组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity2(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate3();//获取第四组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity3(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate4();//获取第五组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity4(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate5();//获取第六组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity5(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate6();//获取第七组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity6(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate7();//获取第八组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity7(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate8();//获取第九组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity8(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate9();//获取第十组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity9(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate10();//获取第十一组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity10(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdVender.getLicenceDate11();//获取第十二组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdVender.setLicenceValidity11(valType);
                        flag+=valType;
                    }
                }
                if (flag.indexOf("2")!=-1){
                    pdVender.setValidityFlag(PdConstant.PD_STATE_2);
                    verderName2.append(pdVender.getName()).append(",");
                }else if(flag.indexOf("1")!=-1){
                    pdVender.setValidityFlag(PdConstant.PD_STATE_1);
                    verderName1.append(pdVender.getName()).append(",");
                }else{
                    pdVender.setValidityFlag(PdConstant.PD_STATE_0);
                }
                pdVenderService.updateValidityFlag(pdVender);
            }
            //发送消息提醒  暂定发送给器械科管理员用户
            if(ObjectUtil.isNotEmpty(verderName1)){ //即将过期提醒
                String verderName1Str=verderName1.toString();
                this.sendMsg(verderName1Str,"validity_code","生产厂家");
            }
            if(ObjectUtil.isNotEmpty(verderName2)){ //已过期提醒
                String verderName2Str=verderName2.toString();
                this.sendMsg(verderName2Str,"validityFlag_code","生产厂家");
            }
        }
//-----------------------
        //获取供应商信息数据
        PdSupplier supplier = new PdSupplier();
        List<PdSupplier> list = pdSupplierService.selectAllList(supplier);
        if (list != null && list.size() > 0) {
            StringBuffer supplierName1=new StringBuffer();
            StringBuffer supplierName2=new StringBuffer();
            for (PdSupplier pdSupplier : list) {
                String flag="0"; //所有证照过期标识
                Date afterMonthDate = null;
                Date validityTerm = null;
                String valType = "0";// 0：未过期  1：即将过期    2：已过期
                validityTerm = pdSupplier.getLicenceDate0();//获取第一组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    //如果有效期不等于当前日期，并且当前日期比证件有效期小，则过期了
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity0(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate1();//获取第二组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity1(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate2();//获取第三组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity2(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate3();//获取第四组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity3(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate4();//获取第五组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity4(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate5();//获取第六组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity5(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate6();//获取第七组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity6(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate7();//获取第八组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity7(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate8();//获取第九组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity8(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate9();//获取第十组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity9(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate10();//获取第十一组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity10(valType);
                        flag+=valType;
                    }
                }
                validityTerm = pdSupplier.getLicenceDate11();//获取第十二组证件有效期
                if (validityTerm != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, remind);
                    valType = this.testDate(date, validityTerm, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdSupplier.setLicenceValidity11(valType);
                        flag+=valType;
                    }
                }
                if (flag.indexOf("2")!=-1){
                    pdSupplier.setValidityFlag(PdConstant.PD_STATE_2);
                    supplierName1.append(pdSupplier.getName()).append(",");
                }else if(flag.indexOf("1")!=-1){
                    pdSupplier.setValidityFlag(PdConstant.PD_STATE_1);
                    supplierName2.append(pdSupplier.getName()).append(",");
                }else{
                    pdSupplier.setValidityFlag(PdConstant.PD_STATE_0);
                }
                pdSupplierService.updateValidityFlag(pdSupplier);
            }
            //发送消息提醒  暂定发送给器械科管理员用户
            if(ObjectUtil.isNotEmpty(supplierName1)){ //即将过期提醒
                String supperName1Str=supplierName1.toString();
                this.sendMsg(supperName1Str,"validity_code","供应商");
            }
            if(ObjectUtil.isNotEmpty(supplierName2)){ //已过期提醒
                String supperName2Str=supplierName2.toString();
                this.sendMsg(supperName2Str,"validityFlag_code","供应商");
            }
        }
    }

    /**
     * 判断证件有效期是否在有效期内
     *
     * @param date
     * @param ValidityTerm
     * @param afterMonthDate
     * @return
     */
    public String testDate(Date date, Date ValidityTerm, Date afterMonthDate) {
        String avlType = PdConstant.PD_STATE_0;
        if ((!DateUtils.isSameDay(date, ValidityTerm)) && date.after(ValidityTerm)) {//过期
            avlType = PdConstant.PD_STATE_2;
        } else if ((DateUtils.isSameDay(date, ValidityTerm)) || afterMonthDate.after(ValidityTerm)) {//即将过期
            avlType = PdConstant.PD_STATE_1;
        }
        return avlType;
    }



    /**
     * 消息推送
     * @param name   公司名称
     * @param templateCode   提醒模板
     * @param validName  供应商/生产厂家
     * @return
     */
    public boolean sendMsg(String name,String templateCode,String validName) {
        Map<String, Object> map = new HashMap<>();
        //获取具有器械科管理员的角色用户Id;
        List<String> ids =new ArrayList<>();
       List<String> userIdList = sysUserService.getUserIdByRoleCode("qxk_admin");
        if (userIdList != null) {
      for(String userId:userIdList) {
          List<SysAnnouncementSend> list = this.announcementSendService.selectMyAnnouncementSendList(templateCode, userId);
          if (list == null || list.size()== 0) {
              ids.add(userId);
          }
         }
         if(ObjectUtils.isNotEmpty(ids)){
          String userIds = String.join(",", ids);
          Map<String, String> strMap = new HashMap<>();
          //模板注入参数
          strMap.put("validName", validName);
          strMap.put("name", name);
          map.put("map", strMap);
          //需要发送消息的用户id
          map.put("userIds", userIds + ",");
          //提醒优先级
          map.put("priority", CommonConstant.PRIORITY_H);
          //短信模板标识
          map.put("templateCode", templateCode);
          //发布人
          map.put("realname", "系统管理员");
          return pushMsgUtil.newSendMessage(map);
           }
        }
        return false;
    }
}
