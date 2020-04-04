package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdVender;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface IPdVenderService extends IService<PdVender> {

    List<PdVender> verify(PdVender pdVender);

    List<PdVender> selectList(PdVender pdVender);

    List<PdVender> selectAllList(PdVender pdVender);

    /**
     * 修改证照有效期标识
     */
    void updateValidityFlag(PdVender pdVender);

    Page<PdVender> selectList(Page<PdVender> pageList, PdVender pdVender);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);


    Result<Object> importExcel(Map<String, MultipartFile> fileMap);
}
