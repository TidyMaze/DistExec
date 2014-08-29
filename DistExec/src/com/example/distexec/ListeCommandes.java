package com.example.distexec;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

public class ListeCommandes extends Activity {

	// private static final String IP = null;
	// private static final int PORTMIN = 9301;
	// private static final int PORTMAX = 9305;
	private Serveur serveur;

	private Handler handler;
	private static Status[] valeurStatus = Status.values();

	private ListView listeCommandes;
	private JSONObject json_reponseServeur = null;

	// animation
	private ProgressBar rechercheServeur;

	private Button refresh;
	private TextView messageUser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_commandes);

		// récupération info serveur
		int idserv = getIntent().getIntExtra("idserv", -1);
		DatabaseHelper dbh = new DatabaseHelper(ListeCommandes.this);
		Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();

		try {
			this.serveur = serveurDAO.queryForId(idserv);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// initialisation de la liste
		this.listeCommandes = (ListView) findViewById(R.id.listeCommandes);
		this.listeCommandes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int id_commande = ((StringItem) listeCommandes
						.getItemAtPosition(position)).second;
				Intent i = new Intent(ListeCommandes.this, VueCommande.class);
				i.putExtra("id_commande", id_commande);
				i.putExtra("id_serveur", serveur.getId());
				startActivity(i);
			}
		});
		
		// animation
		this.rechercheServeur = (ProgressBar) findViewById(R.id.progressBar_rechercheServeur);
		this.rechercheServeur.setVisibility(View.INVISIBLE);

		// information pour l'utilisateur (bouton et texte)
		this.refresh = (Button) findViewById(R.id.bouton_refresh);
		this.refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshList();
			}
		});

		this.messageUser = (TextView) findViewById( R.id.text_info_connexion );

		// initialisation du handler
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (valeurStatus[msg.what]) {

				case START:
					rechercheServeur.setVisibility(View.VISIBLE);
					setText("Connexion au serveur" , Color.GRAY );
					break;

				case DEMANDE_:
					//rechercheServeur.setVisibility(View.VISIBLE);
					setText("Connexion au serveur" , Color.parseColor("#FFFF3300") );
					break;

				case RECUPERE_:
					//rechercheServeur.setVisibility(View.VISIBLE);
					setText("Connexion au serveur" , Color.parseColor("#FFFF3300") );
					break;

				case CONVERTIE_:
					//rechercheServeur.setVisibility(View.VISIBLE);
					setText("Connexion au serveur" , Color.parseColor("#FFFF3300") );
					break;

				case OK:
					rechercheServeur.setVisibility(View.INVISIBLE);
					updateListeCommande();
					setText("OK" , Color.GREEN );
					break;

				case NO_INTERNET:
					rechercheServeur.setVisibility(View.INVISIBLE);
					makeToast("Vous devez connecter votre appareil à internet");
					setText("Vous devez connecter votre appareil à internet", Color.MAGENTA );
					break;

				case ConnexionException:
					rechercheServeur.setVisibility(View.INVISIBLE);
					setText("Impossible de se connecter" , Color.RED );
					makeToast("Ce serveur n'est pas à l'écoute sur aucun des ports");
					break;

				case JSONException:
					rechercheServeur.setVisibility(View.INVISIBLE);
					setText("Le serveur renvoie quelque chose d'incompréhensible" , Color.RED );
					break;

				case ERROR_:
					rechercheServeur.setVisibility(View.INVISIBLE);
					setText("Une erreur est survenue" , Color.RED );
					break;

				default:
					rechercheServeur.setVisibility(View.INVISIBLE);
					setText("Une erreur encore plus grave est survenue" , Color.RED );
					break;
				}
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();

		// on sort si on a déjà la liste
		if (this.json_reponseServeur != null) return;

		refreshList();

	}

	public void refreshList() {

		this.json_reponseServeur = null;

		// connexion au serveur, en attente de la réponse
		ThreadListeCommande connexion_serveur = new ThreadListeCommande(this);
		connexion_serveur.start();

		//this.makeToast("Connexion au serveur");
	}

	public void setJSONResponseServeur(JSONObject json_reponse) {
		this.json_reponseServeur = json_reponse;
	}

	public Serveur getServeur() {
		return this.serveur;
	}

	public Handler getHandler() {
		return this.handler;
	}

	public void setText( String texte , int couleurTexte ) {
		this.messageUser.setTextColor(couleurTexte);
		this.messageUser.setText( texte );
	}


	public void makeToast(String message) {
		Toast.makeText(ListeCommandes.this, message, Toast.LENGTH_SHORT).show();
	}

	private void updateListeCommande() {

		this.supprimerAnciennesCommandes();

		// modification des données JSON récupéré
		DatabaseHelper dbh = new DatabaseHelper(ListeCommandes.this);
		Dao<Commande, Integer> commandeDAO = dbh.getCommandeDao();
		List<StringItem> listeLignes = new ArrayList<StringItem>();
		JSONArray json_listeCommande;

		try {

			json_listeCommande = json_reponseServeur.getJSONArray("commandes");

			for (int i = 0; i < json_listeCommande.length(); i++) {
				JSONObject commande = json_listeCommande.getJSONObject(i);
				int id_commande = commande.getInt("id");
				String nom_commande = commande.getString("nom");
				String description_commande = commande.getString("description");
				String script_commande = commande.getString("script");

				// ajout de la commande dans la liste (pour la ListView)
				listeLignes.add(new StringItem(nom_commande, id_commande));

				// ajout de la commande dans la BD
				Commande nouvelle_commande = new Commande(nom_commande,
						description_commande, script_commande, id_commande);
				commandeDAO.create(nouvelle_commande);
			}
			System.out.println("modification des données recu terminée");

			// ajout des données dans la ListView
			this.listeCommandes.setAdapter(new ArrayAdapter<StringItem>(
					getBaseContext(), android.R.layout.simple_list_item_1,
					listeLignes));
			System.out.println("la liste des commandes à été affichée");

		} catch (JSONException e) {
			e.printStackTrace();
			this.makeToast("Les données reçu ne contiennent n'ont pas le format approprié (c'est bien du JSON, mais il manque quelque chose)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void supprimerAnciennesCommandes() {
		DatabaseHelper dbh = new DatabaseHelper(ListeCommandes.this);
		Dao<Commande, Integer> commandeDAO = dbh.getCommandeDao();
		try {
			commandeDAO.delete(commandeDAO.queryForAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("suppression ancienne commande terminé");
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
