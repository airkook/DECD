/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.task;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.ExcelExportInfo;
import com.fitech.framework.core.util.ExcelUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.papp.decd.ftp.MySFTP;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.pojo.ValidateResult;
import com.fitech.papp.decd.model.vo.AutoReportInfoVo;
import com.fitech.papp.decd.service.AutoReportManageService;
import com.fitech.papp.decd.service.CdsManagerService;
import com.fitech.papp.decd.service.DataReportService;
import com.fitech.papp.decd.util.Constants;
import com.jcraft.jsch.ChannelSftp;

/**
 * @author xyf
 * 
 */
public class AutoReportTask {

    /** 日志 */
    private final Log log = LogFactory.getLog(AutoReportTask.class);

    /** 正在运行标志位 */
    private static boolean isRunning = false;

    /** 自动报送管理service注解 */
    @Autowired
    private AutoReportManageService autoReportManageService;

    /** 数据上报 service注解 */
    @Autowired
    private DataReportService dataReportService;

    /** 大额存单数据维护Service */
    @Autowired
    private CdsManagerService cdsManagerService;

    /**
     * 自动报送定时器主方法
     */
    public void doRun() {
        if (isRunning) {
            log.info("自动报送任务正在执行中,time : " + (new Date()));
            return;
        }
        log.info("开始执行自动报送任务,time : " + (new Date()));
        long t1 = System.currentTimeMillis();
        isRunning = true;
        try {
            String time = DateUtil.getToday("HH:mm");
            System.out.println(time);
            List<AutoReportInfoVo> autoReportInfoList = autoReportManageService.queryReportOrgInfoList();
            Boolean chinaCenterAutoWrite = false;
            Boolean shanghaiQssAutoWrite = false;
            for (AutoReportInfoVo autoInfo : autoReportInfoList) {
                // 中国外汇交易中心
                if (Constants.REPORT_ORG_CHINACENTER == autoInfo.getReportOrgId() && "1".equals(autoInfo.getIsUsed())
                        && "1".equals(autoInfo.getFlag()) && time.equals(autoInfo.getTime())) {
                    chinaCenterAutoWrite = true;
                }
                // 上海清算所
                if (Constants.REPORT_ORG_SHANGHAIQSS == autoInfo.getReportOrgId() && "1".equals(autoInfo.getIsUsed())
                        && "1".equals(autoInfo.getFlag()) && time.equals(autoInfo.getTime())) {
                    shanghaiQssAutoWrite = true;
                }
            }
            if (!chinaCenterAutoWrite && !shanghaiQssAutoWrite) {
                log.info("当前时间没有启用自动报送的机构！");
                isRunning = false;
                log.info("结束执行自动报送任务,time : " + (new Date()));
                log.info("自动报送任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
                return;
            }
            String today = DateUtil.getTodayDateStr();
            List<String> termList = new ArrayList<String>();
            // 根据报送日查询报送日历
            List<ReportCalendarInfo> calendarInfoList = autoReportManageService.findCalendarInfoList(today);
            // 如果今天是节假日后的第一天
            if (calendarInfoList != null && calendarInfoList.size() > 0) {
                log.info("今天是报送日历中配置的报送日" + today);
                for (ReportCalendarInfo rci : calendarInfoList) {
                    if (!termList.contains(rci.getYwfsrDate())) {
                        termList.add(rci.getYwfsrDate());
                    }
                }
            }
            else {
                String lastDay = DateUtil.getLastDay(today);
                // 判断昨天是否在报送日历中配置了报送日
                ReportCalendarInfo rri = autoReportManageService.findCalInfoByYwfsr(lastDay);
                if (rri != null) {
                    log.info(lastDay + "的数据报送已在报送日历中配置了报送日，将在" + rri.getReportDate() + "报送和抓取反馈！");
                    isRunning = false;
                    log.info("结束执行自动报送任务,time : " + (new Date()));
                    log.info("自动报送任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
                    return;
                }
                else {
                    int week = DateUtil.getWeekOfDate(new Date());
                    // 如果今天是周末
                    if (week == 0 || week == 6) {
                        log.info("今天是周末无须报送和抓取反馈，将在下周一进行报送和抓取反馈！");
                        isRunning = false;
                        log.info("结束执行自动报送任务,time : " + (new Date()));
                        log.info("自动报送任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
                        return;
                    }
                    // 如果今天是星期一,需要报送星期五、六、日的数据
                    else if (week == 1) {
                        log.info("今天是周一，将报送和抓取反馈周五、周六、周日的数据！");
                        String sunday = lastDay;
                        String saturday = DateUtil.getLastDay(sunday);
                        String friday = DateUtil.getLastDay(saturday);
                        termList.add(sunday);
                        termList.add(saturday);
                        termList.add(friday);
                    }
                    else {
                        termList.add(lastDay);
                    }
                }
            }
            if (termList.size() > 0) {
                log.info("需要报送的业务发生日如下：");
                for (String term : termList) {
                    log.info(term);
                }
            }
            for (String term : termList) {
                // 先校验这一期的数据
                log.info("开始校验" + term + "的所有数据");
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("term", term);
                cdsManagerService.validateData(paramMap);
                // 查询是否有校验不通过的记录
                List<ValidateResult> validateList = cdsManagerService.findUnPassValiResList(term);
                if (validateList != null && validateList.size() > 0) {
                    log.info(term + "的数据存在校验不通过记录" + validateList.size() + "条，不可自动报送");
                    continue;
                }
                else {
                    log.info("完成校验" + term + "的所有数据，校验全部通过");
                }

                if (chinaCenterAutoWrite) {
                    log.info("开始生成中国外汇交易中心的" + term + "报表文件：");
                    String writeResult = this.writeReport(term, Constants.REPORT_ORG_CHINACENTER.toString());
                    if (StringUtil.isEmpty(writeResult)) {
                        log.info("结束生成中国外汇交易中心的" + term + "报表文件：成功");

                        // 生成报表文件成功后将该期数据的状态更改为审核通过
                        cdsManagerService.updateReviewPassStatus(term);

                        log.info("开始上报中国外汇交易中心的" + term + "报表文件：");
                        String reportResult = this.uploadReport(term, Constants.REPORT_ORG_CHINACENTER.toString());
                        if (StringUtil.isEmpty(reportResult)) {
                            log.info("结束上报中国外汇交易中心的" + term + "报表文件：成功");
                        }
                        else {
                            log.info("结束上报中国外汇交易中心的" + term + "报表文件：失败," + reportResult);
                        }
                    }
                    else {
                        log.info("结束生成中国外汇交易中心的" + term + "报表文件：失败," + writeResult);
                    }
                }
                if (shanghaiQssAutoWrite) {
                    log.info("开始生成上海清算所的" + term + "报表文件：");
                    String writeResult = this.writeReport(term, Constants.REPORT_ORG_SHANGHAIQSS.toString());
                    if (StringUtil.isEmpty(writeResult)) {
                        log.info("结束生成上海清算所的" + term + "报表文件：成功");

                        // 生成报表文件成功后将该期数据的状态更改为审核通过
                        cdsManagerService.updateReviewPassStatus(term);

                        log.info("开始上报上海清算所的" + term + "报表文件：");
                        String reportResult = this.uploadReport(term, Constants.REPORT_ORG_SHANGHAIQSS.toString());
                        if (StringUtil.isEmpty(reportResult)) {
                            log.info("结束上报上海清算所的" + term + "报表文件：成功");
                        }
                        else {
                            log.info("结束上报上海清算所的" + term + "报表文件：失败," + reportResult);
                        }
                    }
                    else {
                        log.info("结束生成上海清算所的" + term + "报表文件：失败," + writeResult);
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("自动报送任务出现错误 ", e);
        }

        isRunning = false;
        log.info("结束执行自动报送任务,time : " + (new Date()));
        log.info("自动报送任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
    }

    /**
     * 生成报文
     * 
     * @return
     */
    public String writeReport(String term, String reportOrgId) throws Exception {
        String fileName = "";
        String message = "";
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("term", term);
            List<CertificatesOfDeposit> cdList = dataReportService.findCdsList(paramMap);
            if (cdList.size() == 0) {
                message = "此期没有数据无法生成报表";
                return message;
            }

            // 拼接本地生成报表路径
            String path = Config.WEBROOTPATH + Config.confMap.get("uploadFilePath");
            System.out.println("path:" + path);
            // 判断该目录下文件夹是否存在， 不存在创建文件夹
            File file = new File(path);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            // 文件夹的日期和文件的日期为业务发生日的第二天
            String date = DateUtil.getNextDay(term).replace("-", "");
            String path1 = path + File.separator + date;
            File file1 = new File(path1);
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdirs();
            }

            // 上海清算所
            if ("2".equals(reportOrgId)) {
                fileName = Config.confMap.get("shanghaiUser") + "_" + date + ".csv";
            }
            // 中国外汇中心
            else if ("1".equals(reportOrgId)) {
                fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                        + "_01" + ".csv";

                String[] list = file1.list();
                System.out.println("生成报表：开始打印文件夹中的文件列表：");
                int j = 1;
                int i = 0;
                while (i != list.length) {
                    if (list[i].equals(fileName)) {
                        System.out.println(fileName);
                        j++;
                        fileName = Config.confMap.get("chinaUser") + "_" + date + "_"
                                + Config.confMap.get("undefinedCode") + "_0" + j + ".csv";
                        i = 0;
                        continue;
                    }
                    i++;
                }
                System.out.println("结束打印文件夹中的文件列表");
            }
            String path2 = path1 + File.separator + fileName;
            System.out.println("生成报表文件为：" + path2);
            // 导出
            ExcelExportInfo setInfo = new ExcelExportInfo();
            // 设置导出信息
            LinkedHashMap<String, List> map = new LinkedHashMap<String, List>();
            map.put("Sheet1", cdList);
            List<String[]> headNames = new ArrayList<String[]>();
            headNames.add(Constants.CDS_HEADNAME);
            List<String[]> fieldNames = new ArrayList<String[]>();
            fieldNames.add(Constants.CDS_FIELDNAME);

            setInfo.setObjsMap(map);
            setInfo.setFieldNames(fieldNames);
            setInfo.setHeadNames(headNames);
            setInfo.setPath(path2);
            ExcelUtil.export2CSV(setInfo);

            // 入结果表 先判断该期数该上报机构下有无数据 无插入 有就更新
            int reportOrgIdInt = Integer.parseInt(reportOrgId);
            List<ReportResultInfo> rri1 = dataReportService.select(reportOrgIdInt, term);

            String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
            // 新增
            if (rri1 == null || rri1.size() == 0) {
                ReportResultInfo rri = new ReportResultInfo();
                rri.setTerm(term);
                rri.setReportOrgId(reportOrgIdInt);
                rri.setReportStatus(Constants.REPORT_RESULT_RELEASE);
                rri.setReportTime(reportTime);
                rri.setReportUserId(1);
                dataReportService.saveReportResult(rri);
            }
            else {
                rri1.get(0).setReportStatus(Constants.REPORT_RESULT_RELEASE);
                rri1.get(0).setReportTime(reportTime);
                rri1.get(0).setReportUserId(1);
                dataReportService.updateReportResult(rri1.get(0));
            }
        }
        catch (Exception e) {
            message = "生成报文失败";
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 上传到ftp
     * 
     * @return
     */
    public String uploadReport(String term, String reportOrgId) throws Exception {
        String message = "";
        // 根据传出机构id 查出ftp地址 端口 用户名 密码 四个参数
        ReportConfigInfo reportConfigInfo = dataReportService.findReportConfigInfo(reportOrgId);
        if (reportConfigInfo == null || StringUtil.isEmpty(reportConfigInfo.getFtpAddress())
                || StringUtil.isEmpty(reportConfigInfo.getFtpPort())
                || StringUtil.isEmpty(reportConfigInfo.getFtpUserId())
                || StringUtil.isEmpty(reportConfigInfo.getFtpPassword())) {
            message = "请先配置好FTP信息";
            return message;
        }

        // 查询报送记录
        int reportOrgIdInt = Integer.parseInt(reportOrgId);
        List<ReportResultInfo> rrilist = dataReportService.select(reportOrgIdInt, term);
        if (rrilist == null || rrilist.size() == 0) {
            message = "请先生成报表";
            return message;
        }
        ReportResultInfo reportResultInfo = rrilist.get(0);
        if (Constants.REPORT_RESULT_EDITDATA.equals(reportResultInfo.getReportStatus())) {
            message = "已修改数据，请重新生成报表";
            return message;
        }
        if (Constants.REPORT_RESULT_FEEDBACKSUCC.equals(reportResultInfo.getReportStatus())) {
            message = "已上报并且反馈结果为成功，无须重新上报";
            return message;
        }

        // 拼接本地生成报表路径
        String path = Config.WEBROOTPATH + Config.confMap.get("uploadFilePath");
        System.out.println("path:" + path);
        // 判断该目录下文件夹是否存在， 不存在创建文件夹
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        // 文件夹的日期和文件的日期为业务发生日的第二天
        String date = DateUtil.getNextDay(term).replace("-", "");
        String path1 = path + File.separator + date;
        File file1 = new File(path1);
        if (!file1.exists() && !file1.isDirectory()) {
            file1.mkdirs();
        }
        // 文件名
        String fileName = "";

        // 判断所属机构 1为中国外汇交易中心 2为上海清算所
        if ("1".equals(reportOrgId)) {
            fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode") + "_01"
                    + ".csv";

            String[] list = file1.list();
            if (list == null || list.length == 0) {
                message = "上报失败：报表不存在，请先生成报表";
                return message;
            }
            int j = 1;
            System.out.println("上报：开始打印文件夹中的文件列表：");
            int i = 0;
            while (i != list.length) {
                if (list[i].equals(fileName)) {
                    System.out.println(fileName);
                    j++;
                    fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                            + "_0" + j + ".csv";
                    i = 0;
                    continue;
                }
                i++;
            }
            System.out.println("结束打印文件夹中的文件列表");
            fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode") + "_0"
                    + (j - 1) + ".csv";

            String path2 = path1 + File.separator + fileName;
            System.out.println("上报文件为：" + path2);
            File file2 = new File(path2);
            if (!file2.exists()) {
                message = "上报失败：报表不存在，请先生成报表";
                return message;
            }

            try {
                TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                } };
                SSLContext sslContext = null;
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManager, new SecureRandom());
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                FTPClient client = new FTPClient();
                client.setSSLSocketFactory(sslSocketFactory);

                // 显式连接开启
                // client.setSecurity(FTPClient.SECURITY_FTPES);
                // 隐式连接开启
                client.setSecurity(FTPClient.SECURITY_FTPS);

                // ftp连接地址，端口号
                client.setAutoNoopTimeout(2);
                System.out.println("连接：" + reportConfigInfo.getFtpAddress() + "，端口：" + reportConfigInfo.getFtpPort());
                client.connect(reportConfigInfo.getFtpAddress(), Integer.parseInt(reportConfigInfo.getFtpPort()));
                // ftp连接用户名，密码
                System.out.println("登录：" + reportConfigInfo.getFtpUserId() + "，" + reportConfigInfo.getFtpPassword());
                client.login(reportConfigInfo.getFtpUserId(), reportConfigInfo.getFtpPassword());
                // 选择ftp目录
                // client.changeDirectory(reportConfigInfo.getUploadPath());
                // FTPFile[] fs = client.list();
                // int n = 0;
                // for (FTPFile ff : fs) {
                // System.out.println(ff.getName());
                // if (ff.getName().equals(date)) {
                // n++;
                // break;
                // }
                // }
                // if (n == 0) {
                // client.createDirectory(date);
                // }
                // client.changeDirectory(date);

                // 上传
                System.out.println("开始上传文件");
                client.upload(file2);
                System.out.println(client.toString());
                client.disconnect(true);
                System.out.println("上传结束，断开连接");

                // 更新报送记录的状态为已上报反馈未抓取
                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_NOTFEEDBACK);
                reportResultInfo.setReportTime(reportTime);
                reportResultInfo.setReportUserId(1);
                dataReportService.updateReportResult(reportResultInfo);
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "上报失败：连接FTP服务器失败";
                return message;
            }
        }
        if ("2".equals(reportOrgId)) {
            fileName = Config.confMap.get("shanghaiUser") + "_" + date + ".csv";
            String path2 = path1 + File.separator + fileName;
            File file2 = new File(path2);
            if (!file2.exists()) {
                message = "上报失败：报表不存在，请先生成报表";
                return message;
            }
            try {
                MySFTP sf = new MySFTP();
                int i = 0;
                ChannelSftp sftp = sf.connect(reportConfigInfo.getFtpAddress(), Integer.parseInt(reportConfigInfo
                        .getFtpPort()), reportConfigInfo.getFtpUserId(), reportConfigInfo.getFtpPassword());

                // 遍历该目录下所有文件名字
                Vector vector = sf.listFiles(reportConfigInfo.getUploadPath(), sftp);
                Iterator it = vector.iterator();
                while (it.hasNext()) {
                    if (it.next().toString().contains(date)) {
                        i++;
                    }
                }
                sftp.cd(reportConfigInfo.getUploadPath());
                if (i == 0) {
                    sftp.mkdir(date);
                }
                sf.upload("/" + reportConfigInfo.getUploadPath() + "/" + date + "/", path2, sftp);
                sftp.disconnect();

                // 更新报送记录的状态为已上报反馈未抓取
                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_NOTFEEDBACK);
                reportResultInfo.setReportTime(reportTime);
                reportResultInfo.setReportUserId(1);
                dataReportService.updateReportResult(reportResultInfo);
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "上报失败：连SFTP服务器失败";
                return message;
            }
        }
        return message;
    }
}
