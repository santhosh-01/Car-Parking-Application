package model;

import java.util.ArrayList;

public class CarEntryExitMaster {

    private String carNumber;
    private ArrayList<CarEntryExit> carEntryExits;

    public CarEntryExitMaster(String carNumber) {
        this.carNumber = carNumber;
        this.carEntryExits = new ArrayList<>();
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void addEntryExit(CarEntryExit car) {
        carEntryExits.add(car);
    }

    public ArrayList<CarEntryExit> getCarEntryExits() {
        return carEntryExits;
    }

    public CarEntryExit getLastCarEntryExit() {
        return carEntryExits.get(carEntryExits.size() - 1);
    }
}
