package org.jeecg.modules.message.util;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
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
    @Resource
    private WebSocket webSocket;

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
        //必填项
        Map<String,String> map =MapUtils.getMap(params,"map");//模板参数
        String templateCode=MapUtils.getString(params,"templateCode");//消息提醒模板code
        String userIds=MapUtils.getString(params,"userIds");//需要发送消息的用户ID集合
        //非必填项
        //提醒优先级: 优先级（L低，M中，H高） 没传的话默认是低优先级
        String Priority=MapUtils.getString(params,"priority");
        String realname=MapUtils.getString(params,"realname");//发布人
        if(StringUtils.isEmpty(templateCode) || StringUtils.isEmpty(userIds)){
            throw new ClassCastException("参数不全");
        }
        if(StringUtils.isEmpty(Priority)){
            Priority=CommonConstant.PRIORITY_L;
        }
        if(StringUtils.isEmpty(realname)){//定时任务过来的提醒，都要传发布人名称
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            realname=sysUser.getRealname();
        }
        String title ="";
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        SysAnnouncement sysAnnouncement = new SysAnnouncement();
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            //模板标题
            title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String str = "${" + entry.getKey() + "}";
                    title = title.replace(str, entry.getValue());
                    content = content.replace(str, entry.getValue());
                }
            }
            sysAnnouncement.setTitile(title);//标题
            sysAnnouncement.setMsgContent(content);//内容
            sysAnnouncement.setUserIds(userIds);//接收用户Id
            sysAnnouncement.setPriority(Priority);//优先级（L低，M中，H高）
            sysAnnouncement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);//消息类型1:通知公告2:系统消息
            sysAnnouncement.setMsgType(CommonConstant.MSG_TYPE_UESR);//消息对象类型（USER:指定用户，ALL:全体用户）
            sysAnnouncement.setSender(realname);//发布人
            sysAnnouncement.setSendTime(new Date());
            sysAnnouncement.setTemplateCode(templateCode);//消息提醒模板
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);//发布状态（0未发布，1已发布，2已撤销）
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);
            String[] userIdList = userIds.substring(0, (userIds.length()-1)).split(",");
            JSONObject obj = new JSONObject();
            obj.put("cmd", "user");
            obj.put("msgId", sysAnnouncement.getId());
            obj.put("msgTxt",title);
            webSocket.sendMoreMessage(userIdList, obj.toJSONString());
        }
        return false;
    }
}
