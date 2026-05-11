package QualidadeDeSoftware.projetoqa;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import qualidadedesoftware.projetoqa.exception.GlobalExceptionHandler;
import qualidadedesoftware.projetoqa.exception.LivroNaoEncontradoException;
import qualidadedesoftware.projetoqa.exception.RegraNegocioException;



class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveTratarRegraNegocioException() {

        RegraNegocioException exception =
                new RegraNegocioException("Erro de regra");

        ResponseEntity<Map<String, Object>> response =
                handler.tratarRegraNegocio(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals(
                "Erro de regra",
                response.getBody().get("erro")
        );
    }

    @Test
    void deveTratarLivroNaoEncontradoException() {

        LivroNaoEncontradoException exception =
                new LivroNaoEncontradoException("Livro não encontrado");

        ResponseEntity<Map<String, Object>> response =
                handler.tratarLivroNaoEncontrado(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertEquals(
                "Livro não encontrado",
                response.getBody().get("erro")
        );
    }
}