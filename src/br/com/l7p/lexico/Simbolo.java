package br.com.l7p.lexico;

import java.util.ArrayList;

public class Simbolo {
	
	private String imagem;
	private String escopo;
	private String tipo;
	
	private String natureza; 
	
	ArrayList<Token> params = new ArrayList<Token>();
	
	public Simbolo(String imagem, String escopo) {
		this.imagem = imagem;
		this.escopo = escopo;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getEscopo() {
		return escopo;
	}

	public void setEscopo(String escopo) {
		this.escopo = escopo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public ArrayList<Token> getParams() {
		return params;
	}

	public void setParams(ArrayList<Token> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "Simbolo [imagem="+imagem+", escopo="+escopo+
				", tipo="+tipo+", natureza="+natureza+
				", params="+params+"]";
	}
	
	

}
