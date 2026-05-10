package QualidadeDeSoftware.projetoqa;

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
        repository.deleteAll();
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
            "inexistente@email.com, senha123",
            "valido@email.com, senhaErrada"
    })
    void deveLancarExcecaoEmCredenciaisInvalidas(String email, String senha) {
        service.cadastrar(new UsuarioCreateRequest("Valido", "valido@email.com", "senha123"));

        assertThrows(RegraNegocioException.class, () ->
                service.autenticar(new LoginRequest(email, senha))
        );
    }

    // ============================================
    // COBERTURA — buscarPorEmail
    // ============================================

    @Test
    void deveBuscarUsuarioPorEmail() {
        service.cadastrar(new UsuarioCreateRequest("Ana", "ana@teste.com", "senha123"));

        UsuarioResponse response = service.buscarPorEmail("ana@teste.com");

        assertNotNull(response.id());
        assertEquals("ana@teste.com", response.email());
        assertEquals("Ana", response.nome());
    }

    @Test
    void deveLancarExcecaoAoBuscarEmailInexistente() {
        assertThrows(RegraNegocioException.class, () ->
                service.buscarPorEmail("naoexiste@teste.com")
        );
    }
}