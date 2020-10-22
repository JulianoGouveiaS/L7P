package br.com.l7p.lexico;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClassificadorPalavras {

	
	/*
	 *  01234567890
	 *0 def minha_funcao(var1:texto):nada{
	 *1 	instrucoes pla pla pla;
	 *2 }
	 *
	 * "minha variavel de texto"
	 */
	public static int variavelAnterior;
	
	public static ArrayList<Token> classifica(BufferedReader texto){
		
		String linha = null;
	
		try {
			int nrLinha = -1;
			
			while((linha = texto.readLine())!= null) {
				
				linha = linha.replaceAll("("
						+ "^\\p{Alpha}func^\\p{Alpha}|^\\p{Alpha}inteiro^\\p{Alpha}|^\\p{Alpha}realistico^\\p{Alpha}|"
						+ "^\\p{Alpha}prosa^\\p{Alpha}|^\\p{Alpha}vacuo^\\p{Alpha}|^\\p{Alpha}cascontrario^\\p{Alpha}|"
						+ "^\\p{Alpha}se^\\p{Alpha}|^\\p{Alpha}no^\\p{Alpha}|^\\p{Alpha}volta^\\p{Alpha}|"
						+ "^\\p{Alpha}bota^\\p{Alpha}|^\\p{Alpha}amostra^\\p{Alpha}|"
						+ "\\(|\\)|\\{|\\}|\\,|\\;|\\:|"
						+ "\\+|\\-|\\*|\\/|\\%|"
						+ "\\<=|\\>=|\\==|\\!=|\\<|\\>|"
						+ "\\+=|\\-=|\\*=|\\/=|\\\"|\\="
						+ ")", " $1 ");
				linha = linha.replace("  ", " ");

				String aux = linha;
				int coluna = 0;
				variavelAnterior = 0;
				
				StringTokenizer st = new StringTokenizer(linha);
				
				while (st.hasMoreTokens()) {
					String imagem = st.nextToken();
					
					if(imagem.startsWith("#")) {
						break;
					}
					
					
					if(!imagem.startsWith("\"")){
						coluna = posicaoColuna(linha, imagem);
						linha = linha.substring(linha.indexOf(imagem) + imagem.length());
						AnalisadorLexico.analisar(imagem, nrLinha, coluna);
						
					}else{
						coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
						linha = linha.substring(linha.indexOf(imagem) + imagem.length());
						
						if(!imagem.endsWith("\"") || imagem.length() == 1){
							int init = variavelAnterior - imagem.length() + 1;
							imagem = st.nextToken();
							coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
							linha = linha.substring(linha.indexOf(imagem) + imagem.length());
							
							while(!imagem.endsWith("\"")) {
								imagem = st.nextToken();
								coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
								linha = linha.substring(linha.indexOf(imagem) + imagem.length());
							}
							
							AnalisadorLexico.adicionaConstanteLiteralTexto(aux.substring(init, variavelAnterior-1), nrLinha , coluna);
						}else {
							AnalisadorLexico.adicionaConstanteLiteralTexto(aux.substring(coluna, variavelAnterior-1), nrLinha , coluna);
						}
						
					}
					
				}
				nrLinha++;
				
				
			}
			
			
			AnalisadorLexico.finalCadeia();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static int posicaoColuna(String linha, String token) {
		variavelAnterior += linha.indexOf(token) + token.length();
		return variavelAnterior;
	}
	
}
