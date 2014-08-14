import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;


public class ThreadClient extends Thread {

	private Socket socketClient;
	private BufferedReader br;

	public ThreadClient(Socket socketClient) throws IOException {
		super();
		this.socketClient = socketClient;
		InputStream is = socketClient.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
	}

	@Override
	public void run() {
		super.run();
		
		while(true){
			try {
				String ligne = br.readLine();
				
				if(ligne != null){
					System.out.println(ligne);
					
					if(ligne == "HELO"){
						System.out.println("bien reçu HELO :-)");
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
