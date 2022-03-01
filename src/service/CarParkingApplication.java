package service;

import java.util.Scanner;

public class CarParkingApplication {

    private final Scanner in;
    private final CarParkingFunctionalities app;

    public CarParkingApplication(){
        in = new Scanner(System.in);
        app = new CarParkingFunctionalitiesImpl();
    }

    public void welcome() {
        System.out.println("########## Welcome to Car Parking Application ##########");
    }

    public void showMenu() {
        while (true) {
            System.out.println("\nMenu");
            System.out.println("1. Entry a Car");
            System.out.println("2. Exit the Car");
            System.out.println("3. Show Floor Maps");
            System.out.println("4. Car History");
            System.out.println("5. Quit Application");
            System.out.print("Please Choose any of the above option: ");
            String ch = in.nextLine().trim();
            if(ch.equals("")) {
                System.out.println("\nYou have not given any option. Please select valid option(1 to 5)!");
                continue;
            }
            int choice;
            try {
                 choice = Integer.parseInt(ch);
            }
            catch (NumberFormatException e) {
                System.out.println("\nYou Entered Non Integer! Please Select correct option");
                continue;
            }
            if(choice == 1) {
                app.parkACar();
            }
            else if(choice == 2) {
                app.exitACar();
            }
            else if(choice == 3) {
                app.showAllParkingSlots();
            }
            else if(choice == 4) {
                app.getCarHistory();
            }
            else if(choice == 5) {
                break;
            }
            else {
                System.out.println("\nYou Entered wrong option! Please Select correct option");
            }
        }
    }

    public void quitMessage() {
        System.out.println("\n########## Thank you for using the Application ##########");
    }

}
