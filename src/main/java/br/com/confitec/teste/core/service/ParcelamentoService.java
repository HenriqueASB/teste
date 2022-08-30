package br.com.confitec.teste.core.service;

import br.com.confitec.teste.core.model.Apolice;
import br.com.confitec.teste.core.model.Cobertura;
import br.com.confitec.teste.core.model.OpcaoParcelamento;
import br.com.confitec.teste.core.model.Parcelamento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ParcelamentoService {

    public  List<Parcelamento> calculaParcelamento(Apolice apolice){

        this.validaApolice(apolice);
        BigDecimal valorTotal = apolice.getListCobertura().stream().map(Cobertura::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<Parcelamento> parcelamentos = new ArrayList<>();

        apolice.getListOpcaoParcelamento().forEach(opcaoParcelamento -> {
            parcelamentos.addAll(this.calculaParcelas(valorTotal,opcaoParcelamento));
        });

        return parcelamentos;
    }

    private void validaApolice(Apolice apolice){
        AtomicBoolean teste = new AtomicBoolean(false);

        if(apolice.getListCobertura().isEmpty() || apolice.getListOpcaoParcelamento().isEmpty()){
            throw new RuntimeException("apolice invalida");
        }

        List<OpcaoParcelamento> opcaoParcelamento = apolice.getListOpcaoParcelamento();

        opcaoParcelamento.forEach(opcao -> {
            if(opcao.getQuantidadeMinimaParcelas() <=0 || opcao.getQuantidadeMaximaParcelas() <= 0 || opcao.getJuros() < 0 ){
                teste.set(true);
            }
        });
        if(teste.get()){
            throw new RuntimeException("apolice invalida");
        }

    }
    private List<Parcelamento> calculaParcelas(BigDecimal valor, OpcaoParcelamento opcaoParcelamento){
        int numeroParcelas = opcaoParcelamento.getQuantidadeMaximaParcelas() - opcaoParcelamento.getQuantidadeMinimaParcelas() +1;
        List<Parcelamento> parcelamentos = new ArrayList<>();


        for (int i =0 ; i < numeroParcelas; i++ ){
            Parcelamento parcelamento = new Parcelamento();
            int parcela = i+ opcaoParcelamento.getQuantidadeMinimaParcelas();
            parcelamento.setQuantidadeParcela(parcela);
            BigDecimal valorParcelamentoTotal = this.calculaValorTotal(valor,parcela,opcaoParcelamento.getJuros());
            parcelamento.setValorParcelamentoTotal(valorParcelamentoTotal);
            BigDecimal valorDemaisParcelas = this.calculaDemaisParcelas(valorParcelamentoTotal, parcela);
            parcelamento.setValorDemaisParcelas(valorDemaisParcelas);
            BigDecimal primeiraParcela = this.calculaPrimeiraParcela(valorParcelamentoTotal, valorDemaisParcelas, parcela);
            parcelamento.setValorPrimeiraParcela(primeiraParcela);
            if(parcela==1){
                parcelamento.setValorDemaisParcelas(null);
            }
            parcelamentos.add(parcelamento);
        }

        return parcelamentos;
    }

    private BigDecimal calculaValorTotal(BigDecimal valor, int parcelas, double juros){

        BigDecimal um = BigDecimal.valueOf(1);
        BigDecimal jurosB = BigDecimal.valueOf(juros);
        BigDecimal etapa1 = jurosB.add(um);
        BigDecimal etapa2 = etapa1.pow(parcelas);
        BigDecimal valorTotal = valor.multiply(etapa2);
        valorTotal = valorTotal.setScale(2,RoundingMode.HALF_EVEN);

        return valorTotal;
    }

    private BigDecimal calculaDemaisParcelas(BigDecimal valor, int parcelas){
        BigDecimal parcelasB = new BigDecimal(parcelas);
        return valor.divide(parcelasB,2, RoundingMode.FLOOR);
    }

    private BigDecimal calculaPrimeiraParcela(BigDecimal valor, BigDecimal demaisParcelas, int parcelas){
        BigDecimal parcelasB = new BigDecimal(parcelas);
        BigDecimal totalParcelas = demaisParcelas.multiply(parcelasB);
        BigDecimal difereça = valor.subtract(totalParcelas);
        return demaisParcelas.add(difereça);

    }
}
