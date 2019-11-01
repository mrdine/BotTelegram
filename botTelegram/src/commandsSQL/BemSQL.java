package commandsSQL;

import interfaces.PostgreSQLCommands;

public class BemSQL implements PostgreSQLCommands {
	
	private String nome;
	private String descricao;
	private int localizacao;
	private int categoria;
	
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

	public int getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(int localizacao) {
		this.localizacao = localizacao;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	@Override
	public void inserir() {
		
		String sqlCommand = "INSERT INTO bem(nome,descricao,localizacao,categoria) VALUES('"+nome+"','"+descricao+"',"+localizacao+","+categoria+");";
		
	}

	@Override
	public void listar() {
		// TODO Auto-generated method stub
		
	}

}
