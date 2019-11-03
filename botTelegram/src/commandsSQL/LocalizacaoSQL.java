package commandsSQL;

import java.sql.SQLException;

import interfaces.PostgreSQLCommands;

public class LocalizacaoSQL implements PostgreSQLCommands {
	
	private String nome;
	private String descricao;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

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
	
	private String imprimirBusca()
	{
		String lista = "Código  / Nome   / Descrição   \n";
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
