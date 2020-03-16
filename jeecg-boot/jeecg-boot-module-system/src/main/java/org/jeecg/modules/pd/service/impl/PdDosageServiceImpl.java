package org.jeecg.modules.pd.service.impl;

import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdGoodsAllocation;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdDosageMapper;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdDosageService;
import org.jeecg.modules.pd.service.IPdGoodsAllocationService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.shiro.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用量表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Service
public class PdDosageServiceImpl extends ServiceImpl<PdDosageMapper, PdDosage> implements IPdDosageService {

    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdGoodsAllocationService pdGoodsAllocationService;

    @Override
    public PdDosage initModal(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());
        PdDosage pdDosage = new PdDosage();
        //部门列表
        SysDepart query = new SysDepart();
        query.setDepartParentId(sysUser.getDepartParentId());
        query.setDepartId(sysUser.getCurrentDepartId());

        if (oConvertUtils.isNotEmpty(id)) { // 查看页面
            pdDosage = this.getById(id);

        } else {  // 新增页面
            pdDosage.setDepartId(sysDepart.getId());
            pdDosage.setDepartName(sysDepart.getDepartName());
            //获取出库单号
            pdDosage.setDosageNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_YL));
            //获取当前日期
            pdDosage.setDosageDate(DateUtils.getDate());
            //登录人姓名
            pdDosage.setDosageBy(sysUser.getId());
            pdDosage.setDosageByName(sysUser.getRealname());
            PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
            pdGoodsAllocation.setDepartId(sysUser.getCurrentDepartId());
            pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
            List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
            //库区库位下拉框
            pdDosage.setGoodsAllocationList(goodsAllocationList);
        }
        return pdDosage;

    }
}
