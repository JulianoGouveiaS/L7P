package br.com.l7p.semantico;

import java.util.ArrayList;

import br.com.l7p.lexico.TabelaSimbolos;
import br.com.l7p.lexico.Token;
import br.com.l7p.sintatico.No;

public class AnalisadorSemantico {
	
	private static No raiz;
	private static ArrayList<String> erros = new ArrayList<String>();
	private static Token defAtual;
	
	public AnalisadorSemantico(No raiz) {
		this.raiz = raiz;
	}

	public static No getRaiz() {
		return raiz;
	}

	public static void setRaiz(No raiz) {
		AnalisadorSemantico.raiz = raiz;
	}

	public static ArrayList<String> getErros() {
		return erros;
	}

	public static void setErros(ArrayList<String> erros) {
		AnalisadorSemantico.erros = erros;
	}

	public static Token getDefAtual() {
		return defAtual;
	}

	public static void setDefAtual(Token defAtual) {
		AnalisadorSemantico.defAtual = defAtual;
	}
	
	
	public void analisar() {
		analisar(raiz);
	}
	
	public Object analisar(No no) {
		
		switch (no.getTipo()) {
		case NO_LISTDEF: return ListDef(no);
		case NO_DEF: return Def(no);
		case NO_LISTPARAM: return ListParam(no);
		case NO_LISTPARAM2: return ListParam2(no);
		case NO_PARAM: return Param(no);
		case NO_TIPO: return Tipo(no);
		case NO_LISTCOMAN: return ListComan(no);
		case NO_COMAN: return Coman(no);
		case NO_DECL: return Decl(no);
		case NO_ATRIB: return Atrib(no);
		case NO_SE: return Se(no);
		case NO_LACO: return Laco(no);
		case NO_RET: return Ret(no);
		case NO_ENTRADA: return Entrada(no);
		case NO_SAIDA: return Saida(no);
		case NO_CHAMADA: return Chamada(no);
		case NO_LISTID: return ListId(no);
		case NO_LISTID2: return ListId2(no);
		case NO_EXPARIT: return ExpArit(no);
		case NO_TERMO: return Termo(no);
		case NO_EXPARIT2: return ExpArit2(no);
		case NO_TERMO2: return Termo2(no);
		case NO_FATOR: return Fator(no);
		case NO_OP1: return Op1(no);
		case NO_OP2: return Op2(no);
		case NO_OPERANDO: return Operando(no);
		case NO_SENAO: return Senao(no);
		case NO_EXPREL: return ExpRel(no);
		case NO_OP3: return Op3(no);
		case NO_LISTARG: return ListArg(no);
		case NO_LISTARG2: return ListArg2(no);
		default:
			System.out.println("Opção de nó inexistente");
			break;
		}
		
		return null;
	}
	
	//<ListArg2> ::=  | ‘,’ <Operando><ListArg2> 
	private Object ListArg2(No no) {
		return null;
	}

	//<ListArg> ::=  | <Operando> <ListArg2>
	private Object ListArg(No no) {
		return null;
	}
	
	//<Op3> ::= ‘>’
	//| ‘>=‘
	//| ‘<‘
	//| ‘<=‘
	//| ‘==‘
	//| ‘!=‘
	private Object Op3(No no) {

		return null;
	}

	//<ExpRel> ::= <ExpArit> <Op3> <ExprArit>
	private Object ExpRel(No no) {
		ArrayList<Token> expArit = (ArrayList<Token>) analisar(no.getFilho(0));
		ArrayList<Token> expArit2 = (ArrayList<Token>) analisar(no.getFilho(2));
		
		String tipo = TabelaSimbolos.getTipo(expArit.get(0));
		String tipo2 = TabelaSimbolos.getTipo(expArit2.get(0));
		
		if(tipo == null) {
			erros.add("Identificador do operando do lado esquerdo da expressão relacional não declarado!");
		}else {
			for(Token token : expArit) {
				if(TabelaSimbolos.getParamDef(token) == null) {
					erros.add("Identificado não declarado: "+token);
				}else if(!TabelaSimbolos.getTipo(token).equals(tipo)) {
					erros.add("Operando com tipo incompatível do lado esquerdo da expressão relacional");
					
				}
			}
		}
		
		if(tipo2 == null) {
			erros.add("Identificador do operando do lado direto da expressão relacional não declarado!");
		}else {
			for(Token token : expArit) {
				if(TabelaSimbolos.getParamDef(token) == null) {
					erros.add("Identificado não declarado: "+token);
				}else if(!TabelaSimbolos.getTipo(token).equals(tipo)) {
					erros.add("Operando com tipo incompatível do lado direito da expressão relacional");
					
				}
			}
		}
		
		if(tipo != null && tipo2 != null && !tipo.equals(tipo2)) {
			erros.add("Tipo do lado esquerdo incompatível com o tipo do lado direito da expressão relacional");
		}
		return null;
	}

	//<Operando> ::= id 
	//				| cli
	//				| clr
	//				| clt
	private Object Operando(No no) {
		return no.getFilho(0).getToken();
	}

	//<Op2> ::= ‘*’
	//			| ‘/‘
	private Object Op2(No no) {
		return null;
	}

	//<Op1> ::= ‘+’
	//			| ‘-‘
	private Object Op1(No no) {
		return null;
	}

	//<Fator> ::= <Operando> | <Chamada> | ‘(‘ <ExpArit> ‘)’
	private Object Fator(No no) {
		if(no.getFilhos().size() == 1){
			Token token = (Token) analisar(no.getFilho(0));
			ArrayList<Token> fator = new ArrayList<Token>();
			fator.add(token);
			return fator;
		}else {
			return analisar(no.getFilho(1));
		}
	}

	//<Termo2> ::= | <Op2> <Termo>
	private Object Termo2(No no) {
		if(no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		}else {
			return analisar(no.getFilho(1));
		}
	}

	//<Termo> ::= <Fator><Termo2>
	private Object Termo(No no) {
		ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(0));
		ArrayList<Token> termo2 = (ArrayList<Token>) analisar(no.getFilho(1));
		fator.addAll(termo2);
		return fator;
	}

	//<ExpArit2> ::=  | <Op1> <ExpArit>
	private Object ExpArit2(No no) {
		if(no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		}else {
			ArrayList<Token> expArit = (ArrayList<Token>) analisar(no.getFilho(1));
			return expArit;
		}
	}

	//<ExpArit> ::= <Termo><ExpArit2>
	private Object ExpArit(No no) {
		ArrayList<Token> termo = (ArrayList<Token>) analisar(no.getFilho(0));
		ArrayList<Token> expArit2 = (ArrayList<Token>) analisar(no.getFilho(1));
		termo.addAll(expArit2);
		return termo;
	}

	//<ListId2> ::= ‘,’ id <ListId2> | 
	private Object ListId2(No no) {
		if(no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		}else {
			Token id = no.getFilho(1).getToken();
			ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(2));
			listId2.add(0, id);
			return listId2;
		}
	}

	//<ListId> ::= id <ListId2>
	private Object ListId(No no) {
		Token id = no.getFilho(0).getToken();
		ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(1));
		listId2.add(0, id);
		return listId2;
	}

	//<Chamada> ::= id ‘(‘ <ListArg> ‘)’ ‘;’
	private Object Chamada(No no) {
		Token id = no.getFilho(0).getToken();
		
		ArrayList<Token> params = TabelaSimbolos.getParamDef(id);
		ArrayList<Token> listArg = (ArrayList<Token>) analisar(no.getFilho(2));
		
		if(params.size() == listArg.size()) {
			for (int i = 0; i < listArg.size(); i++) {
				if(!TabelaSimbolos.getTipo(params.get(i)).equals(listArg.get(i))) {
					erros.add("Argumento de chamada com tipo incompatível em relação a sua delcaração: "+ params.get(i));
				}
				
			}
		}else {
			erros.add("Número de argumentos incorreto para o número de parametros da função");
		}
		
		return null;
	}

	//<Saida> ::= ‘escreva’ ‘(‘ <Operando> ‘)’ ‘;’ 
	private Object Saida(No no) {
		return null;
	}

	//<Entrada> ::= ‘leia’ ‘(‘ id ‘)’ ‘;’ 
	private Object Entrada(No no) {
		return null;
	}

	//<Ret> ::= ‘retorna’ <Fator> ‘;’
	private Object Ret(No no) {
		String tipoDef = TabelaSimbolos.getTipo(defAtual);
		ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(1));
		
		for(Token tok: fator) {
			if(TabelaSimbolos.getTipo(tok) == null) {
				erros.add("Identificador não declarado!");
			}else if(!TabelaSimbolos.getTipo(tok).equals(tipoDef)) {
				erros.add("Tipo de retorno não compatível com o retorno da função: "+tok);
			}
		}
		return null;
	}

	//<Laco> ::= ‘enquanto’  ‘(‘  <ExpRel> ’)’ <Coman>
	private Object Laco(No no) {
		analisar(no.getFilho(2));
		analisar(no.getFilho(4));
		return null;
	}

	//<Senao> ::= ‘senao’ <Coman> | 
	private Object Senao(No no) {
		if(!no.getFilhos().isEmpty()) {
			analisar(no.getFilho(1));
		}
		return null;
	}

	//<Se> ::= ‘se’ ‘(‘ <ExpRel> ‘)’ <Coman> <Senao>
	private Object Se(No no) {
		analisar(no.getFilho(2));
		analisar(no.getFilho(4));
		analisar(no.getFilho(5));
		return null;
	}

	//<Atrib> ::= id ‘=‘ <ExpArit> ‘;’
	private Object Atrib(No no) {
		Token id = no.getFilho(0).getToken();
		String tipo = TabelaSimbolos.getTipo(id);
		
		if(tipo != null) {
			ArrayList<Token> exprArit = (ArrayList<Token>) analisar(no.getFilho(2));
			
			for(Token operan: exprArit) {
				String tipoOperan = TabelaSimbolos.getTipo(operan);
				if(tipoOperan == null) {
					erros.add("Identificador do lado direito não declarado");
				}else if(!tipoOperan.equals(tipo)) {
					erros.add("Identificador do lado direito incompatível com o tipo declarado: " + operan);
				}
			}
		}else {
			erros.add("Identificador não foi declarado");
		}
		return null;
	}

	//<Decl> ::= <ListId> ‘:’ <Tipo> ‘;’
	private Object Decl(No no) {
		ArrayList<Token> listId = (ArrayList<Token>) analisar(no.getFilho(0));
		String tipo = (String) analisar(no.getFilho(2));
		
		for(Token id: listId) {
			if(TabelaSimbolos.getTipo(id) != null) {
				erros.add("Identificar já declarado: " +id);
			}else {
				TabelaSimbolos.setTipo(id, tipo);
			}
		}
		
		return null;
	}

	//<Coman> ::= <Decl>
	//			| <Atrib>
	//			| <Se>
	//			| <Laco>
	//			| <Ret>
	//			| <Entrada>
	//			| <Saida>
	//			| <Chamada> ‘;’
	//			| ‘{’ <ListComan> ‘}’
	private Object Coman(No no) {
		if(no.getFilhos().size() > 1) {
			analisar(no.getFilho(1));
		}else {
			analisar(no.getFilho(0));
		}
		return null;
	}

	//<ListComan> ::=  | <Coman><ListComan>
	private Object ListComan(No no) {
		if(!no.getFilhos().isEmpty()) {
			analisar(no.getFilho(0));
			analisar(no.getFilho(1));
		}
		return null;
	}

	//<Tipo> ::= ‘inteiro’
	//			| ‘real’
	//			| ‘texto’
	//			| ‘nada’
	private Object Tipo(No no) {
		return no.getFilho(0).getToken().getImagem();
	}

	//<Param> ::= id ‘:’ <Tipo>
	private Object Param(No no) {
		Token token = no.getFilho(0).getToken();
		String tipo = (String) analisar(no.getFilho(2));
		String tipoId = TabelaSimbolos.getTipo(token);
		
		if(tipoId == null) {
			TabelaSimbolos.setTipo(token, tipo);
			TabelaSimbolos.setParam(token);
		}else {
			erros.add("A variável de parametros já foi declarada! Id: "+ token);
		}
		return null;
	}
	
	
	
	

	//<ListParam2> ::=  ‘,’ <Param><ListParam2> | 
	private Object ListParam2(No no) {
		if(!no.getFilhos().isEmpty()) {
			analisar(no.getFilho(1));
			analisar(no.getFilho(2));
		}		
		return null;
	}

	//<ListParam> ::= <Param><ListParam2> |
	private Object ListParam(No no) {
		if(!no.getFilhos().isEmpty()) {
			analisar(no.getFilho(0));
			analisar(no.getFilho(1));
		}
		return null;
	}

	//<Def> ::= ‘def’  id ‘(‘ <ListParam> ‘)’  ‘:’  <Tipo> ‘{‘ <ListComan> ‘}’
	private Object Def(No no) {
		Token id = no.getFilho(1).getToken();
		String tipo = (String) analisar(no.getFilho(6));
		defAtual = id;
		
		if(TabelaSimbolos.getTipo(id) != null) {
			erros.add("Essa função já foi declarada: " + id);
		}else {
			TabelaSimbolos.setTipo(id, tipo);
			analisar(no.getFilho(3));
			analisar(no.getFilho(8));
		}
		return null;
	}
	
	
	

	//<ListDef> ::= <Def><ListDef> |
	private Object ListDef(No no) {
		if(!no.getFilhos().isEmpty()) {
			analisar(no.getFilho(0));
			analisar(no.getFilho(1));
		}
		return null;
	}
	
}

