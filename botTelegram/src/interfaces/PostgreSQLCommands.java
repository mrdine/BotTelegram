package interfaces;

import concetion.Postgres;

/**
 * Interface a ser implementada pelas classes BemSQL, CategoriaSQL e LocalizacaoSQL
 * 
 * @author danielvis
 *
 */
public interface PostgreSQLCommands {
	
	/** Instancia do objeto de conexão com o banco de dados */
	Postgres bdConection = Postgres.getInstance();
	
	/**
	 * Executa comando sql de inserção
	 * */
	void inserir();
	
	/**
	 * Executa comando sql de busca
	 * */
	String listar();
	
	
}
