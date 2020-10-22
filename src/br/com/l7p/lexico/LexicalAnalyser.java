package br.com.l7p.lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LexicalAnalyser {

	public static void main(String[] args) {
		try {
			File file = new File("src//br//com//l7p//lexico//programa.l7p");
			BufferedReader br = new BufferedReader(new FileReader(file));
			ClassificadorPalavras meuClassificador = new ClassificadorPalavras();
			meuClassificador.classifica(br);
			
			AnalisadorLexico.printToken();
			AnalisadorLexico.printErros();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		

		
		

	}

}
