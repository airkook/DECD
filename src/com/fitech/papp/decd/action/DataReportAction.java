package com.fitech.papp.decd.action;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.ExcelExportInfo;
import com.fitech.framework.core.util.ExcelUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.ftp.MySFTP;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.model.vo.DataReportVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.DataReportService;
import com.fitech.papp.decd.util.Constants;
import com.jcraft.jsch.ChannelSftp;

/**
 * 数据上报action
 * 
 * @author xjj
 * 
 */
public class DataReportAction extends DefaultBaseAction {
    private static final long serialVersionUID = 6410381425726210498L;

    /** 日志 */
    private final Log log = LogFactory.getLog(DataReportAction.class);

    /** 数据上报 service注解 */
    @Autowired
    private DataReportService dataReportService;

    /** 数据上报 List */
    List<DataReportVo> dataReportList;

    /** 期数 */
    private String term;

    /** 用户名 */
    private String reportOrgName;

    /** 弹出信息 */
    private String message;

    /** 机构id */
    private String reportOrgId;

    private final String encoding = "UTF-8";

    private final String contentType = "application/json";

    /** 用户信息 */
    private UserInfo userInfo;

    /** 路径 */
    private String filePath;

    private String fileName;

    /**
     * 初始化
     * 
     * @return
     */
    public String init() throws Exception {
        // 默认为当前时间的前一天
        String now = DateUtil.getTodayDateStr();
        term = DateUtil.getLastDay(now);
        dataReportList = dataReportService.select(term);
        return SUCCESS;

    }

    /**
     * 查询
     * 
     * @return
     */
    public String search() throws Exception {
        dataReportList = dataReportService.select(term);
        return SUCCESS;

    }

    /**
     * 生成报文
     * 
     * @return
     */
    public String writeReport() throws Exception {
        String fileName = "";
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("term", term);
            paramMap.put("status", Constants.CDS_STATUS_REVIEW_PASS);
            List<CertificatesOfDeposit> cdList = dataReportService.findCdsList(paramMap);
            if (cdList.size() == 0) {
                message = "此期没有数据无法生成报表";
                return search();
            }

            // 拼接本地生成报表路径
            String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                    + Config.confMap.get("uploadFilePath");
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

            userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
            String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
            // 新增
            if (rri1 == null || rri1.size() == 0) {
                ReportResultInfo rri = new ReportResultInfo();
                rri.setTerm(term);
                rri.setReportOrgId(reportOrgIdInt);
                rri.setReportStatus(Constants.REPORT_RESULT_RELEASE);
                rri.setReportTime(reportTime);
                rri.setReportUserId(userInfo.getUserId());
                dataReportService.saveReportResult(rri);
            }
            else {
                rri1.get(0).setReportStatus(Constants.REPORT_RESULT_RELEASE);
                rri1.get(0).setReportTime(reportTime);
                rri1.get(0).setReportUserId(userInfo.getUserId());
                dataReportService.updateReportResult(rri1.get(0));
            }
            message = "生成报文成功";
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            message = "生成报文失败";
            log.error(e);
            e.printStackTrace();
        }
        return search();
    }

    /**
     * 上传到ftp
     * 
     * @return
     */
    public String uploadReport() throws Exception {
        // 根据传出机构id 查出ftp地址 端口 用户名 密码 四个参数
        ReportConfigInfo reportConfigInfo = dataReportService.findReportConfigInfo(reportOrgId);
        if (reportConfigInfo == null || StringUtil.isEmpty(reportConfigInfo.getFtpAddress())
                || StringUtil.isEmpty(reportConfigInfo.getFtpPort())
                || StringUtil.isEmpty(reportConfigInfo.getFtpUserId())
                || StringUtil.isEmpty(reportConfigInfo.getFtpPassword())) {
            message = "请先配置好FTP信息";
            return search();
        }

        // 查询报送记录
        int reportOrgIdInt = Integer.parseInt(reportOrgId);
        List<ReportResultInfo> rrilist = dataReportService.select(reportOrgIdInt, term);
        if (rrilist == null || rrilist.size() == 0) {
            message = "请先生成报表";
            return search();
        }
        ReportResultInfo reportResultInfo = rrilist.get(0);
        if (Constants.REPORT_RESULT_EDITDATA.equals(reportResultInfo.getReportStatus())) {
            message = "已修改数据，请重新生成报表";
            return search();
        }
        if (Constants.REPORT_RESULT_FEEDBACKSUCC.equals(reportResultInfo.getReportStatus())) {
            message = "已上报并且反馈结果为成功，无须重新上报";
            return search();
        }

        // 拼接本地生成报表路径
        String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                + Config.confMap.get("uploadFilePath");
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
                return search();
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
                return search();
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
                userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_NOTFEEDBACK);
                reportResultInfo.setReportTime(reportTime);
                reportResultInfo.setReportUserId(userInfo.getUserId());
                dataReportService.updateReportResult(reportResultInfo);

                message = "上报成功";

            }
            catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                message = "上报失败：连接FTP服务器失败";
                return search();
            }
        }
        if ("2".equals(reportOrgId)) {
            fileName = Config.confMap.get("shanghaiUser") + "_" + date + ".csv";
            String path2 = path1 + File.separator + fileName;
            File file2 = new File(path2);
            if (!file2.exists()) {
                message = "上报失败：报表不存在，请先生成报表";
                return search();
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
                        break;
                    }
                }
                sftp.cd(reportConfigInfo.getUploadPath());
                if (i == 0) {
                    sftp.mkdir(date);
                }
                sf.upload("/" + reportConfigInfo.getUploadPath() + "/" + date + "/", path2, sftp);
                sftp.disconnect();

                // 更新报送记录的状态为已上报反馈未抓取
                userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_NOTFEEDBACK);
                reportResultInfo.setReportTime(reportTime);
                reportResultInfo.setReportUserId(userInfo.getUserId());
                dataReportService.updateReportResult(reportResultInfo);

                message = "上报成功";
            }
            catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                message = "上报失败：连接SFTP服务器失败";
                return search();
            }
        }
        return search();
    }

    /**
     * 抓取反馈
     * 
     * @return
     * @throws Exception
     */
    public void touckFeedBack() throws Exception {
        String jsonStr = "";
        JSONObject jsonObject = new JSONObject();
        Map<String, String> map = new HashMap<String, String>();
        this.getResponse().setContentType("text/json");
        this.getResponse().setCharacterEncoding("UTF-8");
        PrintWriter writer = this.getResponse().getWriter();

        // 根据传出机构id 查出ftp地址 端口 用户名 密码 四个参数
        ReportConfigInfo reportConfigInfo = dataReportService.findReportConfigInfo(reportOrgId);
        if (reportConfigInfo == null || StringUtil.isEmpty(reportConfigInfo.getFtpAddress())
                || StringUtil.isEmpty(reportConfigInfo.getFtpPort())
                || StringUtil.isEmpty(reportConfigInfo.getFtpUserId())
                || StringUtil.isEmpty(reportConfigInfo.getFtpPassword())) {
            map.put("result", "error");
            map.put("message", "请先配置好FTP信息");
            jsonObject = JSONObject.fromObject(map);
            jsonStr = jsonObject.toString();
            writer.write(jsonStr);
            return;
        }

        // 查询报送记录
        int reportOrgIdInt = Integer.parseInt(reportOrgId);
        List<ReportResultInfo> rrilist = dataReportService.select(reportOrgIdInt, term);
        if (rrilist == null || rrilist.size() == 0) {
            map.put("result", "error");
            map.put("message", "请先生成报表");
            jsonObject = JSONObject.fromObject(map);
            jsonStr = jsonObject.toString();
            writer.write(jsonStr);
            return;
        }
        ReportResultInfo reportResultInfo = rrilist.get(0);
        if (Constants.REPORT_RESULT_EDITDATA.equals(reportResultInfo.getReportStatus())) {
            map.put("result", "error");
            map.put("message", "已修改数据，请重新生成报表");
            jsonObject = JSONObject.fromObject(map);
            jsonStr = jsonObject.toString();
            writer.write(jsonStr);
            return;
        }
        if (Constants.REPORT_RESULT_RELEASE.equals(reportResultInfo.getReportStatus())) {
            map.put("result", "error");
            map.put("message", "请先进行上报");
            jsonObject = JSONObject.fromObject(map);
            jsonStr = jsonObject.toString();
            writer.write(jsonStr);
            return;
        }
        if (Constants.REPORT_RESULT_FEEDBACKSUCC.equals(reportResultInfo.getReportStatus())) {
            map.put("result", "error");
            map.put("message", "已上报并且反馈结果为成功，无须重新抓取反馈");
            jsonObject = JSONObject.fromObject(map);
            jsonStr = jsonObject.toString();
            writer.write(jsonStr);
            return;
        }

        String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                + Config.confMap.get("downloadFilePath");
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
                    log.error(e);
                    e.printStackTrace();
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
                    map.put("result", "error");
                    map.put("message", "反馈文件不存在");
                }
                else {
                    map.put("result", "success");
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
                                        userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
                                        String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                        reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEDBACKSUCC);
                                        reportResultInfo.setReportTime(reportTime);
                                        reportResultInfo.setReportUserId(userInfo.getUserId());
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
                                        userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
                                        String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                        reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEBBACKFAIL);
                                        reportResultInfo.setReportTime(reportTime);
                                        reportResultInfo.setReportUserId(userInfo.getUserId());
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
                                            log.error(e);
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
                                        map.put("result", "error");
                                        map.put("message", "反馈文件格式错误");
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
                        log.error(e);
                        e.printStackTrace();
                        map.put("result", "error");
                        map.put("message", "解析反馈文件出现错误");
                    }
                }
            }
            catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                map.put("result", "error");
                map.put("message", "抓取反馈过程中出现错误：" + e.getMessage());
            }
        }
        // 中国外汇中心
        if (("1").equals(reportOrgId)) {
            String fileName = "";
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
                    log.error(e);
                    e.printStackTrace();
                    map.put("result", "error");
                    map.put("message", "远程服务器尚未创建反馈文件夹");
                    jsonObject = JSONObject.fromObject(map);
                    jsonStr = jsonObject.toString();
                    writer.write(jsonStr);
                    return;
                }
                // qclient.changeDirectory("102283_江苏银行/"+request.getParameter("date").replace("-",""));
                FTPFile[] fs = client.list();
                int i = 0;
                downloadFileName = fileName;
                for (FTPFile ff : fs) {
                    System.out.println(ff.getName());
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
                    map.put("result", "error");
                    map.put("message", "反馈文件不存在");
                }
                else {
                    map.put("result", "success");
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
                            List<String> cpdmList = this.findCpdmList();
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
                                userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
                                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEDBACKSUCC);
                                reportResultInfo.setReportTime(reportTime);
                                reportResultInfo.setReportUserId(userInfo.getUserId());
                                dataReportService.updateReportResult(reportResultInfo);
                            }
                            else {
                                // 更新报送记录状态
                                userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
                                String reportTime = DateUtil.getToday(DateUtil.DATA_TIME);
                                reportResultInfo.setReportStatus(Constants.REPORT_RESULT_FEEBBACKFAIL);
                                reportResultInfo.setReportTime(reportTime);
                                reportResultInfo.setReportUserId(userInfo.getUserId());
                                dataReportService.updateReportResult(reportResultInfo);
                            }
                            // 保存反馈结果
                            dataReportService.saveFeedbackInfoList(feedbackInfoList);
                        }
                    }
                    catch (Exception e) {
                        log.error(e);
                        e.printStackTrace();
                        map.put("result", "error");
                        map.put("message", "解析反馈文件出现错误");
                    }
                }
            }
            catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                map.put("result", "error");
                map.put("message", "连接FTP服务器失败");
            }
        }
        jsonObject = JSONObject.fromObject(map);
        jsonStr = jsonObject.toString();
        writer.write(jsonStr);
    }

    /**
     * 从报表文件中获得产品代码列表
     * 
     * @return
     * @throws Exception
     */
    private List<String> findCpdmList() throws Exception {
        List<String> cpdmList = new ArrayList<String>();
        // 拼接本地生成报表路径
        String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                + Config.confMap.get("uploadFilePath");
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

    /**
     * 下载反馈文件
     * 
     * @throws Exception
     */
    public void doDownLoad() throws Exception {
        // 拼接本地生成报表路径
        String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                + Config.confMap.get("downloadFilePath");
        // 文件夹的日期和文件的日期为业务发生日的第二天
        String date = DateUtil.getNextDay(term).replace("-", "");
        String path1 = path + File.separator + date;
        File file = null;
        // 文件名
        String fileName = "";
        // 上海清算所
        if ("2".equals(reportOrgId)) {
            fileName = Config.confMap.get("shanghaiUser") + "_" + date + "_f.csv";
            file = new File(path1 + File.separator + fileName);
        }
        // 中国外汇中心
        if (("1").equals(reportOrgId)) {
            String fileName1 = "";
            fileName = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_01" + ".csv";
            fileName1 = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_02" + ".csv";
            file = new File(path1 + File.separator + fileName1);
            if (!file.exists()) {
                file = new File(path1 + File.separator + fileName);
            }
        }

        this.getResponse().reset();
        this.getResponse().setContentType("application/octet-stream;charset=UTF-8");
        this.getResponse().addHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        this.getResponse().setHeader("Accept-ranges", "bytes");

        OutputStream os = this.getResponse().getOutputStream();
        byte b[] = new byte[500];
        long fileLength = file.length();
        String length = String.valueOf(fileLength);
        this.getResponse().setHeader("Content-length", length);
        FileInputStream in = new FileInputStream(file);
        for (int n = 0; (n = in.read(b)) != -1;) {
            os.write(b, 0, n);
        }
        in.close();
        os.close();
    }

    /**
     * @return the dataReportList
     */
    public List<DataReportVo> getDataReportList() {
        return dataReportList;
    }

    /**
     * @param dataReportList the dataReportList to set
     */
    public void setDataReportList(List<DataReportVo> dataReportList) {
        this.dataReportList = dataReportList;
    }

    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @param term the term to set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return the reportOrgName
     */
    public String getReportOrgName() {
        return reportOrgName;
    }

    /**
     * @param reportOrgName the reportOrgName to set
     */
    public void setReportOrgName(String reportOrgName) {
        this.reportOrgName = reportOrgName;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the reportOrgId
     */
    public String getReportOrgId() {
        return reportOrgId;
    }

    /**
     * @param reportOrgId the reportOrgId to set
     */
    public void setReportOrgId(String reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    /**
     * @return the userInfo
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo the userInfo to set
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
