package interfaces;

import concetion.Postgres;

public interface PostgreSQLCommands {
	
	Postgres bdConection = Postgres.getInstance();
	
	void inserir();
	
	void listar();
	
	
}
