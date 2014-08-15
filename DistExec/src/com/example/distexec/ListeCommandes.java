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

import com.j256.ormlite.dao.Dao;
import com.example.distexec.BD.DatabaseHelper;
import com.example.distexec.BD.Serveur;
import com.example.distexec.BD.Commande;

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
	
	JSONObject json_reponseServeur = null;
	
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
				Intent i = new Intent( ListeCommandes.this, VueCommande.class);
				i.putExtra("id_commande", id_commande);
				i.putExtra("id_serveur", serveur.getId() );
				startActivity(i);
				
			}
		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		try {

			Thread connexion_serveur = new Thread( new Runnable() {
				@Override
				public void run() {

					try {

						// connexion au serveur
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
						 * 		etat: "lister"
						 * }
						 */
						JSONObject json_listerCommande = new JSONObject();
						json_listerCommande.put( "etat" , "lister" );
						pw.println( json_listerCommande.toString() );
						pw.flush();
						System.out.println("demande des commandes envoyé");

						// récupère la liste des commandes du serveur
						String ligne = null;
						while( ligne == null ) {
							ligne = br.readLine(); 	// bloquant !
						}
						JSONObject reponse_json = new JSONObject( ligne );  
						System.out.println("liste des commandes (json) : " + ligne );

						json_reponseServeur = reponse_json;

					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			connexion_serveur.start();
            while( json_reponseServeur == null ) {
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
			
            // suppression des anciennes commandes enregistrés dans la BD
            DatabaseHelper dbh = new DatabaseHelper(ListeCommandes.this);
    		Dao<Commande, Integer> commandeDAO = dbh.getCommandeDao();
            try {
				commandeDAO.delete( commandeDAO.queryForAll() );
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("suppression ancienne commande terminé");
            
            // modification des données JSON récupéré
            List<StringItem> listeLignes = new ArrayList<StringItem>();
            JSONArray json_listeCommande = json_reponseServeur.getJSONArray("commandes");
            for( int i = 0 ; i < json_listeCommande.length() ; i++ ) {
                JSONObject commande = json_listeCommande.getJSONObject( i );
                int id_commande = commande.getInt("id");
                String nom_commande = commande.getString("nom");
                String description_commande = commande.getString("description");
                String script_commande = commande.getString("script");
                
                // ajout de la commande dans la liste (pour la ListView)
                listeLignes.add( new StringItem( nom_commande , id_commande ) );

                // ajout de la commande dans la BD
                Commande nouvelle_commande = new Commande( nom_commande, description_commande, script_commande, id_commande );
                commandeDAO.create( nouvelle_commande ); 
            }
            System.out.println("modification des données recu terminée");
            
            // ajout des données dans la ListView
            this.listeCommandes.setAdapter(new ArrayAdapter<StringItem>(getBaseContext(), android.R.layout.simple_list_item_1, listeLignes));
            System.out.println("la liste des commandes à été affichée");
            
		} catch (JSONException e) {
			// json.put() -> if the key is null
			e.printStackTrace();
		} catch (SQLException e) {
			// commandeDAO.create( ... )
			e.printStackTrace();
		}
		
		
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
