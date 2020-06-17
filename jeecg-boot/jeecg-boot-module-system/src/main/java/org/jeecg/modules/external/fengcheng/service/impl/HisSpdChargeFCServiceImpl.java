package org.jeecg.modules.external.fengcheng.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.entity.HisSpdChargeFC;
import org.jeecg.modules.external.fengcheng.mapper.HisSpdChargeFCMapper;
import org.jeecg.modules.external.fengcheng.service.IHisSpdChargeFCService;
import org.jeecg.modules.external.fengcheng.service.IPdDosageFCZYYService;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCZhongyiUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdDosageMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jiangxz
 * @description 丰城中医院计费
 * @date 2020-5-26
 */
@Service
public class HisSpdChargeFCServiceImpl extends ServiceImpl<HisSpdChargeFCMapper, HisSpdChargeFC> implements IHisSpdChargeFCService {

    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdGoodsAllocationService pdGoodsAllocationService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdDosageDetailService pdDosageDetailService;
    @Autowired
    private IPdStockLogService pdStockLogService;
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private PdDosageMapper pdDosageMapper;

    private static Logger logger = LoggerFactory.getLogger(HisSpdChargeFCServiceImpl.class);


}
