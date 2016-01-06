package com.fitech.framework.comp.net.ftp;

/**
 * Ftp抓取的文件信息类
 * 
 * @author rds
 * @date 2012-03-21
 */
public class FileInfo{
	/**
	 * 文件记录ID
	 */
	private String id;
	/**
	 * 文件序列号
	 */
	private int fileSequence;
	/**
	 * 源文件名称
	 */
	private String srcFileName;
	/**
	 * 目标文件名称
	 */
	private String destFileName;
	/**
	 * 源文件大小
	 */
	private long srcFileSize;
	/**
	 * 目标文件大小
	 */
	private long destFileSize;
	/**
	 * ftp处理标识
	 * 0:等待抓取;1：抓取成功;2：抓取失败
	 */
	private int ftpFlag;
	/**
	 * ftp处理标识
	 * 0:等待抓取;1：抓取成功;2：抓取失败
	 */
	public static final int FTPFLAG_WAIT = 0;
	public static final int FTPFLAG_SUCCESS = 1;
	public static final int FTPFLAG_FAILED = 2;
	
	/**
	 * 文件后缀
	 */
	private String fileSuffix;
	
	/**
	 * 删除源文件处理结果标识
	 */
	private boolean delSrcFlag;
	
	public FileInfo(){}

	public int getFileSequence() {
		return fileSequence;
	}

	public void setFileSequence(int fileSequence) {
		this.fileSequence = fileSequence;
	}

	public String getSrcFileName() {
		return srcFileName;
	}

	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}

	public String getDestFileName() {
		return destFileName;
	}

	public void setDestFileName(String destFileName) {
		this.destFileName = destFileName;
	}

	public long getSrcFileSize() {
		return srcFileSize;
	}

	public void setSrcFileSize(long srcFileSize) {
		this.srcFileSize = srcFileSize;
	}

	public long getDestFileSize() {
		return destFileSize;
	}

	public void setDestFileSize(long destFileSize) {
		this.destFileSize = destFileSize;
	}

	public int getFtpFlag() {
		return ftpFlag;
	}

	public void setFtpFlag(int ftpFlag) {
		this.ftpFlag = ftpFlag;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public boolean getDelSrcFlag() {
		return delSrcFlag;
	}

	public void setDelSrcFlag(boolean delSrcFlag) {
		this.delSrcFlag = delSrcFlag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
