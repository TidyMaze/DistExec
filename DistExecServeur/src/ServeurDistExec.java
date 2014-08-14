import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServeurDistExec {
	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;

	ServerSocket ss;
	
	public ServeurDistExec(){
		try {
			ss = NetworkUtil.findServerSocketSocket(PORTMIN, PORTMAX);
			
			Thread threadNouveauClients = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							Socket socketClient = ss.accept();
							ThreadClient tClient = new ThreadClient(socketClient);  
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			threadNouveauClients.start();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
	}
}
