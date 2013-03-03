package model;

import exception.ReservaException;

public class ReservaEquipamento extends Reserva {

    private Equipament equipamento;

    // Mensagens
    private final String EQUIPAMENTO_NULO = "O equipamneto esta nulo.";

    public ReservaEquipamento(String data, String hora, Equipament equipamento) throws ReservaException {
        super(data, hora);
        this.setEquipamento(equipamento);
    }

    public Equipament getEquipamento() {
        return this.equipamento;
    }

    public void setEquipamento(Equipament equipamento) throws ReservaException {
        if (equipamento == null)
            throw new ReservaException(EQUIPAMENTO_NULO);
        this.equipamento = equipamento;
    }

    public boolean equals(ReservaEquipamento obj) {
        return (super.equals(obj) && this.getEquipamento().equals(obj.getEquipamento()));
    }

    @Override public String toString() {
        return "ReservaEquipamento [equipamento=" + this.getEquipamento() + ", toString()=" + super.toString() + "]";
    }

}
