package org.jeecg.modules.pd.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdRejected;
import org.jeecg.modules.pd.entity.PdRejectedDetail;
import org.jeecg.modules.pd.mapper.PdRejectedDetailMapper;
import org.jeecg.modules.pd.mapper.PdRejectedMapper;
import org.jeecg.modules.pd.service.IPdRejectedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Service
public class PdRejectedServiceImpl extends ServiceImpl<PdRejectedMapper, PdRejected> implements IPdRejectedService {

    @Autowired
    private PdRejectedMapper pdRejectedMapper;
    @Autowired
    private PdRejectedDetailMapper pdRejectedDetailMapper;

    @Override
    public void saveMain(PdRejected pdRejected, List<PdRejectedDetail> pdRejectedDetailList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdRejected.setRejectedDate(DateUtils.getDate("yyyy-MM-dd"));
        pdRejected.setDepartId(sysUser.getCurrentDepartId());
        pdRejected.setDepartParentId(sysUser.getDepartParentId());
        pdRejectedMapper.insert(pdRejected);

        if(CollectionUtils.isNotEmpty(pdRejectedDetailList)) {
            for (PdRejectedDetail detail : pdRejectedDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRejectedId(pdRejected.getId());
                detail.setDelFlag(PdConstant.DEL_FLAG_0);
                pdRejectedDetailMapper.insert(detail);
            }
        }
        // 处理退货库存
        // 保存退货日志记录
    }
}
