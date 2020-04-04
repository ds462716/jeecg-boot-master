package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdProductRule;
import org.jeecg.modules.pd.mapper.PdEncodingRuleMapper;
import org.jeecg.modules.pd.mapper.PdProductRuleMapper;
import org.jeecg.modules.pd.service.IPdEncodingRuleDetailService;
import org.jeecg.modules.pd.service.IPdEncodingRuleService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

    @Autowired
    private PdProductRuleMapper pdProductRuleMapper;

    @Autowired
    private SqlSession sqlsession;
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

    @Override
    public Result<Object> deleteV(String id) {
        try{
            PdProductRule pdProductRule = new PdProductRule();
            pdProductRule.setRuleId(id);
            List<PdProductRule> pdProductRules = pdProductRuleMapper.selectList(pdProductRule);
            if(CollectionUtils.isNotEmpty(pdProductRules)){
                return Result.error("删除失败!，当前编码规则被使用不能删除");
            }
            this.removeById(id);
            PdEncodingRule pdEncodingRule = new PdEncodingRule();
            pdEncodingRule.setId(id);
            pdEncodingRuleDetailService.removeByCodeId(pdEncodingRule);
            return Result.ok("删除成功!");
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteBatchV(String ids) {
        try{
            PdEncodingRuleMapper dao = sqlsession.getMapper(PdEncodingRuleMapper.class);
            List<String> idList = Arrays.asList(ids.split(","));
            if(idList!=null && idList.size()>0){
                boolean bl = true;
                for(String id : idList){
                    PdProductRule pdProductRule = new PdProductRule();
                    pdProductRule.setRuleId(id);
                    List<PdProductRule> pdProductRules = pdProductRuleMapper.selectList(pdProductRule);
                    if(CollectionUtils.isNotEmpty(pdProductRules)){
                        bl = false;
                        continue;
                    }
                    this.removeById(id);
                    PdEncodingRule pdEncodingRule = new PdEncodingRule();
                    pdEncodingRule.setId(id);
                    pdEncodingRuleDetailService.removeByCodeId(pdEncodingRule);
                }
                if(bl){
                    return Result.ok("批量删除成功!");
                }else{
                    return Result.ok("部分删除成功，被使用的不能删除!");
                }
            }else{
                return Result.error("删除失败,参数不正确!");
            }
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
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
