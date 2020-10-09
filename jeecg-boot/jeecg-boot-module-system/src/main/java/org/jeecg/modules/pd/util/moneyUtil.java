package org.jeecg.modules.pd.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class moneyUtil {

    public static void main(String[] args) {
        // 具体的金额（单位元）
        String value = "88000067898";
        BigDecimal bigDecimal = new BigDecimal(value);
        // 转换为万元（除以10000）
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        // 保留两位小数
        DecimalFormat formater = new DecimalFormat("0");
        // 四舍五入
        formater.setRoundingMode(RoundingMode.HALF_UP);

        // 格式化完成之后得出结果
        String formatNum = formater.format(decimal);
        //System.out.println(formatNum);
        System.out.println(getNumberWan("88000067898"));
        System.out.println(getNumberWan("0"));
    }

    /**元转万元且四舍五入取整*/
    public static Double getNumberWan(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        // 转换为万元（除以10000）
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        double   f1   =   decimal.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
