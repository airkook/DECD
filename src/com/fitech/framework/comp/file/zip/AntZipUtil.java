package com.fitech.framework.comp.file.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class AntZipUtil {
	public void antZip(String dest,String src){
		Zip zip = new Zip();
	   zip.setBasedir(new File(src));
	   zip.setDestFile(new File(dest));
	   Project p = new Project();
	   p.setBaseDir(new File(src));
	   zip.setProject(p);
	   zip.execute();
	}
	
	/**
	 * 解压缩文件srcF到dir目录下
	 * 
	 * @param srcF
	 * @param dir
	 * @return
	 */
	public static List expandFile(File srcF, File dir) {
		String encoding = "GB2312";
		ZipFile zf = null;
		List<String> files = new ArrayList<String>();

		try {
			zf = new ZipFile(srcF, encoding);
			Enumeration e = zf.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) e.nextElement();
				extractFile(srcF, dir, zf.getInputStream(ze), ze.getName(),
						new Date(ze.getTime()), ze.isDirectory());
				files.add(ze.getName());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} finally {

			if (zf != null) {

				try {

					zf.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return files;
	}
	
	/**
	 * 
	 * @param srcF
	 * @param dir
	 * @param compressedInputStream
	 * @param entryName
	 * @param entryDate
	 * @param isDirectory
	 * @throws IOException
	 */
	protected static void extractFile(File srcF, File dir,
			InputStream compressedInputStream, String entryName,
			Date entryDate, boolean isDirectory) throws IOException {
		boolean overwrite = true;
		FileUtils fileUtils = FileUtils.newFileUtils();
		File f = fileUtils.resolveFile(dir, entryName);
		try {
			if (!overwrite && f.exists()
					&& f.lastModified() >= entryDate.getTime()) {
				return;
			}

			File dirF = fileUtils.getParentFile(f);
			if (dirF != null) {
				dirF.mkdirs();
			}

			if (isDirectory) {
				f.mkdirs();
			} else {
				byte[] buffer = new byte[1024];
				int length = 0;
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(f);

					while ((length = compressedInputStream.read(buffer)) >= 0) {
						fos.write(buffer, 0, length);
					}

					fos.close();
					fos = null;
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}

			fileUtils.setFileLastModified(f, entryDate.getTime());
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

	}
	
	
	 /**
     * 获取XML文档
     * @param File XML文件
     * @return Document
     * @throws DocumentException
     */
    public static Document getDocument(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
        	e.printStackTrace();
            return null;
        }
		
        return document;
    }
    
    /**
     * 取得根节点
     * @param File XML文件
     * @return Element
     */
    public static Element getRootElement(File file) {
        Document doc = null;
        try {
            doc = getDocument(file);
        } catch (DocumentException e) {
           return null;
        }
        
        if(doc != null) {
	        Element root = null;
	        root = doc.getRootElement();
	        return root;
        }
        else {
            return null;
        }
    }
}
