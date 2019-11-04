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

	//controle de off-set, isto �, a partir deste ID ser� lido as mensagens pendentes na fila
	private int m=0;
	
	private TelegramBot bot = TelegramBotAdapter.build("1028381418:AAE1ixyIWE_mKGe3PA09uE0vE6YWi3waD_4");

	//objeto respons�vel por receber as mensagens
	private GetUpdatesResponse updatesResponse;
	
	//objeto respons�vel por gerenciar o envio de respostas
	private SendResponse sendResponse;
	
	//objeto respons�vel por gerenciar o envio de a��es do chat
	private BaseResponse baseResponse;
	
	private CategoriaSQL categoria;
	private LocalizacaoSQL localizacao;
	private BemSQL bem;
	
	private String nome;
	private String descricao;
	private int localizacaoBem;
	private int categoriaBem;
	private int codigoBem;
	private int isTrueBemPorLocalizacao = 0;
	private int isTrueBemPorCodigo = 0;
	private int isTrueBemPorDescricao = 0;
	
	public void init() {
		
		List<Update> updates;
		
		/** loop infinito pode ser alterado por algum timer de intervalo curto */
		while (true){
			
			//System.out.println("Executando");
			
			// executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial) 
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			//lista de mensagens
			updates = updatesResponse.updates();
			
			//updates.add(up);
			
			//an�lise de cada a��o da mensagem
			for (Update update : updates) {
				
				//atualiza��o do off-set
				m = update.updateId()+1;
				
				// imprime op��o para /home toda vez que o usu�rio termina uma a��o no bot ou interage pela primeira vez com o bot
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
							+ "\n /localizacao"
							+ "\n /gerarRelatorio"
							));
					
					System.out.println("Segundo estou executando esse comando:" + update.message().text());
					
					
				// Chama fun��es para fazer os procedimentos
				} else if (comando.contentEquals("/categoria")) {
					
					initCategoria();
					
				} else if (comando.contentEquals("/bem")) {
					
					initBem();
					
				} else if (comando.contentEquals("/localizacao")) {
					
					initLocalizacao();
				} else if(comando.contentEquals("/gerarRelatorio")) {
					
					gerarRelatorio();
				}
				
				init();	
			}
		}
	}

	private void gerarRelatorio() {

		List<Update> relatorio;
		bem = new BemSQL();
		categoria = new CategoriaSQL();
		localizacao = new LocalizacaoSQL();
		
		while(true) {
			
			updatesResponse =  bot.execute(new GetUpdates().limit(1).offset(m));
			
			relatorio = updatesResponse.updates();
			
			for (Update update: relatorio){
				
				m = update.updateId()+1;
				
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
						"Relat�rio geral do sistema: \n"
						+ "Lista de bens cadastrados: "
						+ bem.listar()));
			}
			 
		}
		
	}

	/** 
	 * faz comandos e chama m�todos de bemSQL
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
						+ "\n /listarBensPorLocalizacao"
						+ "\n /listarBensPorCodigo"
						+ "\n /listarBensPorNome"
						+ "\n /listarBensPorDescricao"
						+ "\n /movimentarBem"
						+ "\n /ok para confirmar escolhas"
						+ "\n /home para retornar ao menu"));
	
				System.out.println("Recebendo mensagem: "+ update.message().text());
				
				String auxiliar = update.message().text();
				
				String comando = null;
				
				if (auxiliar.contains("/")){
					
					comando =  update.message().text();
					
				} else if(auxiliar.contains("@")) {
					
					auxiliar = auxiliar.replace("@","");
					
					codigoBem = Integer.parseInt(auxiliar);
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Para onde voc� quer mover o bem? \n"
							+ "OBS: Digitar '%' ao final do novo local!\n"
							+ localizacao.listar()));
				initBem();
				
				} else if(auxiliar.contains("%")) {
					
					auxiliar = auxiliar.replace("%","");
					
					int localDepois = Integer.parseInt(auxiliar);
					
					bem.movimentarBem(codigoBem, localDepois);
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"O bem j� est� no novo local: \n"
							+ bem.buscarPorCodigo(codigoBem)));
				
				initBem();
					
				} else if(auxiliar.contains(".")) {
					
					descricao = auxiliar;
					
					if (isTrueBemPorDescricao > 0) {
						
						baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
						
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
								"O bem procurado est� listado (ou n�o) abaixo: \n"
								+ bem.buscarPorDescricao(auxiliar)));
						
						isTrueBemPorDescricao = 0;
						initBem();
					}
					
				initBem();
					
				} else if (auxiliar.contains("+")) {
					
					auxiliar = auxiliar.replace("+","");
					
					localizacaoBem = Integer.parseInt(auxiliar);
					
					if (isTrueBemPorLocalizacao > 0) {
						
						System.out.println(localizacaoBem);
						
						baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
						
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
								"Os bens desse local est�o listados abaixo: \n"
								+ bem.listarPorLocalizacao(localizacaoBem)));
						isTrueBemPorLocalizacao = 0;
						initBem();
					}
					
							
					initBem();
				
				} else if (auxiliar.contains("*")) {
					
					auxiliar = auxiliar.replace("*","");
					int codigo = Integer.parseInt(auxiliar);
					
					if (isTrueBemPorCodigo > 0) {
						
						System.out.println(codigo);
						
						baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
						
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
								"O bem procurado est� listado (ou n�o) abaixo: \n"
								+ bem.buscarPorCodigo(codigo)));
						
						isTrueBemPorCodigo = 0;
						initBem();
					}
					
					initBem();
				} else if (auxiliar.contains("_")) {
					
					auxiliar = auxiliar.replace("_","");
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"O bem procurado est� listado (ou n�o) abaixo: \n"
							+ bem.buscarPorNome(auxiliar)));
					initBem();
					
				} else if (auxiliar.contains("-")) {
					
					auxiliar = auxiliar.replace("-","");
					categoriaBem = Integer.parseInt(auxiliar);
							
					initBem();
					
				} else {
					
					nome = auxiliar;
					
					initBem();
				}
				
				// Op��es de categoria
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
							"Digite o c�digo da categoria do bem cadastrado: \n"
							+ "OBS: Digitar '-' ao final do n�mero!\n"
							+ categoria.listar()
							));
					
					initBem();
				
				} else if (comando.equals("/addLocalizacaoBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite o c�digo da localiza��oo do bem cadastrado: \n"
							+ "OBS: Digitar '+' ao final do n�mero!" 
							+ localizacao.listar()
									));
					
					initBem();
					
				} else if (comando.equals("/addDescricaoBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Adicione uma descri��o ao bem: \n"
							+ "OBS: Digitar '.' ao final de sua descri��o"));
					
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
				} else if (comando.equals("/listarBensPorLocalizacao")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Voc� quer listar os bens de qual local? \n"
							+ "OBS: Digitar '+' ao final do n�mero!"
							+ localizacao.listar()));
					isTrueBemPorLocalizacao++;
					initBem();
					
				} else if (comando.equals("/listarBensPorCodigo")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite o c�digo do bem que voc� quer buscar: \n"
							+ "OBS: Digitar '*' ao final do n�mero!"
							));
					isTrueBemPorCodigo++;
					initBem();
					
				} else if (comando.equals("/listarBensPorNome")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite o nome do bem que voc� quer buscar: \n"
							+ "OBS: Digitar '_' ao final do nome!"
							));
					initBem();
					
				} else if (comando.equals("/listarBensPorDescricao")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descri��o do bem que voc� quer buscar: \n"
							+ "OBS: Digitar '.' ao final da descri��o!"
							));
					isTrueBemPorDescricao++;
					initBem();
					
				}  else if (comando.equals("/movimentarBem")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite o c�digo do bem que voc� quer mover: \n"
							+ "OBS: Digitar '@' ao final da descri��o!"
							));
					isTrueBemPorDescricao++;
					initBem();
				
				}
			}
		}	
	}


	/** 
	 * faz comandos e chama m�todos de localizacaoSQL
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
						"Menu Localiza��o: "
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
				
				// Op��es de categoria
				if (comando.equals("/addLocalizacao")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							" Adicione um nome de Localiza��o: "));
					
					initLocalizacao();
					
				} else if (comando.equals("/listarLocalizacoes")) {
					
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), localizacao.listar()));
					
					initLocalizacao();
				
				} else if (comando.equals("/addDescricaoLocalizacao")) {
			
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), 
							"Adicione uma descri��o � localiza��o: "
							+ "Adicione um ponto final � sua descri��o"));
					
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
	 * faz comandos e chama m�todos de categoriaSQL
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
				
				// Op��es de categoria
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
							"Adicione uma descri��o � categoria: "
							+ "Adicione um ponto final � sua descri��o"));
					
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