package main;

import constant.DriverLevel;
import entity.BusLine;
import entity.Driver;
import entity.DriverManagement;
import logic_handle.BusLineService;
import logic_handle.DriverManagementService;
import logic_handle.DriverService;
import sun.applet.Main;
import util.DataUtil;
import util.FileUtil;

import java.sql.*;
import java.util.*;

public class MainRun {

    public static List<Driver> DRIVERS;
    public static List<BusLine> BUS_LINES;
    public static List<DriverManagement> DRIVER_MANAGEMENTS;

    // Ten cua driver va dia chi URL cua co so du lieu
    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";

    //  Ten nguoi dung va mat khau cua co so du lieu
    public static final String USER = "admin";
    public static final String PASS = "admin";

    public static void main(String[] args) {


        // lấy dữ liệu sẵn có từ oracle database
        initDataFromJDBC();


        // lấy dữ liệu sẵn có từ file đã lưu
//        initData();

        menu();


    }

    private static void initDataFromJDBC() {
        DRIVERS = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            //test kết nối
            System.out.println(connection.getSchema());
            System.out.println(connection);
            String sqlQuery = "SELECT * FROM DRIVER";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            List<Driver> drivers = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone = resultSet.getString(4);
                String level = resultSet.getString(5);
                Driver driver = new Driver(name, address, phone, id, level);
                DRIVERS.add(driver);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        BUS_LINES = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            //test kết nối
            System.out.println(connection.getSchema());
            System.out.println(connection);
            String sqlQuery = "SELECT * FROM BUS_LINE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            List<BusLine> busLines = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                float distance = resultSet.getFloat(2);
                int stopStationNumber = resultSet.getInt(3);

                BusLine busLine = new BusLine(id, distance, stopStationNumber);
                BUS_LINES.add(busLine);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        DRIVER_MANAGEMENTS = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            //test kết nối
            System.out.println(connection.getSchema());
            System.out.println(connection);
            String sqlQuery = "SELECT * FROM BUS_DRIVER_MANAGEMENT";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            List<DriverManagement> driverManagements = new ArrayList<>();
            while (resultSet.next()) {
                int driverId = resultSet.getInt(1);
                int busLineId = resultSet.getInt(2);
                int round = resultSet.getInt(3);
                Driver driver = null;
                for (int i = 0; i < MainRun.DRIVERS.size(); i++) {
                    if (driverId == MainRun.DRIVERS.get(i).getId()) {
                        driver = MainRun.DRIVERS.get(i);
                        break;
                    }
                }
                BusLine busLine = null;
                for (int i = 0; i < MainRun.BUS_LINES.size(); i++) {
                    if (busLineId == MainRun.BUS_LINES.get(i).getId()) {
                        busLine = MainRun.BUS_LINES.get(i);
                        break;
                    }
                }


                if (MainRun.DRIVER_MANAGEMENTS.isEmpty()) {
                    Map<BusLine, Integer> busLineMap = new HashMap<>();
                    busLineMap.put(busLine, round);
                    DriverManagement driverManagement = new DriverManagement(driver, busLineMap);
                    DRIVER_MANAGEMENTS.add(driverManagement);
                }else {
                    for (int i = 0; i < DRIVER_MANAGEMENTS.size(); i++) {
                        if (driverId == DRIVER_MANAGEMENTS.get(i).getDriver().getId()){
                            DRIVER_MANAGEMENTS.get(i).getBusLines().put(busLine, round);
                        }else {
                            Map<BusLine, Integer> busLineMap = new HashMap<>();
                            busLineMap.put(busLine, round);
                            DriverManagement driverManagement = new DriverManagement(driver, busLineMap);
                            DRIVER_MANAGEMENTS.add(driverManagement);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initData() {
        DRIVERS = (List<Driver>) FileUtil.readDataFromFile(DriverService.DRIVER_FILE_NAME);
        if (DataUtil.isEmptyCollection(DRIVERS)) {
            DRIVERS = new ArrayList<>();
        }

        BUS_LINES = (List<BusLine>) FileUtil.readDataFromFile(BusLineService.BUS_LINE_FILE_NAME);
        if (DataUtil.isEmptyCollection(BUS_LINES)) {
            BUS_LINES = new ArrayList<>();
        }

        DRIVER_MANAGEMENTS = (List<DriverManagement>) FileUtil.readDataFromFile(DriverManagementService.DRIVER_MANAGEMENT_FILE_NAME);
        if (DataUtil.isEmptyCollection(DRIVER_MANAGEMENTS)) {
            DRIVER_MANAGEMENTS = new ArrayList<>();
        }
    }

    private static void menu() {
        do {
            int functionChoice = showMenu();
            switch (functionChoice) {
                case 1:
                    DriverService.createNewDriver();
                    break;
                case 2:
                    DriverService.showDriver();
                    break;
                case 3:
                    BusLineService.createNewBusLine();
                    break;
                case 4:
                    BusLineService.showBusLine();
                    break;
                case 5:
                    DriverManagementService.createDrivingSchedule();
                    break;
                case 6:
                    DriverManagementService.showData();
                    break;
                case 7:
                    DriverManagementService.sortDrivingDriver();
                    break;
                case 8:
                    DriverManagementService.calculateTotalRound();
                    break;
                case 9:
                    // System.exit(0);
                    return;
            }
        } while (true);
    }

    private static int showMenu() {
        printMenu();
        int functionChoice = -1;
        do {
            try {
                functionChoice = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Chức năng cần chọn là 1 số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (functionChoice >= 1 && functionChoice <= 9) {
                break;
            }
            System.out.print("Chức năng vừa chọn không hợp lệ, vui lòng nhập lại: ");
        } while (true);
        return functionChoice;
    }

    private static void printMenu() {
        System.out.println("--------PHẦN MỀM QUẢN LÝ PHÂN CÔNG LÁT XE BUÝT------");
        System.out.println("1. Nhập danh sách lái xe mới.");
        System.out.println("2. In ra danh sách lái xe mới.");
        System.out.println("3. Nhập danh sách tuyến xe mới.");
        System.out.println("4. In ra danh sách tuyến xe mới.");
        System.out.println("5. Phân công lái xe cho các tài xế.");
        System.out.println("6. In ra danh sách đã phân công.");
        System.out.println("7. Sắp xếp danh sách phân công lái xe.");
        System.out.println("8. Lập bảng thống kê tổng khoảng cách chạy xe trong ngày của từng lái xe.");
        System.out.println("9. Thoát");
        System.out.print(" Xin mời chọn chức năng: ");
    }


}
