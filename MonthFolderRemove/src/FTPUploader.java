import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

public class FTPUploader {
	private String serverIp;
	private int serverPort;
	private String user;
	private String password;

	public FTPUploader(String serverIp, int serverPort, String user, String password) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.user = user;
		this.password = password;
	}

	public boolean remove(String filePath, String fileName, String folderName)
			throws SocketException, IOException, Exception {
		FTPClient ftpClient = new FTPClient();
		boolean result = true;
		try {
			ftpClient.setControlEncoding("EUC-KR");
			ftpClient.connect(serverIp, serverPort); // ftp 연결
			ftpClient.setControlEncoding("EUC-KR");
			int reply = ftpClient.getReplyCode(); // 응답코드받기
			if (!FTPReply.isPositiveCompletion(reply)) { // 응답이 false 라면 연결 해제
				ftpClient.disconnect();
				throw new Exception(serverIp + " FTP 서버 연결 실패");
			}
			ftpClient.setSoTimeout(1000 * 1000); // timeout 설정
			ftpClient.login(user, password); // ftp 로그인
			try {
				ftpClient.changeWorkingDirectory(filePath);
				ftpClient.enterLocalPassiveMode();
				ftpClient.type(FTP.BINARY_FILE_TYPE);
				FTPFile[] listFiles = ftpClient.listFiles();
				if(listFiles != null){
					for(int i=0; i<listFiles.length; i++){
						if(listFiles[i].getName().equals(fileName)){
							if(ftpClient.changeWorkingDirectory(filePath+"/"+folderName)){
								result = true;
								FTPFile[] targetFiles = ftpClient.listFiles();
								for(int j=0; j<targetFiles.length; j++){
									ftpClient.deleteFile(targetFiles[j].getName());
								}
								ftpClient.changeWorkingDirectory(filePath);
								break;
							}else{
								result = false;
								break;
							}
						}
					}
					if(ftpClient.deleteFile(folderName)){
						result = true;
					}else{
						result = false;
					}
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			return result; 
		} finally {
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		}
	}

}