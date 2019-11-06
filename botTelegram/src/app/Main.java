package app;

import interfaces.TelegramBotInterface;
import commandsSQL.BemSQL;
import commandsSQL.CategoriaSQL;
import commandsSQL.LocalizacaoSQL;
import concetion.Postgres;

public class Main {

	public static void main(String[] args) {
		
		//Classe antiga de comunicação com telegram
		//TelegramBotInterface bot = new TelegramBotInterface();
		//bot.init();
		
		// comunicação com telegram
		BotComunicacao bot = new BotComunicacao();
		// iniciar comunicação com usuario
		bot.init();
		
		
	}

}

