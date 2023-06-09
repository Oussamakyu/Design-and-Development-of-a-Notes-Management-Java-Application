package com.ensah.bo;

import java.util.ArrayList;
import java.util.List;


public class Niveau {

	
	private Long idNiveau;

	private String alias;

	private String titre;

	private List<Module> modules = new ArrayList<>();

	private List<InscriptionAnnuelle> inscriptions;

	private Filiere filiere;
	private long idNextNiveau;

	public Niveau(Long idNiveau, String alias, String titre, Filiere filiere,long idNextNiveau) {
		this.idNiveau = idNiveau;
		this.alias = alias;
		this.titre = titre;
		this.filiere = filiere;
		this.idNextNiveau=idNextNiveau;
	}

	public Niveau(String pNiveauAlias) {
		this.alias = pNiveauAlias;
	}

	public Niveau(String alias, String titre) {
		this.alias = alias;
		this.titre = titre;
	}

	public Niveau(Long idNiveau) {
		this.idNiveau = idNiveau;
	}

	public Niveau(Long idNiveau, String alias, String titre) {
		this.idNiveau = idNiveau;
		this.alias = alias;
		this.titre = titre;
	}

    public Niveau() {

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

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
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

	public long getIdNextNiveau() {
		return idNextNiveau;
	}

	public void setIdNextNiveau(long idNextNiveau) {
		this.idNextNiveau = idNextNiveau;
	}

	public void addModule(Module m){
		modules.add(m);
	}

	@Override
	public String toString() {
		return "alias du niveau :'" + alias + '\'' +
				'}';
	}
}