package commandsSQL;

import java.sql.SQLException;

import interfaces.PostgreSQLCommands;
/**
 * Classe que faz as operações SQL dos bens
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
	private String lista;
	
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
	 * Retorna a descri��o do bem
	 * @return String descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Adiciona a descri��o do bem
	 * @param descricao New descri��o
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Retorna a localiza��o do bem
	 * @return int localiza��oo
	 */
	public int getLocalizacao() {
		return localizacao;
	}
	
	/**
	 * Adiciona a localiza��o do bem
	 * @param localizacao New localiza��o
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
		bdConection.executeSQLCommand(sqlCommand);
		
	}

	/**
	 * Gera o relatorio, listando os bens ordenados por localizacao, categoria e nome
	 * @return String lista de bens ordenados 
	 */
	@Override
	public String listar() {
		String sqlCommand = "SELECT * FROM bem ORDER BY localizacao, categoria, nome";
		bdConection.executeSQLCommand(sqlCommand);
		
		lista = imprimirBusca();
		
		return lista;
	}
	
	/**
	 * Lista os bens de uma localizacao
	 * @param int codigo da localizacao
	 * @return String lista de bens
	 */
	public String listarPorLocalizacao(int localizacao)
	{
		String sqlCommand = "SELECT * FROM bem WHERE localizacao =" + localizacao;
		bdConection.executeSQLCommand(sqlCommand);
		
		lista = imprimirBusca();
		return lista;
		
	}
	
	/**
	 * Lista os bens de uma localizacao
	 * @param String nome da localizacao
	 * @return String lista de bens
	 */
	public void listarPorLocalizacao(String localizacao)
	{
		String sqlCommand = "SELECT bem.codigo,bem.nome,bem.descricao,bem.localizacao,bem.categoria FROM bem, localizacao WHERE localizacao.nome ='" + localizacao+"' INTERSECT SELECT bem.codigo,bem.nome,bem.descricao,bem.localizacao,bem.categoria FROM bem";
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
		
	}
	
	/**
	 * Buscar um bem especifico
	 * @param int codigo do bem
	 * @return String linha com as informaçoes do bem
	 */
	public String buscarPorCodigo(int codigo)
	{
		String sqlCommand = "SELECT * FROM bem WHERE codigo =" + codigo;
		bdConection.executeSQLCommand(sqlCommand);
		
		lista = imprimirBuscaporCodigo();
		
		return lista;
	}
	
	/**
	 * Buscar bens pelo nome
	 * @param String nome do bem
	 * @return String lista dos bens
	 */
	public String buscarPorNome(String nome)
	{
		String sqlCommand = "SELECT * FROM bem WHERE nome ='" + nome +"'";
		bdConection.executeSQLCommand(sqlCommand);
		
		lista = imprimirBusca();
		return lista;
	}
	
	/**
	 * Buscar bens pela descricao
	 * @param String descricao do bem
	 * @return String lista dos bens
	 */
	public String buscarPorDescricao(String descricao)
	{
		String sqlCommand = "SELECT * FROM bem WHERE descricao ='" + descricao + "'";
		bdConection.executeSQLCommand(sqlCommand);
		
		lista = imprimirBusca();
		return lista;
	}
	
	/**
	 * Movimenta um bem para outra localizacao
	 * @param int codigo do bem
	 * @param int codigo da localizacao
	 */
	public void movimentarBem(int bemID, int localizacao)
	{
		String sqlCommand = "UPDATE bem SET localizacao =" + localizacao +" WHERE codigo =" + bemID +";";
		bdConection.executeSQLCommand(sqlCommand);
	}
	
	/**
	 * Imprimir resultados da busca
	 * @return String lista dos bens
	 */
	private String imprimirBusca()
	{
		String lista = "Código  / Nome   / Descrição  / Localização  / Categoria \n";
        try {
			while (bdConection.rs.next()) {
				lista = lista + bdConection.rs.getString(1) + "      " + bdConection.rs.getString(2) + "   " + bdConection.rs.getString(3) + "    " + bdConection.rs.getString(4) + "         " + bdConection.rs.getString(5) + "   \n" ;
			}
		System.out.println(lista);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return lista;
	}
	
	/**
	 * Imprimir resultados da busca
	 * @return String lista dos bens
	 */
	private String imprimirBuscaporCodigo() {
		
		String lista = "Código  / Nome   / Localização  \n";
        try {
			while (bdConection.rs.next()) {
				lista = lista + bdConection.rs.getString(1) + "      " + bdConection.rs.getString(2) + "   " + bdConection.rs.getString(4) + "   \n" ;
			}
		System.out.println(lista);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return lista;
	}

}
