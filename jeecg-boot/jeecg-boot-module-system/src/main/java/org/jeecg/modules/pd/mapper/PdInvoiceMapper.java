package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdInvoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_invoice
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
public interface PdInvoiceMapper extends BaseMapper<PdInvoice> {

    public boolean deleteById(@Param("id") String id);

}
