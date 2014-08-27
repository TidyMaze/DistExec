package com.example.distexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.example.distexec.BD.Serveur;

public class ThreadListeCommande extends Thread {

	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private Serveur serveur;
	private ListeCommandes listeCommandes;

	public ThreadListeCommande(ListeCommandes listeCommande) {
		this.serveur = listeCommande.getServeur();
		this.listeCommandes = listeCommande; // listeCommande
	}

	@Override
	public void run() {

		try {

			// connexion au serveur
			Socket socket = NetworkUtil.findSocket(serveur.getIp(), PORTMIN, PORTMAX);

			System.out.println("port distant : " + socket.getPort());
			System.out.println("port local   : " + socket.getLocalPort());

			// canaux d'écriture
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter pw = new PrintWriter(osw);

			// canaux de lecture
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			// demande la liste des commandes du serveur
			/*
			 * Au format JSON : { etat: "lister" }
			 */
			JSONObject json_listerCommande = new JSONObject();
			try {
				json_listerCommande.put("etat", "lister");
			} catch (JSONException e) {
				// If the value is non-finite number or if the key is null.
				e.printStackTrace();
			}
			pw.println(json_listerCommande.toString());
			pw.flush();
			System.out.println("demande des commandes envoyé");

			// récupère la liste des commandes du serveur
			String ligne = null;
			while (ligne == null) {
				ligne = br.readLine(); // bloquant !
			}
			
			// la convertie en JSON
			JSONObject reponse_json = new JSONObject(ligne);
			
			// on indique au thread principale (l'activité) que le résultat est près
			this.listeCommandes.setJSONResponseServeur(reponse_json);
			this.sendMessage( Status.OK );
			System.out.println("liste des commandes (json) : " + ligne);

		} catch (ConnexionException e) {
			e.printStackTrace();
			this.sendMessage( Status.ConnexionException );

		} catch (SocketException e) {
			// if there is an error in the underlying protocol, such as a TCP error.
			e.printStackTrace();
			this.sendMessage( Status.ERROR_ );
			
		} catch (IOException e) {
			e.printStackTrace();
			this.sendMessage( Status.ERROR_ );
			
		} catch (JSONException e) {
			e.printStackTrace();
			this.sendMessage( Status.JSONException );
		}
		
	}
	
	private void sendMessage( Status status ) {
		Handler h = this.listeCommandes.getHandler();
		h.sendMessage( h.obtainMessage( status.ordinal() , 0 , 0) );		
	}
	
}
