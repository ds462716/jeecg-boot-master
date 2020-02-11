package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.pd.entity.PdCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 产品分类
 * @Author: zxh
 * @Date:   2020-01-15
 * @Version: V1.0
 */
public interface PdCategoryMapper extends BaseMapper<PdCategory> {

    /**
     *   修改菜单状态字段： 是否子节点
     */
    @Update("update pd_category set is_leaf=#{leaf} where id = #{id}")
    int setMenuLeaf(@Param("id") String id,@Param("leaf") int leaf);

    void removeByCodeId(PdCategory pdCategory);

    List<PdCategory> selectCategoryOneList(PdCategory pdCategory);
}
