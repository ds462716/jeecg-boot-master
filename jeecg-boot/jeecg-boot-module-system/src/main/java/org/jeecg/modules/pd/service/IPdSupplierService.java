package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.model.PdSupplierTreeModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface IPdSupplierService extends IService<PdSupplier> {

    List<PdSupplier> verify(PdSupplier pdSupplier);

    List<PdSupplier> selectList(PdSupplier pdSupplier);

    List<PdSupplier> selectAllList(PdSupplier pdSupplier);
    /**
     * 修改证照有效期标识
     */
    void updateValidityFlag(PdSupplier pdSupplier);

    Page<PdSupplier> selectList(Page<PdSupplier> pageList, PdSupplier pdSupplier);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);

    Result<Object> importExcel(Map<String, MultipartFile> fileMap);

    List<PdSupplierTreeModel> queryTreeList(PdSupplier pdSupplier);
}
