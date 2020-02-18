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

	/**
	 * 下拉组件 标题
	 */
	private String label;

	/**
	 * 下拉组件 值
	 */
	private String value;

	/**
	 * 下拉组件 联动ID，父组件值
	 */
	private String parent;


	private List<PdGoodsAllocationPage> children = new ArrayList<PdGoodsAllocationPage>();
}
