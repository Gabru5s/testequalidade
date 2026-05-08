package qualidadedesoftware.projetoqa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import qualidadedesoftware.projetoqa.dto.LoginRequest;
import qualidadedesoftware.projetoqa.dto.UsuarioCreateRequest;
import qualidadedesoftware.projetoqa.dto.UsuarioResponse;
import qualidadedesoftware.projetoqa.exception.RegraNegocioException;
import qualidadedesoftware.projetoqa.repository.UsuarioRepository;
import qualidadedesoftware.projetoqa.service.UsuarioService;


@SpringBootTest
@Import(TestcontainersConfiguration.class)
class UsuarioServiceTest {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll(); // Garante isolamento entre testes
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        UsuarioCreateRequest request = new UsuarioCreateRequest("Carlos", "carlos@teste.com", "senha123");
        UsuarioResponse response = service.cadastrar(request);
        
        assertNotNull(response.id());
        assertEquals("carlos@teste.com", response.email());
    }

    @Test
    void naoDevePermitirEmailDuplicado() {
        UsuarioCreateRequest request = new UsuarioCreateRequest("User1", "dup@email.com", "123");
        service.cadastrar(request);

        assertThrows(RegraNegocioException.class, () -> service.cadastrar(request));
    }

    @ParameterizedTest
    @CsvSource({
        "inexistente@email.com, senha123", // Email não cadastrado
        "valido@email.com, senhaErrada"     // Senha não confere
    })
    void deveLancarExcecaoEmCredenciaisInvalidas(String email, String senha) {
        service.cadastrar(new UsuarioCreateRequest("Valido", "valido@email.com", "senha123"));
        
        assertThrows(RegraNegocioException.class, () -> 
            service.autenticar(new LoginRequest(email, senha))
        );
    }
}