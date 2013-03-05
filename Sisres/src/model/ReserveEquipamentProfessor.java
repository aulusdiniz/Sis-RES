package model;

import exception.ReserveException;

public class ReserveEquipamentProfessor extends ReserveEquipament {

    private Professor professor;

    private final String PROFESSOR_NULL = "O professor esta nulo.";

    public ReserveEquipamentProfessor(String date, String hour, Equipament equipament, Professor professor)
            throws ReserveException {
        super(date, hour, equipament);
        this.setProfessor(professor);
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) throws ReserveException {
        if (professor == null)
            throw new ReserveException(PROFESSOR_NULL);
        this.professor = professor;
    }

    public boolean equals(ReserveEquipamentProfessor obj) {
        return (super.equals(obj) && this.getEquipament().equals(obj.getEquipament()));
    }

    @Override 
    public String toString() {
        return "ReserveEquipamentProfessor [professor=" + this.getEquipament().toString() + ", toString()=" + super.toString()
                + "]";
    }

}
