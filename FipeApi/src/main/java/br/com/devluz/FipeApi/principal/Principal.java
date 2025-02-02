package br.com.devluz.FipeApi.principal;

import br.com.devluz.FipeApi.model.Dados;
import br.com.devluz.FipeApi.model.Modelos;
import br.com.devluz.FipeApi.model.Veiculo;
import br.com.devluz.FipeApi.service.ConsumirAPI;
import br.com.devluz.FipeApi.service.ConverterDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        if (opcao.toLowerCase().contains("car")) {
            url = API_URL + "/carros/marcas/";
        } else if (opcao.toLowerCase().contains("mot")) {
            url = API_URL + "/motos/marcas/";
        } else if (opcao.toLowerCase().contains("caminh")) {
            url = API_URL + "/caminhos/marcas/";
        } else {
            System.out.println("Opção inválida, tente novamente.");
            showMenu();
        }

        var json = consome.obterDados(url);

        var listaDados = converte.obterLista(json, Dados.class);
        listaDados.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Escolha o código do modelo.");
        var opcaoModelo = scanner.nextLine();
        url = url + opcaoModelo + "/modelos/";

        json = consome.obterDados(url);

        var listaModelos = converte.obterDados(json, Modelos.class);
        listaModelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do nome do carro que deseja buscar.");

        var nomeVeiculo = scanner.nextLine();

        List<Dados> modelosFiltrados = listaModelos.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .toList();

        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo.");
        var codigoModelo = scanner.nextLine();

        url += codigoModelo + "/anos/";

        json = consome.obterDados(url);

        var anos = converte.obterLista(json, Dados.class);

        List<String> listaAnos = anos.stream()
                .map(Dados::codigo)
                .collect(Collectors.toList());

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < listaAnos.size() ; i++) {
            var urlAnos = url + listaAnos.get(i);
            json = consome.obterDados(urlAnos);
            Veiculo veiculo = converte.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        veiculos.forEach(System.out::println);
    }
}
