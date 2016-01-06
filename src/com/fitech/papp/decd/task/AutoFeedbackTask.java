/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.task;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import com.fitech.framework.core.util.StringUtil;
import com.fitech.papp.decd.ftp.MySFTP;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.AutoReportInfoVo;
import com.fitech.papp.decd.service.AutoReportManageService;
import com.fitech.papp.decd.service.DataReportService;
import com.fitech.papp.decd.util.Constants;
import com.jcraft.jsch.ChannelSftp;

/**
 * @author xyf
 * 
 */
public class AutoFeedbackTask {

    /** 日志 */
    private final Log log = LogFactory.getLog(AutoFeedbackTask.class);

    /** 正在运行标志位 */
    private static boolean isRunning = false;

    /** 自动抓取反馈管理service注解 */
    @Autowired
    private AutoReportManageService autoReportManageService;

    /** 数据上报 service注解 */
    @Autowired
    private DataReportService dataReportService;

    /**
     * 自动抓取反馈定时器主方法
     */
    public void doRun() {
        if (isRunning) {
            log.info("自动抓取反馈任务正在执行中,time : " + (new Date()));
            return;
        }
        log.info("开始执行自动抓取反馈任务,time : " + (new Date()));
        long t1 = System.currentTimeMillis();
        isRunning = true;
        try {
            String time = DateUtil.getToday("HH:mm");
            System.out.println(time);
            List<AutoReportInfoVo> autoReportInfoList = autoReportManageService.queryReportOrgInfoList();
            Boolean chinaCenterAuto = false;
            Boolean shanghaiQssAuto = false;
            for (AutoReportInfoVo autoInfo : autoReportInfoList) {
                // 中国外汇交易中心
                if (Constants.REPORT_ORG_CHINACENTER == autoInfo.getReportOrgId() && "1".equals(autoInfo.getIsUsed())
                        && "2".equals(autoInfo.getFlag()) && time.equals(autoInfo.getTime())) {
                    chinaCenterAuto = true;
                }
                // 上海清算所
                if (Constants.REPORT_ORG_SHANGHAIQSS == autoInfo.getReportOrgId() && "1".equals(autoInfo.getIsUsed())
                        && "2".equals(autoInfo.getFlag()) && time.equals(autoInfo.getTime())) {
                    shanghaiQssAuto = true;
                }
            }
            if (!chinaCenterAuto && !shanghaiQssAuto) {
                log.info("当前时间没有启用自动抓取反馈的机构！");
                isRunning = false;
                log.info("结束执行自动抓取反馈任务,time : " + (new Date()));
                log.info("自动抓取反馈任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
                return;
            }
            String today = DateUtil.getTodayDateStr();
            List<String> termList = new ArrayList<String>();
            // 根据报送日查询报送日历
            List<ReportCalendarInfo> calendarInfoList = autoReportManageService.findCalendarInfoList(today);
            // 如果今天是报送日历中的报送日，将报送日历中需要报送的所有业务发生日
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
                    log.info("结束执行自动抓取反馈任务,time : " + (new Date()));
                    log.info("自动抓取反馈任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
                    return;
                }
                else {
                    int week = DateUtil.getWeekOfDate(new Date());
                    // 如果今天是周末
                    if (week == 0 || week == 6) {
                        log.info("今天是周末无须报送和抓取反馈，将在下周一进行报送和抓取反馈！");
                        isRunning = false;
                        log.info("结束执行自动抓取反馈任务,time : " + (new Date()));
                        log.info("自动抓取反馈任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
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
                log.info("需要抓取反馈的业务发生日如下：");
                for (String term : termList) {
                    log.info(term);
                }
            }
            for (String term : termList) {
                if (chinaCenterAuto) {
                    log.info("开始抓取中国外汇交易中心的" + term + "业务发生日的反馈文件：");
                    String writeResult = this.touckFeedBack(term, Constants.REPORT_ORG_CHINACENTER.toString());
                    if (StringUtil.isEmpty(writeResult)) {
                        log.info("抓取中国外汇交易中心的" + term + "业务发生日的反馈文件：成功");
                    }
                    else {
                        log.info("结束抓取中国外汇交易中心的" + term + "业务发生日的反馈文件：失败," + writeResult);
                    }
                }
                if (shanghaiQssAuto) {
                    log.info("开始抓取上海清算所的" + term + "业务发生日的反馈文件：");
                    String writeResult = this.touckFeedBack(term, Constants.REPORT_ORG_SHANGHAIQSS.toString());
                    if (StringUtil.isEmpty(writeResult)) {
                        log.info("抓取上海清算所的" + term + "业务发生日的反馈文件：成功");
                    }
                    else {
                        log.info("结束抓取上海清算所的" + term + "业务发生日的反馈文件：失败," + writeResult);
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("自动抓取反馈任务出现错误 ", e);
        }

        isRunning = false;
        log.info("结束执行自动抓取反馈任务,time : " + (new Date()));
        log.info("自动抓取反馈任务,花费时间 (秒): " + (System.currentTimeMillis() - t1) / 1000.0);
    }

    /**
     * 抓取反馈
     * 
     * @return
     * @throws Exception
     */
    public String touckFeedBack(String term, String reportOrgId) throws Exception {
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
        if (Constants.REPORT_RESULT_RELEASE.equals(reportResultInfo.getReportStatus())) {
            message = "请先进行上报";
            return message;
        }
        if (Constants.REPORT_RESULT_FEEDBACKSUCC.equals(reportResultInfo.getReportStatus())) {
            message = "已上报并且反馈结果为成功，无须重新抓取反馈";
            return message;
        }

        String path = Config.WEBROOTPATH + Config.confMap.get("downloadFilePath");
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

        String fileName = "";
        String downloadFileName = "";

        // 上海清算所
        if ("2".equals(reportOrgId)) {
            fileName = Config.confMap.get("shanghaiUser") + "_" + date + "_f.csv";
            try {
                MySFTP sf = new MySFTP();
                ChannelSftp sftp = sf.connect(reportConfigInfo.getFtpAddress(), Integer.parseInt(reportConfigInfo
                        .getFtpPort()), reportConfigInfo.getFtpUserId(), reportConfigInfo.getFtpPassword());
                if (sftp == null) {
                    throw new Exception("SFTP连接失败");
                }
                Vector vector = null;
                try {
                    vector = sf.listFiles("/" + reportConfigInfo.getFeedbackPath() + "/" + date + "/", sftp);
                }
                catch (Exception e) {
                    throw new Exception("远程服务器尚未创建反馈文件夹");
                }
                Iterator it = vector.iterator();
                int i = 0;
                while (it.hasNext()) {
                    if (it.next().toString().contains(fileName)) {
                        i++;
                        sf.download("/" + reportConfigInfo.getFeedbackPath() + "/" + date + "/", fileName, path1
                                + File.separator + fileName, sftp);
                        downloadFileName = fileName;
                    }
                }
                sftp.disconnect();
                if (i == 0) {
                    message = "反馈文件不存在";
                    return message;
                }
                else {
                    // 解析结果入库
                    try {
                        DataInputStream in = new DataInputStream(new FileInputStream(new File(path1 + File.separator
                                + downloadFileName)));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String line1 = reader.readLine();// 第一行信息，为标题信息，不用
                        if (line1 != null) {
                            String line2 = reader.readLine();// 第二行信息
                            if (line2 != null) {
                                // 处理状态,错误信息
                                String item[] = line2.split(",");
                                if (item.length >= 1) {
                                    String flag = item[0];// 处理状态
                                    String result = item.length == 2 ? item[1] : "";// 错误信息
                                    List<FeedbackInfo> feedbackInfoList = new ArrayList<FeedbackInfo>();
                                    // 成功
                                    if (Constants.FEEDBACK_FEEDBACKSUCC.equals(flag)) {
                                        // 更新报送记录状态
                                        String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                        reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEDBACKSUCC);
                                        reportResultInfo.setReportTime(reportTime);
                                        reportResultInfo.setReportUserId(1);
                                        dataReportService.updateReportResult(reportResultInfo);
                                        // 保存反馈结果
                                        FeedbackInfo feedbackInfo = new FeedbackInfo();
                                        feedbackInfo.setTerm(term);
                                        feedbackInfo.setReportOrgId(Integer.parseInt(reportOrgId));
                                        feedbackInfo.setFlag(Constants.FEEDBACK_FEEDBACKSUCC);
                                        feedbackInfoList.add(feedbackInfo);
                                    }
                                    // 失败
                                    else if (Constants.FEEDBACK_FEEDBACKFAIL.equals(flag)) {
                                        // 更新报送记录状态
                                        String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                        reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEBBACKFAIL);
                                        reportResultInfo.setReportTime(reportTime);
                                        reportResultInfo.setReportUserId(1);
                                        dataReportService.updateReportResult(reportResultInfo);
                                        try {
                                            // 拆分解析失败的反馈结果
                                            String[] errorArr = result.split(";");
                                            // 循环每个产品的错误信息
                                            for (String errorInfo : errorArr) {
                                                if (StringUtil.isEmpty(errorInfo)) {
                                                    continue;
                                                }
                                                // 取出产品代码
                                                String cpdm = StringUtil.getAlpNumbers(errorInfo);
                                                String error = errorInfo.substring(errorInfo.indexOf(":") + 1);
                                                String[] errArr = error.split(" ");
                                                for (String err : errArr) {
                                                    FeedbackInfo feedbackInfo = new FeedbackInfo();
                                                    feedbackInfo.setTerm(term);
                                                    feedbackInfo.setReportOrgId(Integer.parseInt(reportOrgId));
                                                    feedbackInfo.setFlag(Constants.FEEDBACK_FEEDBACKFAIL);
                                                    feedbackInfo.setCpdm(cpdm);
                                                    feedbackInfo.setErrorInfo(err);
                                                    feedbackInfoList.add(feedbackInfo);
                                                }
                                            }
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            // 拆分出现错误则将整个错误信息保存
                                            FeedbackInfo feedbackInfo = new FeedbackInfo();
                                            feedbackInfo.setTerm(term);
                                            feedbackInfo.setReportOrgId(Integer.parseInt(reportOrgId));
                                            feedbackInfo.setFlag(Constants.FEEDBACK_FEEDBACKFAIL);
                                            feedbackInfo.setErrorInfo(result);
                                            feedbackInfoList.add(feedbackInfo);
                                        }
                                    }
                                    else {
                                        message = "反馈文件格式错误";
                                        return message;
                                    }
                                    // 根据期数和上报机构ID删除反馈结果记录
                                    dataReportService.delFeedback(term, reportOrgId);
                                    // 保存反馈结果
                                    dataReportService.saveFeedbackInfoList(feedbackInfoList);
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        message = "解析反馈文件出现错误";
                        return message;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "抓取反馈过程中出现错误：" + e.getMessage();
                return message;
            }
        }
        // 中国外汇中心
        if (("1").equals(reportOrgId)) {
            String fileName1 = "";
            fileName = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_01" + ".csv";
            fileName1 = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_02" + ".csv";
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
                File file3 = new File(path1);
                // 如果文件夹不存在则创建
                if (!file3.exists() && !file3.isDirectory()) {
                    file3.mkdir();
                }
                // ftp连接地址，端口号
                client.connect(reportConfigInfo.getFtpAddress(), Integer.parseInt(reportConfigInfo.getFtpPort()));
                // ftp连接用户名，密码
                client.login(reportConfigInfo.getFtpUserId(), reportConfigInfo.getFtpPassword());
                // 选择ftp目录
                try {
                    // client.changeDirectory(reportConfigInfo.getUploadPath());
                    client.changeDirectory(date);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    message = "远程服务器尚未创建反馈文件夹";
                    return message;
                }
                // qclient.changeDirectory("102283_江苏银行/"+request.getParameter("date").replace("-",""));
                FTPFile[] fs = client.list();
                int i = 0;
                downloadFileName = fileName;
                for (FTPFile ff : fs) {
                    // 如果反馈文件存在02则优先下载它
                    if (ff.getName().equals(fileName1)) {
                        i++;
                        File localFile = new File(path1 + File.separator + ff.getName());
                        client.download(fileName1, localFile);
                        downloadFileName = fileName1;
                        if (i == 2) {
                            break;
                        }
                    }
                    if (ff.getName().equals(fileName)) {
                        i++;
                        File localFile = new File(path1 + File.separator + ff.getName());
                        client.download(fileName, localFile);
                        if (i == 2) {
                            break;
                        }
                    }
                }
                client.logout();
                client.disconnect(true);
                if (i == 0) {
                    message = "反馈文件不存在";
                    return message;
                }
                else {
                    // 解析结果入库
                    try {
                        DataInputStream in = new DataInputStream(new FileInputStream(new File(path1 + File.separator
                                + downloadFileName)));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String line1 = reader.readLine();// 第一行信息，为标题信息，不用
                        if (line1 != null) {
                            String line = null;
                            List<FeedbackInfo> feedbackInfoList = new ArrayList<FeedbackInfo>();
                            // 从报表文件中获得产品代码列表
                            List<String> cpdmList = this.findCpdmList(term, reportOrgId);
                            // 根据期数和上报机构ID删除反馈结果记录
                            dataReportService.delFeedback(term, reportOrgId);
                            // 备案信息文件,文件内行号,存单代码,业务发生日,备案状态,备注,数据校验结果
                            Boolean errorFlag = false;
                            while ((line = reader.readLine()) != null) {
                                String[] items = line.split(",");
                                int rowNum = Integer.parseInt(items[1]);// 文件内行号
                                String validateMessage = items[items.length - 1];// 数据校验结果
                                if (!"数据正确".equals(validateMessage)) {
                                    errorFlag = true;
                                    String cpdm = cpdmList.get(rowNum - 1);// 产品代码
                                    FeedbackInfo feedbackInfo = new FeedbackInfo();
                                    feedbackInfo.setTerm(term);
                                    feedbackInfo.setReportOrgId(Integer.parseInt(reportOrgId));
                                    feedbackInfo.setFlag(Constants.FEEDBACK_FEEDBACKFAIL);
                                    feedbackInfo.setCpdm(cpdm);
                                    feedbackInfo.setErrorInfo(validateMessage);
                                    feedbackInfoList.add(feedbackInfo);
                                }
                            }
                            // 如果每一条记录都返回正确则插入结果为成功
                            if (!errorFlag) {
                                FeedbackInfo feedbackInfo = new FeedbackInfo();
                                feedbackInfo.setTerm(term);
                                feedbackInfo.setReportOrgId(Integer.parseInt(reportOrgId));
                                feedbackInfo.setFlag(Constants.FEEDBACK_FEEDBACKSUCC);
                                feedbackInfoList.add(feedbackInfo);

                                // 更新报送记录状态
                                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEDBACKSUCC);
                                reportResultInfo.setReportTime(reportTime);
                                reportResultInfo.setReportUserId(1);
                                dataReportService.updateReportResult(reportResultInfo);
                            }
                            else {
                                // 更新报送记录状态
                                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEBBACKFAIL);
                                reportResultInfo.setReportTime(reportTime);
                                reportResultInfo.setReportUserId(1);
                                dataReportService.updateReportResult(reportResultInfo);
                            }
                            // 保存反馈结果
                            dataReportService.saveFeedbackInfoList(feedbackInfoList);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        message = "解析反馈文件出现错误";
                        return message;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "连接FTP服务器失败";
                return message;
            }
        }
        return message;
    }

    /**
     * 从报表文件中获得产品代码列表
     * 
     * @return
     * @throws Exception
     */
    private List<String> findCpdmList(String term, String reportOrgId) throws Exception {
        List<String> cpdmList = new ArrayList<String>();
        // 拼接本地生成报表路径
        String path = Config.WEBROOTPATH + Config.confMap.get("uploadFilePath");
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
        fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode") + "_01"
                + ".csv";

        String[] list = file1.list();
        if (list == null || list.length == 0) {
            return null;
        }
        int j = 1;
        int i = 0;
        while (i != list.length) {
            if (list[i].equals(fileName)) {
                j++;
                fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                        + "_0" + j + ".csv";
                i = 0;
                continue;
            }
            i++;
        }
        fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode") + "_0"
                + (j - 1) + ".csv";

        String path2 = path1 + File.separator + fileName;
        File file2 = new File(path2);
        if (!file2.exists()) {
            return null;
        }
        DataInputStream in = new DataInputStream(new FileInputStream(new File(path2)));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line1 = reader.readLine();// 第一行信息，为标题信息，不用
        if (line1 != null) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split(",");
                String cpdm = items[2];// 产品代码是第3列
                cpdmList.add(cpdm);
            }
        }
        return cpdmList;
    }
}
