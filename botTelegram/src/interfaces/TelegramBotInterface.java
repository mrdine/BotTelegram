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

import commandsSQL.BemSQL;
import commandsSQL.CategoriaSQL;
import commandsSQL.LocalizacaoSQL;

public class TelegramBotInterface{

	//controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
	private int m=0;
	
	private TelegramBot bot = TelegramBotAdapter.build("1028381418:AAE1ixyIWE_mKGe3PA09uE0vE6YWi3waD_4");

	//objeto responsável por receber as mensagens
	private GetUpdatesResponse updatesResponse;
	
	//objeto responsável por gerenciar o envio de respostas
	private SendResponse sendResponse;
	
	//objeto responsável por gerenciar o envio de ações do chat
	private BaseResponse baseResponse;
	
	private CategoriaSQL categoria;
	private LocalizacaoSQL localizacao;
	private BemSQL bem;
	
	private String nome;
	private String descricao;
	private int localizacaoBem;
	private int categoriaBem;
	
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
						"/home para nova consulta"
						+ "\n /ok para confirmar escolhas"));
				
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
					
					initBem();
					
				} else if (comando.contentEquals("/localizacao")) {
					
					initLocalizacao();
				}
				
				init();	
			}
		}
	}

	/** 
	 * faz comandos e chama métodos de bemSQL
	 */
	private void initBem() {
		List<Update> bens;
		
		bem = new BemSQL();
		categoria = new CategoriaSQL();
		localizacao = new LocalizacaoSQL();
		
		while(true) {
			
			updatesResponse =  bot.execute(new GetUpdates().limit(1).offset(m));
			
			bens = updatesResponse.updates();
			
			for (Update update : bens) {
			
				m = update.updateId()+1;
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
			
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
						"Menu categoria: "
						+ "\n /addBem"
						+ "\n /addDescricaoBem"
						+ "\n /addCategoriaBem"
						+ "\n /addLocalizacaoBem"
						+ "\n /salvarBem"
						+ "\n /listarBens"
						+ "\n /ok para confirmar escolhas"
						+ "\n /home para retornar ao menu"));
	
				System.out.println("Recebendo mensagem: "+ update.message().text());
				
				String auxiliar = update.message().text();
				
				String comando = null;
				
				if (auxiliar.contains("/")){
					
					comando =  update.message().text();
					
				} else if(auxiliar.contains(".")) {
					
					descricao = auxiliar;
					
					initBem();
					
				} else if (auxiliar.contains("+")) {
					
					auxiliar = auxiliar.replace("+","");
					localizacaoBem = Integer.parseInt(auxiliar);
							
					initBem();
				
				} else if (auxiliar.contains("-")) {
					
					auxiliar = auxiliar.replace("-","");
					categoriaBem = Integer.parseInt(auxiliar);
							
					initBem();
					
				} else {
					
					nome = auxiliar;
					
					initBem();
				}
				
				// Opções de categoria
				if (comando.equals("/addBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							" Adicione um nome ao bem: "));
					
					initBem();
					
				} else if (comando.equals("/listarBens")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), bem.listar()));
					
					initBem();
				
				} else if (comando.equals("/addCategoriaBem")) {
			
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite o código da categoria do bem cadastrado: \n"
							+ "OBS: Digitar '-' ao final do número!\n"
							+ categoria.listar()
							));
					
					initBem();
				
				} else if (comando.equals("/addLocalizacaoBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite o código da localizaçãoo do bem cadastrado: \n"
							+ "OBS: Digitar '+' ao final do número!" 
							+ localizacao.listar()
									));
					
					initBem();
					
				} else if (comando.equals("/addDescricaoBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Adicione uma descrição ao bem: + \n"
							+ "Adicione um ponto final à sua descrição"));
					
					initBem();
					
				} else if (comando.equals("/salvarBem")){
					
					bem.setNome(nome);
					bem.setDescricao(descricao);
					bem.setLocalizacao(localizacaoBem);
					bem.setCategoria(categoriaBem);
					bem.inserir();
					
					initBem();
					
				} else if (comando.equals("/home")) {
					
					init();
				}
			}
		}	
	}


	/** 
	 * faz comandos e chama métodos de localizacaoSQL
	 */
	private void initLocalizacao() {
		
		List<Update> localizacoes;
		localizacao = new LocalizacaoSQL();
		
		while(true) {
			
			updatesResponse =  bot.execute(new GetUpdates().limit(1).offset(m));
			
			localizacoes = updatesResponse.updates();
			
			for (Update update : localizacoes) {
			
				m = update.updateId()+1;
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
			
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
						"Menu Localização: "
						+ "\n /addLocalizacao"
						+ "\n /addDescricaoLocalizacao "
						+ "\n /salvarLocalizacao"
						+ "\n /listarLocalizacoes"
						+ "\n /ok para confirmar escolhas"
						+ "\n /home para retornar ao menu"));
	
				System.out.println("Recebendo mensagem: "+ update.message().text());
				
				String auxiliar = update.message().text();
				
				String comando = null;
				
				if (auxiliar.contains("/")){
					
					comando =  update.message().text();
					
				} else if(auxiliar.contains(".")) {
					
					descricao = auxiliar;
					
					initLocalizacao();
					
				} else {
					
					nome = auxiliar;
					
					initLocalizacao();
				}
				
				// Opções de categoria
				if (comando.equals("/addLocalizacao")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							" Adicione um nome de Localização: "));
					
					initLocalizacao();
					
				} else if (comando.equals("/listarLocalizacoes")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), localizacao.listar()));
					
					initLocalizacao();
				
				} else if (comando.equals("/addDescricaoLocalizacao")) {
			
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Adicione uma descrição à localização: "
							+ "Adicione um ponto final à sua descrição"));
					
					initLocalizacao();
					
				} else if (comando.equals("/salvarLocalizacao")){
					
					localizacao.setNome(nome);
					localizacao.setDescricao(descricao);
					localizacao.inserir();
					
					initLocalizacao();
					
				} else if (comando.equals("/home")) {
					
					init();
				}
			}
		}
	}

	/** 
	 * faz comandos e chama métodos de categoriaSQL
	 */
	
	private void initCategoria() {
		
		List<Update> categorias;
		categoria = new CategoriaSQL();
		
		while(true) {
			
			updatesResponse =  bot.execute(new GetUpdates().limit(1).offset(m));
			
			categorias = updatesResponse.updates();
			
			for (Update update : categorias) {
			
				m = update.updateId()+1;
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
			
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
						"Menu categoria: "
						+ "\n /addCategoria"
						+ "\n /addDescricaoCategoria "
						+ "\n /listarCategorias"
						+ "\n /salvarCategoria"
						+ "\n /ok para confirmar escolhas"
						+ "\n /home para retornar ao menu"));
	
				System.out.println("Recebendo mensagem: "+ update.message().text());
				
				String auxiliar = update.message().text();
				
				String comando = null;
				
				if (auxiliar.contains("/")){
					
					comando =  update.message().text();
					
				} else if(auxiliar.contains(".")) {
					
					descricao = auxiliar;
					
					initCategoria();
					
				} else {
					
					nome = auxiliar;
					
					initCategoria();
				}
				
				// Opções de categoria
				if (comando.equals("/addCategoria")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							" Adicione um nome de categoria: "));
					
					initCategoria();
					
				} else if (comando.equals("/listarCategorias")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), categoria.listar()));
					
					initCategoria();
				
				} else if (comando.equals("/addDescricaoCategoria")) {
			
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Adicione uma descrição à categoria: "
							+ "Adicione um ponto final à sua descrição"));
					
					initCategoria();
					
				} else if (comando.equals("/salvarCategoria")){
					
					categoria.setNome(nome);
					categoria.setDescricao(descricao);
					categoria.inserir();
					
					initCategoria();
					
				} else if (comando.equals("/home")) {
					
					init();
				}
			}
		}
	}
}