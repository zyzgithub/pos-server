package com.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class FileUtil {

	private static final Log LOG = LogFactory.getLog(FileUtil.class);

	public boolean write(String path, String content, String fileName, boolean append) {
		boolean writeok = false;
		FileOutputStream fp = null;
		Writer out = null;
		try {
			String file_path = path + File.separator + fileName;
			fp = new FileOutputStream(file_path, append);
			out = new BufferedWriter(new OutputStreamWriter(fp, "UTF-8"));
			// out = new BufferedWriter(new OutputStreamWriter(fp,"GB2312"));
			out.write(content);
			writeok = true;
		} catch (IOException e) {
			LOG.debug(this.getClass().getName() + e);
			writeok = false;
		} finally {
			try {
				if (out != null)
					out.close();
				out = null;
			} catch (IOException e) {
			}
			try {
				if (fp != null)
					fp.close();
				fp = null;
			} catch (IOException e) {
			}
		}
		return writeok;
	}

	public String read(String path, String fileName) {
		BufferedReader br = null;
		InputStreamReader reader = null;
		FileInputStream input = null;
		StringBuffer sb = new StringBuffer();
		try {
			input = new FileInputStream(path + File.separator + fileName);
			reader = new InputStreamReader(input, "UTF-8");
			br = new BufferedReader(reader);
			String s1 = "";
			while ((s1 = br.readLine()) != null) {
				sb.append(s1).append("\n");
			}
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 将一个文件从一个目录拷贝到另一个目录
	 * 
	 * @param sourceDir
	 *            String
	 * @param destDir
	 *            String
	 * @param fileName
	 *            String
	 * @throws Exception
	 */
	public static void copyFile(String sourceDir, String destDir, String fileName) throws Exception {

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			File userDir = new File(destDir);
			if (!userDir.exists()) {
				userDir.mkdirs();
			}
			File destFile = new File(destDir + "/" + fileName);
			if (destFile.exists()) {
				return;
			}
			LOG.debug("开始读取文件流");
			LOG.debug("原文件:" + sourceDir + "/" + fileName);
			LOG.debug("目标文件:" + destDir + "/" + fileName);
			fis = new FileInputStream(sourceDir + "/" + fileName);
			fos = new FileOutputStream(destDir + "/" + fileName);

			byte[] buffer = new byte[1024];
			int count = fis.read(buffer);
			while (count != -1) {

				fos.write(buffer, 0, count);
				count = fis.read(buffer);
			}

		} catch (Exception ex) {
			LOG.error(ex);
			throw ex;

		} finally {
			if(null != fos)
				fos.close();
			if(null != fis)
				fis.close();
		}
	}

	/**
	 * 将一个文件从一个目录拷贝到另一个目录
	 * 
	 * @param sourceDir
	 *            String
	 * @param destDir
	 *            String
	 * @param fileName
	 *            String
	 * @throws Exception
	 */
	public static void copyFile(String sourceDir, String destDir, String fileName, String newFileName) throws Exception {

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			File userDir = new File(destDir);
			if (!userDir.exists()) {
				userDir.mkdirs();
			}
			File destFile = new File(destDir + "/" + newFileName);
			if (destFile.exists()) {
				return;
			}
			LOG.debug("开始读取文件流");
			fis = new FileInputStream(sourceDir + "/" + fileName);
			fos = new FileOutputStream(destDir + "/" + newFileName);

			byte[] buffer = new byte[1024];
			int count = fis.read(buffer);
			while (count != -1) {

				fos.write(buffer, 0, count);
				count = fis.read(buffer);
			}

		} catch (Exception ex) {
			LOG.error(ex);
			throw ex;

		} finally {
			if(null != fos)
				fos.close();
			if(null != fis)
				fis.close();
		}
	}

	/**
	 * 删除目录
	 * 
	 * @param sourceDir
	 *            String
	 * @throws Exception
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除目录下所有文件
	 * 
	 * @param sourceDir
	 *            String
	 * @throws Exception
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static boolean write(String path, byte bytes[], String fileName, boolean append) {
		boolean writeok = false;
		FileOutputStream fp = null;
		BufferedOutputStream out = null;
		try {

			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String file_path = path + File.separator + fileName;
			fp = new FileOutputStream(file_path, append);
			out = new BufferedOutputStream(fp);
			// out = new BufferedWriter(new OutputStreamWriter(fp,"GB2312"));
			out.write(bytes);
			writeok = true;
		} catch (IOException e) {
			LOG.debug(e);
			writeok = false;
		} finally {
			try {
				if (out != null)
					out.close();
				out = null;
			} catch (IOException e) {
			}
			try {
				if (fp != null)
					fp.close();
				fp = null;
			} catch (IOException e) {
			}
		}
		return writeok;
	}

	/**
	 * 读取源文件内容
	 * 
	 * @param filename
	 *            String 文件路径
	 * @throws IOException
	 * @return byte[] 文件内容
	 */
	public static byte[] readFile(String path, String filename) {

		File file = new File(path + File.separator + filename);
		if (filename == null || filename.equals("")) {
			throw new NullPointerException("无效的文件路径");
		}
		long len = file.length();
		byte[] bytes = new byte[(int) len];

		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			int r = bufferedInputStream.read(bytes);
			if (r != len)
				throw new IOException("读取文件不正确");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != bufferedInputStream)
				try {
					bufferedInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return bytes;

	}

	/**
	 * 删除文件
	 * 
	 * @param sourceDir
	 *            String
	 * @throws Exception
	 */
	public static void delFile(String path) {
		File file = new File(path);
		file.delete();
	}
	/**
	 * 压缩zip包
	 * @param scrFile 要压缩的文件列表
	 * @param zipPath 压缩包的名称
	 * @return 
	 */
	public static String zipCompressor(List<File> srcFile , String zipPath){
	        if (srcFile == null || srcFile.isEmpty())  return null;
	        
	        Project prj = new Project();  
	        Zip zip = new Zip();  
	        zip.setProject(prj);  
	        //设置zip的路径包括名称
	        zip.setDestFile(new File(zipPath));  
	        FileSet fileSet = new FileSet(); 
	        for (File file : srcFile) {
				fileSet.setFile(file);
			}
	        fileSet.setProject(prj);  
	        zip.addFileset(fileSet);  
	        //执行打包
	        zip.execute();
	        return zipPath;
	}
	/**
	 * 压缩zip包
	 * @param srcDir 要压缩目录
	 * @param zipPath 压缩包的名称
	 * @return 
	 */
	public static String zipCompressor(File srcDir , String zipPath){
		if (srcDir == null || !srcDir.exists())  return null;
		
		Project prj = new Project();  
		Zip zip = new Zip();  
		zip.setProject(prj);  
		//设置zip的路径包括名称
		zip.setDestFile(new File(zipPath));  
		FileSet fileSet = new FileSet(); 
		fileSet.setDir(srcDir);
		fileSet.setProject(prj);  
		zip.addFileset(fileSet);  
		//执行打包
		zip.execute();
		return zipPath;
	}
}
