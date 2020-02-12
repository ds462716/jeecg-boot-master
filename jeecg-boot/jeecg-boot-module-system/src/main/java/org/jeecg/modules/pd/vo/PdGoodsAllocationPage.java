package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecg.modules.pd.entity.PdGoodsAllocation;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 货区货位表
 * @Author: jiangxz
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class PdGoodsAllocationPage extends PdGoodsAllocation {

	/**部门名称*/
	private String departName;

	private List<PdGoodsAllocationPage> children = new ArrayList<PdGoodsAllocationPage>();
}
