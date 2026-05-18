package qualidadedesoftware.projetoqa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CepResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        @JsonProperty("ibge") String ibge,
        Boolean erro
) {}