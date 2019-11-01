package commandsSQL;

import interfaces.PostgreSQLCommands;
/**
 * Classe que faz as operações dos bens de acordo com as chamadas SQL
 * 
 * @author Danielvis
 * 
 * */
public class BemSQL implements PostgreSQLCommands {
	
	/**
	 * Atributos de um bem
	 * */
	private String nome;
	private String descricao;
	private int localizacao;
	private int categoria;
	
	/**
	 * Retorna o nome do bem
	 * @param void
	 * @return String nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Adiciona o nome do bem
	 * @param nome New nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Retorna a descrição do bem
	 * @return String descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Adiciona a descrição do bem
	 * @param descricao New descrição
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Retorna a localização do bem
	 * @return int localizaçãoo
	 */
	public int getLocalizacao() {
		return localizacao;
	}
	
	/**
	 * Adiciona a localização do bem
	 * @param localizacao New localização
	 */

	public void setLocalizacao(int localizacao) {
		this.localizacao = localizacao;
	}

	/**
	 * Retorna a categoria do bem
	 * @return int categoria
	 */
	public int getCategoria() {
		return categoria;
	}

	/**
	 * Adiciona a categoria do bem
	 * @param categoria New categoria
	 */
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}
	/**
	 * Insere um novo bem
	 */
	@Override
	public void inserir() {
		
		String sqlCommand = "INSERT INTO bem(nome,descricao,localizacao,categoria) VALUES('"+nome+"','"+descricao+"',"+localizacao+","+categoria+");";
		
	}

	/**
	 * Lista os bens de acordo com parâmetros passados
	 */
	@Override
	public void listar() {
		// TODO Auto-generated method stub
		
	}

}
