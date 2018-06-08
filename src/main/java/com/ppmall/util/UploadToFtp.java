package com.ppmall.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadToFtp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "C:\\Users\\ygnet\\Desktop\\test0528\\菜单管理\\";
		List<File> uploadList = new ArrayList<>();
		File fileCap = new File(path);
		File fileList[] = fileCap.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			uploadList.add(file);
		}
		try {
			FtpUtil.uploadFile(uploadList,"pub/images");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
