package qualidadedesoftware.projetoqa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import qualidadedesoftware.projetoqa.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveRedirecionarParaLoginQuandoCredenciaisInvalidas() throws Exception {

        mockMvc.perform(post("/login")
                .param("email", "inexistente@email.com")
                .param("senha", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {

        mockMvc.perform(post("/cadastro")
                .param("nome", "Carlos")
                .param("email", "carlos@email.com")
                .param("senha", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

    @Test
    void deveRedirecionarCadastroQuandoEmailJaExiste() throws Exception {

        mockMvc.perform(post("/cadastro")
                .param("nome", "Carlos")
                .param("email", "duplicado@email.com")
                .param("senha", "123456"));

        mockMvc.perform(post("/cadastro")
                .param("nome", "Carlos 2")
                .param("email", "duplicado@email.com")
                .param("senha", "654321"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cadastro?error"));
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {

        mockMvc.perform(post("/cadastro")
                .param("nome", "Usuario")
                .param("email", "usuario@email.com")
                .param("senha", "123456"));

        mockMvc.perform(post("/login")
                .param("email", "usuario@email.com")
                .param("senha", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

    @Test
    void deveFazerLogoutComSucesso() throws Exception {

        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}