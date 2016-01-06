package com.fitech.framework.core.util;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ExcelExportInfo {

    /** 工作薄 */
    private HSSFWorkbook wb;

    /** key:sheet名称 val: sheet里的数据 */
    private LinkedHashMap<String, List> objsMap;

    /** 每个sheet里的表头的每一列的名称 */
    private List<String[]> headNames;

    /** 每个sheet里的每行数据的对象的属性 */
    private List<String[]> fieldNames;

    /** 每个sheet里的每行数据的需要红色背景标识的对象的属性 */
    private List<String[]> redNames;

    /** Excel输出流 */
    private OutputStream out;

    /** CSV输出路径 */
    private String path;

    /** 表头行样式 */
    private CellStyle headStyle;

    /** 表头行字体 */
    private Font headFont;

    /** 红色单元格样式 */
    private CellStyle redStyle;

    /** 每个sheet里的第二列要显示的公式类型，在 typeDispFlag为true时使用 */
    private List<String> typeNames;

    /** 是否需要每个sheet里的第二列要显示的公式类型，默认不显示 */
    private boolean typeDispFlag = false;

    /** 数据类型，默认为bean的list (1:数据类型为bean的list 2：数据类型为object[]的list) */
    private Integer dataType = 1;

    /** 每个sheet里的每行数据的需要红色背景标识的对象的下标，在dataType为2时使用 */
    private List<Integer[]> redIndexs;

    /**
     * wb
     * 
     * @return the wb
     */

    public HSSFWorkbook getWb() {
        return wb;
    }

    /**
     * @param wb the wb to set
     */
    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }

    /**
     * objsMap
     * 
     * @return the objsMap
     */

    public LinkedHashMap<String, List> getObjsMap() {
        return objsMap;
    }

    /**
     * @param objsMap the objsMap to set
     */
    public void setObjsMap(LinkedHashMap<String, List> objsMap) {
        this.objsMap = objsMap;
    }

    /**
     * headNames
     * 
     * @return the headNames
     */

    public List<String[]> getHeadNames() {
        return headNames;
    }

    /**
     * @param headNames the headNames to set
     */
    public void setHeadNames(List<String[]> headNames) {
        this.headNames = headNames;
    }

    /**
     * fieldNames
     * 
     * @return the fieldNames
     */

    public List<String[]> getFieldNames() {
        return fieldNames;
    }

    /**
     * @param fieldNames the fieldNames to set
     */
    public void setFieldNames(List<String[]> fieldNames) {
        this.fieldNames = fieldNames;
    }

    /**
     * out
     * 
     * @return the out
     */

    public OutputStream getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(OutputStream out) {
        this.out = out;
    }

    /**
     * headStyle
     * 
     * @return the headStyle
     */

    public CellStyle getHeadStyle() {
        return headStyle;
    }

    /**
     * @param headStyle the headStyle to set
     */
    public void setHeadStyle(CellStyle headStyle) {
        this.headStyle = headStyle;
    }

    /**
     * headFont
     * 
     * @return the headFont
     */

    public Font getHeadFont() {
        return headFont;
    }

    /**
     * @param headFont the headFont to set
     */
    public void setHeadFont(Font headFont) {
        this.headFont = headFont;
    }

    /**
     * redStyle
     * 
     * @return the redStyle
     */

    public CellStyle getRedStyle() {
        return redStyle;
    }

    /**
     * @param redStyle the redStyle to set
     */
    public void setRedStyle(CellStyle redStyle) {
        this.redStyle = redStyle;
    }

    /**
     * redNames
     * 
     * @return the redNames
     */

    public List<String[]> getRedNames() {
        return redNames;
    }

    /**
     * @param redNames the redNames to set
     */
    public void setRedNames(List<String[]> redNames) {
        this.redNames = redNames;
    }

    /**
     * typeNames
     * 
     * @return the typeNames
     */

    public List<String> getTypeNames() {
        return typeNames;
    }

    /**
     * @param typeNames the typeNames to set
     */
    public void setTypeNames(List<String> typeNames) {
        this.typeNames = typeNames;
    }

    /**
     * typeDispFlag
     * 
     * @return the typeDispFlag
     */

    public boolean isTypeDispFlag() {
        return typeDispFlag;
    }

    /**
     * @param typeDispFlag the typeDispFlag to set
     */
    public void setTypeDispFlag(boolean typeDispFlag) {
        this.typeDispFlag = typeDispFlag;
    }

    /**
     * dataType
     * 
     * @return the dataType
     */

    public Integer getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * redIndexs
     * 
     * @return the redIndexs
     */

    public List<Integer[]> getRedIndexs() {
        return redIndexs;
    }

    /**
     * @param redIndexs the redIndexs to set
     */
    public void setRedIndexs(List<Integer[]> redIndexs) {
        this.redIndexs = redIndexs;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
