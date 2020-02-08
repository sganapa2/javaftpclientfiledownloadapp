package org.ftpclient;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpDownloader {

    public static void main(String[] args) {
        try {
            FtpDownloader ftpDownloader =
            		new FtpDownloader("test.rebex.net", "demo", "password");
            //new FTPDownloader("ftp://test.rebex.net/", "demo", "password");
            //    new FTPDownloader("ftp_server.journaldev.com", "ftp_user@journaldev.com", "ftpPassword");
            
            	
            //ftpDownloader.downloadFile("sitemap.xml", "/Users/pankaj/tmp/sitemap.xml");
            ftpDownloader.downloadFile("/pub/example/readme.txt", "/home/sganapa/eclipse-workspace/ftpdownloaderclient/readme.txt");
            System.out.println("FTP File downloaded successfully");
            ftpDownloader.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	FTPClient ftp = null;

    public FtpDownloader(String host, String user, String pwd) throws Exception {
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
        
        
        listDirectory(ftp, "/pub/example/", "/", 0);
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
                if (currentFileName.equals(".")
                        || currentFileName.equals("..")) {
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
            }
        }
    }
}
