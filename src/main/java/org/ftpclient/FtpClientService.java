package org.ftpclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FtpClientService {

	@Autowired
	public FtpConfiguration ftpConfiguration;

	FTPClient ftp = null;

	public void executeTask() {
		try {
			ftpDownloader();
			downloadFile(ftpConfiguration.getRemoteFilePath(), ftpConfiguration.getLocalFilePath());
			System.out.println("FTP File downloaded successfully");
			disconnect();
		} catch (SocketException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ftpDownloader() throws SocketException, IOException, Exception {
		String host = ftpConfiguration.getFtpHost();
		String user = ftpConfiguration.getFtpUser();
		String pwd = ftpConfiguration.getPassword();

		ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		int reply;
		ftp.connect(host);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new Exception("Exception in connecting to FTP Server");
		}
		ftp.login(user, pwd);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();

		//listDirectory(ftp, "/pub/example/", "/", 0);
	}

	private void listDirectory(FTPClient ftpClient, String parentDir, String currentDir, int level) throws IOException {
		String dirToList = "/";
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}
		FTPFile[] subFiles = ftp.listFiles(dirToList);
		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					// skip parent directory and directory itself
					continue;
				}
				for (int i = 0; i < level; i++) {
					System.out.print("\t");
				}
				if (aFile.isDirectory()) {
					System.out.println("[..." + currentFileName + "....]");
					listDirectory(ftp, dirToList, currentFileName, level + 1);
				} else {
					System.out.println("CURR-FILE::: " + currentFileName);
				}
			}
		}
	}

	public void downloadFile(String remoteFilePath, String localFilePath) {
		try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
			this.ftp.retrieveFile(remoteFilePath, fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		if (this.ftp.isConnected()) {
			try {
				this.ftp.logout();
				this.ftp.disconnect();
			} catch (IOException f) {
				// do nothing as file is already downloaded from FTP server
				f.printStackTrace();
			}
		}
	}

}
