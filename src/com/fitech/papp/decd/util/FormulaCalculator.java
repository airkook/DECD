package com.fitech.papp.decd.util;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * 
 * FormulaCalculator
 * 
 * @author xyf
 * 
 */
public class FormulaCalculator {

    /**
     * 公式计算
     * 
     * @param formula
     * @param mapVars
     * @return
     * @throws Exception
     */
    public static BigDecimal calculate(String formula, Map<String, Number> mapVars) throws Exception {
        if (mapVars != null) {
            for (Map.Entry<String, Number> entry : mapVars.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                formula = formula.replaceAll(key, value);
            }
        }
        JexlEngine jexl = new JexlEngine();
        // Create an expression object
        String jexlExp = formula;
        Expression e = jexl.createExpression(jexlExp);

        // Create a context and add data
        JexlContext jc = new MapContext();

        // Now evaluate the expression, getting the result
        Object o = e.evaluate(jc);
        return new BigDecimal(o.toString());
    }

    /**
     * 公式检验
     * 
     * @param formula
     * @param mapVars
     * @return
     * @throws Exception
     */
    public static Boolean validate(String formula, Map<String, Object> mapVars) throws Exception {
        JexlEngine jexl = new JexlEngine();
        // Create an expression object
        String jexlExp = formula;
        Expression e = jexl.createExpression(jexlExp);

        // Create a context and add data
        JexlContext jc = new MapContext();
        if (mapVars != null) {
            for (Map.Entry<String, Object> entry : mapVars.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                jc.set(key, value);
            }
        }

        // Now evaluate the expression, getting the result
        Object o = e.evaluate(jc);
        return Boolean.parseBoolean(o.toString());
    }

}
