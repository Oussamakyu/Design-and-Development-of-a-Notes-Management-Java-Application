package com.ensah.bo;

import java.util.List;


public class Niveau {

	
	private Long idNiveau;

	private String alias;

	private String titre;

	private List<java.lang.Module> modules;

	private List<InscriptionAnnuelle> inscriptions;

	private Filiere filiere;

	public Niveau(String pNiveauAlias) {
		this.alias = pNiveauAlias;
	}


	public Niveau(Long idNiveau) {
		this.idNiveau = idNiveau;
	}

	public Niveau(Long idNiveau, String alias, String titre) {
		this.idNiveau = idNiveau;
		this.alias = alias;
		this.titre = titre;
	}


    public Long getIdNiveau() {
		return idNiveau;
	}

	public void setIdNiveau(Long idNiveau) {
		this.idNiveau = idNiveau;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public List<java.lang.Module> getModules() {
		return modules;
	}

	public void setModules(List<java.lang.Module> modules) {
		this.modules = modules;
	}

	public List<InscriptionAnnuelle> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<InscriptionAnnuelle> inscriptions) {
		this.inscriptions = inscriptions;
	}

	public Filiere getFiliere() {
		return filiere;
	}

	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}

	
	
}