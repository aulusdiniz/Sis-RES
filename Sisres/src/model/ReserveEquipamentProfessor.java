package model;

import exception.ReserveException;

public class ReserveEquipamentProfessor extends ReserveEquipament {

    private Professor professor;

    private final String PROFESSOR_NULL = "O professor esta nulo.";

    public ReserveEquipamentProfessor(String date, String hour, 
    								  Equipament equipament, 
    								  Professor professor) throws ReserveException {
        super(date, hour, equipament);
        this.setProfessor(professor);
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) throws ReserveException {
        if (professor == null)
            throw new ReserveException(PROFESSOR_NULL);
        else{
        	//Nothing here
        }
        this.professor = professor;
    }

    public boolean equals(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return (super.equals(reserveEquipamentProfessor) && this.getEquipament().equals(reserveEquipamentProfessor.getEquipament()));
    }

    @Override 
    public String toString() {
        return "ReserveEquipamentProfessor [professor=" + this.getEquipament().toString() + ", toString()=" + super.toString()
                + "]";
    }

}
