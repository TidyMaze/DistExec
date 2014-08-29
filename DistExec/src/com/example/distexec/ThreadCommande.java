package com.example.distexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.distexec.BD.Commande;
import com.example.distexec.BD.Serveur;

import android.os.Handler;
import android.util.Log;


public class ThreadCommande extends Thread {


	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private Serveur serveur;
	private Commande commande;
	private VueCommande vueCommande;

	public ThreadCommande( VueCommande vueCommande ) {

		this.vueCommande = vueCommande;
		this.serveur = vueCommande.getServeur();
		this.commande = vueCommande.getCommande();
	}


	@Override
	public void run() {

		this.sendMessage( Status.START );
		
		if( !NetworkUtil.isConnected( this.vueCommande.getApplicationContext()) ) {
			System.out.println("VueCommande.class : android non connecté ! (clic sur bouton 'executer')");
			//Toast.makeText(getApplicationContext(), "Vous devez connecter votre appareil à internet", Toast.LENGTH_SHORT).show();
			this.sendMessage( Status.NO_INTERNET );
			return;
		}
		
		
		// connexion au serveur
		try {

			Socket socket = NetworkUtil.findSocket( serveur.getIp(), PORTMIN, PORTMAX );
			System.out.println( "port distant : " + socket.getPort() );
			System.out.println( "port local : " + socket.getLocalPort() );

			// canaux d'écriture
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter pw = new PrintWriter(osw);

			// canaux de lecture
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			// demande l'exécution d'une commande sur le serveur
			/* Au format JSON :
			 * {
			 * 		etat: "executer"
			 * 		id: "id_commande"
			 * }
			 */
			this.sendMessage( Status.DEMANDE_ );
			JSONObject json_listerCommande = new JSONObject();
			try {
				json_listerCommande.put( "etat" , "executer" );
				json_listerCommande.put( "id" , commande.getIdServeur() );
			} catch (JSONException e) {
				// If the value is non-finite number or if the key is null.
				e.printStackTrace();
			}
			pw.println( json_listerCommande.toString() );
			pw.flush();	

			
			
			
			// récupère la liste des commandes du serveur
			this.sendMessage( Status.RECUPERE_ );
			String ligne = null;
			while (ligne == null) {
				ligne = br.readLine(); // bloquant !
			}

			// la convertie en JSON
			this.sendMessage( Status.CONVERTIE_ );
			JSONObject reponse_json = new JSONObject(ligne);

			// on indique au thread principale (l'activité) que le résultat est près
			this.vueCommande.setJSONResponseServeur(reponse_json);
			this.sendMessage( Status.OK );
			System.out.println("liste des commandes (json) : " + ligne);

		}
		catch( JSONException e ) {
			e.printStackTrace();
			this.sendMessage( Status.JSONException );
		} catch (IOException e) {
			e.printStackTrace();
			this.sendMessage( Status.ERROR_ );
		}
		catch( ConnexionException e ) {
			e.printStackTrace();
			this.sendMessage( Status.ConnexionException );
			Log.e("info" , "exception : " + e.getMessage() );
		}


	}

	
	private void sendMessage( Status status ) {
		Handler h = this.vueCommande.getHandler();
		h.sendMessage( h.obtainMessage( status.ordinal() , 0 , 0) );		
	}
	

}
