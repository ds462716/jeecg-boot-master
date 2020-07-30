package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.ExLabInstrInf;
import org.jeecg.modules.external.entity.ExLabPurpose;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;
import org.jeecg.modules.pd.entity.PdDosage;

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
    IPage<HisChargeInf> selectList(Page<HisChargeInf> pageList, HisChargeInf hisChargeInf);

    List<HisChargeInf> selectListForFCRMYY(HisChargeInf hisChargeInf);

    IPage<HisChargeInf> selectListForFCRMYY(Page<HisChargeInf> pageList, HisChargeInf hisChargeInf);

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

    //获取检验项目明细--HIS
    public List<ExInspectionItems> selectExjianYanHis(ExInspectionItems exInspectionItems);

    //获取检验项目明细--LIS
    public List<ExInspectionItems> selectExjianYanLis(ExInspectionItems exInspectionItems);

    //获取检验项目明细(复查类)--LIS
    public List<ExInspectionItems> selectExjianYanLisFc(ExInspectionItems exInspectionItems);

    /*查询his系统门诊病人退费信息*/
    String  queryMztfList(PdDosage pdDosage);

   /*查询LIS检验目的信息*/
    List<ExLabPurpose> selectExLabPurpose(ExLabPurpose exLabPurpose);

    /*查询LIS系统仪器设备信息*/
    List<ExLabInstrInf> selectExLabInstrInf();

    boolean deleteByDepartParentId(String departParentId);
}
