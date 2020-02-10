package org.ftpclient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FtpDownloader implements CommandLineRunner {

	@Autowired
	public FtpClientService ftpClientService;
	
    public static void main(String[] args) {
    	SpringApplication.run(FtpDownloader.class, args);
    }

	@Override
	public void run(String... arg0) throws Exception {
		try {
			ftpClientService.executeTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	

}
