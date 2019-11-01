package concetion;

import java.sql.*;

public class Postgres {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        String url = "jdbc:postgresql://"+"tuffi.db.elephantsql.com"+":5432"+"/fcrrwpsq";
        String username = "fcrrwpsq";
        String password = "7nhXk7OI2taPsBUUd02RvPxGAJjItWjC";
        
        //iniciar conexão
        try {
            Connection db = DriverManager.getConnection(url, username, password);
            Statement st = db.createStatement();
            // exemplo de comando de consulta postgresql
            ResultSet rs = st.executeQuery("SELECT * FROM localizacao");
            while (rs.next()) {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(2));
                System.out.print("Column 2 returned ");
                System.out.println(rs.getString(3));
            }
            rs.close();
            st.close();
            }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}