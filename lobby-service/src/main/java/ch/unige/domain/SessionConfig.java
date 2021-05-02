package ch.unige.domain;

public class SessionConfig {
	
	private int action;
	private int aventure;
	private int horreur;
	private int sci_fi;
	private int documentaire;
	
	public SessionConfig(int action, int aventure, int horreur, int sci_fi, int doc) {
		this.action = action;
		this.aventure = aventure;
		this.horreur = horreur;
		this.sci_fi = sci_fi;
		this.documentaire = doc;
		
	}
}
