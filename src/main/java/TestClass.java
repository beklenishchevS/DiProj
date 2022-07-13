import java.sql.*;

public class TestClass {
    private static String tableName = "full_Info";

    private static String url = "jdbc:mysql://localhost:55000/dProj?useSSL=false";
    private static String user = "root";
    private static String password = "mysqlpw";

    public static void main(String args[]){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            Statement st = con.createStatement();
            String sql = ("SELECT * FROM " + tableName);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Timestamp id = rs.getTimestamp("timeDate");
                System.out.println(id.toLocalDateTime().getHour());
            }

            con.close();
        } catch (Exception e){

        }
    }
}
