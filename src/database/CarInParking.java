package database;

import model.Car;

import java.util.ArrayList;

public class CarInParking {

    private static final ArrayList<Car> cars;

    static {
        cars = new ArrayList<>();
    }

    public void addCars(Car car) {
        cars.add(car);
    }

    public boolean isCarNumberExist(String carNo) {
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                return true;
            }
        }
        return false;
    }

    public void removeCar(String carNo) {
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                cars.remove(car);
                return;
            }
        }
    }
}
