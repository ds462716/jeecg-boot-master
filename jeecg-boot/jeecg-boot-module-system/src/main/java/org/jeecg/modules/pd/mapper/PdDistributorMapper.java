package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdDistributor;

import java.util.List;

/**
 * @Description: 配送商
 * @Author: jiangxz
 * @Date:   2020年7月17日 11:34:42
 * @Version: V1.0
 */
public interface PdDistributorMapper extends BaseMapper<PdDistributor> {

    List<PdDistributor> verify(PdDistributor pdDistributor);

    List<PdDistributor> selectList(PdDistributor pdDistributor);

    void updateValidityFlag(PdDistributor pdDistributor);

    List<PdDistributor> selectAllList(PdDistributor pdDistributor);

    Page<PdDistributor> selectListByPage(Page<PdDistributor> page, @Param("entity") PdDistributor entity);
}
