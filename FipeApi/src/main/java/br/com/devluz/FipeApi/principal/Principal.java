package br.com.devluz.FipeApi.principal;

import java.util.Scanner;

public class Principal {
    private  Scanner scanner = new Scanner(System.in);
    private final String API_URL = "https://parallelum.com.br/fipe/api/v1/";

    public void showMenu () {
        var menu = """
                ---> Opções <---
                Carros
                Motos
                Caminhões
                """;

    }
}
