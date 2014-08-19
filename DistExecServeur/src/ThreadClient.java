
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import BD.Commande;
import BD.DatabaseHelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;



public class ThreadClient extends Thread {

	private Socket socketClient;
	private BufferedReader br;
	private PrintWriter pw;
	
	public ThreadClient(Socket socketClient) throws IOException {
		super();
		
		this.socketClient = socketClient;
		
		InputStream is = socketClient.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
		
		OutputStream outputStream = socketClient.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter( outputStream );
		this.pw = new PrintWriter( osw );
		
	}

	@Override
	public void run() {
		super.run();
		
		while(true){
					
			try {
				
				String ligne = br.readLine();
				if( ligne != null ) {
					
					try {
						
						JSONObject reponse_json = new JSONObject( ligne );
						
						if( reponse_json.has("etat") ) {
							
							String etat = reponse_json.getString("etat");
							switch(etat) 
							{
							case "lister" :
								this.sendCommandes();
								break;
							case "executer" :
								this.executerCommande(reponse_json);
								break;
							default :
								this.sendCommandes();
								break;
							}
							
						}
						else {
							// ne possède pas le champs etat
							
						}	
						
					} catch (JSONException e) {
						// Ce n'est pas du JSON !!!
						e.printStackTrace();
					}
					
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	private void executerCommande( JSONObject json ) {

		if( !json.has("id") ) return; 	// normalement impossible

		// recupération de l'identifiant de la commande
		int id = -1;
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			// Ne devrait jamais passer par là, car on a déja vérifier que la clé "id" existe
			e.printStackTrace();
		}

		try {

			// recherche du script dans la BD
			JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
			DatabaseHelper dbh = new DatabaseHelper(cs);
			Dao<Commande, Integer> commandeDao = dbh.getCommandeDao();						

			Commande la_commande = commandeDao.queryForId(id);
			
			File script = new File( la_commande.getScript() );
			System.out.println( la_commande.getScript() + " " + script.getAbsolutePath() );
			
			if( !script.exists() ) {
				System.out.println("le fichier script n'existe pas sur le serveur");
				
				// le dire au client 
				//
				//
				//
			}
			else {
				
				String extension = la_commande.getScript().substring( la_commande.getScript().indexOf('.') );				
				
				
				// execution de la commande
				try {
					
					Runtime.getRuntime().exec( "cmd /c start " + la_commande.getScript() );

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
	}
		
	private void sendCommandes() {
	
		try {
			
			// connexion � la BD
			JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
			DatabaseHelper dbh = new DatabaseHelper(cs);
			Dao<Commande, Integer> commandeDao = dbh.getCommandeDao();
			
			// conversion des donn�es de la BD en JSON
			JSONObject json_commandes = new JSONObject();
			try {
				json_commandes.put( "commandes" , commandeDao.queryForAll() );
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			// envoie des donn�es convertie en JSON
			System.out.println( " --  Server : data :: " + json_commandes.toString() );
			
			this.pw.println( json_commandes.toString() );
			this.pw.flush();	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
