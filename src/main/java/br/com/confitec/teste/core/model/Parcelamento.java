package br.com.confitec.teste.core.model;


import lombok.Data;

import java.math.BigDecimal;
@Data
public class Parcelamento {

    private Integer quantidadeParcela;
    private BigDecimal valorPrimeiraParcela;
    private BigDecimal valorDemaisParcelas;
    private BigDecimal valorParcelamentoTotal;
}
