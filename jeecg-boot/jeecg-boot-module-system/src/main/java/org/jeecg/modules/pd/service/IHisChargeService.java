package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.vo.ExHisMzInfPage;

import java.util.List;

/**
 * @Description: HIS系统接口相关服务
 * @Author: jeecg-boot
 * @Date:  2020-4-29
 * @Version: V1.0
 */
public interface IHisChargeService extends IService<HisChargeInf> {

    /**
     * 分页查询
     * @param pageList
     * @param hisChargeInf
     * @return
     */
    Page<HisChargeInf> selectList(Page<HisChargeInf> pageList, HisChargeInf hisChargeInf);

   /*查询his系统收费项目基础信息*/
	List<HisChargeInf> selectByHisCharge();

    /*删除后保存所有收费项目基础信息*/
    void saveMain(List<HisChargeInf> list);

    /*查询his系统科室信息*/
    List<HisDepartInf> selectHisDepart();

    /*查询his系统用户信息*/
    List<HisUserInf> selectHisUser();

    /*查询his系统病人信息*/
    List<PdDosage> queryPatientInfoList(PdDosage pdDosage);

    //计费信息插入HIS中间表(门诊)
    public int saveExHisMzInf(ExHisMzInfPage exHisMzInf);

    //计费信息插入HIS中间表(住院)
    public int saveExHisZyInf(PdDosage pdDosage, List<PdDosageDetail> chargeArray);

    //获取检验项目明细
    public List<ExInspectionItems> selectExjianYan(ExInspectionItems exInspectionItems);


}
