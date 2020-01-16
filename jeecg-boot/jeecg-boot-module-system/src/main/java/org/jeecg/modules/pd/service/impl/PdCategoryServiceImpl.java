package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdCategory;
import org.jeecg.modules.pd.mapper.PdCategoryMapper;
import org.jeecg.modules.pd.service.IPdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;

/**
 * @Description: 产品分类
 * @Author: zxh
 * @Date:   2020-01-15
 * @Version: V1.0
 */
@Service
public class PdCategoryServiceImpl extends ServiceImpl<PdCategoryMapper, PdCategory> implements IPdCategoryService {

    @Autowired
    private PdCategoryMapper pdCategoryMapper;

    /**
     * 添加一级或二级分类
     * @param pdCategory
     */
    @Override
    public void addPdCategory(PdCategory pdCategory) {

        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if(CommonConstant.MENU_TYPE_0.equals(pdCategory.getType())) {
            pdCategory.setParentId(null);
        }
        //----------------------------------------------------------------------
        String pid = pdCategory.getParentId();
        if(oConvertUtils.isNotEmpty(pid)) {
            //设置父节点不为叶子节点
            this.pdCategoryMapper.setMenuLeaf(pid, 0);
        }
        pdCategory.setCreateTime(new Date());
        pdCategory.setLeaf(true);
        this.save(pdCategory);

    }
    /**
     * 修改一级或二级分类
     * @param pdCategory
     */
    @Override
    public void editPdCategory(PdCategory pdCategory) {
        PdCategory p = this.getById(pdCategory.getId());
        if(p==null) {
            throw new JeecgBootException("未找到分类信息");
        }else{
            pdCategory.setUpdateTime(new Date());
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if(CommonConstant.MENU_TYPE_0.equals(pdCategory.getType())) {
                pdCategory.setParentId("");
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            int count = this.count(new QueryWrapper<PdCategory>().lambda().eq(PdCategory::getParentId, pdCategory.getId()));
            if(count==0) {
                pdCategory.setLeaf(true);
            }
            //----------------------------------------------------------------------
            this.updateById(pdCategory);

            //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
            String pid = pdCategory.getParentId();
            if((oConvertUtils.isNotEmpty(pid) && !pid.equals(p.getParentId())) || oConvertUtils.isEmpty(pid)&&oConvertUtils.isNotEmpty(p.getParentId())) {
                //a.设置新的父菜单不为叶子节点
                this.pdCategoryMapper.setMenuLeaf(pid, 0);
                //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                int cc = this.count(new QueryWrapper<PdCategory>().lambda().eq(PdCategory::getParentId, p.getParentId()));
                if(cc==0) {
                    if(oConvertUtils.isNotEmpty(p.getParentId())) {
                        this.pdCategoryMapper.setMenuLeaf(p.getParentId(), 1);
                    }
                }

            }

        }
    }
}
