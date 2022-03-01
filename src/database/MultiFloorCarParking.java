package database;

import model.ParkingLot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class MultiFloorCarParking {

    public static int floors, rows, columns, pathWidth;
    public static String path;
    public static final ArrayList<ParkingLot> parkingLots;

    static {
        FileInputStream file;
        Properties prop = null;
        try {
            file = new FileInputStream("resources/config.properties");
            prop = new Properties();
            prop.load(file);
        } catch (FileNotFoundException e) {
            System.out.println("Given Properties File Path is Invalid!");
        } catch (IOException e) {
            System.out.println("Property File is not loaded!");
        }
        if(prop != null) {
            MultiFloorCarParking.floors = Integer.parseInt(prop.getProperty("noOfFloors"));
            MultiFloorCarParking.rows = Integer.parseInt(prop.getProperty("noOfRows"));
            MultiFloorCarParking.columns = Integer.parseInt(prop.getProperty("noOfColumns"));
            MultiFloorCarParking.pathWidth = Integer.parseInt(prop.getProperty("pathWidth"));
            MultiFloorCarParking.path = prop.getProperty("path");
        }
        parkingLots = new ArrayList<>();
        setParkingLots();
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    private static void setParkingLots() {
        for(int i = 0; i < floors; ++i) {
            ParkingLot parkingLot = new ParkingLot(rows, columns);
            parkingLots.add(parkingLot);
        }
    }

    public int getFloors() {
        return floors;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getPathWidth() {
        return pathWidth;
    }

    public String getPath() {
        return path;
    }
}
