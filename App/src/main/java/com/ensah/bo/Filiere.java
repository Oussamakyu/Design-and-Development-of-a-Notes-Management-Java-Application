package com.ensah.bo;


import java.util.Set;


/**
 * Represente une filière.
 * 
 * 
 * @author T. BOUDAA
 *
 */

public class Filiere {
	
	
	private Long idFiliere;

	private String titreFiliere;

	private String codeFiliere;

	private int anneeaccreditation;

	private int anneeFinaccreditation;

	private Set<Niveau> niveaux;

	public Filiere(Long idFiliere) {
		this.idFiliere = idFiliere;
	}

	public Filiere(Long idFiliere, String titreFiliere) {
		this.idFiliere = idFiliere;
		this.titreFiliere = titreFiliere;
	}

	public Filiere(Long idFiliere, String titreFiliere, String codeFiliere, int anneeaccreditation, int anneeFinaccreditation) {
		this.idFiliere = idFiliere;
		this.titreFiliere = titreFiliere;
		this.codeFiliere = codeFiliere;
		this.anneeaccreditation = anneeaccreditation;
		this.anneeFinaccreditation = anneeFinaccreditation;
	}

	public Long getIdFiliere() {
		return idFiliere;
	}

	public void setIdFiliere(Long idFiliere) {
		this.idFiliere = idFiliere;
	}

	public String getTitreFiliere() {
		return titreFiliere;
	}

	public void setTitreFiliere(String titreFiliere) {
		this.titreFiliere = titreFiliere;
	}

	public String getCodeFiliere() {
		return codeFiliere;
	}

	public void setCodeFiliere(String codeFiliere) {
		this.codeFiliere = codeFiliere;
	}

	public int getAnneeaccreditation() {
		return anneeaccreditation;
	}

	public void setAnneeaccreditation(int anneeaccreditation) {
		this.anneeaccreditation = anneeaccreditation;
	}

	public int getAnneeFinaccreditation() {
		return anneeFinaccreditation;
	}

	public void setAnneeFinaccreditation(int anneeFinaccreditation) {
		this.anneeFinaccreditation = anneeFinaccreditation;
	}

	public Set<Niveau> getNiveaux() {
		return niveaux;
	}

	public void setNiveaux(Set<Niveau> niveaux) {
		this.niveaux = niveaux;
	}

	
	
}