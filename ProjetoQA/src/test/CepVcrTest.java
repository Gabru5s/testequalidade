package qualidadedesoftware.projetoqa;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CepVcrTest {

    @Test
    void deveLerRespostaGravadaDoViaCep() throws Exception {
        InputStream arquivo = getClass()
                .getClassLoader()
                .getResourceAsStream("vcr/viacep-01001000.json");

        assertNotNull(arquivo, "Arquivo VCR não encontrado");

        String json = new String(arquivo.readAllBytes(), StandardCharsets.UTF_8);

        assertTrue(json.contains("\"cep\": \"01001-000\""));
        assertTrue(json.contains("\"logradouro\": \"Praça da Sé\""));
        assertTrue(json.contains("\"bairro\": \"Sé\""));
        assertTrue(json.contains("\"localidade\": \"São Paulo\""));
        assertTrue(json.contains("\"uf\": \"SP\""));
        assertTrue(json.contains("\"ibge\": \"3550308\""));
    }
}