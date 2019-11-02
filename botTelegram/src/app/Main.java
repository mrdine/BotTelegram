package app;

import interfaces.TelegramBotInterface;
import commandsSQL.BemSQL;
import commandsSQL.CategoriaSQL;
import commandsSQL.LocalizacaoSQL;
import concetion.Postgres;

public class Main {

	public static void main(String[] args) {
		
		TelegramBotInterface bot = new TelegramBotInterface();
		
		bot.init();
		
		// Instancia da conexão com banco de dados
		Postgres bdConection = Postgres.getInstance();
		
		
		// testes
		BemSQL bem = new BemSQL();
		CategoriaSQL categoria = new CategoriaSQL();
		LocalizacaoSQL localizacao = new LocalizacaoSQL();
		
		//localizacao.listar();
		//categoria.listar();
		bem.listar();
	}

}

