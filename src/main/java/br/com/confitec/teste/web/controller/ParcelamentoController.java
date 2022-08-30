package br.com.confitec.teste.web.controller;

import br.com.confitec.teste.core.model.Apolice;
import br.com.confitec.teste.core.model.Parcelamento;
import br.com.confitec.teste.core.service.ParcelamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/confitec/teste/parcelamento")
public class ParcelamentoController {

    @Autowired
    private ParcelamentoService service;

    @PostMapping
    public ResponseEntity<List<Parcelamento>> post(@RequestBody Apolice apolice){
        return new ResponseEntity<>(service.calculaParcelamento(apolice), HttpStatus.OK);
    }
}
