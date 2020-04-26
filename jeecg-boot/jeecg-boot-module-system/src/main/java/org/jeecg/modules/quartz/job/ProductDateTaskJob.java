package org.jeecg.modules.quartz.job;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.service.IPdDepartConfigService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 产品证照有效期到期定时任务
 *
 * @Author Scott
 */
@Slf4j
public class ProductDateTaskJob implements Job {

    @Autowired
    private PushMsgUtil pushMsgUtil;
    @Autowired
    private IPdProductService pdProductService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private ISysAnnouncementSendService announcementSendService;
    @Autowired
    private IPdDepartConfigService PdDepartConfigService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        /**
         * 获取产品证件信息，判断是否超过有效期；
         * 判断证件有效期是否大于当前日期，如果大于当前日期，直接设置为已过期
         * 获取静态字典表配置有效期天数信息，
         * 如果不大于当前日期，那么加上设置的有效期天数，在判断是否大于当前日期，如果大于，则设置为即将过期
         * 根据有效期天数信息判断表中的证件有效期是否是已过期，或者是即将过期，如果是已过期或者是即将过期，则更新表中的有效期标识字段；
         */
        log.info("产品证照有效期到期定时任务进来了，时间:" + DateUtils.getTimestamp());
        Date date = DateUtils.getDate();//当前日期
        Integer venderRemind = Integer.valueOf(PdConstant.REMINDER_DETE_5);//设定的常量值（默认的有效期限）
        String venderDay = PdDepartConfigService.findPdDepartConfig(PdConstant.REMINDER_TYPE_5,"");
        if (!StringUtils.isEmpty(venderDay)) {
            venderRemind = Integer.valueOf(venderDay);
        }
        //获取生产厂家信息数据
        PdProduct product = new PdProduct();
        List<PdProduct> productList = pdProductService.selectList(product);
        if (productList != null && productList.size() > 0) {
             StringBuffer productName1=new StringBuffer();
            StringBuffer productName2=new StringBuffer();
            String departId=productList.get(0).getDepartId();
            for (PdProduct pdProduct : productList) {
                String flag="0";//所有证照过期标识
                Date afterMonthDate = null;
                String valType = "0";// 0：未过期  1：即将过期    2：已过期
                Date licenceDate = pdProduct.getLicenceDate0();//获取第一组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    //如果有效期不等于当前日期，并且当前日期比证件有效期小，则过期了
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity0(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate1();//获取第二组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity1(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate2();//获取第三组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity2(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate3();//获取第四组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity3(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate4();//获取第五组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity4(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate5();//获取第六组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity5(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate6();//获取第七组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity6(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate7();//获取第八组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity7(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate8();//获取第九组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity8(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate9();//获取第十组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity9(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate10();//获取第十一组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity10(valType);
                        flag+=valType;
                    }
                }
                licenceDate = pdProduct.getLicenceDate11();//获取第十二组证件有效期
                if (licenceDate != null) {
                    afterMonthDate = DateUtils.getDateToAddDate(date, venderRemind);
                    valType = this.testDate(date, licenceDate, afterMonthDate);
                    if (!PdConstant.PD_STATE_0.equals(valType)) {
                        pdProduct.setLicenceValidity11(valType);
                        flag+=valType;
                    }
                }
                if (flag.indexOf("2")!=-1){
                    pdProduct.setValidityFlag(PdConstant.PD_STATE_2);
                   if(!PdConstant.MSG_SEND_STATUS_2.equals(pdProduct.getMsgSendState())){
                       productName2.append(pdProduct.getName()).append(",");
                       pdProduct.setMsgSendState(PdConstant.MSG_SEND_STATUS_2);
                   }
                }else if(flag.indexOf("1")!=-1){
                    pdProduct.setValidityFlag(PdConstant.PD_STATE_1);
                    if(!PdConstant.MSG_SEND_STATUS_1.equals(pdProduct.getMsgSendState())){
                        productName1.append(pdProduct.getName()).append(",");
                        pdProduct.setMsgSendState(PdConstant.MSG_SEND_STATUS_1);
                    }
                }else{
                    pdProduct.setValidityFlag(PdConstant.PD_STATE_0);
                    pdProduct.setMsgSendState(PdConstant.MSG_SEND_STATUS_0);
                }
                pdProductService.updateValidityFlag(pdProduct);
            }
            //发送消息提醒
            if(ObjectUtil.isNotEmpty(productName1)){ //即将过期提醒
                String templateCode="productSoonExpire_code";
                String prodName1Str=productName1.toString();
                this.sendMsg(departId,prodName1Str,templateCode,PdConstant.AUDIT_MENU_9);
            }
            if(ObjectUtil.isNotEmpty(productName2)){ //已过期提醒
                String templateCode="productExpire_code";
                String prodName2Str=productName2.toString();
                this.sendMsg(departId,prodName2Str,templateCode,PdConstant.AUDIT_MENU_9);
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
     * @param prodName   产品名称
     * @param templateCode   提醒模板
     * @param prodName  产品名称
     * @return
     */
    public boolean sendMsg(String departId,String prodName,String templateCode ,String auditMenu) {
        Map<String, Object> map = new HashMap<>();
        List<String> ids =new ArrayList<>();
        List<String> userIdList =pdDepartService.findMenuUser(departId,auditMenu);
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
          strMap.put("prodName", prodName);
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
