package org.ftpclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpConfiguration {

	@Value("${app.ftp.host}")
	private String ftpHost;

	@Value("${app.ftp.user}")
	private String ftpUser;
	
	@Value("${app.ftp.password}")
	private String password;
	
	@Value("${app.ftp.remotefilepath}")
	private String remoteFilePath;
	
	@Value("${app.ftp.localfilepath}")
	private String localFilePath;
	
	public String getRemoteFilePath() {
		return remoteFilePath;
	}

	public void setRemoteFilePath(String remoteFilePath) {
		this.remoteFilePath = remoteFilePath;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "FtpConfiguration [ftpHost=" + ftpHost + ", ftpUser=" + ftpUser + "]";
	}

}
