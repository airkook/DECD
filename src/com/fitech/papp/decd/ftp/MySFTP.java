package com.fitech.papp.decd.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author xujj
 */
public class MySFTP {

    /**
     * 
     * @param host
     * 
     * @param port
     * 
     * @param username
     * 
     * @param password
     * 
     * @return
     */
    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        }
        catch (Exception e) {

        }
        return sftp;
    }

    /**
     * �ϴ��ļ�
     * 
     * @param directory �ϴ���Ŀ¼
     * @param uploadFile Ҫ�ϴ����ļ�
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �����ļ�
     * 
     * @param directory ����Ŀ¼
     * @param downloadFile ���ص��ļ�
     * @param saveFile ���ڱ��ص�·��
     * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ���ļ�
     * 
     * @param directory Ҫɾ���ļ�����Ŀ¼
     * @param deleteFile Ҫɾ����ļ�
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �г�Ŀ¼�µ��ļ�
     * 
     * @param directory Ҫ�г���Ŀ¼
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(directory);
    }

    public static void main(String[] args) {
        MySFTP sf = new MySFTP();
        String host = "192.168.0.1";
        int port = 22;
        String username = "root";
        String password = "root";
        String directory = "/home/httpd/test/";
        String uploadFile = "D:\\tmp\\upload.txt";
        String downloadFile = "upload.txt";
        String saveFile = "D:\\tmp\\download.txt";
        String deleteFile = "delete.txt";
        ChannelSftp sftp = sf.connect(host, port, username, password);
        sf.upload(directory, uploadFile, sftp);
        sf.download(directory, downloadFile, saveFile, sftp);
        sf.delete(directory, deleteFile, sftp);
        try {
            sftp.cd(directory);
            sftp.mkdir("ss");
            System.out.println("finished");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
