package com.example.distexec.BD;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "serveurs")
public class Serveur {
	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField(unique=true)
	private String nom;
	
	@DatabaseField(unique=true)
	private String ip;

	public Serveur() {
		super();
	}

	public Serveur(String nom, String ip) {
		super();
		this.nom = nom;
		this.ip = ip;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


}
