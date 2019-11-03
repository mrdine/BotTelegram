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

public class TelegramBotInterface extends CategoriaSQL{

	//controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
	private int m=0;
	
	TelegramBot bot = TelegramBotAdapter.build("1028381418:AAE1ixyIWE_mKGe3PA09uE0vE6YWi3waD_4");

	//objeto responsável por receber as mensagens
	GetUpdatesResponse updatesResponse;
	
	//objeto responsável por gerenciar o envio de respostas
	SendResponse sendResponse;
	
	//objeto responsável por gerenciar o envio de ações do chat
	BaseResponse baseResponse;
	
	public void init() {
		
		List<Update> updates;
		
		//loop infinito pode ser alterado por algum timer de intervalo curto
		while (true){
			
			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			//lista de mensagens
			updates = updatesResponse.updates();
			
			//updates.add(up);
			
			//análise de cada ação da mensagem
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				
				// imprime opção para /home toda vez que o usuário termina uma ação no bot ou interage pela primeira vez com o bot
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
						"Digite /home para nova consulta"
						+ "\nDigite /next para seguir navegando"));
				
				String comando = update.message().text();
				System.out.println("Primeiro estou executando esse comando:" + comando);
				
				// imprime menu
				if (comando.equals("/home")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
										
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							" Menu Principal: "
							+ "\n /categoria "
							+ "\n /bem "
							+ "\n /localizacao"));
					
					System.out.println("Segundo estou executando esse comando:" + update.message().text());
					
					
				// Chama funções para fazer os procedimentos
				} else if (comando.contentEquals("/categoria")) {
					
					initCategoria();
					
				} else if (comando.contentEquals("/bem")) {
					
					
				} else if (comando.contentEquals("/localizacao")) {
					
					
				}
				init();	
			}
		}
	}



	/** 
	 * faz comandos e chama métodos de categoriaSQL
	 */
	
	private void initCategoria() {
		
		List<Update> updates2;
		
		CategoriaSQL categoria = new CategoriaSQL();
		
		while(true) {
			
			updatesResponse =  bot.execute(new GetUpdates().limit(1).offset(m));
			
			updates2 = updatesResponse.updates();
			
			for (Update update : updates2) {
			
				m = update.updateId()+1;
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
			
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
						"Menu categoria: "
						+ "\n /addCategoria"
						+ "\n /addDescricaoCategoria "
						+ "\n /listarCategorias"
						+ "\n /next para seguir navegando"
						+ "\n /home para retornar ao menu"));
	
				System.out.println("Recebendo mensagem: "+ update.message().text());
				
				String auxiliar = update.message().text();
				
				String comando = null;
				
				if (auxiliar.contains("/")){
					
					comando =  update.message().text();
					
				} else if(auxiliar.contains(".")) {
					
					categoria.setDescricao(auxiliar);
					
					System.out.println("O nome da categoria da vez: " + categoria.getNome());
					System.out.println("O conteúdo recebido na vez foi esse: " + auxiliar);
					
					categoria.inserir();
					
					initCategoria();
					
				} else {
					
					categoria.setNome(auxiliar);
					
					System.out.println("O nome da categoria da vez: " + categoria.getNome());
					System.out.println("O conteúdo recebido na vez foi esse: " + auxiliar);
					
					categoria.inserir();
					
					initCategoria();
				}
				
				System.out.println("O nome da categoria da vez: " + categoria.getNome());
				System.out.println("O conteúdo recebido na vez foi esse: " + auxiliar);
			
				// Opções de categoria
				if (comando.equals("/addCategoria")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							" Adicione um nome de categoria: "));
					
					initCategoria();
					
				} else if (comando.equals("/listarCategorias")) {
				
					categoria.listar();
					
					initCategoria();
				
				} else if (comando.equals("/addDescricaoCategoria")) {
			
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Adicione uma descrição à categoria: "
							+ "Adicione um ponto final à sua descrição"));
					
					initCategoria();
				} else if (comando.equals("/home")) {
					
					init();
				}
			}
		}
	}
}