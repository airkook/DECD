package com.fitech.papp.decd.util;

public class Constants {

    /** 大额存单数据校验状态：未校验 */
    public static final String VALIDATE_STATUS_UNVALI = "";

    /** 大额存单数据校验状态：不通过 */
    public static final String VALIDATE_STATUS_UNPASS = "0";

    /** 大额存单数据校验状态：通过 */
    public static final String VALIDATE_STATUS_PASS = "1";

    /** 大额存单数据状态：删除 */
    public static final String CDS_STATUS_DEL = "0";

    /** 大额存单数据状态：正常 */
    public static final String CDS_STATUS_NORMAL = "1";

    /** 大额存单数据状态：补录 */
    public static final String CDS_STATUS_EDIT = "2";

    /** 大额存单数据状态：提交审核 */
    public static final String CDS_STATUS_REVIEW = "3";

    /** 大额存单数据状态：审核不通过 */
    public static final String CDS_STATUS_REVIEW_UNPASS = "4";

    /** 大额存单数据状态：审核通过 */
    public static final String CDS_STATUS_REVIEW_PASS = "5";

    /** 校验类型：基本 */
    public static final String VALIDATE_TYPE_BASE = "1";

    /** 校验类型：表内 */
    public static final String VALIDATE_TYPE_TABLE_INSIDE = "2";

    /** 校验类型：表间 */
    public static final String VALIDATE_TYPE_TABLE_EACH = "3";

    /** 校验类型：合计 */
    public static final String VALIDATE_TYPE_TERM = "4";

    /** 校验结果：不通过 */
    public static final String VALIDATE_RESULT_UNPASS = "0";

    /** 校验结果：通过 */
    public static final String VALIDATE_RESULT_PASS = "1";

    /** 上报状态：0.已生成报表，未上报 */
    public static final String REPORT_RESULT_RELEASE = "0";

    /** 上报状态：1.已上报，反馈未抓取 */
    public static final String REPORT_RESULT_NOTFEEDBACK = "1";

    /** 上报状态：2.已上报，反馈结果：成功 */
    public static final String REPORT_RESULT_FEEDBACKSUCC = "2";

    /** 上报状态：3.已上报，反馈结果：失败 */
    public static final String REPORT_RESULT_FEEBBACKFAIL = "3";

    /** 上报状态：4.已修改数据，未重新生成报表 */
    public static final String REPORT_RESULT_EDITDATA = "4";

    /** 反馈状态：S.成功 */
    public static final String FEEDBACK_FEEDBACKSUCC = "S";

    /** 反馈状态：E.失败 */
    public static final String FEEDBACK_FEEDBACKFAIL = "E";

    /** 上报机构：1.中国外汇交易中心 */
    public static final Integer REPORT_ORG_CHINACENTER = 1;

    /** 上报机构：2.上海清算所 */
    public static final Integer REPORT_ORG_SHANGHAIQSS = 2;

    /** 大额存单：字段中文名称 */
    public static final String[] CDS_HEADNAME = new String[] { "发行人全称", "发行人账号", "产品代码", "业务发生日", "发行起始日", "发行终止日",
            "计划发行总量(亿元)", "当日发行金额(亿元)", "累计发行金额(亿元)", "产品期限", "发行对象", "起点金额(万元)", "计息方式", "基准利率种类", "利差(BP/%)",
            "初始发行利率(%)", "付息频率", "是否可提前支取", "是否可赎回", "当日提前支取金额(亿元)", "累计提前支取金额(亿元)", "当日赎回金额(亿元)", "累计赎回金额(亿元)",
            "当日兑付金额(亿元)", "累计兑付金额(亿元)", "每期存单余额(亿元)", "余额总计(亿元)" };

    /** 大额存单：字段英文名称 */
    public static final String[] CDS_FIELDNAME = new String[] { "fxrqc", "fxrzh", "cpdm", "ywfsr", "fxqsr", "fxzzr",
            "jhfxzl", "drfxje", "ljfxje", "cpqx", "fxdx", "qdje", "jxfs", "jzllzl", "lc", "csfxll", "fxpl", "sfktqzc",
            "sfksh", "drtqzcje", "ljtqzcje", "drshje", "ljshje", "drdfje", "ljdfje", "mqcdye", "yezj" };
}
