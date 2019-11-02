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

import commandsSQL.CategoriaSQL;

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
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " Digite /home para saber as opções de consulta"));
				System.out.println("Recebendo mensagem:"+ update.message().text());
				String comando = update.message().text();
				
				String conteudo = null;
				
				// Imprime menu toda vez que o usuário termina uma ação no bot
				if (comando.equals("/home")) {
				
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " ----------- MENU ----------- "
							+ "\n /categoria \n /bem \n /localizacao"));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
				}
				
				/** 
				 * faz comandos e chama métodos de categoriaSQL
				 */
				if (comando.equals("/categoria")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " O que desejas fazer? /addCategoria "
							+ "\n /listarCategorias "));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
					System.out.println("Recebendo mensagem:"+ update.message().text());
					
					conteudo = update.message().text();
					
					CategoriaSQL categoria = new CategoriaSQL();
					
					if (conteudo.equals("/addCategoria")) {
						
						categoria.setNome(conteudo);
						categoria.inserir();
						
						
					} else if (conteudo.equals("/listarCategorias")) {
						
						categoria.listar();
						
					}
					
				}
				

				/** 
				 * faz comandos e chama métodos de localizacaoSQL
				 */
				if (comando.equals("/localizacao")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), " Digite a nova localização "
							+ "que desejas adicionar à empresa: "));
					System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					
					System.out.println("Recebendo mensagem:"+ update.message().text());
					conteudo = update.message().text();
					
				}
				

				/** 
				 * faz comandos e chama métodos de bemSQL
				 */
				if (comando.equals("/bem")) {
					
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
