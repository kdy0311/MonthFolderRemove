import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author kdy
 * 2021-04-08
 * 농공산단, 하이옥스에 있는 데이터를 사업개발실로 하루마다 옮김.
 * 농공산단->운영서버->사업개발실
 * 하이옥스->운영서버->사업개발실
 *
 */
public class Main {
	
	private static final Logger LOG = Logger.getGlobal();
    private static final String BD_URL = "192.168.0.4";					// 사업개발실 URL
    private static final String BD_USER = "ehdud0311";					// 사업개발실 계정
    private static final int BD_PORT = 2121;							// 사업개발실 port
    private static final String BD_PW = "itman1234!@";					// 사업개발실 비밀번호
    private static final String BD_NG_PATH = "/NASHDD/12_EMS2_PMS/수집데이터/농공산단";	// 사업개발실 디렉토리(서버)
    private static final String BD_HI_PATH = "/NASHDD/12_EMS2_PMS/수집데이터/HIOX";		// 사업개발실 디렉토리(서버)
    
    public static void main(String[] args) throws SecurityException, IOException {
    	// 로그 설정 start
		/*Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        LOG.setLevel(Level.INFO);
        Handler handler = new FileHandler("/home/Oadr/monDelete.log", true);
        CustomLogFormatter formatter = new CustomLogFormatter();
        handler.setFormatter(formatter);
        LOG.addHandler(handler);*/
        // 로그 설정 end
        
        downloadFile( BD_NG_PATH, "_ng" );
        downloadFile( BD_HI_PATH, "_hi" );
        
    }
    public static void downloadFile(String serverPath, String id){
    	Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM");
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		String folderName = sdf1.format(cal.getTime())+id;		// 2021_05_ng
		String fileName = sdf1.format(cal.getTime())+id+".zip";	// 2021_05_ng.zip
        FTPUploader ftpUploader = new FTPUploader(BD_URL, BD_PORT, BD_USER, BD_PW);
        try {
			if(ftpUploader.remove(serverPath, fileName, folderName)){
				LOG.info("::>> "+folderName+" 삭제 완료");
			}else{
				LOG.info("::>> "+folderName+" 삭제 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}