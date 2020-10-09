package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdDistributor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description: 配送商
 * @Author: jiangxz
 * @Date:   2020年7月17日 11:34:42
 * @Version: V1.0
 */
public interface IPdDistributorService extends IService<PdDistributor> {

    List<PdDistributor> verify(PdDistributor pdDistributor);

    List<PdDistributor> selectList(PdDistributor pdDistributor);

    List<PdDistributor> selectAllList(PdDistributor pdDistributor);
    /**
     * 修改证照有效期标识
     */
    void updateValidityFlag(PdDistributor pdDistributor);

    Page<PdDistributor> selectList(Page<PdDistributor> pageList, PdDistributor pdDistributor);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);

    Result<Object> importExcel(Map<String, MultipartFile> fileMap);
}
