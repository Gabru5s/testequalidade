package qualidadedesoftware.projetoqa.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import qualidadedesoftware.projetoqa.dto.CepResponse;

@Component
public class ViaCepClient {

    private final RestClient restClient;

    public ViaCepClient(@Value("${viacep.base-url:https://viacep.com.br}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public CepResponse consultarCep(String cep) {
        return restClient.get()
                .uri("/ws/{cep}/json/", cep)
                .retrieve()
                .body(CepResponse.class);
    }
}