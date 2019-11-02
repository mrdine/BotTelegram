package app;

import interfaces.TelegramBotInterface;

public class Main {

		public static void main(String[] args) {
					
			TelegramBotInterface bot = new TelegramBotInterface();
			
			bot.init();
					
		}

}
