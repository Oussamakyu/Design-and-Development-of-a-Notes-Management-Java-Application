package com.ensah;

public class Student {
    private double idStudent;
    private String cne;
    private String name;
    private String surname;
    private int level;
    private String filiere;
    private String classe;
    public Student (){}

    public Student(double idStudent, String cne, String name, String surname, int level, String filiere, String classe) {
        this.idStudent = idStudent;
        this.cne = cne;
        this.name = name;
        this.surname = surname;
        this.level = level;
        this.filiere = filiere;
        this.classe = classe;
    }

    public Student(double idStudent, String cne, String name, String surname, int level,boolean type) {
        this.idStudent = idStudent;
        this.cne = cne;
        this.name = name;
        this.surname = surname;
        this.level = level;
    }

    public double getIdStudent() {
        return idStudent;
    }

    public String getCne() {
        return cne;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getLevel() {
        return level;
    }

    public String getFiliere() {
        return filiere;
    }

    public String getClasse() {
        return classe;
    }
}
