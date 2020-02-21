package org.jeecg.modules.message.util;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息生成工具
 */

@Component
public class PushMsgUtil {

    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    /**
     * @param msgType      消息类型 1短信 2邮件 3微信
     * @param templateCode 消息模板码
     * @param map          消息参数
     * @param sentTo       接收消息方
     */
    public boolean sendMessage(String msgType, String templateCode, Map<String, String> map, String sentTo) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        SysMessage sysMessage = new SysMessage();
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            sysMessage.setEsType(msgType);
            sysMessage.setEsReceiver(sentTo);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String str = "${" + entry.getKey() + "}";
                    title = title.replace(str, entry.getValue());
                    content = content.replace(str, entry.getValue());
                }
            }
            sysMessage.setEsTitle(title);
            sysMessage.setEsContent(content);
            sysMessage.setEsParam(JSONObject.toJSONString(map));
            sysMessage.setEsSendTime(new Date());
            sysMessage.setEsSendStatus(SendMsgStatusEnum.WAIT.getCode());
            sysMessage.setEsSendNum(0);
             if (sysMessageService.save(sysMessage)) {
                    return true;
             }
        }
        return false;
    }

    /**
     * 专门用于发送系统站内信接口方法
     * @param params 消息参数
     */
    public boolean newSendMessage(Map<String, Object> params) {
        Map<String,String> map =MapUtils.getMap(params,"map");
        String templateCode=MapUtils.getString(params,"templateCode");//purchase_submitMsg
        String userIds=MapUtils.getString(params,"userIds");
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String str = "${" + entry.getKey() + "}";
                    title = title.replace(str, entry.getValue());
                    content = content.replace(str, entry.getValue());
                }
            }
                SysAnnouncement sysAnnouncement = new SysAnnouncement();
                sysAnnouncement.setTitile(title);//标题
                sysAnnouncement.setMsgContent(content);//内容
                sysAnnouncement.setUserIds(userIds);//接收用户Id
                sysAnnouncement.setPriority(CommonConstant.PRIORITY_L);//优先级（L低，M中，H高）
                sysAnnouncement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);//消息类型1:通知公告2:系统消息
                sysAnnouncement.setMsgType(CommonConstant.MSG_TYPE_UESR);//消息对象类型（USER:指定用户，ALL:全体用户）
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                sysAnnouncement.setSender(sysUser.getRealname());//发布人
                sysAnnouncement.setSendTime(new Date());
                sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);//发布状态（0未发布，1已发布，2已撤销）
                sysAnnouncementService.saveAnnouncement(sysAnnouncement);
        }
        return false;
    }
}
