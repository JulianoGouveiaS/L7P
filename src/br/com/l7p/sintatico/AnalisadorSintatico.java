package br.com.l7p.sintatico;

import java.util.ArrayList;

import br.com.l7p.lexico.TabelaSimbolos;
import br.com.l7p.lexico.Token;

public class AnalisadorSintatico {

	private Token token;
	private int pToken;
	private static ArrayList<Token> tokens = new ArrayList<Token>();
	private static ArrayList<String> erros = new ArrayList<String>();
	
	private String escopo = "";
	
	public AnalisadorSintatico(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}
	
	public void leToken() {
		if(token != null && token.getClasse().equals("ID")){
			TabelaSimbolos.addSimbolo(token, escopo);
		}
		token = tokens.get(pToken);
		pToken++;
	}
	
	public Token lookHead() {
		return tokens.get(pToken);
	}
	
	private Token lastToken() {
		return tokens.get(pToken-2);
	}
	
	public void analisar() {
		pToken = 0;
		leToken();
		ListDef();
		if (!token.getClasse().equals("$")) {
			erros.add("Erro: esperado um final de cadeia.");
		}
	}
	
	public static ArrayList<Token> getTokens(){
		return tokens;
	}
	
	public static ArrayList<String> getErros(){
		return erros;
	}
	
	public static void printTokens() {
		System.out.println("Impressão Tokens");
		for(Token token : tokens) {
			System.out.println(token);
		}
	}
	
	public static void printErroTokens() {
		System.out.println("Impressão Erros:");
		for(String erro : erros) {
			System.out.println(erro);
		}
	}
	
	
	// "<ListDef> ::= <Def><ListDef> |"
	private void ListDef() {
		if (token.getImagem().equals("faz")) {
			Def();
			ListDef();
		}
		
	}
	
	// <Def> ::= ‘def’  id ‘(‘ <ListParam> ‘)’  ‘:’  <Tipo> ‘{‘ <ListComan> ‘}’
	private void Def() {
		if (token.getImagem().equals("faz")) {
			leToken();
			if (token.getClasse().equals("ID")) {
				escopo = token.getImagem();
				leToken();
				if (token.getImagem().equals("(")) {
					leToken();
					ListParam();
					if (token.getImagem().equals(")")) {
						leToken();
						if(token.getImagem().equals(":")) {
							leToken();
							Tipo();
							if(token.getImagem().equals("{")) {
								leToken();
								ListComan();
								if(token.getImagem().equals("}")) {
									leToken();
								}else{
									Token lastToken = lastToken();
									erros.add("Erro: esperado um '}'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
								}
							}else {
								Token lastToken = lastToken();
								erros.add("Erro: esperado um '{'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
							}
						}else {
							Token lastToken = lastToken();
							erros.add("Erro: esperado um ':'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());

						}
					}else {
						Token lastToken = lastToken();
						erros.add("Erro: esperado um ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
					}
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado um '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado um 'identificador'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o 'def'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	



	/*
	 * <ListComan> ::=  | <Coman><ListComan>
	 * 
	 */
	private void ListComan() {
		if(token.getClasse().equals("ID") || token.getImagem().equals("seEsseTrem")
				|| token.getImagem().equals("no") || token.getImagem().equals("bota")
				|| token.getImagem().equals("amostra") || token.getImagem().equals("volta")
				|| token.getImagem().equals("{")
				) {
		Coman();
		ListComan();
		}
	}

	/*
	 * <Coman> ::= <Decl>
			| <Atrib>
			| <Se>
			| <Laco>
			| <Ret>
			| <Entrada>
			| <Saida>
			| <Chamada> ‘;’
			| ‘{’ <ListComan> ‘}’

	 */
	
	private void Coman() {
		
		if (token.getClasse().equals("ID")) {
			Token lookHead = lookHead();
			// Decl | Atrib | Chamada
			if (lookHead.getImagem().equals("=")) {
				Atrib();				
			}else if(lookHead.getImagem().equals("(")) {
				Chamada();
				if(token.getImagem().equals(";")) {
					leToken();
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Decl();
			}
		}else if (token.getImagem().equals("volta")) {
			Ret();
		}else if (token.getImagem().equals("seEsseTrem")) {
			Se();
		}else if (token.getImagem().equals("equanto")) {
			Laco();
		}else if (token.getImagem().equals("bota")) {
			Entrada();
		}else if (token.getImagem().equals("amostra")) {
			Saida();
		}else if (token.getImagem().equals("{")) {
			leToken();
			ListComan();
			if (token.getImagem().equals("}")) {
				leToken();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o '}'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}
		else {
			erros.add("Erro: esperado o 'id'|'retorna'|'se'|'equanto'|'leia'|'escreva'. Linha: " + token.getLinha() + "Coluna: " + token.getColuna());
		}
	}

	//<Saida> ::= ‘escreva’ ‘(‘ <Operando> ‘)’ ‘;’ 
	private void Saida() {
		if(token.getImagem().equals("amostra")) {
			leToken();
			if(token.getImagem().equals("(")) {
				leToken();
				Operando();
				if(token.getImagem().equals(")")) {
					leToken();
					if(token.getImagem().equals(";")) {
						leToken();
					}else { 
						Token lastToken = lastToken();
						erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
					}
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado o ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado a palavra reservada 'escreva'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	/*
	 * <Operando> ::= id 
			| cli
			| clr
			| clt
	 */
	private void Operando() {
		if(token.getClasse().equals("ID")) {
			leToken();
		}else if(token.getClasse().equals("CLI")) {
			leToken();
		}else if(token.getClasse().equals("CLR")) {
			leToken();
		}else if(token.getClasse().equals("CLT")) {
			leToken();
		}
		
	}

	//<Entrada> ::= ‘leia’ ‘(‘ id ‘)’ ‘;’ 
	private void Entrada() {
		if(token.getImagem().equals("bota")) {
			leToken();
			if(token.getImagem().equals("(")) {
				leToken();
				if(token.getClasse().equals("ID")) {
					leToken();
					if(token.getImagem().equals(")")) {
						leToken();
						if(token.getImagem().equals(";")) {
							leToken();
						}else {
							Token lastToken = lastToken();
							erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
						}
					}else {
						Token lastToken = lastToken();
						erros.add("Erro: esperado o ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
					}
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado um 'identificador'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado uma palavra reservada 'leia'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}
	


	//<Laco> ::= ‘enquanto’  ‘(‘  <ExpRel> ’)’ <Coman>
	private void Laco() {
		if(token.getImagem().equals("no")) {
			leToken();
			if(token.getImagem().equals("(")) {
				leToken();
				ExpRel();
				if(token.getImagem().equals(")")) {
					leToken();
					Coman();
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado o ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado a palavra reservada 'equanto'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	//<ExpRel> ::= <ExpArit> <Op3> <ExprArit>
	private void ExpRel() {
		ExpArit();
		Op3();
		ExpArit();	
	}

	/*
	 * <Op3> ::= ‘>’
		| ‘>=‘
		| ‘<‘
		| ‘<=‘
		| ‘==‘
		| ‘!=‘
	 */
	
	private void Op3() {
		if(token.getImagem().equals(">"))
			leToken();
		else if(token.getImagem().equals(">="))
			leToken();
		else if(token.getImagem().equals("<"))
			leToken();
		else if(token.getImagem().equals("<="))
			leToken();
		else if(token.getImagem().equals("=="))
			leToken();
		else if(token.getImagem().equals("!="))
			leToken();
		else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado um dos operadores a seguir: '>' ou '>=' ou '<' ou '<=' ou '==' ou '!=' . Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
	}

	//<ExpArit> ::= <Termo><ExpArit2>
	private void ExpArit() {
		Termo();
		ExpArit2();
		
	}

	//<ExpArit2> ::=  | <Op1> <ExpArit>
	private void ExpArit2() {
		if(token.getClasse().equals("OPA")) {
			Op1();
			ExpArit();
		}
		
	}

	//<Op1> ::= ‘+’
	//		  | ‘-‘

	private void Op1() {
		if(token.getImagem().equals("+")) {
			leToken();
		}else if(token.getImagem().equals("-")) {
			leToken();
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o '+' ou '-'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
	}

	//<Termo> ::= <Fator><Termo2>
	private void Termo() {
		Fator();
		Termo2();
		
	}
	
	//<Termo2> ::= | <Op2> <Termo>
	private void Termo2() {
		if(token.getClasse().equals("OPA")) {
			Op2();
			Termo();
		}
		
	}

	//<Op2> ::= ‘*’
	//        | ‘/‘
	private void Op2() {
		if(token.getImagem().equals("*")) {
			leToken();
		}else if(token.getImagem().equals("/")) {
			leToken();
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o '*' ou '/'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
	}

	//<Fator> ::= <Operando> | <Chamada> | ‘(‘ <ExpArit> ‘)’
	private void Fator() {
		if(token.getClasse().equals("ID")) {
			Token lookHead = lookHead();
			if(lookHead.getImagem().equals("(")) {
				Chamada();
			}else {
				Operando();
			}
		}else if(token.getClasse().equals("CLI") || token.getClasse().equals("CLR") || token.getClasse().equals("CLT")){
			Operando();
		}else if(token.getImagem().equals("(")) {
			leToken();
			ExpArit();
			if(token.getImagem().equals(")")) {
				leToken();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	//<Se> ::= ‘se’ ‘(‘ <ExpRel> ‘)’ <Coman> <Senao>
	private void Se() {
		if(token.getImagem().equals("seEsseTrem")) {
			leToken();
			if(token.getImagem().equals("(")) {
				leToken();
				ExpRel();
				if(token.getImagem().equals(")")) {
					leToken();
					Coman();
					Senao();
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado o ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado a palavra reservada 'se'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}
	
	//<Senao> ::= ‘senao’ <Coman> | 
	private void Senao() {
		if(token.getImagem().equals("cascontrario")) {
			leToken();
			Coman();
		}
		
	}

	//<Ret> ::= ‘retorna’ <Fator> ‘;’
	private void Ret() {
		if(token.getImagem().equals("volta")) {
			leToken();
			Fator();
			if(token.getImagem().equals(";")) {
				leToken();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado a palavra reservada 'retorno'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	/* <Decl> ::= <ListId> ‘:’ <Tipo> ‘;’
	 * 
	 */
	private void Decl() {
		ListId();
		
		if (token.getImagem().equals(":")){
			leToken();
			Tipo();
			if(token.getImagem().equals(";")) {
				leToken();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
			
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o ':'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
	}

	//<ListId> ::= id <ListId2>
	private void ListId() {
		leToken();
		ListId2();
	}
	
	

	//<ListId2> ::= ‘,’ id <ListId2> | 
	private void ListId2() {
		if(token.getImagem().equals(",")) {
			leToken();
			if (token.getClasse().equals("ID")) {
				leToken();
				ListId2();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado um 'identificador'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}
		
	}

	//<Chamada> ::= id ‘(‘ <ListArg> ‘)’ ‘;’
	private void Chamada() {
		leToken();
		if(token.getImagem().equals("(")) {
			leToken();
			ListArg();
			if(token.getImagem().equals(")")) {
				leToken();
				if(token.getImagem().equals(";")) {
					leToken();
				}else {
					Token lastToken = lastToken();
					erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
				}
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o ')'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o '('. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	//<ListArg> ::=  | <Operando> <ListArg2>
	private void ListArg() {
		if(token.getClasse().equals("ID")) {
			Operando();
			ListArg2();
		}
		
	}

	//<ListArg2> ::=  | ‘,’ <Operando><ListArg2> 
	private void ListArg2() {
		if(token.getImagem().equals(",")) {
			leToken();
			Operando();
			ListArg2();
		}
	}

	//<Atrib> ::= id ‘=‘ <ExpArit> ‘;’
	private void Atrib() {
		leToken();
		if(token.getImagem().equals("=")) {
			leToken();
			ExpArit();
			if(token.getImagem().equals(";")) {
				leToken();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o ';'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado o '='. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
		
	}

	/*
	 * <Tipo> ::= ‘inteiro’
		| ‘real’
		| ‘texto’
		| ‘nada’
		| 
	 */
	private void Tipo() {
		if(token.getClasse().equals("PR")) {
			if(token.getClasse().equals("inteiro"))
				leToken();
			else if(token.getImagem().equals("realistico"))
				leToken();
			else if(token.getImagem().equals("prosa"))
				leToken();
			else if(token.getImagem().equals("vacuo"))
				leToken();
			else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado um dos tipos a seguir: 'inteiro', 'real', 'prosa' ou 'vacuo. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}else {
			Token lastToken = lastToken();
			erros.add("Erro: esperado uma das palavras reservadas a seguir: 'inteiro', 'real', 'prosa' ou 'vacuo' " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
		}
	}

	// <ListParam> ::= <Param><ListParam2> |
	private void ListParam() {
		if(token.getClasse().contentEquals("ID")) {
			Param();
			ListParam2();
		}
	}

	//<Param> ::= id ‘:’ <Tipo>
	private void Param() {
		if(token.getClasse().equals("ID")) {
			leToken();
			if(token.getImagem().equals(":")) {
				leToken();
				Tipo();
			}else {
				Token lastToken = lastToken();
				erros.add("Erro: esperado o ':'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
			}
		}			Token lastToken = lastToken();
		erros.add("Erro: esperado um 'identificador'. Linha: " + lastToken.getLinha() + "Coluna: " + lastToken.getColuna());
	}

	//<ListParam2> ::=  ‘,’ <Param><ListParam2> | 
	private void ListParam2() {
		if(token.getImagem().equals(",")) {
			leToken();
			Param();
			ListParam();
		}
		
	}


}
