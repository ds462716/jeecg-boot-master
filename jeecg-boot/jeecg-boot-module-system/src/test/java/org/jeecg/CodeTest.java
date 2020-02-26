package org.jeecg;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.util.BarCodeUtil;

import java.util.HashMap;
import java.util.Map;

public class CodeTest {

    public static void main(String[] args) {

        Map<String,Object> map = new HashMap<>();
        //HIBC测试
        BarCodeUtil.scanCode("+J123123451","+122713C001L5",map);
        BarCodeUtil.scanCode("+J123123451/122713C001L5","+J123123451/122713C001L5",map);
        BarCodeUtil.scanCode("+J123123451","+$3C001LV",map);
        BarCodeUtil.scanCode("+J123123451/$3C001LV","+J123123451/$3C001LV",map);
        BarCodeUtil.scanCode("+J123123451","+$$02123C001LW",map);
        BarCodeUtil.scanCode("+J123123451/$$02123C001LW","+J123123451/$$02123C001LW",map);
        BarCodeUtil.scanCode("+J123123451","+$$12123C001LW",map);
        BarCodeUtil.scanCode("+J123123451/$$12123C001LW","+J123123451/$$12123C001LW",map);
        BarCodeUtil.scanCode("+J123123451","+$$20215123C001L/",map);
        BarCodeUtil.scanCode("+J123123451/$$20215123C001L/","+J123123451/$$20215123C001L/",map);
        BarCodeUtil.scanCode("+J123123451","+$$31202153C001L+",map);
        BarCodeUtil.scanCode("+J123123451/$$31202153C001L+","+J123123451/$$31202153C001L+",map);
        BarCodeUtil.scanCode("+J123123451","+$$4120216113C001L2",map);
        BarCodeUtil.scanCode("+J123123451/$$4120216113C001L2","+J123123451/$$4120216113C001L2",map);
        BarCodeUtil.scanCode("+J123123451","+$$5122713C001L2",map);
        BarCodeUtil.scanCode("+J123123451/$$5122713C001L2","+J123123451/$$5122713C001L2",map);
        BarCodeUtil.scanCode("+J123123451","+$$612271113C001L5 ",map);
        BarCodeUtil.scanCode("+J123123451/$$612271113C001L5 ","+J123123451/$$612271113C001L5 ",map);
        BarCodeUtil.scanCode("+J123123451","+$$73C001LY  ",map);
        BarCodeUtil.scanCode("+J123123451/$$73C001LY  ","+J123123451/$$73C001LY  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$82402123C001L3  ",map);
        BarCodeUtil.scanCode("+J123123451/$$82402123C001L3  ","+J123123451/$$82402123C001L3  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$82412123C001L3  ",map);
        BarCodeUtil.scanCode("+J123123451/$$82412123C001L3  ","+J123123451/$$82412123C001L3  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$82420216123C001LC  ",map);
        BarCodeUtil.scanCode("+J123123451/$$82420216123C001LC  ","+J123123451/$$82420216123C001LC  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$82431202163C001LD  ",map);
        BarCodeUtil.scanCode("+J123123451/$$82431202163C001LD  ","+J123123451/$$82431202163C001LD  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$8244120216183C001LN  ",map);
        BarCodeUtil.scanCode("+J123123451/$$8244120216183C001LN  ","+J123123451/$$8244120216183C001LN  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$8245122713C001LG  ",map);
        BarCodeUtil.scanCode("+J123123451/$$8245122713C001LG  ","+J123123451/$$8245122713C001LG  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$824612271183C001LQ  ",map);
        BarCodeUtil.scanCode("+J123123451/$$824612271183C001LQ  ","+J123123451/$$824612271183C001LQ  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$82473C001L5  ",map);
        BarCodeUtil.scanCode("+J123123451/$$82473C001L5  ","+J123123451/$$82473C001L5  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$824LP  ",map);
        BarCodeUtil.scanCode("+J123123451/$$824LP  ","+J123123451/$$824LP  ",map);
        BarCodeUtil.scanCode("+J123123451","+$$90010009953C001LH  ",map);
        BarCodeUtil.scanCode("+J123123451/$$90010009953C001LH  ","+J123123451/$$90010009953C001LH  ",map);
        BarCodeUtil.scanCode("+J123123451","/$$31202153C001L+",map);
        BarCodeUtil.scanCode("+J123123451","+$$31202153C001L+",map);
        BarCodeUtil.scanCode("+J123123451/$$31202153C001L+","+J123123451/$$31202153C001L+",map);
        BarCodeUtil.scanCode("+A123BJC5D6E71G","+83278f8G9h0j2G",map);

        //GS1扫码测试
        BarCodeUtil.scanCode("01006139947416081719011421NWU092663G","01006139947416081719011421NWU092663G",map);
        BarCodeUtil.scanCode("010064316926546217191212100008918409","010064316926546217191212100008918409",map);
        BarCodeUtil.scanCode("010064316926540017191218100008926988","010064316926540017191218100008926988",map);
        BarCodeUtil.scanCode("01045473270852361721033130110180404A48A","01045473270852361721033130110180404A48A",map);
        BarCodeUtil.scanCode("01245473270618141720013130110170131K011","01245473270618141720013130110170131K011",map);
        BarCodeUtil.scanCode("0104987350625717","1720090010171011",map);
        BarCodeUtil.scanCode("0106936775502064","171807253011002160102",map);
        BarCodeUtil.scanCode("0104987350369116","1720070010170804",map);
        BarCodeUtil.scanCode("0108717648073854","17191231108011771",map);
        BarCodeUtil.scanCode("0106942180385123","17191201108512012891001",map);
        //System.out.println(getSubCount("+J123123451+$$90010009953C001LH++","+"));

		System.out.println(BarCodeUtil.getPrdNumber("+J12312341/"));
		System.out.println(BarCodeUtil.getPrdNumber("+J123123451"));
		System.out.println(BarCodeUtil.getPrdNumber("+J1231234+/"));
		System.out.println(BarCodeUtil.getPrdNumber("9320200222085037656"));
		System.out.println(BarCodeUtil.GetEANUPN("93202002220850376561718061910180616061"));
		System.out.println(BarCodeUtil.getPrdNumber("01006139947416081719011421NWU092663G"));
		System.out.println(BarCodeUtil.getPrdNumber("010064316926546217191212100008918409"));
		System.out.println(BarCodeUtil.getPrdNumber("010064316926540017191218100008926988"));
		System.out.println(BarCodeUtil.getPrdNumber("01045473270852361721033130110180404A48A"));
		System.out.println(BarCodeUtil.getPrdNumber("01245473270618141720013130110170131K011"));
		System.out.println(BarCodeUtil.getPrdNumber("0104987350625717"));
		System.out.println(BarCodeUtil.getPrdNumber("0106936775502064"));
		System.out.println(BarCodeUtil.getPrdNumber("0104987350369116"));
		System.out.println(BarCodeUtil.getPrdNumber("0108717648073854"));
		System.out.println(BarCodeUtil.getPrdNumber("0106942180385123"));
		System.out.println(BarCodeUtil.getPrdNumber("6938777608669"));
		System.out.println(BarCodeUtil.getPrdNumber("8033837912460"));
		System.out.println(BarCodeUtil.getPrdNumber("+J123123451"));
		System.out.println(BarCodeUtil.getPrdNumber("+J123123451+122713C001L/"));
		System.out.println(BarCodeUtil.getPrdNumber("+J1231234+/+122713C001L/"));
		System.out.println(BarCodeUtil.getPrdNumber("+J1231234+/"));
		System.out.println(BarCodeUtil.getPrdNumber("+J123123411+"));
		System.out.println(BarCodeUtil.getPrdNumber("+J12312341++"));
		System.out.println(BarCodeUtil.getPrdNumber("+J12312341+1"));
		System.out.println(BarCodeUtil.getPrdNumber("+J12312341/1"));

        System.out.println(DateUtils.str2Date("2018-08-09",DateUtils.date_sdf.get()));
    }
}
