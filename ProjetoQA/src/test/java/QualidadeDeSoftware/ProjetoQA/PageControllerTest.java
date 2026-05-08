package QualidadeDeSoftware.ProjetoQA;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRedirecionarParaLoginQuandoNaoTemSessaoNaIndex() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void deveRedirecionarParaLivrosQuandoTemSessaoNaIndex() throws Exception {

        mockMvc.perform(get("/")
                .sessionAttr("usuarioId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

    @Test
    void deveAbrirPaginaLoginQuandoNaoTemSessao() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void deveRedirecionarLoginQuandoUsuarioJaEstaLogado() throws Exception {

        mockMvc.perform(get("/login")
                .sessionAttr("usuarioId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

    @Test
    void deveAbrirPaginaCadastroQuandoNaoTemSessao() throws Exception {

        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro"));
    }

    @Test
    void deveRedirecionarCadastroQuandoUsuarioJaEstaLogado() throws Exception {

        mockMvc.perform(get("/cadastro")
                .sessionAttr("usuarioId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }

  

    @Test
    void deveAbrirPaginaLivrosQuandoTemSessao() throws Exception {

        mockMvc.perform(get("/livros")
                .sessionAttr("usuarioId", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("livros"))
                .andExpect(model().attributeExists("usuarioId"))
                .andExpect(model().attributeExists("livros"));
    }



    @Test
    void deveAbrirFormularioNovoLivro() throws Exception {

        mockMvc.perform(get("/livros/novo")
                .sessionAttr("usuarioId", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("livro-form"))
                .andExpect(model().attributeExists("usuarioId"));
    }


    @Test
    void deveRedirecionarQuandoLivroNaoExiste() throws Exception {

        mockMvc.perform(get("/livros/editar/id-invalido")
                .sessionAttr("usuarioId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livros"));
    }
}