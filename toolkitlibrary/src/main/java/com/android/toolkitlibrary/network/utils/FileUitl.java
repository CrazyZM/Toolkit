package com.android.toolkitlibrary.network.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.android.toolkitlibrary.network.base.FFApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;




/**
 * 网络的判断
 * 
 * @author maidoumi
 * 
 */
public class FileUitl {

	public static long removeAll(File file_parent) {
		if (file_parent.isFile()) {
			return 0;
		}
		File[] files = file_parent.listFiles();
		if (files == null) {
			return 0;
		}
		long size = 0;
		for (File file : files) {
			if (file.isDirectory()) {
				size += removeAll(file);
				file.delete();
			} else {
				size += file.length();
				file.delete();
			}
		}
		return size;
	}

	/**
	 * 复制文件到另一个路径
	 * 
	 * @param fromPath
	 * @param toPath
	 * @param rewrite
	 * @return
	 */
	public static boolean copyImage(String fromPath, String toPath,
                                    boolean rewrite) {
		boolean flag = false;
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		if (!fromFile.exists()) {
			return flag;
		}
		if (!fromFile.isFile()) {
			return flag;
		}
		if (!fromFile.canRead()) {
			return flag;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		try {
			FileInputStream isFrom = new FileInputStream(fromFile);
			FileOutputStream osTo = new FileOutputStream(toFile);
			byte tempByte[] = new byte[1024];
			int tempLength;
			while ((tempLength = isFrom.read(tempByte)) > 0) {
				osTo.write(tempByte, 0, tempLength);
			}
			osTo.flush();
			isFrom.close();
			osTo.close();

		}

		catch (Exception ex) {
			ex.printStackTrace();
			return flag;
		}
		scanFile(toFile);
		flag = true;
		return flag;
	}

	/**
	 * 更新系统相册
	 * 
	 * @param file
	 */

	public static void scanFile(File file) {
		Intent intent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		FFApplication.app.sendBroadcast(intent);
	}





	// 删除目录下所有文件
	public static void deleteAll(String path, boolean deleteDir) {
		File file = new File(path);
		deleteAll(file, deleteDir);
	}

	/**
	 * 删除所有
	 * 
	 * @param file
	 * @param deleteDir
	 */
	public static void deleteAll(File file, boolean deleteDir) {
		if (!file.exists())
			return;
		if (file.isFile() || (file.list().length == 0 && deleteDir)) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteAll(files[i], true);
				files[i].delete();
			}
			if (file.exists() && deleteDir) // 如果文件本身就是目录 ，就要删除目录
				file.delete();
		}
	}

	// 创建目录
	public static String createDir(String parentPath, String childDirName) {
		File parent = new File(parentPath);
		if (parent.exists() && parent.isDirectory()) {
			File child = new File(parentPath + "/" + childDirName);
			if (!child.exists() && child.mkdir()) {
				return child.getAbsolutePath();
			}
			if (child.exists() && child.isDirectory()) {
				return child.getAbsolutePath();
			}
		}
		return null;
	}

	/**
	 * 生成文件夹
	 * 
	 * @param path
	 * @return
	 */

	public static String createDir(String path) {
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			return path;
		}
		if (dir.mkdirs()) {
			return dir.getAbsolutePath();
		}
		return null;
	}

	// 使用url生成文件名称
	public static String fileNameFromURL(String url) {
		if (url != null) {
			return String.valueOf(url.hashCode());
		}
		return null;
	}

	// 根据文件路径判断文件是否可用
	public static boolean fileExist(String filePath) {
		File file = new File(filePath);
		return file.exists() && (file.length() > 0);
	}

	public static boolean isSDCardAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 获取SD卡可用空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getSdCrardFree() {
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		long blockSize;
		long freeBlocks;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			blockSize = sf.getBlockSizeLong();
			freeBlocks = sf.getAvailableBlocksLong();
		} else {
			freeBlocks = sf.getAvailableBlocks();
			blockSize = sf.getBlockSize();
		}
		return (freeBlocks * blockSize) / 1024 / 1024;
	}

	/**
	 * 获取SD的容量
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getSDAllSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		long blockSize;
		long allBlocks;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			blockSize = sf.getBlockSizeLong();
			allBlocks = sf.getBlockCountLong();
		} else {
			allBlocks = sf.getAvailableBlocks();
			blockSize = sf.getBlockCount();
		}
		return (allBlocks * blockSize) / 1024 / 1024;
	}

	public static boolean isSdFreeEnough() {
		if (FileUitl.isSDCardAvailable()) {
			if (!Environment.getExternalStorageDirectory()
					.canWrite()) {
				return false;
			}
			if (getSdCrardFree() > 20) {
				return true;
			}
		} else {
			if (!Environment.getDataDirectory().canWrite()) {
				return false;
			}
			if (getSdCrardFree() > 20) {
				return true;
			}
		}
		return false;
	}
}
