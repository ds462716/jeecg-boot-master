package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.external.entity.ExLabInstrInf;
import org.jeecg.modules.external.mapper.ExLabInstrInfMapper;
import org.jeecg.modules.external.service.IExLabInstrInfService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Service
public class ExLabInstrInfServiceImpl extends ServiceImpl<ExLabInstrInfMapper, ExLabInstrInf> implements IExLabInstrInfService {
    @Autowired
    private ExLabInstrInfMapper exLabInstrInfMapper;
    @Autowired
    private IPdDepartService pdDepartService;

    @Override
    public Page<ExLabInstrInf> selectList(Page<ExLabInstrInf> page, ExLabInstrInf exLabInstrInf) {
        Page<ExLabInstrInf> pg = page.setRecords(exLabInstrInfMapper.selectList(exLabInstrInf));
        List<ExLabInstrInf> labInstrInf = pg.getRecords();
        if(labInstrInf!=null && labInstrInf.size()>0){
            //查询所有部门
            List<SysDepart> sysDeparts = pdDepartService.selectListTree(new SysDepart());
            //将部门转换成map
            Map<String, String> map = sysDeparts.stream().collect(Collectors.toMap(SysDepart::getId, SysDepart::getDepartName, (key1, key2) -> key2));
            //拼接成部门名称列表用
            for(ExLabInstrInf entity :labInstrInf){
                String testDepartId=entity.getTestDepartId();
                if(StringUtils.isNotEmpty(testDepartId)){
                    List<String> ids = Arrays.asList(entity.getTestDepartId().split(","));
                    if(ids!=null && ids.size()>0){
                        String testDepartNames = "";
                        for(String idKey:ids){
                            testDepartNames+=map.get(idKey)+",";
                        }
                        if(testDepartNames.endsWith(",")){
                            testDepartNames = testDepartNames.substring(0,testDepartNames.length()-1);
                        }
                        entity.setTestDepartNames(testDepartNames);
                    }
                }
            }
        }
        return pg;
    }

    @Override
    public List<ExLabInstrInf> getExLabInstrInf(ExLabInstrInf exLabInstrInf) {
        return baseMapper.getExLabInstrInf(exLabInstrInf);
    }


    @Override
    @Transactional
    public void synUpdateInstrInf(List<ExLabInstrInf> labInstrInfList) {
        if (labInstrInfList != null && labInstrInfList.size() > 0) {
            for(ExLabInstrInf inf: labInstrInfList){
                ExLabInstrInf exLabInstrInf=exLabInstrInfMapper.getOne(inf);
                if(exLabInstrInf==null){
                    exLabInstrInfMapper.insert(inf);
                }
            }
        }
    }
}
