package database;

import model.Car;

import java.util.ArrayList;

public class CarTable {

    private static final ArrayList<Car> cars;

    static {
        cars = new ArrayList<>();
    }

    public boolean isCarNumberExist(String carNo) {
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                return true;
            }
        }
        return false;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public Car getCarByCarNo(String carNo) {
        Car res = null;
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                res = car;
            }
        }
        return res;
    }

}
