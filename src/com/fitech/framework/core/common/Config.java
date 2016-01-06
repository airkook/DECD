package com.fitech.framework.core.common;

import java.util.HashMap;
import java.util.Map;

public class Config {

    /**
     * 系统的file.separator
     */
    public static final String FILESEPARATOR = System.getProperty("file.separator");

    /** 系统根路径 */
    public static String WEBROOTPATH = "";

    /**
     * 分页显示记录时，每页显示的记录数
     */
    public static final int PER_PAGE_ROWS = 20;

    /**
     * 分页对象的存放在Request中的名称
     */
    public static final String APART_PAGE_OBJECT = "ApartPage";

    /**
     * 返回查询记录集的名称
     */
    public static final String RECORDS = "Records";

    /*
     * 数据库类型名称
     */
    public static String DB_TYPE_NAME = "";

    /*
     * ORACLE数据库名称
     */
    public static String DB_ORACLE_NAME = "Oracle";

    /**
     * 配置信息映射集
     */
    public static Map confMap = new HashMap();

    /** 是否连接PORTAL系统 */
    public static boolean NEWPORTAL = false;

    /** PORTAL系统URL */
    public static String NEWPORTALURL = "";

    /** 是否分发 */
    public static boolean ISADAPTER = false;

    /** 分发url */
    public static String ADAPTERURL = "";

    /**
     * 用户登录后,保存到Session中的名称
     */
    public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";

}
