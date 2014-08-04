package de.contentcreation.pplive.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

public class FileHandler {
	private static FileHandler instance = null;

	public static FileHandler getInstance() {
		if (instance == null) {
			instance = new FileHandler();
		}
		return instance;
	}

	public void downloadFile(HttpServletResponse response, File file)
			throws IOException, FileNotFoundException {
		StringBuilder type = new StringBuilder("attachment; filename=");
		type.append(file.getName());
		response.setContentLength((int) file.length());
		response.setCharacterEncoding("application/octet-stream");
		response.setHeader("Content-Disposition", type.toString());

		OutputStream os = response.getOutputStream();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[10000];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer)) > 0) {
			os.write(buffer, 0, bytesRead);
		}
		os.flush();
		fis.close();
	}
}
