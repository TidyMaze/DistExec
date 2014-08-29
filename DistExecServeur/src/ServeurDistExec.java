

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;


public class ServeurDistExec {
	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private int port;
	
	ServerSocket ss;
	Thread threadNouveauClients;
	boolean arret = true;
	
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
							System.out.println(" --  Serveur : new client on port " + socketClient.getPort() );
							ThreadClient tClient = new ThreadClient(socketClient); 	
							tClient.start();
						} catch (IOException e) {
							//e.printStackTrace();
							System.out.println("une erreur est apparu, ou le serveur est arrêté");
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


	public int getPort() {
		return port;
	}	
	
	public String getIpLocal() {
		
		try {
			
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if ( !inetAddress.isLoopbackAddress() ) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        System.out.println("IP address " + ipAddress);
                        return ipAddress;
                    }
                }
            }
				
			
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
		/*
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "erreur, impossible d'obtenir l'adresse ip local";
		}
		*/
	}
	
}
