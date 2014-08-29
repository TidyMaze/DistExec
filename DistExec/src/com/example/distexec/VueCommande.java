package com.example.distexec;


import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.distexec.BD.Commande;
import com.example.distexec.BD.DatabaseHelper;
import com.example.distexec.BD.Serveur;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VueCommande extends Activity {

	private Serveur serveur;
	private Commande commande;

	private Handler handler;
	private static Status[] valeurStatus = Status.values();

	private JSONObject json_reponseServeur = null;

	// animation
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commande);

		DatabaseHelper dbh = new DatabaseHelper(VueCommande.this);


		// récupération des données concernant le serveur (id...)
		int idserv = getIntent().getIntExtra("id_serveur", -1);
		Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
		try {
			this.serveur = serveurDAO.queryForId(idserv);
		} catch (SQLException e) {
			e.printStackTrace();
		}


		// récupération des données concernant la commande
		int id_commande = getIntent().getIntExtra("id_commande", -1);
		Dao<Commande, Integer> commandeDAO = dbh.getCommandeDao();

		try {
			List<Commande> listeCommande = commandeDAO.query( commandeDAO.queryBuilder().where().eq( "idServeur" , id_commande ).prepare() );
			this.commande = listeCommande.get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("id commande recu : " + id_commande );
		System.out.println("commande nom : " + this.commande.getNom() );



		// affichage des informations
		TextView nom_commande = (TextView)findViewById(R.id.nom_commande);
		nom_commande.setText( this.commande.getNom() );


		TextView description = (TextView)findViewById(R.id.description);
		description.setText( this.commande.getDescription() );


		Button executer = (Button)findViewById(R.id.executer);
		executer.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				executerCommande();
			}
		});

		// animation
		this.progressBar = (ProgressBar) findViewById( R.id.progressBar_executionCommande );
		this.progressBar.setVisibility(View.INVISIBLE);
		
		// initialisation du handler
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (valeurStatus[msg.what]) {

				case START:
					progressBar.setVisibility(View.VISIBLE);
					break;

				case DEMANDE_:
					break;

				case RECUPERE_:
					break;

				case CONVERTIE_:
					break;

				case OK:
					// traitement de la réponse du serveur ici !
					progressBar.setVisibility(View.INVISIBLE);
					traiterReponse();
					break;

				case NO_INTERNET:
					progressBar.setVisibility(View.INVISIBLE);
					makeToast("Vous devez connecter votre appareil à internet");
					break;

				case ConnexionException:
					progressBar.setVisibility(View.INVISIBLE);
					makeToast("Ce serveur n'est pas à l'écoute sur aucun des ports");
					break;
				case JSONException:
					progressBar.setVisibility(View.INVISIBLE);
					makeToast("Le serveur renvoie quelque chose d'incompréhensible");
					break;

				case ERROR_:
					progressBar.setVisibility(View.INVISIBLE);
					makeToast("Une erreur est survenue");
					break;

				default:
					progressBar.setVisibility(View.INVISIBLE);
					makeToast("Mais que ce passe t'il ??");
					break;
				}				

			}
		};

	}

	public void executerCommande() {
		this.json_reponseServeur = null;
		ThreadCommande connexion_serveur = new ThreadCommande( this);
		connexion_serveur.start();
	}


	public void traiterReponse() {
		
		// deux cas normalement impossible
		if( this.json_reponseServeur == null ) return;
		if( !this.json_reponseServeur.has("etat") ) return;
		
		try {
			
			String etat = this.json_reponseServeur.getString("etat");
			if( etat.equals("OK") ) {
				this.makeToast("la commande a été exécutée");
			}
			else if( etat.equals("erreur") ) {
				
				String erreur = this.json_reponseServeur.getString("erreur");
				this.makeToast("une erreur est survenue : " + erreur );
			}
			else {
				this.makeToast("mais qu'est ce que c'est ?");
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	



	public Serveur getServeur() {
		return serveur;
	}

	public Commande getCommande() {
		return commande;
	}

	public Handler getHandler() {
		return this.handler;
	}

	public void makeToast(String message) {
		Toast.makeText(VueCommande.this, message, Toast.LENGTH_SHORT).show();
	}

	public void setJSONResponseServeur(JSONObject json_reponse) {
		this.json_reponseServeur = json_reponse;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commande, menu);
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
