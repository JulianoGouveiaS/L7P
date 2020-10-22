package br.com.l7p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.com.l7p.lexico.AnalisadorLexico;
import br.com.l7p.lexico.ClassificadorPalavras;
import br.com.l7p.semantico.AnalisadorSemantico;
import br.com.l7p.sintatico.AnalisadorSintatico;
import br.com.l7p.sintatico.AnalisadorSintaticoGeradorArvore;
import br.com.l7p.sintatico.No;

public class Main {

    public static void main(String[] args) {
        lexico();
        sintatico();
    }

    public static void lexico() {
        String linha = null;
        String originalImage = null;
        String newLine = null;
        String newImage = null;
        try {
            File file = new File("src//br//com//l7p//lexico//programa.l7p");
            BufferedReader br = new BufferedReader(new FileReader(file));
            ClassificadorPalavras classificador = new ClassificadorPalavras();

            classificador.classifica(br);

            System.out.println("### ANALISDOR LÉXICO ###");
            if (!AnalisadorLexico.getErros().isEmpty()) {
                System.out.println("####  Analisador Léxico com os seguintes erros: ####");
                AnalisadorLexico.printErros();
            } else {
                System.out.println("### Análise Léxica concluída com sucesso ###");
                System.out.println(AnalisadorLexico.getTokens());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sintatico() {
        System.out.println("### ANALISDOR SINTÁTICO ###");
        AnalisadorSintaticoGeradorArvore sintatico = new AnalisadorSintaticoGeradorArvore(AnalisadorLexico.getTokens());
        sintatico.analisar();
        if (!AnalisadorSintaticoGeradorArvore.getErros().isEmpty()) {
            System.out.println("### Analisador Sintático com os seguinte erros:");
            System.out.println(AnalisadorSintaticoGeradorArvore.getErros());
        } else {
            System.out.println("### Análise Sintática concluída com sucesso ###");
            AnalisadorSintaticoGeradorArvore.mostrarArvore();
            semantico(AnalisadorSintaticoGeradorArvore.getRaiz());
        }
    }

    public static void semantico(No raiz) {
        System.out.println("### ANALISADOR SEMANTICO ###");
        AnalisadorSemantico semantico = new AnalisadorSemantico(raiz);
        semantico.analisar();
        if (!AnalisadorSemantico.getErros().isEmpty()) {
            System.out.println("### Analisador semantico com os seguinte erros:");
            System.out.println(AnalisadorSemantico.getErros());
        } else {
            System.out.println("### Análise semantica concluída com sucesso ###");
        }
    }

}
