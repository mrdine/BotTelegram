package commandsSQL;

import java.sql.SQLException;

import interfaces.PostgreSQLCommands;

/**
 * Classe que faz as operações SQL das categorias
 * 
 * @author Danielvis
 * 
 * */
public class CategoriaSQL implements PostgreSQLCommands {
	
	/** atributos de uma categoria */
	private String nome;
	private String descricao;
	
	/**
	 * Retornar nome da categoria
	 * @return String nome
	 * */
	public String getNome() {
		return nome;
	}
	/**
	 * Define nome da categoria 
	 * @param String nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * Retorna descrição da categoria 
	 * @return String descrição
	 */
	public String getDescricao() {
		return descricao;
	}
	
	/**
	 * Definedescrição da categoria 
	 * @param String descrição
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	@Override
	public void inserir() {
		String sqlCommand = "INSERT INTO categoria(nome, descricao) VALUES('"+nome+"','"+descricao+"');";
		bdConection.executeSQLCommand(sqlCommand);
		
	}
	@Override
	public String listar() {
		String sqlCommand = "SELECT * FROM categoria";
		bdConection.executeSQLCommand(sqlCommand);
		
		String lista = imprimirBusca();
		
		return lista;
		
	}
	/** 
	 * Imprime resultado da busca
	 * @return String lista das categorias buscadas
	 * */
	protected String imprimirBusca()
	{
		String lista = "C�digo  / Nome   / Descri��o   \n";
        try {
        	
			while (bdConection.rs.next()) {
				lista = lista + bdConection.rs.getString(1) + "      " + bdConection.rs.getString(2) + "      " + bdConection.rs.getString(3) + "   \n" ;
				
			}
		
		System.out.println(lista);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return lista;
	}
	
}
