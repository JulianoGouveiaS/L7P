package br.com.l7p.lexico;

public class Erros {

	String imagem;
	int linha;
	int coluna;

	public Erros(String imagem, int linha, int coluna) {
		super();
		this.imagem = imagem;
		this.linha = linha;
		this.coluna = coluna;
	}

	@Override
	public String toString() {
		return "Erros [imagem= " + imagem + ", linha= " + linha + 
				" coluna= "+coluna+"]";
	
	}

}
