import java.sql.*;

public class Saver {
    private static String url = "jdbc:mysql://localhost:55000/dProj?useSSL=false";
    private static String user = "root";
    private static String password = "mysqlpw";

    public Saver() {

    }

    public static void save(Record record) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        saveSugar(record.getSugar(), conn);
        conn.close();
        conn = DriverManager.getConnection(url, user, password);
        saveDate(record.getDate(), conn);
        conn.close();
    }

    private static void saveSugar(Double sugar, Connection conn) throws SQLException {
        String tableName = "Sugar";
        String sql = " insert into "+ tableName +" (sugar) values (?)";
        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setFloat(1, sugar.floatValue());
        preparedStmt.execute();
    }

    private static void saveDate(Timestamp timestamp, Connection conn) throws SQLException {
        String tableName = "Record";
        String sql = " insert into "+ tableName +" (time) values (?)";
        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setDate(1, new Date(timestamp.getTime()));
        preparedStmt.execute();
    }
}



