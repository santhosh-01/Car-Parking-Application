package model;

import database.CarInParking;
import database.MultiFloorCarParking;
import service.Billing;

import java.time.LocalTime;
import java.util.ArrayList;

public class ParkingLot {

    private final int row;
    private final int col;
    private final ArrayList<ArrayList<Cell>> mat;
    private int vacancy;

    private static final int inBetweenPathWidth;

    static {
        inBetweenPathWidth = new MultiFloorCarParking().getPathWidth();
    }

    public ParkingLot(int row, int col) {
        vacancy = row * col;
        this.row = row + (((row - 1)/2)+2) * inBetweenPathWidth;
        this.col = col + (2 * inBetweenPathWidth);
        this.mat = new ArrayList<>();
        formatMatrix();
    }

    private void formatMatrix() {
        if(inBetweenPathWidth == 1) {
            for (int i = 0; i < row; ++i)
            {
                ArrayList<Cell> cells = new ArrayList<>();
                for(int j = 0; j < col; ++j)
                {
                    if(i == row - 1 || i % 3 == 0 || j == 0 || j == col - 1) {
                        PathCell pathCell = new PathCell();
                        pathCell.setPosition((row - i) + "/" + (j + 1));
                        cells.add(pathCell);
                    }
                    else {
                        ParkingCell parkingCell = new ParkingCell();
                        parkingCell.setPosition((row - i + 1) + "/" + (j + 1));
                        parkingCell.setIsParked("E");
                        cells.add(parkingCell);
                    }
                }
                this.mat.add(cells);
            }
        }
        else if(inBetweenPathWidth == 2){
            for (int i = 0; i < row; ++i)
            {
                ArrayList<Cell> cells = new ArrayList<>();
                for(int j = 0; j < col; ++j)
                {
                    if(i <= 1 || i >= row - 2 || i % 4 == 0 || i % 4 == 1 || j <= 1 || j >= col - 2) {
                        PathCell pathCell = new PathCell();
                        pathCell.setPosition((row - i) + "/" + (j + 1));
                        cells.add(pathCell);
                    }
                    else {
                        ParkingCell parkingCell = new ParkingCell();
                        parkingCell.setPosition((row - i + 1) + "/" + (j + 1));
                        parkingCell.setIsParked("E");
                        cells.add(parkingCell);
                    }
                }
                this.mat.add(cells);
            }
        }
    }

    public int getVacancy() {
        return vacancy;
    }

    public boolean isParkingFull() {
        return this.vacancy == 0;
    }

    public int[] getFirstParkingPosition() {
        int[] pos = new int[2];
        pos[0] = -1;
        pos[1] = -1;
        outer: for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if(this.mat.get(i).get(j).getCellValue().equals("-")) {
                    pos[0] = i;
                    pos[1] = j;
                    break outer;
                }
            }
        }
        return pos;
    }

    public int[] getCarNumberPosition(String CarNo) {
        int[] pos = new int[2];
        pos[0] = -1;
        pos[1] = -1;
        for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                Car car = null;
                if(this.mat.get(i).get(j) instanceof ParkingCell) {
                    car = ((ParkingCell)this.mat.get(i).get(j)).getCar();
                }
                if(car != null && car.getCarNumber() != null && car.getCarNumber().equals(CarNo)) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }

    public void showParkingLot() {
        System.out.println();
        for(int i = 0; i < row; ++i)
        {
            for(int j = 0; j < col; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("-") || mat.get(i).get(j).getCellValue().equals("X")) {
                    System.out.printf("%8s ",mat.get(i).get(j).getCellValue());
                }
                else System.out.printf("%8s ",mat.get(i).get(j).getCellValue());
            }
            System.out.println();
        }
    }

    public boolean exitACar(int[] pos) {
        ParkingCell parkingCell = ((ParkingCell)mat.get(pos[0]).get(pos[1]));
        parkingCell.setCarExitTime(LocalTime.now());
        long seconds = parkingCell.calculateCarParkedInSeconds();
        System.out.println("\nTotal Car Parking Time: " + seconds + " seconds");
        Billing billing = new Billing();
        System.out.println("Bill Amount: Rs." + billing.calculateBill(seconds));
        CarInParking carInParking = new CarInParking();
        carInParking.removeCar(((ParkingCell) mat.get(pos[0]).get(pos[1])).getCar().getCarNumber());
        parkingCell.setCar(null);
        this.vacancy ++;
        return true;
    }

    public void showModifiedParkingLot() {
        System.out.println();
        for(int i = 0; i < row; ++i)
        {
            for(int j = 0; j < col; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("X")) {
                    System.out.printf("%8s ",mat.get(i).get(j).getCellValue());
                }
                else System.out.printf("%8s ",(i + 1) + "/" + (j + 1) + "-" +
                        ((ParkingCell)mat.get(i).get(j)).getIsParked());
            }
            System.out.println();
        }
        System.out.println("\nHere, All Parking Places represented by R/C-E or R/C-P");
        System.out.println("R means Rth Rows, C means Cth Column");
        System.out.println("E stands for Empty, P stands for Parked");
    }

    public void showDetailedPath() {
        System.out.println();
        for(int i = 0; i < row; ++i)
        {
            for(int j = 0; j < col; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("X")) {
                    System.out.printf("%8s ",mat.get(i).get(j).getPosition() +
                            ((PathCell)mat.get(i).get(j)).getDirection());
                }
                else {
                    System.out.printf("%8s ", mat.get(i).get(j).getCellValue());
                }
            }
            System.out.println();
        }
        System.out.println("\nHere, Car path represented by R/C{D}");
        System.out.println("R means Rth Rows, C means Cth Column and " +
                "D represents Direction that the car can travel to reach parking place");
    }

    public void setDetailedLeftEntryPath(int r, int c) {
        if(mat.get(r+1).get(c).getCellValue().equals("X")) {
            int j = 0;
            for(int i = row - 1; i > r+1; --i) {
                if (mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell) mat.get(i).get(j)).setDirection("{^}");
                }
            }
            for(; j < c; ++j) {
                if(mat.get(r + 1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r + 1).get(j)).setDirection("{>}");
                }
            }
            if(mat.get(r + 1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r + 1).get(c)).setDirection("{^}");
            }
        }
        else {
            int i = row - (inBetweenPathWidth);
            int j = col - (inBetweenPathWidth);
            if(mat.get(row - 1).get(0) instanceof  PathCell) {
                ((PathCell)mat.get(row - 1).get(0)).setDirection("{^}");
            }
            for(int j1 = 0 ; j1 < j ; ++j1) {
                if(mat.get(i).get(j1) instanceof  PathCell) {
                    ((PathCell)mat.get(i).get(j1)).setDirection("{>}");
                }
            }
            for(int i1 = i; i1 > r - 1; --i1) {
                if(mat.get(i1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{^}");
                }
            }
            for(int j1 = j; j1 > c; --j1) {
                if(mat.get(r-1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r-1).get(j1)).setDirection("{<}");
                }
            }
            if(mat.get(r-1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r-1).get(c)).setDirection("{v}");
            }
        }
    }

    public void setDetailedEntryPath(int r, int c) {
        int j = inBetweenPathWidth - 1;
        if(mat.get(r-1).get(c).getCellValue().equals("X")) {
            for(int i = row - 1; i >= r; --i) {
                if (mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell) mat.get(i).get(j)).setDirection("{^}");
                }
            }
            for(; j < c; ++j) {
                if(mat.get(r - 1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r - 1).get(j)).setDirection("{>}");
                }
            }
            if(mat.get(r - 1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r - 1).get(c)).setDirection("{v}");
            }
        }
        else {
            int to = col - (inBetweenPathWidth);
            for(int i = row - 1; i >= r - 1; --i) {
                if (mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell) mat.get(i).get(j)).setDirection("{^}");
                }
            }
            for(; j < to; ++j) {
                if(mat.get(r - 2).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r - 2).get(j)).setDirection("{>}");
                }
            }
            for(int i = r - 2; i <= r; ++i) {
                if(mat.get(i).get(to) instanceof  PathCell) {
                    ((PathCell)mat.get(i).get(to)).setDirection("{v}");
                }
            }
            for(; j > c; --j) {
                if(mat.get(r + 1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r + 1).get(j)).setDirection("{<}");
                }
            }
            if(mat.get(r + 1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r + 1).get(c)).setDirection("{^}");
            }
        }
    }

    public void setDetailedLeftExitPath(int r, int c) {
        int j = inBetweenPathWidth - 1;
        int jj = col - (inBetweenPathWidth);
        if(mat.get(r+1).get(c).getCellValue().equals("X")) {
            int i = r - 2;
            if(mat.get(r-1).get(c).getCellValue().equals("X")) i = r - 1;
            for(int j1 = c; j1 < jj; ++j1) {
                if(mat.get(r+1).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(r+1).get(j1)).setDirection("{>}");
                }
            }
            for(int i1 = r+1; i1 > i; --i1) {
                if(mat.get(i1).get(jj) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(jj)).setDirection("{^}");
                }
            }
            for(int j1 = jj; j1 > j; --j1) {
                if(mat.get(i).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(i).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = i; i1 < row; ++i1) {
                if(mat.get(i1).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
        }
        else {
            for(int j1 = c; j1 > j; --j1) {
                if(mat.get(r-1).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(r-1).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = r - 1; i1 < row; ++i1) {
                if(mat.get(i1).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
        }
    }

    public void setDetailedExitPath(int r, int c) {
        int i = row - (inBetweenPathWidth);
        int j = col - (inBetweenPathWidth);
        if(mat.get(r-1).get(c).getCellValue().equals("X")) {
            for(int j1 = c; j1 < j; ++j1) {
                if(mat.get(r - 1).get(j1) instanceof  PathCell) {
                    ((PathCell)mat.get(r - 1).get(j1)).setDirection("{>}");
                }
            }
            for(int i1 = r - 1; i1 < i; ++i1) {
                if(mat.get(i1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
            for(int j1 = j; j1 >= 0; --j1) {
                if(mat.get(i).get(j1) instanceof  PathCell) {
                    ((PathCell)mat.get(i).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = i; i1 < row; ++i1) {
                if(mat.get(i1).get(0) instanceof  PathCell) {
                    ((PathCell)mat.get(i1).get(0)).setDirection("{v}");
                }
            }
        }
        else {
            for(int j1 = c; j1 > 0; --j1) {
                if(mat.get(r+1).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(r+1).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = r + 1; i1 < row; ++i1) {
                if(mat.get(i1).get(0) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(0)).setDirection("{v}");
                }
            }
        }
    }

    public boolean parkCarAtPosition(Car car, int row, int col) {
        if(!mat.get(row).get(col).getCellValue().equals("-")) return false;
        if(mat.get(row).get(col) instanceof ParkingCell) {
            ((ParkingCell)mat.get(row).get(col)).setCar(car);
            ((ParkingCell)mat.get(row).get(col)).setParkedTime(LocalTime.now());
            ((ParkingCell)mat.get(row).get(col)).setIsParked("P");
        }
        CarInParking carInParking = new CarInParking();
        carInParking.addCars(car);
        this.vacancy --;
        return true;
    }

    public void removeDirections() {
        for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if(mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i).get(j)).setDirection("");
                }
            }
        }
    }

    public ParkingCell getPathCellByPosition(int r, int c) {
        return (ParkingCell) mat.get(r).get(c);
    }
}
