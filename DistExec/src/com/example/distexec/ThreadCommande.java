package com.example.distexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.distexec.BD.Commande;
import com.example.distexec.BD.Serveur;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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

			// demande la liste des commandes du serveur
			/* Au format JSON :
			 * {
			 * 		etat: "executer"
			 * 		id: "id_commande"
			 * }
			 */
			JSONObject json_listerCommande = new JSONObject();
			json_listerCommande.put( "etat" , "executer" );
			json_listerCommande.put( "id" , commande.getIdServeur() );
			pw.println( json_listerCommande.toString() );
			pw.flush();	

		}
		catch( JSONException e ) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
