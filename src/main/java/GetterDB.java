import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GetterDB {
    private static String tableName = DataBaseParameters.TABLE_NAME;

    private static String url = DataBaseParameters.URL;
    private static String user = DataBaseParameters.USER;
    private static String password = DataBaseParameters.PASSWORD;

    private final Long userID;


    public GetterDB(Long userID){
        this.userID = userID;
    }

    public Record[] getRecords(Timestamp dateIsInterested) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        LocalDateTime localDateTime = dateIsInterested.toLocalDateTime();
        LocalDateTime newDate = localDateTime.plusDays(1);
        Timestamp datePlusOne = Timestamp.valueOf(newDate);
        String sql = ("SELECT * FROM " + tableName + " WHERE timeDate BETWEEN '"+dateIsInterested+
                "' AND '"+datePlusOne+"' AND user=" + userID);
        PreparedStatement preparedStmt = con.prepareStatement(sql);
        ResultSet rs = preparedStmt.executeQuery(sql);
        List<Record> result = new ArrayList<>();
        while (rs.next()) {
            Timestamp timestamp = rs.getTimestamp("timeDate");
            Double sugar = rs.getDouble("sugar");
            Double xe = rs.getDouble("XE");
            Double shortIns = rs.getDouble("short_insulin");
            Double longIns = rs.getDouble("long_insulin");
            Double coefficient = rs.getDouble("coefficient");
            Record record = new Record(userID, timestamp, sugar, xe, shortIns, longIns, coefficient);
            result.add(record);
        }
        con.close();
        Record[] arrayResult = new Record[result.size()];
        return result.toArray(arrayResult);
    }
}
