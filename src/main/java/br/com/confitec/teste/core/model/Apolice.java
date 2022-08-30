package br.com.confitec.teste.core.model;

import lombok.Data;

import java.util.List;

@Data
public class Apolice {

    private List<Cobertura> listCobertura;
    private List<OpcaoParcelamento> listOpcaoParcelamento;

}
