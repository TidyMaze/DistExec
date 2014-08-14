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
	
	public ServeurDistExec(){
		
	}
	
	
	public void start() {
		
		if( arret == false ) System.out.println(" --  Server already started");
		else {
			
			System.out.println(" --  Launching server");
			
			try {
				ss = NetworkUtil.findServerSocketSocket(PORTMIN, PORTMAX);			
			} catch (ConnectException e) {
				e.printStackTrace();
			}
			
			this.threadNouveauClients = new Thread(new Runnable() {			
				@Override
				public void run() {
					while( !arret ){
						try {
							Socket socketClient = ss.accept();	// bloquant
							ThreadClient tClient = new ThreadClient(socketClient); 
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			this.threadNouveauClients.start();
			arret = false;
			
			System.out.println(" --  Serveur started");
		}
		
		
	}
	
	public void stop() {
		
		if( arret == true ) System.out.println(" --  Server already stopped");
		else {
			
			try {
				this.ss.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			this.threadNouveauClients.interrupt();
			arret = true;
			
			System.out.println(" --  Serveur stopped");
		}
		
	}
	
	public void restart() {
		this.stop();
		this.start();
	}
	
}
