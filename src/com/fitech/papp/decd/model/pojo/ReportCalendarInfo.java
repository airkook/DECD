package com.fitech.papp.decd.model.pojo;


/**
 * ReportCalendarInfo entity. @author MyEclipse Persistence Tools
 */

public class ReportCalendarInfo implements java.io.Serializable {

    // Fields

    private Integer calendarInfoId;

    private String reportYear;

    private String reportDate;

    private String ywfsrDate;

    // Constructors

    /** default constructor */
    public ReportCalendarInfo() {
    }

    /** full constructor */
    public ReportCalendarInfo(Integer calendarInfoId, String reportYear, String reportDate, String ywfsrDate) {
        this.calendarInfoId = calendarInfoId;
        this.reportYear = reportYear;
        this.reportDate = reportDate;
        this.ywfsrDate = ywfsrDate;
    }

    // Property accessors

    public Integer getCalendarInfoId() {
        return this.calendarInfoId;
    }

    public void setCalendarInfoId(Integer calendarInfoId) {
        this.calendarInfoId = calendarInfoId;
    }

    public String getReportYear() {
        return this.reportYear;
    }

    public void setReportYear(String reportYear) {
        this.reportYear = reportYear;
    }

    public String getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getYwfsrDate() {
        return this.ywfsrDate;
    }

    public void setYwfsrDate(String ywfsrDate) {
        this.ywfsrDate = ywfsrDate;
    }

}
