package concetion;

import java.sql.*;

public class Postgres {
	
	private static Postgres instance = new Postgres();
	
	private String url;
	private String username;
	private String password;
	
	private Connection db;
	private Statement st;
	public ResultSet rs;
	
	public static Postgres getInstance()
	{
		return instance;
	}
	
	private Postgres()
	{
		try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        url = "jdbc:postgresql://"+"tuffi.db.elephantsql.com"+":5432"+"/fcrrwpsq";
        username = "fcrrwpsq";
        password = "7nhXk7OI2taPsBUUd02RvPxGAJjItWjC";
        
        //iniciar conexão
        try {
            db = DriverManager.getConnection(url, username, password);
            st = db.createStatement();
            // exemplo de comando de consulta postgresql
            /*
            rs = st.executeQuery("SELECT * FROM localizacao");
            while (rs.next()) {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(2));
                System.out.print("Column 2 returned ");
                System.out.println(rs.getString(3));
            }
            rs.close();
            st.close();
            */
            }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void executeSQLCommand(String command)
	{
		try {
			rs = st.executeQuery(command);
		} 
		catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
}