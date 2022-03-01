package app;

import service.CarParkingApplication;

public class MainClass {

    public static void main(String[] args) {

        CarParkingApplication app = new CarParkingApplication();

        app.welcome();

        app.showMenu();

        app.quitMessage();

    }

}
