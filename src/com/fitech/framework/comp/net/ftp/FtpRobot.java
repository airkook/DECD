package com.fitech.framework.comp.net.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.util.StringUtil;

/**
 * 	<b>****FTP文件传输机器人****</b>
 *  <li>实现多服务器，多线程，文件下载、上传</li>
 */
public class FtpRobot 
{
	private Logger log = Logger.getLogger(FtpRobot.class);
	
	private String host = null;
	private Integer port = null;
	private String username = null;
	private String password = null;
	private FTPClient ftp = null;
	private FTPFile[] files  = null;
	private String[] fileNames = null;
	
	private boolean exceptionFlag =false;
	
	private boolean modelstate;
	private boolean initFlag =false;
	
	public FtpRobot(String host, Integer port, String username,String password){
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
	}
	
	/**
	 * 初始化的一些方法调用
	 */
	public boolean initRobot() throws Exception{
		return login();
	}
		
	/**重新登录*/
	public void reLogin(){
		
	}
	
	/**
	 * 登陆服务器，返回登陆状态
	 * @return Boolean state
	 */
	private boolean login () throws Exception
	{
		if (StringUtil.isEmpty(host) || port == null || StringUtil.isEmpty(username) || StringUtil.isEmpty(password))
			return false;
	
		boolean loginstate = false;
		try {
			ftp =new FTPClient();
			
			ftp.connect(host, port.intValue());
			loginstate=ftp.login(username, password);
		} catch (Exception e) {
			throw new RuntimeException("[Ftp-Err]连接异常："+e.getMessage());
		}
		
		return loginstate;
	}
	
	private void setBinaryModel(){
		try {
			modelstate=ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
			log.info("[Ftp-Info]二进制模式："+modelstate);
		} catch (IOException e) {
			log.error("[Ftp-Err]设置传输模式异常："+e.getMessage());
			exceptionFlag=true;
			//exitFtpForException();
		}
	}	
	
	/**
	 * 改变处理目录
	 * @param wordDir
	 * @return
	 */
	public boolean changeWorkDir(String workDir) throws Exception{
		boolean workdirstate = false;
		
		try {
			workdirstate=ftp.changeWorkingDirectory(workDir);
		} catch (IOException e) {
			log.error("[Ftp-Err]切换工作目录异常："+e.getMessage());
			exceptionFlag=true;
			//exitFtpForException();
			throw e;
		}
		
		return workdirstate;
	}
	
	/**
	 * 获取当前目录下的所有文件名
	 * @return String[] fileNames
	 */
	public String[] listNames(){
		try {
			fileNames=ftp.listNames();
		} catch (IOException e) {
			log.error(e.getMessage());
			exceptionFlag=true;
			exitFtpForException();
		}
		return fileNames;
	}
	
	public FTPFile[] listFiles(String path){
		try {
			files = ftp.listFiles(path);
		} catch (IOException e) {
			log.error("[Ftp-Err]列出目录下的文件异常：",e);
			exceptionFlag=true;
			exitFtpForException();
		}
		return files;
	}
	
	/**
	 * 下载数据文件
	 * @param String localPath 本地路径
	 * @param String fileName 需要下载的文件名
	 */
	public boolean downloadFile(String localPath ,String fileName){
		boolean downloadFlag = false;
		FileOutputStream op = null;
		
		if(StringUtil.isEmpty(localPath) == true || StringUtil.isEmpty(fileName) == true) return downloadFlag;
		
		try {
			File localFile = new File(localPath);
			if(!localFile.exists())
				localFile.mkdir();
			op = new FileOutputStream(formatPath(localPath) + fileName);
			//ftp.listNames();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			downloadFlag=ftp.retrieveFile(new String(fileName.getBytes("GBK"),"ISO-8859-1"),op);
			op.close();
		} catch (FileNotFoundException e) {
			log.error("[Ftp-Err]下载异常：", e);
			exceptionFlag=true;
			//exitFtpForException();			
		} catch (IOException e) {
			log.error("[Ftp-Err]文件输出流异常：", e);
			exceptionFlag=true;
			//exitFtpForException();
		}finally {
			try{
				if(op != null) op.close();
			}catch(Exception e){
				log.error(e);
			}
			//if(!downloadFlag) this.exitFtp();
		}
		
		return downloadFlag;
	}
	
	/**
	 * 下载数据文件
	 * @param String localPath 本地路径
	 * @param fileInfo FileInfo 需要下载的文件信息对象
	 */
	public boolean downloadFile(String localPath ,FileInfo fileInfo){
		boolean downloadFlag = false;
		FileOutputStream op = null;
		
		if(StringUtil.isEmpty(localPath) == true || fileInfo == null || StringUtil.isEmpty(fileInfo.getSrcFileName()) == true) return downloadFlag;
		
		try {
			String locaFileNm = formatPath(localPath) + fileInfo.getSrcFileName();
			File localFile = new File(localPath);
			if(!localFile.exists()) localFile.mkdir();
			
			File file = new File(locaFileNm);
			op = new FileOutputStream(file);
			//ftp.listNames();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			downloadFlag=ftp.retrieveFile(new String(fileInfo.getSrcFileName().getBytes("GBK"),"ISO-8859-1"),op);
			if(downloadFlag == true){
				fileInfo.setDestFileName(locaFileNm);
				fileInfo.setDestFileSize(file.length());
			}
		} catch (Exception e) {
			log.error("[Ftp-Err]下载异常：", e);
			exceptionFlag=true;			
		}finally {
			try{
				if(op != null) op.close();
			}catch(Exception e){
				log.error(e);
			}
		}
		
		return downloadFlag;
	}
	
	/**
	 * 上传数据文件
	 * 
	 * @param remotePath String 本地路径
	 * @param fileInfo FileInfo 需要下载的文件信息对象
	 */
	public boolean uploadFile(String remotePath,String localPath ,FileInfo fileInfo){
		boolean uploadFlag = false;
		FileInputStream in = null;
		
		if(StringUtil.isEmpty(localPath) == true || fileInfo == null || StringUtil.isEmpty(fileInfo.getSrcFileName()) == true) return uploadFlag;
		
		try {
			String locaFileNm = formatPath(localPath) + fileInfo.getSrcFileName();
			File localFile = new File(localPath);
			if(!localFile.exists()) localFile.mkdir();
			
			File file = new File(locaFileNm);
			in = new FileInputStream(file);
			//ftp.listNames();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(remotePath);
			uploadFlag=ftp.storeFile(fileInfo.getDestFileName(),in);
		} catch (Exception e) {
			log.error("[Ftp-Err]上传异常：", e);
			exceptionFlag=true;			
		}finally {
			try{
				if(in != null) in.close();
			}catch(Exception e){
				log.error(e);
			}
		}
		
		return uploadFlag;
	}
	
	/***
	 * 获取FTP服务器当前目录的文件列表
	 * @return
	 * @throws Exception
	 */
	public String[] listFileNames() throws Exception{
		return ftp.listNames();
	}
	
	/**
	 * 删除服务器上，某目录下的某个文件
	 * @param remotePath
	 * @param fileName
	 * @return
	 */
	public boolean deleteFile(String remotePath ,String fileName){
		boolean delFlag =false;
		try {
			//log.info("delPath:"+FileUtil.formatePaht(remotePath)+fileName);
			delFlag=ftp.deleteFile(formatPath(remotePath)+new String (fileName.getBytes("GBK"),"ISO-8859-1"));
		} catch (IOException e) {
			log.error("[Ftp-Err]删除服务器文件异常：",e);
			exceptionFlag=true;
			exitFtpForException();
		}finally{
			if(!delFlag)
				this.exitFtp();
		}
		return delFlag;
	}
	
	public void exitFtp(){		
		try {
			if(ftp != null){
				ftp.abor();
				ftp.disconnect();
			}
		} catch (IOException e) {
			log.error("[Ftp-Err]退出登录异常：",e);
		}
	}
	
	/**
	 * 由于异常发生，退出登录
	 */
	public void exitFtpForException (){
		log.info("[Ftp-Info]由于发生了不期待的异常，退出登录！");
		try {
			if(exceptionFlag){
				ftp.abor();
				ftp.disconnect();
			}
		} catch (IOException e) {
			log.error("[Ftp-Err]退出登录异常：",e);
		}
	}
	
	private static String formatPath(String path) {
		return replacePath(path)
			+ (replacePath(path).endsWith(Config.FILESEPARATOR) ? "" : Config.FILESEPARATOR);
	}

	private static String replacePath(String path) {
		return path = path.replace('\\', Config.FILESEPARATOR.charAt(0));
	}
}