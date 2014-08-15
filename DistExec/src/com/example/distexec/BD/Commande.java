package com.example.distexec.BD;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "commandes")
public class Commande {
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(unique = true)
	private String nom;
	
	@DatabaseField
	private String description;

	@DatabaseField
	private String script;

	@DatabaseField(unique = true)
	private int idServeur;

	public Commande() {
		super();
	}

	public Commande( String nom, String description, String script, int id_serveur ) {
		super();
		this.idServeur = id_serveur;
		this.nom = nom;
		this.description = description;
		this.script = script;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getIdServeur() {
		return idServeur;
	}

	public void setIdServeur(int idServeur) {
		this.idServeur = idServeur;
	}
	
	
}
