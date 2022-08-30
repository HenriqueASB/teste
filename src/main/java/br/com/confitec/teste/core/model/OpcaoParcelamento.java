package br.com.confitec.teste.core.model;

import lombok.Data;

@Data
public class OpcaoParcelamento {

    private int quantidadeMinimaParcelas;
    private int quantidadeMaximaParcelas;
    private double juros;

}
