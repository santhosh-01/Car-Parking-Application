package service;

import database.CarEntryExitTable;
import database.CarInParking;
import database.CarTable;
import database.MultiFloorCarParking;
import model.*;
import util.OrdinalNumber;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class CarParkingFunctionalitiesImpl implements CarParkingFunctionalities {

    private final Scanner in;

    private final MultiFloorCarParking obj;
    private final ArrayList<ParkingLot> arr;

    private final CarEntryExitTable carEntryExitTable;

    public CarParkingFunctionalitiesImpl(){
        in = new Scanner(System.in);
        obj = new MultiFloorCarParking();
        arr = obj.getParkingLots();
        carEntryExitTable = new CarEntryExitTable();
    }

    private String getOrdinalNumberForFloors(int num) {
        if(num == 0) return "Ground";
        else return OrdinalNumber.getOrdinalNo(num);
    }

    @Override
    public void  parkACar() {
        if(!isParkingAvailable()) {
            System.out.println("\nSorry! Parking Full!");
            return;
        }
        if(!acceptBillingAmount()) return;
        Car car = createCar();
        if(car == null) return;
        if(!confirmCarDetails(car)) return;
        int ind = getLowestFloorWithVacancy();
        System.out.println("\nParking Place is Available in " + getOrdinalNumberForFloors(ind) + " floor");
        System.out.print("Do you want to proceed further? (Yes / No): ");
        String choice = in.nextLine().trim();
        if(choice.equalsIgnoreCase("yes")) {
            ParkingLot parkingLot = arr.get(ind);
            parkACar(parkingLot,ind,car);
        }
        else {
            printParkingAvailableFloors();
            System.out.print("Select any of the above floor number: ");
            int floorNo = Integer.parseInt(in.nextLine().trim());
            ParkingLot parkingLot = arr.get(floorNo);
            parkACar(parkingLot, floorNo,car);
        }
    }

    private boolean isParkingAvailable() {
        for (int i = 0; i < obj.getFloors(); ++i) {
            if (arr.get(i).getVacancy() != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean acceptBillingAmount() {
        while (true) {
            System.out.println("\nBilling Amount for parking a car: Rs.0.5 per second");
            System.out.print("Are you going to park a car? (Yes / No): ");
            String choice = in.nextLine().trim();
            if(choice.equalsIgnoreCase("yes")) return true;
            else if(choice.equalsIgnoreCase("no")) return false;
            else if(choice.equals("")) System.out.println("\nYou have not entered any input! Please Enter (Yes / No)");
            else System.out.println("\nInvalid Input! Please Enter (Yes / No)");
        }
    }

    private Car createCar() {
        Car car;
        String carNo, carBrand, carModel;
        while (true) {
            System.out.print("\nEnter Car Number(should be less than or equal to 5 characters): ");
            carNo = in.nextLine().trim();
            if(carNo.equals("")) System.out.println("\nYou have not entered any input! Please enter valid Car Number");
            else if(carNo.length() > 5) System.out.println("\nYou have entered more than 5 characters as Car Number." +
                    "\nPlease Enter valid Car Number(should be less than or equal to 5 characters): ");
            else break;
        }
        CarInParking carInParking = new CarInParking();
        if(carInParking.isCarNumberExist(carNo)) {
            System.out.println("\nDuplicate Car! Given Car Number is already in parking");
            return null;
        }
        CarTable carTable = new CarTable();
        if(carTable.isCarNumberExist(carNo)) {
            car = carTable.getCarByCarNo(carNo);
        }
        else {
            while (true) {
                System.out.print("Enter Car Brand: ");
                carBrand = in.nextLine().trim();
                if(carBrand.equals("")) System.out.println("\nYou have not entered any input! " +
                        "Please enter valid Car Brand");
                else break;
            }
            while (true) {
                System.out.print("Enter Car Model Number: ");
                carModel = in.nextLine().trim();
                if(carModel.equals("")) System.out.println("\nYou have not entered any input! " +
                        "Please enter valid Car Model");
                else break;
            }
            car = new Car(carNo,carBrand,carModel);

            CarEntryExitMaster carEntryExitMaster = new CarEntryExitMaster(carNo);
            carEntryExitTable.addCar(carEntryExitMaster);

            carTable.addCar(car);
        }
        return car;
    }

    private boolean confirmCarDetails(Car car) {
        while (true) {
            String carNo = car.getCarNumber();
            String carBrand = car.getCarBrand();
            String carModel = car.getCarModel();
            System.out.println("\n1. Car Number: " + carNo);
            System.out.println("2. Car Brand: " + carBrand);
            System.out.println("3. Car Model Number: " + carModel);
            System.out.println("4. Continue Parking");
            System.out.println("5. Cancel Parking");
            System.out.print("If you want to update car details choose the above option (1 or 2 or 3): ");
            int choice = Integer.parseInt(in.nextLine().trim());
            if(choice == 1) {
                System.out.print("Enter the Car Number: ");
                carNo = in.nextLine().trim();
                car.setCarNumber(carNo);
            }
            else if(choice == 2) {
                System.out.print("Enter the Car Brand: ");
                carBrand = in.nextLine().trim();
                car.setCarBrand(carBrand);
            }
            else if(choice == 3) {
                System.out.print("Enter the Model Number: ");
                carModel = in.nextLine().trim();
                car.setCarModel(carModel);
            }
            else if(choice == 4) break;
            else if(choice == 5) return false;
            else System.out.println("\nYou Entered wrong option! Please Select correct option");
        }
        return true;
    }

    private int getLowestFloorWithVacancy() {
        for (int i = 0; i < obj.getFloors(); ++i) {
            if (arr.get(i).getVacancy() != 0) {
                return i;
            }
        }
        return -1;
    }

    private void parkACar(ParkingLot parkingLot, int ind, Car car) {
        while (true) {
            int[] position = parkingLot.getFirstParkingPosition();
            if(position[0] != -1 && position[1] != -1) {
                System.out.println("\nEmpty Parking Place is available at " +
                        (position[0] + 1) + "/" + (position[1] + 1));
                System.out.print("Do you agree to proceed further with above parking location? ");
            }
            String choice = in.nextLine().trim();
            int row,col;
            if(choice.equalsIgnoreCase("yes")) {
                row = position[0];
                col = position[1];
            }
            else {
                System.out.println("\nDetailed Floor Map of " + getOrdinalNumberForFloors(ind) + " Floor");
                parkingLot.showModifiedParkingLot();
                System.out.print("Select any one Empty Parking Place in (R/C) format: ");
                String[] pos = in.nextLine().trim().split("/");
                row = Integer.parseInt(pos[0]) - 1;
                col = Integer.parseInt(pos[1]) - 1;
            }
            if (parkingLot.parkCarAtPosition(car, row, col)) {
                System.out.println("\nDetailed Path to park the car in the given parking place " +
                        "at " + getOrdinalNumberForFloors(ind) + " floor");
                if(obj.getPath().equals("L")) {
                    parkingLot.setDetailedLeftEntryPath(row, col);
                }
                else {
                    parkingLot.setDetailedEntryPath(row, col);
                }
                parkingLot.showDetailedPath();
                parkingLot.removeDirections();
                CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
                ParkingCell parkingCell = parkingLot.getPathCellByPosition(row,col);
                CarEntryExit carEntryExit = new CarEntryExit(parkingCell.getParkedTime(),
                        new int[]{row,col,ind});
                carEntryExitMaster.addEntryExit(carEntryExit);
                break;
            }
            else {
                System.out.println("\nSorry! Given Parking Place is invalid. " +
                        "Please Enter Valid Empty Parking Place");
            }
        }
        System.out.println("\nCar Number " + car.getCarNumber() + " is parked successfully in " +
                getOrdinalNumberForFloors(ind) + " floor");
    }

    private void printParkingAvailableFloors() {
        System.out.println("\nParking Place is Available in the following floors");
        for (int j = 0; j < arr.size(); ++j) {
            if(!arr.get(j).isParkingFull()) {
                if(j == 0) System.out.println("Floor " + (j) + " (Ground Floor)");
                else System.out.println("Floor " + (j));
            }
        }
    }

    @Override
    public void exitACar() {
        if(!isCarAvailable()) {
            System.out.println("\nSorry! No Cars are available to exit!");
            return;
        }
        System.out.print("\nEnter the Car Number to exit: ");
        String carNo = in.nextLine().trim();
        CarTable carTable = new CarTable();
        Car car = carTable.getCarByCarNo(carNo);
        if(!confirmCarDetailsForExit(car)) return;
        for (int i = 0; i < obj.getFloors(); ++i) {
            ParkingLot parkingLot = arr.get(i);
            int[] pos = parkingLot.getCarNumberPosition(carNo);
            if(pos[0] != -1 && pos[1] != -1) {
                System.out.println("Car Position : " + (pos[0] + 1) + "/" + (pos[1] + 1) + " at " +
                        getOrdinalNumberForFloors(i) + " floor");
                if(parkingLot.exitACar(pos)) {
                    System.out.println("\nDetailed Path to exit the car from the parking place " +
                            "at " + getOrdinalNumberForFloors(i) + " floor");
                    if(obj.getPath().equals("L")) {
                        parkingLot.setDetailedLeftExitPath(pos[0], pos[1]);
                    }
                    else {
                        parkingLot.setDetailedExitPath(pos[0], pos[1]);
                    }
                    parkingLot.showDetailedPath();
                    parkingLot.removeDirections();

                    ParkingCell parkingCell = parkingLot.getPathCellByPosition(pos[0],pos[1]);
                    CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
                    CarEntryExit carEntryExit = carEntryExitMaster.getLastCarEntryExit();
                    carEntryExit.setExitTime(parkingCell.getCarExitTime());

                    parkingCell.setParkedTime(null);
                    parkingCell.setCarExitTime(null);
                    parkingCell.setIsParked("E");
                }
                System.out.println("\nCar Number " + carNo + " removed successfully from " +
                        getOrdinalNumberForFloors(i) + " floor ");
                return;
            }
        }
        System.out.println("Sorry! Given Car Number is invalid (or) not available in any floors.");
    }

    private boolean isCarAvailable() {
        int maxVacancy = obj.getRows() * obj.getColumns();
        for (int i = 0; i < obj.getFloors(); ++i) {
            if (arr.get(i).getVacancy() != maxVacancy) {
                return true;
            }
        }
        return false;
    }

    private boolean confirmCarDetailsForExit(Car car) {
        String carNo = car.getCarNumber();
        String carBrand = car.getCarBrand();
        String carModel = car.getCarModel();
        System.out.println("\nCar Number: " + carNo);
        System.out.println("Car Brand: " + carBrand);
        System.out.println("Car Model Number: " + carModel);
        System.out.print("Do you want the car with above details to exit? ");
        String choice = in.nextLine().trim();
        return choice.equalsIgnoreCase("yes");
    }

    @Override
    public void showAllParkingSlots() {
        for (int i = obj.getFloors() - 1; i >= 0; --i) {
            System.out.println("\nFloor Map of " + getOrdinalNumberForFloors(i) + " Floor");
            arr.get(i).showParkingLot();
        }
    }

    @Override
    public void getCarHistory() {
        System.out.print("\nEnter Car Number: ");
        String carNumber = in.nextLine();
        CarTable carTable = new CarTable();
        if(!checkCar(carNumber)) return;
        Car car = carTable.getCarByCarNo(carNumber);
        String carNo = car.getCarNumber();
        String carBrand = car.getCarBrand();
        String carModel = car.getCarModel();
        System.out.println("\nCar Information:");
        System.out.println("Car Number: " + carNo);
        System.out.println("Car Brand: " + carBrand);
        System.out.println("Car Model Number: " + carModel);
        System.out.println("\nParking History:");
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(carNumber);
        for (CarEntryExit carEntryExit:carEntryExitMaster.getCarEntryExits()) {
            LocalTime time1 = carEntryExit.getEntryTime();
            LocalTime time2 = carEntryExit.getExitTime();
            if(time1 == null) System.out.print("00:00:00  ");
            else System.out.print(getTime(time1) + "  ");
            if(time2 == null ) System.out.print("00:00:00  ");
            else System.out.print(getTime(time2) + "  ");
            int[] pos = carEntryExit.getPosition();
            System.out.println(pos[0] + "/" + pos[1] + " in " + getOrdinalNumberForFloors(pos[2]) + " floor");
        }
    }

    private boolean checkCar(String carNumber) {
        if(carNumber.equals("")) {
            System.out.println("\nGiven Car Number is Empty");
            return false;
        }
        CarTable carTable = new CarTable();
        if(!carTable.isCarNumberExist(carNumber)) {
            System.out.println("\nSorry! Given Car not found!!");
            return false;
        }
        return true;
    }

    private String getTime(LocalTime time) {
        return time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

}