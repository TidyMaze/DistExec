package com.example.distexec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import com.example.distexec.BD.DatabaseHelper;
import com.example.distexec.BD.Serveur;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListeCommandes extends Activity {

	private static final String IP = null;
	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private Serveur serveur;
	
	
	private ListView listeCommandes;
	
	private void buildList(){
    	
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_commandes);
		
		int idserv = getIntent().getIntExtra("idserv", -1);
		
		DatabaseHelper dbh = new DatabaseHelper(ListeCommandes.this);
		Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
		
		try {
			this.serveur = serveurDAO.queryForId(idserv);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.listeCommandes = (ListView)findViewById(R.id.listeCommandes);
		this.listeCommandes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				/*
				int idServ = ((StringItem)listeServeurs.getItemAtPosition(position)).second;
				Intent i = new Intent(ListeServeurs.this, ListeCommandes.class);
				i.putExtra("idserv", idServ);
				startActivity(i);
				*/
				
				int id_commande = ((StringItem)listeCommandes.getItemAtPosition(position)).second;
				Intent i = new Intent( ListeCommandes.this, Commande.class);
				i.putExtra("id_commande", id_commande);
				startActivity(i);
				
			}
		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					OutputStreamWriter osw = null;

					try {

						Socket socket = NetworkUtil.findSocket( serveur.getIp(), PORTMIN, PORTMAX );
						System.out.println( "port distant : " + socket.getPort() );
						System.out.println( "port local : " + socket.getLocalPort() );

						OutputStream os = socket.getOutputStream();
						osw = new OutputStreamWriter(os);
						PrintWriter pw = new PrintWriter(osw);

						// envoie de la demande de r�cup�ration (des commandes)
						JSONObject json = new JSONObject();
						json.put( "etat" , "lister" );
						pw.println( json.toString() );
						pw.flush();


						// r�cup�ration des commandes
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						String ligne = null;
						while( ligne == null ) {
							ligne = br.readLine(); 	// bloquant !
						}
						JSONObject reponse_json = new JSONObject( ligne );  
						System.out.println("liste des commandes (json) : " + ligne );

						// affichage des commandes dans la ListView
						if( reponse_json.has("commandes") ) {

							List<StringItem> listeLignes = new ArrayList<StringItem>();
							JSONArray liste = reponse_json.getJSONArray("commandes");

							for( int i = 0 ; i < liste.length() ; i++ ) {
								JSONObject commande = liste.getJSONObject( i );
								int id_commande = commande.getInt("id");
								String nom_commande = commande.getString("nom");
								String description_commande = commande.getString("description");

								// ajout de la commande dans la liste (pour la ListView)
								listeLignes.add( new StringItem( nom_commande , id_commande ) );


								// ajout de la commande dans la BD
								//
								//
								//

							}

							System.out.println("avant setAdapter");
							ListView listeCommandes = (ListView)findViewById(R.id.listeCommandes);
							listeCommandes.setAdapter(new ArrayAdapter<StringItem>(getBaseContext(), android.R.layout.simple_list_item_1, listeLignes));
							System.out.println("ajout de la liste des commandes");

						}
						else {
							// erreur ?
							System.out.println( "erreur, commandes n'existe pas dans la réponse json du serveur" );

						}


					} catch (ConnectException e) {
						Toast.makeText(ListeCommandes.this, "Hey, je ne trouve pas de serveur !", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if(osw != null){
							try {
								osw.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start(); 
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liste_commandes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
