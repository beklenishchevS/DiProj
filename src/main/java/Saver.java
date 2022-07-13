import java.sql.*;

public class Saver {
    private static String url = DataBaseParameters.URL;
    private static String user = DataBaseParameters.USER;
    private static String password = DataBaseParameters.PASSWORD;
    private static String tableName = DataBaseParameters.TABLE_NAME;

    public Saver() {

    }

    public static void save(Record record) throws SQLException, ClassNotFoundException {
        if (record.isEmpty()){
            return;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        Double[] recordAsArray = record.getRecordAsArray();
        Connection conn = DriverManager.getConnection(url, user, password);
        String sql = " insert into "+ tableName +" (timeDate," +
                "sugar," +
                " XE," +
                " short_insulin," +
                " long_insulin," +
                " coefficient, " +
                "user) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setTimestamp(1, record.getDate());
        for (int i=2; i<7; i++){
            prepareInfo(preparedStmt, i, recordAsArray[i]);
        }
        preparedStmt.setLong(7, record.getUserID());
        preparedStmt.execute();
        conn.close();
    }

    private static void prepareInfo(PreparedStatement preparedStmt, int i, Double info) throws SQLException {
        try {
            preparedStmt.setFloat(i, info.floatValue());
        } catch (Exception e) {
            System.out.println(e);
            preparedStmt.setNull(i, 0);
        }
    }


}



