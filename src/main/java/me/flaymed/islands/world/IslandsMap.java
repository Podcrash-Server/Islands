package me.flaymed.islands.world;

import me.flaymed.islands.bridges.data.BridgePoint;
import me.flaymed.islands.location.IDPoint2Point;
import me.flaymed.islands.location.Point;
import java.util.ArrayList;
import java.util.List;

public class IslandsMap {
    private List<Point> greenOres;
    private List<Point> yellowOres;
    private List<Point> blueOres;
    private List<Point> redOres;
    private List<Point> chests;
    private List<BridgePoint> bridgeData;

    private Point middle;

    private String bridgeType;

    private List<IDPoint2Point> bridges;

    public IslandsMap() {
        super();
        this.greenOres = new ArrayList<>();
        this.yellowOres = new ArrayList<>();
        this.blueOres = new ArrayList<>();
        this.redOres = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.bridges = new ArrayList<>();
    }

    public List<Point> getGreenOres() {
        return greenOres;
    }

    public void setGreenOres(List<Point> greenOres) {
        this.greenOres = greenOres;
    }

    public List<Point> getYellowOres() {
        return yellowOres;
    }

    public void setYellowOres(List<Point> yellowOres) {
        this.yellowOres = yellowOres;
    }

    public List<Point> getBlueOres() {
        return blueOres;
    }

    public void setBlueOres(List<Point> blueOres) {
        this.blueOres = blueOres;
    }

    public List<Point> getRedOres() {
        return redOres;
    }

    public void setRedOres(List<Point> redOres) {
        this.redOres = redOres;
    }

    public List<Point> getChests() {
        return chests;
    }

    public void setChests(List<Point> chests) {
        this.chests = chests;
    }

    public Point getMiddle() {
        return middle;
    }

    public void setMiddle(Point middle) {
        this.middle = middle;
    }

    public List<IDPoint2Point> getBridges() {
        return bridges;
    }

    public void setBridges(List<IDPoint2Point> bridges) {
        this.bridges = bridges;
    }

    public List<BridgePoint> getBridgeData() {
        return bridgeData;
    }

    public void setBridgeData(List<BridgePoint> bridgeData) {
        this.bridgeData = bridgeData;
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
    }
}
