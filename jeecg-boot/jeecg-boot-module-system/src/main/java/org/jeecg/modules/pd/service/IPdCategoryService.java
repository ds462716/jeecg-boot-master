package org.jeecg.modules.pd.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 产品分类
 * @Author: zxh
 * @Date:   2020-01-15
 * @Version: V1.0
 */
public interface IPdCategoryService extends IService<PdCategory> {

    void addPdCategory(PdCategory pdCategory);

    void editPdCategory(PdCategory pdCategory);

    List<PdCategory> selectCategoryOneList(PdCategory pdCategory);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);
}
