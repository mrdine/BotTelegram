package concetion;

import java.sql.*;

/**
 * Classe singleton que faz conexão e requisições ao banco de dados PostgreSQL
 * 
 * @author danielvis
 *
 */
public class Postgres {
	
	/** instancia unica da classe */
	private static Postgres instance = new Postgres();
	
	/** atributos de identificação do banco de dados */
	private String url;
	private String username;
	private String password;
	
	/** atributos responsaveis por requisições */ 
	private Connection db;
	private Statement st;
	public ResultSet rs;
	
	/** 
	 * Retorna instancia unica da classe
	 * @return istancia Postgres
	 */
	public static Postgres getInstance()
	{
		return instance;
	}
	
	/**
	 * Metodo construtor da classe que inicia conexao com o banco de dados
	 */
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
            
            }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	/**
	 * Requisita a execução de um comando PostgreSQL
	 * @param command
	 */
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