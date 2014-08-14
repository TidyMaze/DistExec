import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServeurDistExec {
	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;

	ServerSocket ss;
	Thread threadNouveauClients;
	boolean arret = true;
	
	int port;
	
	public ServeurDistExec(){
		
	}
	
	
	public void start() {
		
		if( arret == false ) System.out.println(" --  Serveur : already started");
		else {
			
			System.out.println(" --  Serveur : launching");
			
			try {
				ss = NetworkUtil.findServerSocketSocket(PORTMIN, PORTMAX);
				this.port = ss.getLocalPort();
				
			} catch (ConnectException e) {
				e.printStackTrace();
			}
			
			this.threadNouveauClients = new Thread(new Runnable() {			
				@Override
				public void run() {
					while( !arret ){
						try {
							Socket socketClient = ss.accept();	// bloquant
							System.out.println(" --  Serveur : new client");
							ThreadClient tClient = new ThreadClient(socketClient); 	
							tClient.start();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			this.threadNouveauClients.start();
			arret = false;
			
			System.out.println(" --  Serveur : started on port " + this.port );
		}
		
		
	}
	
	public void stop() {
		
		if( arret == true ) System.out.println(" --  Server : already stopped");
		else {
			
			try {
				this.ss.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			this.threadNouveauClients.interrupt();
			arret = true;
			
			System.out.println(" --  Serveur : stopped");
		}
		
	}
	
	public void restart() {
		this.stop();
		this.start();
	}
	
}
