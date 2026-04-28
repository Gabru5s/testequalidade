package QualidadeDeSoftware.ProjetoQA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<?> tratarRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", Instant.now().toString(),
                "erro", ex.getMessage()
        ));
    }

    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<?> tratarLivroNaoEncontrado(LivroNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", Instant.now().toString(),
                "erro", ex.getMessage()
        ));
    }
}