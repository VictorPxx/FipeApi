package br.com.devluz.FipeApi.principal;

import br.com.devluz.FipeApi.model.Dados;
import br.com.devluz.FipeApi.model.Modelos;
import br.com.devluz.FipeApi.service.ConsumirAPI;
import br.com.devluz.FipeApi.service.ConverterDados;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private  Scanner scanner = new Scanner(System.in);
    private final String API_URL = "https://parallelum.com.br/fipe/api/v1";
    private ConsumirAPI consome = new ConsumirAPI();
    private ConverterDados converte = new ConverterDados();

    public void showMenu () {
        String url = "";
        var menu = """
                ---> Opções <---
                Carros
                Motos
                Caminhões
                """;
        System.out.print(menu + "\nEscolha uma opção: ");

        String opcao = scanner.nextLine();

        if(opcao.toLowerCase().contains("car")){
            url = API_URL + "/carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            url = API_URL + "/motos/marcas";
        } else if (opcao.toLowerCase().contains("caminh")) {
            url = API_URL + "/caminhos/marcas";
        } else {
            System.out.println("Opção inválida, tente novamente.");
            showMenu();
        }

        var json = consome.obterDados(url);
        System.out.println(json);

        var listaDados = converte.obterLista(json, Dados.class);
        listaDados.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        var opcaoModelo = scanner.nextLine();
        url = url + "/" + opcaoModelo + "/modelos";

        json = consome.obterDados(url);
        System.out.println(json);

        var listaModelos = converte.obterDados(json, Modelos.class);
        listaModelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
    }
}
