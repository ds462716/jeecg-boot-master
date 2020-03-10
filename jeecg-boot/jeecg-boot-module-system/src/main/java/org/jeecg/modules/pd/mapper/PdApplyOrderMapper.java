package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;

import java.util.List;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdApplyOrderMapper extends BaseMapper<PdApplyOrder> {

    List<PdApplyOrder> selectList(PdApplyOrder pdApplyOrder);

    List<PdApplyOrderPage> chooseApplyOrderList(PdApplyOrderPage applyOrderPage);

    /**
     * 增加发货数量 用于出库保存/提交 add by jiangxz 20200309
     *
     * @return
     */
    void additionArrivalCount(PdApplyOrder pdApplyOrder);

    /**
     * 扣减发货数量 用于出库拒绝 add by jiangxz 20200309
     *
     * @return
     */
    void subtractArrivalCount(PdApplyOrder pdApplyOrder);
}
