package br.com.l7p.lexico;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
	
	private static final List<Token> tokens = new ArrayList<Token>();
	private static final List<Erros> erros = new ArrayList<Erros>();

	private String testeSeminario;

	/*
	 * PR -> Palavra Reservada
	 * DE -> Delimitador
	 * OPA -> Operador Aritimético
	 * OPC -> Operador comparativo
	 * OA -> Operador Atribuição
	 * 
	 * CLI -> Constante Literal Inteira
	 * CLR -> Constante Literal Real
	 * CLT -> Constante Literal Texto
	 */
	// Xandao top demais
	public static enum Simbolos{
		PR, DE, OPA, OPC, OA, CLI, CLR, CLT, ID, $
	}
	
	public static void analisar(String imagem, int linha, int coluna) {

		if(PalavrasReservadas.isPalavraReservada(imagem))
			tokens.add(new Token(imagem, Simbolos.PR.toString(), -1, linha, coluna));
		else if(PalavrasReservadas.isDelimitador(imagem))
			tokens.add(new Token(imagem, Simbolos.DE.toString(), -1, linha, coluna));
		else if(PalavrasReservadas.isOperadorAritimetico(imagem))
			tokens.add(new Token(imagem, Simbolos.OPA.toString(), -1, linha, coluna));
		else if(PalavrasReservadas.isOperadorComparativo(imagem))
			tokens.add(new Token(imagem, Simbolos.OPC.toString(), -1, linha, coluna));
		else if(PalavrasReservadas.isOperadorAtribuicao(imagem))
			tokens.add(new Token(imagem, Simbolos.OA.toString(), -1, linha, coluna));
		else if(imagem.matches("\\d\\d*"))
			tokens.add(new Token(imagem, Simbolos.CLI.toString(), -1, linha, coluna));
		else if(imagem.matches("\\d\\d*\\.\\d\\d*"))
			tokens.add(new Token(imagem, Simbolos.CLR.toString(), -1, linha, coluna));
		else if(imagem.matches("\\p{Alpha}\\p{Alnum}*"))
			tokens.add(new Token(imagem, Simbolos.ID.toString(), -1, linha, coluna));
		else erros.add(new Erros(imagem, linha, coluna));		
	}
		
	public static void adicionaConstanteLiteralTexto(String imagem, int linha, int coluna) {
		tokens.add(new Token(imagem, Simbolos.CLT.toString(), -1, linha, coluna));
	}
	
	public static void finalCadeia() {
		tokens.add(new Token("$", Simbolos.$.toString(), -1, -1, -1));
	}
	
	public static void printToken() {
		System.out.println("Tokens");
		for (Token tok: tokens) {
			System.out.println(tok +" - " + tok.getClasse());
		}
	}
	
	public static void printErros() {
		System.out.println("Erros");
		for (Erros err: erros) {
			System.out.println(err);
		}
	}
	
	
	public static ArrayList<Token> getTokens(){
		return (ArrayList<Token>) tokens;
	}
	
	public static ArrayList<Erros> getErros(){
		return (ArrayList<Erros>) erros;
	}
	
}
