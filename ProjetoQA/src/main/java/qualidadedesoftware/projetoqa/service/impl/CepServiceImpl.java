package qualidadedesoftware.projetoqa.service.impl;

import org.springframework.stereotype.Service;

import qualidadedesoftware.projetoqa.client.ViaCepClient;
import qualidadedesoftware.projetoqa.dto.CepResponse;
import qualidadedesoftware.projetoqa.exception.RegraNegocioException;
import qualidadedesoftware.projetoqa.service.CepService;

@Service
public class CepServiceImpl implements CepService {

    private final ViaCepClient viaCepClient;

    public CepServiceImpl(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    @Override
    public CepResponse consultar(String cep) {
        String cepLimpo = cep == null ? "" : cep.replaceAll("\\D", "");

        if (!cepLimpo.matches("\\d{8}")) {
            throw new RegraNegocioException("CEP deve conter 8 dígitos.");
        }

        CepResponse response = viaCepClient.consultarCep(cepLimpo);

        if (response == null || Boolean.TRUE.equals(response.erro())) {
            throw new RegraNegocioException("CEP não encontrado.");
        }

        return response;
    }
}