package commandsSQL;

import java.sql.SQLException;

import interfaces.PostgreSQLCommands;

/**
 * Classe que faz as operações SQL das localizações
 * 
 * @author Danielvis
 * 
 * */
public class LocalizacaoSQL implements PostgreSQLCommands {
	
	/** Atributos de uma localização */
	private String nome;
	private String descricao;
	
	/** 
	 * Retorna nome da localizacao
	 * @return String nome
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Define nome da localização
	 * @param String nome 
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/** 
	 * Retorna descrição da localizacao
	 * @return String descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * Define descricao da localização
	 * @param String descricao 
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void inserir() {
		
		String sqlCommand = "INSERT INTO localizacao(nome, descricao) VALUES('"+nome+"','"+descricao+"');";
		bdConection.executeSQLCommand(sqlCommand);
		
	}

	@Override
	public String listar() {
		String sqlCommand = "SELECT * FROM localizacao";
		bdConection.executeSQLCommand(sqlCommand);
		
		String lista = imprimirBusca();
		
		return lista;
	}
	
	/** 
	 * Imprime resultado da busca
	 * @return String lista daslocalizações buscadas
	 * */
	private String imprimirBusca()
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
