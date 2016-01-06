package com.fitech.framework.comp.net.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fitech.framework.core.common.Config;



/**
 * FTP服务器方法提供者，提供了文件下载方法
 * 
 * @author rds
 * @Date 2012-03-21
 */
public class FtpService{
	
	Logger log = Logger.getLogger(FtpService.class);
	
	private FtpRobot robot = null;

	private boolean readState=false;
	private boolean downloadFlag =false;
	private boolean cdFlag =false;
	
	/**
	 * 批量文件下载
	 * 
	 * @param fileArray FTP文件列表
	 * @param configer FTP服务器配置对象
	 * @return 批量下载的文件列表
	 * @throws Exception
	 */
	public List<FileInfo> get(List<FileInfo> fileArray,ConfInfo configer) throws Exception
	{
		
		if(fileArray == null || fileArray.size()==0){
			log.info("[Ftp-Info]文件列表为空，退出。");
			return null;
		}
		
		if(configer==null || configer.getHost().equals("") ){
			log.info("[Ftp-Info]配置文件对象为空，退出。");
			return null;
		}
		
		/**可以被处理的数据包文件列表*/
		List<FileInfo> fileList= new ArrayList<FileInfo>();
		try{
			log.info("[Ftp-Info]当前FTP任务执行["+configer.getHost()+":"+configer.getPort()+"开始]......");
			robot = new FtpRobot(configer.getHost(),configer.getPort(),configer.getUserName(),configer.getPassword());
			
			/**初始化FtpClient，获取登录信息*/
			readState=robot.initRobot();
			
			/**初始化成功！*/
			if(readState)
			{
				log.info("[Ftp-Info]初始化状态："+readState);
				cdFlag=robot.changeWorkDir(configer.getRemotePath());
				log.info("[Ftp-Info]进入远程目录：" + configer.getRemotePath() + ",执行结果："+cdFlag);
				 
					/*****************************************
					 * 判断数据库记录里的数据包在ftp服务器上是否存在。
					 * 只抓取在服务器上存在的数据包。
					 * ***************************************
					 */
					if(fileArray!=null && fileArray.size()>0)
					{
						String [] fileNames =robot.listFileNames();
						
						if(fileNames==null || fileNames.length==0){
							log.info("远程目录中没有文件，退出！！！");
							return null;
						}
						FileInfo tmpFile = null;	
						for(int j=0;j<fileArray.size();j++){
							tmpFile =fileArray.get(j);
							//System.out.println(tmpFile.getSrcFileName());
							for(int k=0;k<fileNames.length;k++){
								if(tmpFile.getSrcFileName().equals(fileNames[k].toString())){
									fileList.add(tmpFile);
									break;
								}
							}
						}
						log.info("[Ftp-Info]找到了" + fileList.size() + "个有效文件!");
						for(int i=0;i<fileList.size();i++)
						{
							log.info("[Ftp-Info]开始抓取："+fileList.get(i).getSrcFileName());
							/**下载数据包文件到本地目录*/
							downloadFlag=robot.downloadFile(configer.getLocalPath(), fileList.get(i));
							/**下载文件成功，删除服务器上的文件*/
							//if(downloadFlag)
							//delRemoteFlag=robot.deleteFile(configer.getRemotepath(),  fileList.get(i).getSrcFileName());
							
							fileList.get(i).setFtpFlag(downloadFlag == true ? FileInfo.FTPFLAG_SUCCESS : FileInfo.FTPFLAG_FAILED);
							log.info("[Ftp-Info]抓取文件" + downloadFlag);
						}
					}
			}
			log.info("[Ftp-Info]当前FTP任务执行[" + configer.getHost() + ":" + configer.getPort()+"]完成！");
		}catch(Exception e){
			log.info("[Ftp-Info]当前FTP任务执行[" + configer.getHost() + ":" + configer.getPort()+"]失败！");
			throw e;
		}finally{
			exitFtp();
		}
		
		return fileList;
	}
	
	/**
	 * 批量文件上传
	 * 
	 * @param fileArray FTP文件列表
	 * @param configer FTP服务器配置对象
	 * @return 批量上传的文件列表
	 * @throws Exception
	 */
	public List<FileInfo> put(List<FileInfo> fileArray,ConfInfo configer) throws Exception
	{
		
		if(fileArray == null || fileArray.size()==0){
			log.info("[Ftp-Info]文件列表为空，退出。");
			return null;
		}
		
		if(configer==null || configer.getHost().equals("") ){
			log.info("[Ftp-Info]配置文件信息为空，退出。");
			return null;
		}
		
		/**可以被处理的数据包文件列表*/
		List<FileInfo> fileList= new ArrayList<FileInfo>();
		try{
			log.info("[Ftp-Info]当前FTP任务执行["+configer.getHost()+":"+configer.getPort()+"开始]......");
			robot = new FtpRobot(configer.getHost(),configer.getPort(),configer.getUserName(),configer.getPassword());
			
			/**初始化FtpClient，获取登录信息*/
			readState=robot.initRobot();
			
			/**初始化成功！*/
			if(readState)
			{
				log.info("[Ftp-Info]初始化状态："+readState);
				cdFlag=robot.changeWorkDir(configer.getRemotePath());
				log.info("[Ftp-Info]进入远程目录：" + configer.getRemotePath() + ",执行结果："+cdFlag);
				 
					/*****************************************
					 * 判断数据库记录里的数据包在ftp服务器上是否存在。
					 * 只抓取在服务器上存在的数据包。
					 * ***************************************
					 */
					if(fileArray!=null && fileArray.size()>0)
					{
						for(int i=0;i<fileArray.size();i++)
						{
							log.info("[Ftp-Info]开始上传：[[["+fileArray.get(i).getSrcFileName()+"]]]");
							/**上传数据包文件到远程目录*/
							downloadFlag=robot.uploadFile(configer.getRemotePath(),configer.getLocalPath(),fileArray.get(i));
							/**下载文件成功，删除服务器上的文件*/
							//if(downloadFlag)
							//delRemoteFlag=robot.deleteFile(configer.getRemotepath(),  fileList.get(i).getSrcFileName());
							
							fileArray.get(i).setFtpFlag(downloadFlag == true ? FileInfo.FTPFLAG_SUCCESS : FileInfo.FTPFLAG_FAILED);
							log.info("[Ftp-Info]上传文件：" + downloadFlag);
						}
					}
			}else{
				log.info("[Ftp-Info]连接[" + configer.getHost() + ":" + configer.getPort()+"]失败！");
			}
			log.info("[Ftp-Info]当前FTP任务执行[" + configer.getHost() + ":" + configer.getPort()+"]完成！");
		}catch(Exception e){
			log.info("[Ftp-Info]当前FTP任务执行[" + configer.getHost() + ":" + configer.getPort()+"]失败！");
			throw e;
		}finally{
			exitFtp();
		}
		
		return fileArray;
	}
		
	/***
	 * 删除Ftp服务器文件
	 * 
	 * @param fileList
	 * @param configer
	 * @throws Exception
	 */
	public List<FileInfo> deleteRemoteFile(List<FileInfo> fileList,ConfInfo configer) throws Exception{

		log.info("删除Ftp服务器文件【开始】");
		if(null==fileList || fileList.size()==0){
			log.info("需要删除的文件列表为空，退出删除程序！！！");
			return null;
		}
			
		FtpRobot robot = new FtpRobot(configer.getHost(),configer.getPort(),configer.getUserName(),configer.getPassword());
		
		/**初始化FtpClient，获取登录信息*/
		boolean readState=robot.initRobot();
		log.info("[FtpService]robot初始化状态："+readState);
		if(readState){
			boolean cdFlag=robot.changeWorkDir(configer.getRemotePath());
			log.info("[FtpService]进入目录"+configer.getRemotePath()+",结果："+cdFlag);
			log.info("有["+fileList.size()+"]个文件需要删除");
			/**删除成功计数器*/
			int delCount=0;
			int fileSize=fileList.size();
			for(int i=0;i<fileSize;i++){
				boolean delRemoteFlag=robot.deleteFile(configer.getRemotePath(),  fileList.get(i).getSrcFileName());
				fileList.get(i).setDelSrcFlag(delRemoteFlag);
				if(delRemoteFlag)
					delCount++;
			}
			if(fileSize==delCount){
				log.info("删除全部文件成功！！！");
			}else{
				log.info("删除文件，成功：["+delCount+"]，失败["+(fileSize-delCount)+"]");
			}
		}
		robot.exitFtp();
		log.info("删除Ftp服务器文件【结束】");
		return fileList;
	}
	
	/******************************************************************************
	 * 获取ftp成功后，文件的大小
	 * @param fileName
	 * @return
	 * @throws Exception
	 ****************************************************************************
	 */
	public long checkFileSize(String fileName) 
	{
		File file = new File(fileName);
		if(file.isDirectory())
			throw new RuntimeException("File:"+fileName+" Is A Directory , Can`t Get FileSize Property !!!");
		log.info("文件名称："+fileName);
		log.info("文件大小："+file.length()+"Byte");
		return file.length();
	}
	
	/**
	 * 退出Ftp服务器，关闭连接
	 */
	private void exitFtp(){
		log.info("[Ftp-Info]关闭FTP连接!");
		robot.exitFtp();
	}
		
	/**
	 * 删除服务器文件
	 * @param remotePath
	 * @param fileName
	 */
	public void deleteFile(String remotePath ,String fileName){
		robot.deleteFile(remotePath, fileName);
	}
}
