package logic_handle;

import entity.BusLine;
import entity.Driver;
import entity.DriverManagement;
import main.MainRun;
import util.DataUtil;
import util.FileUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DriverManagementService {

    public static final String DRIVER_MANAGEMENT_FILE_NAME = "driver_management.dat";


    public static boolean checkData() {
        return DriverService.isEmptyDriver() || BusLineService.isEmptyBusLine();
    }

    public static void createDrivingSchedule() {
        if (checkData()) {
            System.out.println("Bạn cần nhập thông tin cho tài xế và tuyến xe trước khi phân công lái xe");
            return;
        }
        System.out.print("Nhập số lượng tài xế mà bạn muốn phân công trong hôm nay: ");
        int newDriverNumber = 0;
        do {
            try {
                newDriverNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tài xế phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (newDriverNumber > 0) {
                break;
            }
            System.out.print("Số lượng tài xế KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
        for (int i = 0; i < newDriverNumber; i++) {
            Driver driver = inputDriver(i);

            System.out.print("Tài xế thứ " + (i + 1) + " muốn lái bao nhiêu tuyến trong ngày hôm nay: ");
            int busLineNumber = 0;
            do {
                try {
                    busLineNumber = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("Số lượng tuyến phải là số nguyên, yêu cầu nhập lại: ");
                    continue;
                }
                if (busLineNumber > 0) {
                    break;
                }
                System.out.print("Số lượng tuyến KHÔNG được là số âm, yêu cầu nhập lại: ");
            } while (true);

            Map<BusLine, Integer> busLineMap = new HashMap<>();
            int totalRound = 0;
            for (int j = 0; j < busLineNumber; j++) {
                BusLine busLine = inputBusLineMap(j);
                int busRound = inputBusRound();
                if ((totalRound + busRound) > 15) {
                    System.out.println("Lái xe này không được phép lái quá 15 lượt mỗi ngày.");
                    break;
                }
                busLineMap.put(busLine, busRound);
                totalRound += busRound;
            }
            DriverManagement driverManagement = new DriverManagement(driver, busLineMap, totalRound);
            MainRun.DRIVER_MANAGEMENTS.add(driverManagement);
            writeDataToDB(driverManagement);
        }
        // ghi dữ liệu vào file
//        FileUtil.writeDataToFile(MainRun.DRIVER_MANAGEMENTS, DRIVER_MANAGEMENT_FILE_NAME);
    }

    private static void writeDataToDB(DriverManagement driverManagement) {
        try {
            Class.forName(MainRun.JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(MainRun.DB_URL, MainRun.USER, MainRun.PASS);
            //test kết nối
            System.out.println(connection.getSchema());
            System.out.println(connection);

            Map<BusLine, Integer> busLines = driverManagement.getBusLines();
            for (Map.Entry<BusLine, Integer> entry : busLines.entrySet()) {

                String sql;
                sql = new StringBuilder().append("INSERT INTO BUS_DRIVER_MANAGEMENT (DRIVER_ID, BUSLINE_ID , ROUND) VALUES ").append("(")
                        .append(driverManagement.getDriver().getId()).append(", ")
                        .append(entry.getKey().getId()).append(", ")
                        .append(entry.getValue()).append(" )").toString();
                Statement statement = connection.createStatement();
                statement.execute(sql);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static int inputBusRound() {
        System.out.print("Nhập số lượt chạy cho tuyến vừa chọn: ");
        int busRound = 0;
        do {
            try {
                busRound = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số lượt chạy của tuyến xe phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (busRound > 0) {
                break;
            }
            System.out.print("Số lượt chạy của tuyến xe KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
        return busRound;
    }

    private static BusLine inputBusLineMap(int j) {

        System.out.print("Nhập tuyến thứ " + (j + 1) + " mà tài xế này muốn lái:");
        BusLine busLine = null;
        do {
            int busLineId = 0;
            do {
                try {
                    busLineId = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("Mã tuyến xe phải là số nguyên, yêu cầu nhập lại: ");
                    continue;
                }
                if (busLineId > 0) {
                    break;
                }
                System.out.print("Mã tuyến xe KHÔNG được là số âm, yêu cầu nhập lại: ");
            } while (true);

            // tìm kiếm xem có tuyến xe trong danh sách hay không, nếu không có yêu cầu nhập lại
            busLine = BusLineService.findById(busLineId);
            if (!DataUtil.isNull(busLine)) {
                break;
            }
            System.out.print("Không tìm thấy tuyến có id vừa nhập, vui lòng nhập lại: ");
        } while (true);
        return busLine;
    }

    private static Driver inputDriver(int i) {
        System.out.print("Nhập tài xế thứ " + (i + 1) + " cần phân công: ");
        Driver driver = null;
        do {
            int driverId = 0;
            do {
                try {
                    driverId = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("Mã tài xế phải là số nguyên, yêu cầu nhập lại: ");
                    continue;
                }
                if (driverId > 0) {
                    break;
                }
                System.out.print("Mã tài xế KHÔNG được là số âm, yêu cầu nhập lại: ");
            } while (true);

            // tìm kiếm xem có tài xế trong danh sách hay không, nếu không có yêu cầu nhập lại
            driver = DriverService.findById(driverId);
            if (!DataUtil.isNull(driver)) {
                break;
            }
            System.out.print("Không tìm thấy tài xế có id vừa nhập, vui lòng nhập lại: ");
        } while (true);


        // kiểm tra xem lái xe này đã tồn tại trong bảng phân công trước đó hay chưa

        return driver;
    }

    public static void showData() {
        MainRun.DRIVER_MANAGEMENTS.forEach(System.out::println);
    }

    public static void sortByDriverName() {
//        MainRun.DRIVER_MANAGEMENTS.sort((o1, o2) -> o1.getDriver().getName().compareTo(o2.getDriver().getName()));
        MainRun.DRIVER_MANAGEMENTS.sort(Comparator.comparing(o -> o.getDriver().getName()));
        showData();
    }

    public static void sortByNumberOfBusLine() {
        MainRun.DRIVER_MANAGEMENTS.sort(Comparator.comparing(o -> o.getBusLines().size()));
        showData();
    }

    public static void sortDrivingDriver() {
        System.out.println("Mời bạn chọn cách sắp xếp danh sách phân công lái xe.");
        System.out.println("1. Phân công theo tên của lái xe.");
        System.out.println("2. Phân công theo số tuyến đăng ký.");
        int choice = 0;
        do {
            try {
                choice = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Chức năng cần chọn là 1 số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (choice >= 1 && choice <= 2) {
                break;
            }
            System.out.print("Chức năng vừa chọn không hợp lệ, vui lòng nhập lại: ");
        } while (true);

        if (choice == 1) {
            sortByDriverName();
        } else {
            sortByNumberOfBusLine();
        }
    }

    public static void calculateTotalRound() {
//        MainRun.DRIVER_MANAGEMENTS.forEach(driverManagement -> {
//            Set<Map.Entry<BusLine, Integer>> entries = driverManagement.getBusLines().entrySet();
//            for (:
//                 ) {
//
//            }
//        });

//        MainRun.DRIVER_MANAGEMENTS.forEach(driverManagement -> { driverManagement.getTotalRound(); });
        System.out.println("Bảng thống kê khoảng cách chạy xe trong ngày của mỗi lái xe là: ");

        MainRun.DRIVER_MANAGEMENTS.forEach(driverManagement -> {
            Set<Map.Entry<BusLine, Integer>> entries = driverManagement.getBusLines().entrySet();
            Iterator<Map.Entry<BusLine, Integer>> iterator = entries.iterator();
            float totalDistance = 0;
            while (iterator.hasNext()) {
                Map.Entry<BusLine, Integer> next = iterator.next();
                BusLine key = next.getKey();
                Integer value = next.getValue();
                float distance = key.getDistance();
                int busRound = value.intValue();
                totalDistance += distance * busRound;
            }

            System.out.println(driverManagement.getDriver().toString() +
                    " : '\t "
                    + totalDistance + " km.");
        });
    }

}
