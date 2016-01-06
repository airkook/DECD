/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;

/**
 * @author Administrator
 * 
 */
public class ValidateUtil {

    /**
     * 长度校验
     * 
     * @param value
     * @param length
     * @param isPermitNull
     * @return
     */
    public static Boolean charlength(String value, String length, String isPermitNull) {
        // 非空校验
        isPermitNull = isPermitNull.replaceAll("\'", "").trim();
        if ("N".equals(isPermitNull) && StringUtil.isEmpty(value)) {
            return false;
        }
        // 长度校验
        length = length.replaceAll("\'", "").trim();
        int valueLength = StringUtil.length(value);
        // 范围性校验
        if (length.indexOf("-") > -1) {
            String[] lengthArea = length.split("-");
            int minLength = Integer.parseInt(lengthArea[0]);
            int maxLength = Integer.parseInt(lengthArea[1]);
            if (valueLength < minLength || valueLength > maxLength) {
                return false;
            }
        }
        // 长度校验
        else if (length.indexOf("=") > -1) {
            length = length.substring(1);
            int validateLength = Integer.parseInt(length);
            if (valueLength != validateLength) {
                return false;
            }
        }
        // 最大长度校验
        else {
            int validateLength = Integer.parseInt(length);
            if (valueLength > validateLength) {
                return false;
            }
        }
        return true;
    }

    /**
     * 日期格式校验
     * 
     * @param value
     * @param format
     * @param isPermitNull
     * @return
     */
    public static Boolean dateformat(String value, String format, String isPermitNull) {
        // 非空校验
        isPermitNull = isPermitNull.replaceAll("\'", "").trim();
        if ("N".equals(isPermitNull) && StringUtil.isEmpty(value)) {
            return false;
        }
        if (StringUtil.isEmpty(value)) {
            return true;
        }
        // 日期格式校验
        format = format.replaceAll("\'", "").trim();
        if (value.length() != format.length()) {
            return false;
        }
        Date date = DateUtil.getFormatDate(value, format);
        if (date == null) {
            return false;
        }
        return true;
    }

    /**
     * 字符串包含校验
     * 
     * @param value
     * @param contain
     * @param isPermitNull
     * @return
     */
    public static Boolean charcontain(String value, String contain, String isPermitNull) {
        // 非空校验
        isPermitNull = isPermitNull.replaceAll("\'", "").trim();
        if ("N".equals(isPermitNull) && StringUtil.isEmpty(value)) {
            return false;
        }
        if (StringUtil.isEmpty(value)) {
            return true;
        }
        // 字符串包含校验
        contain = contain.replaceAll("\'", "").trim();
        String[] items = contain.split("\\|");
        // 如果是多选以、相隔
        String[] valueArr = value.split("、");
        for (String val : valueArr) {
            Boolean flag = false;
            for (String item : items) {
                if (item.equals(val)) {
                    flag = true;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数字长度校验
     * 
     * @param value
     * @param ilength
     * @param dlength
     * @param isPermitNull
     * @return
     */
    public static Boolean numlength(String value, String ilength, String dlength, String isPermitNull) {
        // 非空校验
        isPermitNull = isPermitNull.replaceAll("\'", "").trim();
        if ("N".equals(isPermitNull) && StringUtil.isEmpty(value)) {
            return false;
        }
        if (StringUtil.isEmpty(value)) {
            return true;
        }
        // 数字长度校验
        // 先判断是否为数字，不是则返回false
        try {
            BigDecimal bvalue = new BigDecimal(value);
        }
        catch (Exception e) {
            return false;
        }
        String ivalue = "";
        String dvalue = "";
        if (value.indexOf(".") > -1) {
            ivalue = value.substring(0, value.indexOf("."));
            dvalue = value.substring(value.indexOf(".") + 1);
        }
        else {
            ivalue = value;
        }
        int ilen = Integer.parseInt(ilength);
        int dlen = Integer.parseInt(dlength);
        // 判断整数位，标-1则不做判断
        if (StringUtil.length(ivalue) > ilen && ilen != -1) {
            return false;
        }
        // 判断小数位，标-1则不做判断
        if (StringUtil.length(dvalue) > dlen && dlen != -1) {
            return false;
        }
        return true;
    }

    /**
     * 字符串匹配校验
     * 
     * @param value
     * @param flag
     * @param instr
     * @return
     */
    public static Boolean charfilter(String value, String flag, String instr) {
        // 这里不做非空校验，value为空时返回true
        if (StringUtil.isEmpty(value) || StringUtil.isEmpty(instr)) {
            return true;
        }
        flag = flag.replaceAll("\'", "").trim();
        instr = instr.replaceAll("\'", "").trim();
        String[] items = instr.split("\\|");
        // 开头校验
        if ("start".equals(flag.toLowerCase())) {
            Boolean existFlag = false;
            for (String item : items) {
                if (value.startsWith(item)) {
                    existFlag = true;
                }
            }
            if (!existFlag) {
                return false;
            }
        }
        // 结尾校验
        if ("end".equals(flag.toLowerCase())) {
            Boolean existFlag = false;
            for (String item : items) {
                if (value.endsWith(item)) {
                    existFlag = true;
                }
            }
            if (!existFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 基于某个字段的校验
     * 
     * @param value
     * @param baseValue
     * @param valiValue
     * @param formu
     * @return
     */
    public static Boolean basevalue(String value, String baseValue, String valiValue, String formu) {
        valiValue = valiValue.replaceAll("\'", "").trim();
        if (baseValue == null) {
            baseValue = "";
        }
        // 基于某个字段的内容为某值时
        if (baseValue.equals(valiValue)) {
            formu = formu.replaceAll("\'", "").trim();
            // 非空
            if ("N".equals(formu) && StringUtil.isEmpty(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 条件型校验
     * 
     * @param ifFormula
     * @param thenFormu
     * @param mapVars
     * @return
     * @throws Exception
     */
    public static Boolean ifthen(String ifFormula, String thenFormu, Map<String, Object> mapVars) throws Exception {
        // 去掉前后的'号
        ifFormula = ifFormula.substring(1, ifFormula.length() - 1);
        thenFormu = thenFormu.substring(1, thenFormu.length() - 1);
        // 开始校验
        Boolean ifFlag = FormulaCalculator.validate(ifFormula, mapVars);
        if (ifFlag) {
            Boolean thenFlag = FormulaCalculator.validate(thenFormu, mapVars);
            if (!thenFlag) {
                return false;
            }
        }
        return true;
    }
}
