package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdCategory;
import org.jeecg.modules.pd.mapper.PdCategoryMapper;
import org.jeecg.modules.pd.service.IPdCategoryService;
import org.jeecg.modules.system.entity.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;

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

    /**
     * 删除分类及他的所有子类
     * @param id
     */
    @Override
    public void removePdCategory(String id) {
        PdCategory pdCategory = this.getById(id);
        if(pdCategory==null) {
            throw new JeecgBootException("未找到分类信息");
        }
        String pid = pdCategory.getParentId();
        int count = this.count(new QueryWrapper<PdCategory>().lambda().eq(PdCategory::getParentId, pid));
        if(count==1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            this.pdCategoryMapper.setMenuLeaf(pid, 1);
        }
        this.pdCategoryMapper.removeByCodeId(pdCategory);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenBy(pdCategory.getId());
    }

    /**
     * 根据父id删除其关联的子节点数据
     *
     * @return
     */
    public void removeChildrenBy(String parentId) {
        LambdaQueryWrapper<PdCategory> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(PdCategory::getParentId, parentId);
        // 查出该主键下的所有子级
        List<PdCategory> pdCategoryList = this.list(query);
        if (pdCategoryList != null && pdCategoryList.size() > 0) {
            String id = ""; // id
            int num = 0; // 查出的子级数量
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = pdCategoryList.size(); i < len; i++) {
                id = pdCategoryList.get(i).getId();
                num = this.count(new LambdaQueryWrapper<PdCategory>().eq(PdCategory::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenBy(id);
                }
            }
        }
    }
}
