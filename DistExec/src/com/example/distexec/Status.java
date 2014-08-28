package com.example.distexec;

public enum Status {
	
	START,
	NO_INTERNET,  
	DEMANDE_LISTE, 
	RECUPERE_LISTE,
	CONVERTIE_LISTE,
	OK,
	ConnexionException,
	JSONException,
	ERROR_, 
}
