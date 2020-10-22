package br.com.l7p.lexico;

import java.util.Arrays;
import java.util.List;

public class PalavrasReservadas {
	
	private static final List<String> pr = initPalavraReservada();
	private static final List<String> opa = initOperadorAritimetico();
	private static final List<String> de = initDelimitador();
	private static final List<String> opc = initOperadorComparativo();
	private static final List<String> oa = initOperadorAtribuicao();
	
	
	private static List<String> initPalavraReservada(){
		return Arrays.asList("faz",
							"inteiro",
							"realistico",
							"prosa",
							"vacuo",
							"seEsseTrem",
							"cascontrario",
							"no",
							"volta",
							"bota",
							"amostra"
							);
	}
	
	private static List<String> initDelimitador(){
		return Arrays.asList("(", ")", "{", "}", ",", ";", ":");
	}
	
	private static List<String> initOperadorAritimetico(){
		return Arrays.asList("+", "-", "*", "/", "%");
	}
	
	
	private static List<String> initOperadorComparativo(){
		return Arrays.asList("<","<=", ">", ">=", "==", "!=");
	}
	
	private static List<String> initOperadorAtribuicao(){
		return Arrays.asList("=", "+=","-=", "*=", "/=");
	}

	public static boolean isPalavraReservada(String imagem) {
		return pr.contains(imagem);
	}
	
	public static boolean isDelimitador(String imagem) {
		return de.contains(imagem);
	}
	
	public static boolean isOperadorAritimetico(String imagem) {
		return opa.contains(imagem);
	}
	
	public static boolean isOperadorComparativo(String imagem) {
		return opc.contains(imagem);
	}
	
	public static boolean isOperadorAtribuicao(String imagem) {
		return oa.contains(imagem);
	}
	
}
