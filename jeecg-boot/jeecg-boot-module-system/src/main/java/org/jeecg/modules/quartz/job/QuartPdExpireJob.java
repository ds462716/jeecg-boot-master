package org.jeecg.modules.quartz.job;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.service.IPdDepartConfigService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/***
 * 更新库存总表及库存明细表的过期状态定时任务
 */
@Slf4j
public class QuartPdExpireJob implements Job {
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private PushMsgUtil pushMsgUtil;
    @Autowired
    private ISysAnnouncementSendService announcementSendService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdDepartConfigService PdDepartConfigService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 定时获取库存总表及明细表数据，进行判断是否超过设置的有效期值，并根据规则进行更新过期状态值
         *
         */
        log.info("-------------------更新过期状态开始-------------------");
        Integer stockRemind = Integer.valueOf(PdConstant.REMINDER_DETE_3);//设定的常量值（默认的有效期限）
        List<PdProductStock> list = pdProductStockService.selectList(new PdProductStock());
        Map<String, Set<String>> m=new HashMap<String, Set<String>>();//存产品ID
        Map<String, Set<String>> p=new HashMap<String, Set<String>>();//存产品名称
        for (PdProductStock pdProductStock : list) {
            PdProductStock pd = new PdProductStock();
            String deptId = pdProductStock.getDepartId();
            Date validDate = pdProductStock.getExpDate();
            Date date = new Date();
            String stockDay = PdDepartConfigService.findPdDepartConfig(PdConstant.REMINDER_TYPE_3,pdProductStock.getDepartParentId());
            if (!StringUtils.isEmpty(stockDay)) {
                stockRemind = Integer.valueOf(stockDay);
            }
            pd.setMsgSendState(PdConstant.MSG_SEND_STATUS_0);
            if (ObjectUtil.isNotEmpty(validDate)){
                if ((!DateUtils.isSameDay(date, validDate)) && date.after(validDate)) { //过期
                    pd.setExpStatus(PdConstant.PD_STATE_2);
                    pd.setMsgSendState(PdConstant.MSG_SEND_STATUS_2);
                }
                Date afterMonthDate = DateUtils.getDateToAddDate(validDate, stockRemind);
                if ((date.before(validDate) && date.after(afterMonthDate))
                        || (DateUtils.isSameDay(date, validDate)
                        || DateUtils.isSameDay(date, afterMonthDate))) { //近效期
                    pd.setExpStatus(PdConstant.PD_STATE_1);
                    pd.setMsgSendState(PdConstant.MSG_SEND_STATUS_1);
                }

                String pdState = pd.getExpStatus();
                if (StringUtils.isNotEmpty(pdState) && !PdConstant.PD_STATE_0.equals(pdState)) {
                    pd.setId(pdProductStock.getId());
                    pd.setDepartId(deptId);
                    pdProductStockService.updateProductStock(pd);
                    if (m.containsKey(deptId)) {
                        Set<String> pids = (Set<String>) m.get(deptId);
                        String pid = pdProductStock.getProductId();
                        if (pids.contains(pid)) {
                            continue;
                        } else {
                            pids.add(pid);
                            m.put(deptId, pids);
                        }
                    } else {
                        Set<String> pids = new HashSet<String>();
                        String pid = pdProductStock.getProductId();
                        pids.add(pid);
                        m.put(deptId, pids);
                    }

                     //判断是否有发送过期消息提醒
                    String msgSendState = pdProductStock.getMsgSendState();
                    if(!pd.getMsgSendState().equals(msgSendState)) {
                        if (p.containsKey(deptId)) {
                            Set<String> names = (Set<String>) m.get(deptId);
                            String pdName = pdProductStock.getProductName();
                            if (names.contains(pdName)) {
                                continue;
                            } else {
                                names.add(pdName);
                                p.put(deptId, names);
                            }

                        } else {
                            Set<String> names = new HashSet<String>();
                            String pdName = pdProductStock.getProductName();
                            names.add(pdName);
                            p.put(deptId, names);
                        }
                    }
                }
            }
        }

        log.info("-------------------更新的主表产品过期状态-------------------");
        log.info(m.toString());
        Iterator<String> iter = m.keySet().iterator();
        while(iter.hasNext()){
            List<String> prodNames=new ArrayList<>();
            String deptName="";
            String deptId = iter.next();
            Set<String> set = m.get(deptId);
            for (String pid : set) {
                PdProductStock productStock = new PdProductStock();
                productStock.setDepartId(deptId);
                productStock.setProductId(pid);
                List<PdProductStock> l = pdProductStockService.selectList(productStock);
                if(CollectionUtils.isNotEmpty(l)) {
                    PdProductStock pk = l.get(0);
                    PdProductStockTotal stockTotal = new PdProductStockTotal();
                    stockTotal.setExpStatus(pk.getExpStatus());
                    stockTotal.setProductId(pid);
                    stockTotal.setDepartId(deptId);
                    pdProductStockTotalService.updateProductStockTotal(stockTotal);
                    prodNames.add(pk.getProductName());
                    deptName = pk.getDeptName();
                }
            }
        }

        log.info("-------------------发送过期产品消息提醒-------------------");
        log.info(p.toString());
        Iterator<String> iterator = p.keySet().iterator();
        while(iterator.hasNext()){
            String deptName="";
            List<String> prodNames=new ArrayList<>();
            String deptId = iterator.next();
            //根据科室ID获取科室名称
            SysDepart sysDepart=new SysDepart();
            sysDepart.setDepartId(deptId);
            List<SysDepart> departList = pdDepartService.selectList(sysDepart);
            if(CollectionUtils.isNotEmpty(departList)){
                deptName= departList.get(0).getDepartName();
            }
            Set<String> set = m.get(deptId);
            for (String name : set) {
                prodNames.add(name);
                 }
            //给库存对应科室管理员发送消息提醒
            //提醒模板：xxx科室用户，您好，你科室下xxx产品有效期已过期或即将过期，请及时处理；
            this.sendMsg(deptId,deptName,"stockProd_msg",prodNames);
        }


        log.info("-------------------更新过期状态结束-------------------");
    }



    /**
     * 消息推送
     * @param deptName   科室名称
     * @param templateCode   提醒模板
     * @param prodNames  产品集合
     * @return
     */
    public boolean sendMsg(String deptId,String deptName,String templateCode,List<String> prodNames) {
        Map<String, Object> map = new HashMap<>();
        //获取具有器械科管理员的角色用户Id;
        List<String> ids =new ArrayList<>();
        //根据科室id获取该科室管理员userId
        List<String> userIdList =pdDepartService.findMenuUser(deptId,PdConstant.AUDIT_MENU_4);
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
                strMap.put("deptName", deptName);//科室名称
                strMap.put("prodNames",String.join(",", prodNames));//产品名称集合
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
