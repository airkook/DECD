package com.fitech.framework.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * 
 * ClassName: ExcelUtil
 * 
 * @Description: Excel操作工具
 * @author xuyf
 * @date 2015-3-29
 */
public class ExcelUtil {

    /** 数据类型为bean的list */
    public static Integer DATATYPE_BEAN = 1;

    /** 数据类型为object[]的list */
    public static Integer DATATYPE_OBJARR = 2;

    /**
     * 
     * export2Excel(根据设置信息和数据导出EXCEL)
     * 
     * @param setInfo
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author xuyf
     */
    public static void export2Excel(ExcelExportInfo setInfo) throws IOException, IllegalArgumentException,
            IllegalAccessException {
        // 初始化
        init(setInfo);
        // 获取Sheet名称
        Set<Entry<String, List>> set = setInfo.getObjsMap().entrySet();
        String[] sheetNames = new String[setInfo.getObjsMap().size()];
        int sheetNameNum = 0;
        for (Entry<String, List> entry : set) {
            sheetNames[sheetNameNum] = entry.getKey();
            sheetNameNum++;
        }
        // 创建所有Sheet
        HSSFSheet[] sheets = getSheets(setInfo.getWb(), setInfo.getObjsMap().size(), sheetNames);
        int sheetNum = 0;
        // 循环Sheet
        for (Entry<String, List> entry : set) {
            // 创建表头
            creatTableHeadRow(setInfo, sheets, sheetNum, setInfo.isTypeDispFlag());
            // 创建表体并插入数据
            String[] fieldNames = setInfo.getFieldNames() == null || setInfo.getFieldNames().size() == 0 ? null
                    : setInfo.getFieldNames().get(sheetNum);
            String[] redNames = setInfo.getRedNames() == null || setInfo.getRedNames().size() == 0 ? null : setInfo
                    .getRedNames().get(sheetNum);
            Integer[] redIndexs = setInfo.getRedIndexs() == null || setInfo.getRedIndexs().size() == 0 ? null : setInfo
                    .getRedIndexs().get(sheetNum);
            List objs = entry.getValue();
            int rowNum = 1; // 去掉表头，所以从1开始
            for (Object obj : objs) {
                // 循环插入每行数据
                HSSFRow contentRow = sheets[sheetNum].createRow(rowNum);
                // contentRow.setHeight((short) 300);
                HSSFCell[] cells = getCells(contentRow, setInfo.getFieldNames().get(sheetNum).length, setInfo
                        .isTypeDispFlag());
                int cellNum = 0; // 去掉一列序号，因此从1开始
                if (setInfo.isTypeDispFlag()) {// 插入公式类型
                    String typeName = setInfo.getTypeNames().get(sheetNum);
                    cells[1].setCellValue(typeName == null ? "" : typeName);
                    cellNum = 1;
                }
                if (fieldNames != null) {
                    for (int num = 0; num < fieldNames.length; num++) {
                        Object value = null;
                        try {
                            if (DATATYPE_OBJARR.equals(setInfo.getDataType())) {
                                Object[] objArr = (Object[]) obj;
                                value = objArr[num];
                            }
                            else {
                                value = ReflectionUtils.invokeGetterMethod(obj, fieldNames[num]);
                            }
                        }
                        catch (Exception e) {
                            value = null;
                        }
                        cells[cellNum].setCellValue(value == null ? "" : value.toString());
                        cells[cellNum].setCellStyle(setInfo.getHeadStyle());
                        cellNum++;
                    }
                }
                rowNum++;
            }
            // 自动调整列宽
            adjustColumnSize(sheets, sheetNum, fieldNames, setInfo.isTypeDispFlag());
            sheetNum++;
        }
        setInfo.getWb().write(setInfo.getOut());
    }

    /**
     * 
     * export2CSV(根据设置信息和数据导出CSV)
     * 
     * @param setInfo
     * @author xuyf
     * @throws Exception
     */
    public static void export2CSV(ExcelExportInfo setInfo) throws Exception {
        if (StringUtil.isEmpty(setInfo.getPath())) {
            return;
        }
        String[] headNames = setInfo.getHeadNames() == null || setInfo.getHeadNames().size() == 0 ? null : setInfo
                .getHeadNames().get(0);
        if (headNames == null || headNames.length == 0) {
            return;
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(setInfo.getPath())));
        String head = "";
        for (String headName : headNames) {
            head = head + headName + ",";
        }
        head = head.substring(0, head.length() - 1);
        bw.write(head);
        bw.write("\r\n");
        for (Entry<String, List> entry : setInfo.getObjsMap().entrySet()) {
            List objs = entry.getValue();
            String[] fieldNames = setInfo.getFieldNames() == null || setInfo.getFieldNames().size() == 0 ? null
                    : setInfo.getFieldNames().get(0);
            for (Object obj : objs) {
                String rowContent = "";
                if (fieldNames != null) {
                    for (int num = 0; num < fieldNames.length; num++) {
                        Object value = null;
                        try {
                            value = ReflectionUtils.invokeGetterMethod(obj, fieldNames[num]);
                        }
                        catch (Exception e) {
                            value = null;
                        }
                        if (value == null) {
                            value = "";
                        }
                        else {
                            try {
                                BigDecimal b = new BigDecimal(String.valueOf(value));
                                value = b.toString();
                            }
                            catch (Exception e) {
                            }
                        }
                        rowContent = rowContent + value + ",";
                    }
                    rowContent = rowContent.substring(0, rowContent.length() - 1);
                    bw.write(rowContent);
                    bw.write("\r\n");
                }
            }
        }
        bw.flush();
        bw.close();
    }

    /**
     * 
     * init(初始化)
     * 
     * @param setInfo
     * @author xuyf
     */
    private static void init(ExcelExportInfo setInfo) {
        setInfo.setWb(new HSSFWorkbook());

        setInfo.setHeadStyle(setInfo.getWb().createCellStyle());
        setInfo.setHeadFont(setInfo.getWb().createFont());
        setInfo.setRedStyle(setInfo.getWb().createCellStyle());

        initHeadFont(setInfo.getHeadFont());
        initHeadCellStyle(setInfo.getHeadStyle(), setInfo.getHeadFont());
        initRedCellStyle(setInfo.getRedStyle());
    }

    /**
     * 
     * adjustColumnSize(自动调整列宽)
     * 
     * @param sheets
     * @param sheetNum
     * @param fieldNames
     * @author xuyf
     */
    private static void adjustColumnSize(HSSFSheet[] sheets, int sheetNum, String[] fieldNames, boolean typeDispFlag) {
        if (fieldNames == null) {
            return;
        }
        int num = typeDispFlag ? 2 : 1;
        for (int i = 0; i < fieldNames.length + num; i++) {
            sheets[sheetNum].autoSizeColumn(i, true);
        }
    }

    /**
     * 
     * creatTableHeadRow(创建表头行)
     * 
     * @param setInfo
     * @param sheets
     * @param sheetNum
     * @author xuyf
     */
    private static void creatTableHeadRow(ExcelExportInfo setInfo, HSSFSheet[] sheets, int sheetNum,
            boolean typeDispFlag) {
        // 表头
        HSSFRow headRow = sheets[sheetNum].createRow(0);
        // headRow.setHeight((short) 350);
        int cellNum = 0;
        // 序号列
        // HSSFCell snCell = headRow.createCell(0);
        // snCell.setCellStyle(setInfo.getHeadStyle());
        // snCell.setCellValue("序号");
        // 公式类型列
        if (typeDispFlag) {
            cellNum = cellNum + 1;
            HSSFCell tyCell = headRow.createCell(1);
            tyCell.setCellStyle(setInfo.getHeadStyle());
            tyCell.setCellValue("公式类型");
        }
        int l = 0;
        if (setInfo.getHeadNames() != null && setInfo.getHeadNames().size() != 0) {
            l = setInfo.getHeadNames().get(sheetNum).length;
        }
        // 列头名称
        for (int num = 0; num < l; num++) {
            HSSFCell headCell = headRow.createCell(cellNum);
            headCell.setCellStyle(setInfo.getHeadStyle());
            headCell.setCellValue(setInfo.getHeadNames().get(sheetNum)[num]);
            cellNum++;
        }
    }

    /**
     * 
     * getSheets(创建所有Sheet)
     * 
     * @param wb
     * @param num
     * @param names
     * @return
     * @author xuyf
     */
    private static HSSFSheet[] getSheets(HSSFWorkbook wb, int num, String[] names) {
        HSSFSheet[] sheets = new HSSFSheet[num];
        for (int i = 0; i < num; i++) {
            sheets[i] = wb.createSheet(names[i]);
        }
        return sheets;
    }

    /**
     * 
     * getCells(创建内容行的每一列(附加一列行号和公式类型))
     * 
     * @param contentRow
     * @param num
     * @return
     * @author xuyf
     */
    private static HSSFCell[] getCells(HSSFRow contentRow, int num, boolean typeDispFlag) {
        if (typeDispFlag) {// 显示公式类型
            num = num + 1;
        }
        HSSFCell[] cells = new HSSFCell[num];

        for (int i = 0, len = cells.length; i < len; i++) {
            cells[i] = contentRow.createCell(i);
        }
        cells[0].setCellValue(contentRow.getRowNum());

        return cells;
    }

    /**
     * 
     * initHeadCellStyle(初始化表头行样式)
     * 
     * @param headStyle
     * @param headFont
     * @author xuyf
     */
    private static void initHeadCellStyle(CellStyle headStyle, Font headFont) {
        // headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setFont(headFont);
        // headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        // headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    }

    /**
     * 
     * initRedCellStyle(初始化红色单元格样式)
     * 
     * @param redStyle
     * @author xuyf
     */
    private static void initRedCellStyle(CellStyle redStyle) {
        redStyle.setAlignment(CellStyle.ALIGN_CENTER);
        redStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    }

    /**
     * 
     * initHeadFont(初始化表头行字体)
     * 
     * @param headFont
     * @author xuyf
     */
    private static void initHeadFont(Font headFont) {
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 11);
        // headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    }

}
