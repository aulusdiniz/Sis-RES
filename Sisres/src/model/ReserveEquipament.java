package model;

import exception.ReserveException;

public class ReserveEquipament extends Reserve {

    private Equipament equipament;

    private final String EQUIPAMENT_NULL = "O equipamneto está nulo.";

    public ReserveEquipament(String date, String hour, Equipament equipament) throws ReserveException {
        super(date, hour);
        this.setEquipament(equipament);
    }

    public Equipament getEquipament() {
        return this.equipament;
    }

    public void setEquipament(Equipament equipament) throws ReserveException {
        if (equipament == null)
            throw new ReserveException(EQUIPAMENT_NULL);
        this.equipament = equipament;
    }

    public boolean equals(ReserveEquipament obj) {
        return (super.equals(obj) && this.getEquipament().equals(obj.getEquipament()));
    }

    
    public String toString() {
        return "ReserveEquipament [equipament=" + this.getEquipament() + ", toString()=" + super.toString() + "]";
    }

}
