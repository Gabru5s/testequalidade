package qualidadedesoftware.projetoqa.service;

import qualidadedesoftware.projetoqa.dto.CepResponse;

public interface CepService {
    CepResponse consultar(String cep);
}