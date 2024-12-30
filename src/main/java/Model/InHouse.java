package Model;

import javafx.fxml.FXML;

/** supplied class inHouse */
public class InHouse extends Part{
    @FXML
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name,price,stock,min,max);
        this.machineId = machineId;
    }
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }
}