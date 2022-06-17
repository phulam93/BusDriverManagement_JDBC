package entity;

import main.MainRun;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusLine implements AutoIdIncreasable, NewDataCreatable, Serializable {

    private static int AUTO_ID = 100;

    private int id;
    private float distance;
    private int stopStationNumber;

    public BusLine() {
        this.increaseId();
    }

    public BusLine(float distance, int stopStationNumber) {
        this.distance = distance;
        this.stopStationNumber = stopStationNumber;
        this.increaseId();
    }

    public BusLine(int id, float distance, int stopStationNumber) {
        this.id = id;
        this.distance = distance;
        this.stopStationNumber = stopStationNumber;
    }

    @Override
    public final void increaseId() {
        this.id = MainRun.BUS_LINES.size() + AUTO_ID + 1;
    }

    public int getId() {
        return id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getStopStationNumber() {
        return stopStationNumber;
    }

    public void setStopStationNumber(int stopStationNumber) {
        this.stopStationNumber = stopStationNumber;
    }

    @Override
    public String toString() {
        return "BusLine{" +
                "id=" + id +
                ", distance=" + distance +
                ", stopStationNumber=" + stopStationNumber +
                '}';
    }

    @Override
    public void inputNewData() {
        System.out.print("Nhập khoảng cách của tuyến xe: ");
        float distance = 0;
        do {
            try {
                distance = new Scanner(System.in).nextFloat();
            } catch (InputMismatchException ex) {
                System.out.print("Khoảng cách của phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (distance > 0) {
                this.setDistance(distance);
                break;
            }
            System.out.print("Khoảng cách của tuyến mới KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);

        System.out.print("Nhập số điểm dừng của tuyến xe: ");
        int stopStationNumber = 0;
        do {
            try {
                stopStationNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số điểm dừng của phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (stopStationNumber > 0) {
                this.setStopStationNumber(stopStationNumber);
                break;
            }
            System.out.print("Số điểm dừng của tuyến mới KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
    }
}
