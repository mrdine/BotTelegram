package interfaces;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {

	public static void main(String[] args) {
				
		TelegramBotInterface bot = new TelegramBotInterface();
		
		bot.init();
				

	}

}

public class TelegramBotInterface {

	public void init() {
	
		TelegramBot bot = TelegramBotAdapter.build("1028381418:AAE1ixyIWE_mKGe3PA09uE0vE6YWi3waD_4");

		//objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;
		
		//objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;
		
		//objeto responsável por gerenciar o envio de ações do chat
		BaseResponse baseResponse;
		
		//controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
		int m=0;
		
		//loop infinito pode ser alterado por algum timer de intervalo curto
		while (true){
			
			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			//lista de mensagens
			List<Update> updates = updatesResponse.updates();
			
			//análise de cada ação da mensagem
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " Digite /home para saber as opções:"));
				System.out.println("Recebendo mensagem:"+ update.message().text());
				String comando = update.message().text();
				
				String conteudo = null;
				
				if (comando.equals("/home")) {
				
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " ----------- MENU ----------- "
							+ "\n /addCategoria \n /addBem \n /addLocalizacao"));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
				}
				
				if (comando.equals("/addCategoria")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " Digite a nova categoria "
							+ "de bem que desejas adicionar à empresa: "));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
					System.out.println("Recebendo mensagem:"+ update.message().text());
					conteudo = update.message().text();
					
				}
				
				if (comando.equals("/addLocalizacao")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " Digite a nova localização "
							+ "que desejas adicionar à empresa: "));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
					System.out.println("Recebendo mensagem:"+ update.message().text());
					conteudo = update.message().text();
					
				}
				
				if (comando.equals("/addBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " Digite o nome do novo"
							+ " bem que desejas adicionar à empresa: "));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
					System.out.println("Recebendo mensagem:"+ update.message().text());
					conteudo = update.message().text();
					
				}
				
			}
			
			
		}
	}

}
