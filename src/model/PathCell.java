package model;

public class PathCell extends Cell{

    private String direction;

    public PathCell() {
        super.setCellValue("X");
        this.direction = "";
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
