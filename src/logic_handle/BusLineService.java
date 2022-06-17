package logic_handle;

import entity.BusLine;
import main.MainRun;
import util.FileUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusLineService {

    public static final String BUS_LINE_FILE_NAME = "bus_line.dat";

    public static boolean isEmptyBusLine() {
        return MainRun.BUS_LINES.isEmpty();
    }

    public static void createNewBusLine() {
        System.out.print("Bạn muốn nhập thêm mấy tuyến xe mới: ");
        int newBusLineNumber = 0;
        do {
            try {
                newBusLineNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tuyến mới phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (newBusLineNumber > 0) {
                break;
            }
            System.out.print("Số lượng tuyến mới KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
        for (int i = 0; i < newBusLineNumber; i++) {
            BusLine busLine = new BusLine();
            busLine.inputNewData();
            MainRun.BUS_LINES.add(busLine);
            writeDataToDB(busLine);
        }

        // lưu dữ liệu vào file
//        FileUtil.writeDataToFile(MainRun.BUS_LINES, BUS_LINE_FILE_NAME);
    }

    private static void writeDataToDB(BusLine busLine) {
        try {
            Class.forName(MainRun.JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(MainRun.DB_URL, MainRun.USER, MainRun.PASS);
            //test kết nối
            System.out.println(connection.getSchema());
            System.out.println(connection);

            String sql;
            sql = new StringBuilder().append("INSERT INTO BUS_LINE (ID, DISTANCE , STOP_STATION_NUMBER) VALUES ").append("(").append(busLine.getId()).append(", ").append(busLine.getDistance()).append(", ").append(busLine.getStopStationNumber()).append(" )").toString();
            Statement statement = connection.createStatement();
            statement.execute(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showBusLine() {
        MainRun.BUS_LINES.forEach(System.out::println);
    }

    public static BusLine findById(int busLineId) {
        return MainRun.BUS_LINES
                .stream()
                .filter(driver -> driver.getId() == busLineId)
                .findFirst()
                .orElse(null);
    }
}
