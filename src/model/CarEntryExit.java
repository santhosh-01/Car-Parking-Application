package model;

import java.time.LocalTime;

public class CarEntryExit {

    private LocalTime entryTime;
    private LocalTime exitTime;
    private int[] position;

    public CarEntryExit(LocalTime entryTime, int[] position) {
        this.entryTime = entryTime;
        this.position = position;
    }

    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public LocalTime getExitTime() {
        return exitTime;
    }

    public int[] getPosition() {
        return position;
    }
}
