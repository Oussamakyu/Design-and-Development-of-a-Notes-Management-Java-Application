package com.ensah.bo;

public class InscriptionMatiere {

	private Long idInscriptionMatiere;

	private double noteSN;
	private double noteSR;
	private double noteFinale;
	private String validation;
	private String plusInfos;

	/** Coefficient */
	private double coefficient;

	private Element matiere;

	private InscriptionAnnuelle inscriptionAnnuelle;

	public InscriptionMatiere() {
	}

	public Long getIdInscriptionMatiere() {
		return idInscriptionMatiere;
	}

	public void setIdInscriptionMatiere(Long idInscriptionMatiere) {
		this.idInscriptionMatiere = idInscriptionMatiere;
	}

	public double getNoteSN() {
		return noteSN;
	}

	public void setNoteSN(double noteSN) {
		this.noteSN = noteSN;
	}

	public double getNoteSR() {
		return noteSR;
	}

	public void setNoteSR(double noteSR) {
		this.noteSR = noteSR;
	}

	public double getNoteFinale() {
		return noteFinale;
	}

	public void setNoteFinale(double noteFinale) {
		this.noteFinale = noteFinale;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getPlusInfos() {
		return plusInfos;
	}

	public void setPlusInfos(String plusInfos) {
		this.plusInfos = plusInfos;
	}

	public Element getMatiere() {
		return matiere;
	}

	public void setMatiere(Element matiere) {
		this.matiere = matiere;
	}

	public InscriptionAnnuelle getInscriptionAnnuelle() {
		return inscriptionAnnuelle;
	}

	public void setInscriptionAnnuelle(InscriptionAnnuelle inscriptionAnnuelle) {
		this.inscriptionAnnuelle = inscriptionAnnuelle;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

}
