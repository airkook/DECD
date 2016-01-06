package com.fitech.framework.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	 
	private static final int BUFFER_SIZE = 8 * 1024;
 
	public static void copyFile(File srcFile, File destFile) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(destFile);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (is.read(buffer) != -1)
				os.write(buffer);
			os.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void copyFile(InputStream input, File destFile) throws IOException {
		OutputStream os = null;
		try {
			if (destFile.exists())
				destFile.delete();
			os = new FileOutputStream(destFile);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (input.read(buffer) != -1)
				os.write(buffer);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	 
	public static void copyFile(String srcPath, String destPath) throws IOException {
		if (srcPath == null || srcPath.equals("") || destPath == null || destPath.equals("")) {
			return;
		}
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		copyFile(srcFile, destFile);
	}
 
	public static boolean deleteFile(String filePath) {
		if (filePath == null || filePath.equals("")) {
			return false;
		}
		File file = new File(filePath.trim());

		return deleteFile(file);
	}

	 
	public static boolean deleteFile(File file) {
		boolean result = true;
		if (!file.exists()) {
			return false;
		}
		String dirPath = null;
		if (file.isFile()) {
			file.delete();
		} else {
			File[] fileList = file.listFiles();
			int fileListSize = fileList.length;
			if (fileList != null && fileListSize > 0) {
				for (int i = 0; i < fileListSize; i++) {
					if (fileList[i].isFile()) {
						// System.out.println(fileList[i].getAbsolutePath());
						fileList[i].delete();
					} else if (fileList[i].isDirectory()) {
						dirPath = fileList[i].getPath();
						deleteFile(dirPath);
					}
				}
			}
			file.delete();
		}
		return result;
	}

 
	public static List readLineByLine(String path, String charset) throws Exception {
		List lst = new ArrayList();

		InputStream is = new FileInputStream(path);
		InputStreamReader isr = null;
		if (charset != null && !charset.equals("")) {
			isr = new InputStreamReader(is, charset);
		} else {
			isr = new InputStreamReader(is);
		}
		BufferedReader br = new BufferedReader(isr);

		String line = br.readLine(); 
		while (line != null) {
			lst.add(line);
			// byte b[] = line.getBytes();
			// for (int i = 0; i < b.length; i++) {
			// System.out.print(b[i]);
			// }
			// System.out.println();
			// System.out.println(line); 
			line = br.readLine(); 
		}
		br.close();
		isr.close();
		is.close();
		return lst;
	}

	 
	public static List readLineByLine(String path) throws Exception {
		return readLineByLine(path, null);
	}

 
	public static String readAllData(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		StringBuffer sb = new StringBuffer();

		try {
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				sb.append((char) tempchar);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String filterStr(String srcStr, int typeId) {
		String result = null;
		if (typeId == 0) {
			try {
				result = new String(srcStr.getBytes("UTF-8"), "ISO8859-1");
			} catch (Exception e) {
				result = null;
			}
		} else if (typeId == 2) {
			try {
				result = new String(srcStr.getBytes("ISO-8859-1"), "GB2312");
			} catch (Exception e) {
				result = null;
			}
		} else if (typeId == 3) {
			try {
				result = new String(srcStr.getBytes("UTF-8"), "GB2312");
			} catch (Exception e) {
				result = null;
			}
		} else {
			try {
				result = new String(srcStr.getBytes("ISO8859-1"), "UTF8");
			} catch (Exception e) {
				e.printStackTrace();
				result = null;
			}
		}
		return result;
	}

	 
	public static String toUtf8String(String fileName) {
		if (fileName == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	
	/**
     * 将文件名转成UTF字符串(解决中文文件名乱码)
     * 
     * @param src String 文件名
     * @return String 转换后的文件名
     */
    public static String toUtf8Strings(String src)
    {
        byte[] b = src.getBytes();
        char[] c = new char[b.length];
        for(int i=0;i<b.length;i++)
        {
            c[i] = (char)(b[i]&0x00FF);
        }
        return new String(c);
    }
}
