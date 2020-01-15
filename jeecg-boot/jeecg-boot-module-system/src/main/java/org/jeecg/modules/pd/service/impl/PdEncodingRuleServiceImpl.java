package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import org.jeecg.modules.pd.mapper.PdEncodingRuleMapper;
import org.jeecg.modules.pd.service.IPdEncodingRuleDetailService;
import org.jeecg.modules.pd.service.IPdEncodingRuleService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @Description: 编码规则表
 * @Author: zxh
 * @Date:   2019-12-26
 * @Version: V1.0
 */
@Service
public class PdEncodingRuleServiceImpl extends ServiceImpl<PdEncodingRuleMapper, PdEncodingRule> implements IPdEncodingRuleService {

    @Autowired
    private IPdEncodingRuleDetailService pdEncodingRuleDetailService;
    @Autowired
    private PdEncodingRuleMapper pdEncodingRuleMapper;

    @Override
    @Transactional
    public void savePdEncodingRule(PdEncodingRule pdEncodingRule) {
        if(pdEncodingRule!=null){
            String s = UUIDUtil.getUuid();
            pdEncodingRule.setId(s);
            List<PdEncodingRuleDetail> pdEncodingRuleDetails = pdEncodingRule.getPdEncodingRuleDetails();
            if(pdEncodingRuleDetails!=null && pdEncodingRuleDetails.size()>0){
                //调整顺序
                comparatorSort(pdEncodingRuleDetails);
                //拼接编码描述
                String str ="";
                //总位数
                int totalDigit = 0;
                //规则简码
                String codeQueryStr="";
                //规则详情
                String codeDetail = "";
                for (int m=0;m<pdEncodingRuleDetails.size();m++) {
                    PdEncodingRuleDetail pdEncodingRuleDetail = pdEncodingRuleDetails.get(m);
                    if(pdEncodingRuleDetail.getIdentifier()!=null){
                        String kh = "";
                        String ruleVaule = pdEncodingRuleDetail.getValue();
                        int length = pdEncodingRuleDetail.getLength();
                        if(!"#".equals(ruleVaule)){
                            kh = "("+ruleVaule+")";
                            codeQueryStr = codeQueryStr +ruleVaule;
                            codeDetail = codeDetail+ruleVaule+"("+pdEncodingRuleDetail.getLength()+")";
                            totalDigit +=length+ruleVaule.length();
                        }else{
                            totalDigit +=length+ruleVaule.length()-1;
                            codeDetail = codeDetail+"("+pdEncodingRuleDetail.getLength()+")";
                        }
                        for(int i=0;i<length;i++){
                            kh += "X";
                        }
                        str+=kh;
                        pdEncodingRuleDetail.setCodeId(pdEncodingRule.getId());
                        pdEncodingRuleDetail.setId(UUIDUtil.getUuid());
                        //如果是固定类型
                        if(PdConstant.IDENTIFIER_TYPE_1.equals(pdEncodingRuleDetail.getType())){
                            pdEncodingRuleDetail.setLength(null);
                        }
                    }
                }
                pdEncodingRule.setCodeDesc(str);
                pdEncodingRule.setTotalDigit(totalDigit);
                pdEncodingRule.setCodeQuery(codeQueryStr);
                pdEncodingRule.setCodeDetail(codeDetail);
                pdEncodingRuleDetailService.saveBatch(pdEncodingRuleDetails);
                this.save(pdEncodingRule);
            }
        }
    }

    /**
     * 编码规则修改
     * @param pdEncodingRule
     */
    @Override
    @Transactional
    public void updatePdEncodingRule(PdEncodingRule pdEncodingRule) {
        if(pdEncodingRule!=null){
            pdEncodingRuleDetailService.removeByCodeId(pdEncodingRule);
            List<PdEncodingRuleDetail> pdEncodingRuleDetails = pdEncodingRule.getPdEncodingRuleDetails();
            if(pdEncodingRuleDetails!=null && pdEncodingRuleDetails.size()>0){
                //调整顺序
                comparatorSort(pdEncodingRuleDetails);
                //拼接编码描述
                String str ="";
                //总位数
                int totalDigit = 0;
                //规则简码
                String codeQueryStr="";
                //规则详情
                String codeDetail = "";
                for (int m=0;m<pdEncodingRuleDetails.size();m++) {
                    PdEncodingRuleDetail pdEncodingRuleDetail = pdEncodingRuleDetails.get(m);
                    if(pdEncodingRuleDetail.getIdentifier()!=null){
                        String kh = "";
                        String ruleVaule = pdEncodingRuleDetail.getValue();
                        int length = pdEncodingRuleDetail.getLength();
                        if(!"#".equals(ruleVaule)){
                            kh = "("+ruleVaule+")";
                            codeQueryStr = codeQueryStr +ruleVaule;
                            codeDetail = codeDetail+ruleVaule+"("+pdEncodingRuleDetail.getLength()+")";
                            totalDigit +=length+ruleVaule.length();
                        }else{
                            totalDigit +=length+ruleVaule.length()-1;
                            codeDetail = codeDetail+"("+pdEncodingRuleDetail.getLength()+")";
                        }
                        for(int i=0;i<length;i++){
                            kh += "X";
                        }
                        str+=kh;
                        pdEncodingRuleDetail.setCodeId(pdEncodingRule.getId());
                        pdEncodingRuleDetail.setId(UUIDUtil.getUuid());
                        //如果是固定类型
                        if(PdConstant.IDENTIFIER_TYPE_1.equals(pdEncodingRuleDetail.getType())){
                            pdEncodingRuleDetail.setLength(null);
                        }
                    }
                }
                pdEncodingRule.setCodeDesc(str);
                pdEncodingRule.setTotalDigit(totalDigit);
                pdEncodingRule.setCodeQuery(codeQueryStr);
                pdEncodingRule.setCodeDetail(codeDetail);
                pdEncodingRuleDetailService.saveBatch(pdEncodingRuleDetails);
                this.updateById(pdEncodingRule);
            }
        }

    }

    /**
     * 查询编码规则表
     * @param page
     * @param pdEncodingRule
     * @return
     */
    @Override
    public Page<PdEncodingRule> selectList(Page<PdEncodingRule> page,PdEncodingRule pdEncodingRule) {
        return page.setRecords(pdEncodingRuleMapper.selectList(pdEncodingRule));
    }

    @Override
    public List<PdEncodingRule> selectList(PdEncodingRule pdEncodingRule) {
        return pdEncodingRuleMapper.selectList(pdEncodingRule);
    }

    //集合对象排序
    public static List<PdEncodingRuleDetail> comparatorSort(List<PdEncodingRuleDetail> pdEncodingRuleDetails){
        Collections.sort(pdEncodingRuleDetails,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof PdEncodingRuleDetail && o2 instanceof PdEncodingRuleDetail){
                    PdEncodingRuleDetail e1 = (PdEncodingRuleDetail) o1;
                    PdEncodingRuleDetail e2 = (PdEncodingRuleDetail) o2;
                    return Integer.parseInt(e1.getCodeOrder()) - Integer.parseInt(e2.getCodeOrder());
                }
                throw new ClassCastException("不能转换为Emp类型");
            }
        });
        return pdEncodingRuleDetails;
    }

}
