package qualidadedesoftware.projetoqa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qualidadedesoftware.projetoqa.dto.CepResponse;
import qualidadedesoftware.projetoqa.service.CepService;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/{cep}")
    public CepResponse consultar(@PathVariable String cep) {
        return cepService.consultar(cep);
    }
}