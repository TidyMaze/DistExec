import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import BD.Commande;
import BD.DatabaseHelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;



public class ThreadClient extends Thread {

	private Socket socketClient;
	private BufferedReader br;
	private BufferedWriter bw;
	
	public ThreadClient(Socket socketClient) throws IOException {
		super();
		
		this.socketClient = socketClient;
		
		InputStream is = socketClient.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
		
		OutputStream outputStream = socketClient.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter( outputStream );
		this.bw = new BufferedWriter( osw );
		
	}

	@Override
	public void run() {
		super.run();
		
		while(true){
			try {
				String ligne = br.readLine();
				if(ligne != null){
					System.out.println(ligne);
					
					switch(ligne) {
					
						case "HELO" :
							System.out.println("bien reçu HELO :-)");
							
							this.sendCommandes();
							break;
						
						default:
							break;
					}
					
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void sendCommandes() {
	
		try {
			JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
			DatabaseHelper dbh = new DatabaseHelper(cs);
			Dao<Commande, Integer> commandeDao = dbh.getCommandeDao();
			
			JSONObject json_commandes = new JSONObject();
			
			try {
				json_commandes.put( "commandes" , commandeDao.queryForAll() );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println( " --  Server : data :: " + json_commandes.toString() );
			
			try {
				json_commandes.write( this.bw );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
