package app;

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

/**
 * Classe responsavel pela comunicação entre a aplicação e o Telegram
 * 
 * @author danielvis
 *
 */
public class BotComunicacao {

	//objeto responsável por receber as mensagens
	GetUpdatesResponse updatesResponse;
	//objeto responsável por gerenciar o envio de respostas
	SendResponse sendResponse;
	//objeto responsável por gerenciar o envio de ações do chat
	BaseResponse baseResponse;
	// lista de mnesagens
	List<Update> updates;
	
	//objeto bot com as informações de acesso
	TelegramBot bot;
	
	String menuMessage;
	boolean enviarMenuMessage;
	
	//estados
	private  int estado;
	private static final int neutro = 0;
	private static final int addBem = 1;
	private static final int addCategoria = 2;
	private static final int addLocalizacao = 3;
	
	private static final int listarBemPorLocalizacao = 4;
	private static final int listarBemPorCodigo = 5;
	private static final int listarBemPorNome = 6;
	private static final int listarBemPorDescricao = 7;
	private static final int movimentarBem = 8;
	
	private static final int listarCategorias = 9;
	private static final int listarLocalizacoes = 10;
	
	
	
	
	//variavel auxiliar para ajudar nos comandos sql
	private int contador;
	
	//objetos
	BemSQL bem;
	CategoriaSQL categoria;
	LocalizacaoSQL localizacao;
	
	/**
	 * Metodo construtor padrão
	 */
	public BotComunicacao()
	{
		//Criação do objeto bot com as informações de acesso
		bot = TelegramBotAdapter.build("1028381418:AAE1ixyIWE_mKGe3PA09uE0vE6YWi3waD_4");
		
		//enviar mesagem inicial
		menuMessage = " Menu Principal: "
				+ "\n /categoria "
				+ "\n /bem "
				+ "\n /localizacao"
				+ "\n /gerarRelatorio";
		
		enviarMenuMessage = true;
		
		estado = neutro;
		
		contador = 0;
		
		bem = new BemSQL();
		categoria = new CategoriaSQL();
		localizacao = new LocalizacaoSQL();
		
	}
	/**
	 * Iniciar conexão e chat com usuario no telegram
	 */
	public void init() {

		
		
		//controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
		int m=0;
		
		//loop infinito pode ser alterado por algum timer de intervalo curto
		while (true){
		
			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			updates = updatesResponse.updates();
			
			String messageRecebida = "";
			
			//análise de cada ação da mensagem
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				
				messageRecebida = update.message().text();
				System.out.println("Recebendo mensagem: "+ messageRecebida);
				
				switch(estado)
				{
				case neutro:
					
					break;
				case addBem:
					adicionarBem(messageRecebida);
					contador++;
					break;
				
				case listarBemPorLocalizacao:
					//envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),bem.listarPorLocalizacao(Integer.parseInt(messageRecebida))));
					estado = neutro;
					enviarMenuMessage = true;
					break;
				
				case listarBemPorCodigo:
					//envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),bem.buscarPorCodigo(Integer.parseInt(messageRecebida))));
					estado = neutro;
					enviarMenuMessage = true;
					break;
				
				case listarBemPorNome:
					//envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),bem.buscarPorNome(messageRecebida)));
					estado = neutro;
					enviarMenuMessage = true;
					break;
					
				case listarBemPorDescricao:
					//envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),bem.buscarPorDescricao(messageRecebida)));
					estado = neutro;
					enviarMenuMessage = true;
					break;
				
				case movimentarBem:
					movimentarBem(messageRecebida);
					contador++;
					break;
					
				case addCategoria:
					adicionarCategoria(messageRecebida);
					contador++;
					break;
					
				
				case addLocalizacao:
					adicionarLocalizacao(messageRecebida);
					contador++;
					break;
					
				
				}
				/////////////////////////////////
				//envio de "Escrevendo" antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				//verificação de ação de chat foi enviada com sucesso
				System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
				
				// enviar menu quando necessário
				if(enviarMenuMessage)
				{
					//envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),menuMessage));
					enviarMenuMessage = false;
				}
				
				
				verificaMessage(update, messageRecebida);
				
				//verificação de mensagem enviada com sucesso
				System.out.println("Mensagem Enviada?" +sendResponse.isOk());
				
			}

		}

	}
	/**
	 * Verifica o que o usuario digitou e realiza a açao solicitada
	 * @param update - responsavel por realizar ações para a mensagem
	 * @param message - mensagem recebida
	 */
	private void verificaMessage(Update update, String message)
	{
		if(message.equals("/menuPrincipal"))
		{
			enviarMenuMessage = true;
		}
		//////////////// bem
		else if(message.equals("/bem"))
		{
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"/addBem"
					+ "\n /gerarRelatorio"
					+ "\n /listarBensPorLocalizacao"
					+ "\n /listarBensPorCodigo"
					+ "\n /listarBensPorNome"
					+ "\n /listarBensPorDescricao"
					+ "\n /movimentarBem"
					+ "\n /menuPrincipal"));
		} 
		
		else if(message.equals("/addBem"))
		{
			contador = 0;
			estado = addBem;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Digite em ordem, um campo por mensagem: \nnome \ndescrição \nlocalização \ncategoria"));
			System.out.println("Digite em ordem, um campo por mensagem: \nnome \ndescrição \nlocalização \ncategoria");
		}
		else if(message.equals("/gerarRelatorio"))
		{
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), bem.listar()));
			
		}
		else if(message.equals("/listarBensPorLocalizacao"))
		{
			estado = listarBemPorLocalizacao;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite o codigo da localização"));
			
		}
		else if(message.equals("/listarBensPorCodigo"))
		{
			estado = listarBemPorCodigo;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite o codigo do bem"));
			
		}
		else if(message.equals("/listarBensPorNome"))
		{
			estado = listarBemPorNome;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite o nome do bem"));
			
		}
		else if(message.equals("/listarBensPorDescricao"))
		{
			estado = listarBemPorDescricao;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite a descrição do bem"));
			
		}
		else if(message.equals("/movimentarBem"))
		{
			contador = 0;
			estado = movimentarBem;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite, um campo por vez nesta ordem, o codigo do bem, e codigo da localização"));
			
		}
		
		/////////////////// categoria
		else if(message.equals("/categoria"))
		{
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"/addCategoria"
					+ "\n /listarCategorias" + "\n /menuPrincipal"));
		} 
		else if(message.equals("/addCategoria"))
		{
			contador = 0;
			estado = addCategoria;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite, um campo por vez nesta ordem, nome da categoria, e descrição"));

		}
		else if(message.contentEquals("/listarCategorias"))
		{
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),categoria.listar()));

		}
		/////////////////
		else if(message.equals("/localizacao"))
		{
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"/addLocalizacao"
					+ "\n /listarLocalizacoes"
					+ "\n /menuPrincipal"));
		} 
		else if(message.equals("/addLocalizacao"))
		{
			contador = 0;
			estado = addLocalizacao;
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "digite, um campo por vez nesta ordem, nome da localizacao, e descrição"));
			
		}
		else if(message.contentEquals("/listarLocalizacoes"))
		{
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),localizacao.listar()));
		}
		else
		{
			//sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Desculpa, não entendi, só entendo palavras dos menus"));

		}
	}
	/**
	 * Recebe parametros do usuario para inserção de bem
	 * @param message - mensagem recebida
	 */
	private void adicionarBem(String message)
	{
		switch(contador)
		{
		case 0:
			bem.setNome(message);
			break;
		case 1:
			bem.setDescricao(message);
			break;
		case 2:
			bem.setLocalizacao(Integer.parseInt(message));
			break;
		case 3:
			bem.setCategoria(Integer.parseInt(message));
			bem.inserir();
			break;
		case 4:
			contador = 0;
			estado = neutro;
			enviarMenuMessage = true;
			break;
		}
	}
	/**
	 * Recebe parametros do usuario para movimentação de bem
	 * @param message - mensagem recebida
	 */
	private void movimentarBem(String message)
	{
		switch(contador)
		{
		case 0:
			bem.setId(Integer.parseInt(message));
			break;
		case 1:
			bem.setLocalizacao(Integer.parseInt(message));
			estado = neutro;
			enviarMenuMessage = true;
			contador = 0;
		
			bem.movimentarBem(bem.getId(), bem.getLocalizacao());
			break;
		
		}
	}
	/**
	 * Recebe parametros do usuario para inserção de categoria
	 * @param message - mensagem recebida
	 */
	private void adicionarCategoria(String message)
	{
		switch(contador)
		{
		case 0:
			categoria.setNome(message);
			break;
		case 1:
			categoria.setDescricao(message);
			estado = neutro;
			enviarMenuMessage = true;
			contador = 0;
			categoria.inserir();
			break;
		}
	}
	/**
	 * Recebe parametros do usuario para inserção de localizacao
	 * @param message - mensagem recebida
	 */
	private void adicionarLocalizacao(String message)
	{
		switch(contador)
		{
		case 0:
			localizacao.setNome(message);
			break;
		case 1:
			localizacao.setDescricao(message);
			estado = neutro;
			enviarMenuMessage = true;
			contador = 0;
			localizacao.inserir();
			break;
		}
	}
	
}