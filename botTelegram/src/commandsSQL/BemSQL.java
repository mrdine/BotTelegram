package commandsSQL;

import java.sql.SQLException;

import interfaces.PostgreSQLCommands;
/**
 * Classe que faz as opera��es dos bens de acordo com as chamadas SQL
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
	 * Lista os bens de acordo com par�metros passados
	 */
	@Override
	public void listar() {
		String sqlCommand = "SELECT * FROM bem ORDER BY localizacao, categoria, nome";
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
		
	}
	
	public void listarPorLocalizacao(int localizacao)
	{
		String sqlCommand = "SELECT * FROM bem WHERE localizacao =" + localizacao;
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
	}
	public void listarPorLocalizacao(String localizacao)
	{
		String sqlCommand = "SELECT bem.codigo,bem.nome,bem.descricao,bem.localizacao,bem.categoria FROM bem, localizacao WHERE localizacao.nome ='" + localizacao+"' INTERSECT SELECT bem.codigo,bem.nome,bem.descricao,bem.localizacao,bem.categoria FROM bem";
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
		
		
	}
	
	public void buscarPorCodigo(int codigo)
	{
		String sqlCommand = "SELECT * FROM bem WHERE codigo =" + codigo;
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
	}
	
	public void buscarPorNome(String nome)
	{
		String sqlCommand = "SELECT * FROM bem WHERE nome ='" + nome +"'";
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
	}
	
	public void buscarPorDescricao(String descricao)
	{
		String sqlCommand = "SELECT * FROM bem WHERE descricao ='" + descricao + "'";
		bdConection.executeSQLCommand(sqlCommand);
		
		imprimirBusca();
	}
	
	public void movimentarBem(int bemID, int localizacao)
	{
		String sqlCommand = "UPDATE bem SET localizacao =" + localizacao +" WHERE codigo =" + bemID +";";
		bdConection.executeSQLCommand(sqlCommand);
	}
	
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

}
