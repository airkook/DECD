package com.fitech.framework.comp.net.ftp;

/**
 * Ftp连接的配置信息类
 * 
 * @author rds
 * @date 2012-03-12
 */
public class ConfInfo {
	/**
	 * Ftp服务器ip
	 */
	private String host;
	/**
	 * Ftp服务器端口
	 */
	private Integer port;
	/**
	 * Ftp服务器用户名
	 */
	private String userName;
	/**
	 * Ftp服务器密码
	 */
	private String password;
	/**
	 * Ftp服务器文件根目录
	 */
	private String remotePath;
	/**
	 * 本地文件存放目录
	 */
	private String localPath;
	/**
	 * 本地文件备份目录
	 */
	private String backupPath;
	
	public ConfInfo(){}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}
}
