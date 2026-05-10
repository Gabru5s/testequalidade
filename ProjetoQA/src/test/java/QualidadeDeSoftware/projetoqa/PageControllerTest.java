package QualidadeDeSoftware.projetoqa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import qualidadedesoftware.projetoqa.ProjetoQaApplication;
import qualidadedesoftware.projetoqa.repository.LivroRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void limparBanco() {
        livroRepository.deleteAll();
    }

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

    @Test
    void deveCarregarContexto() {
        // Contexto já sobe via @SpringBootTest — cobre a classe principal
    }

    @Test
    void deveExecutarMainSemErros() {
        assertDoesNotThrow(() ->
                ProjetoQaApplication.main(new String[]{})
        );
    }

    @Test
    void deveRetornarForbiddenEmLivrosQuandoSemSessao() throws Exception {

        mockMvc.perform(get("/livros"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornarForbiddenEmNovoLivroQuandoSemSessao() throws Exception {

        mockMvc.perform(get("/livros/novo"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornarForbiddenEmEditarLivroQuandoSemSessao() throws Exception {

        mockMvc.perform(get("/livros/editar/qualquer-id"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveAbrirPaginaEditarLivroQuandoLivroExiste() throws Exception {

        String isbn = "ISBN-EDIT-" + System.currentTimeMillis();

        String json = """
                {
                    "titulo": "Livro Editar",
                    "autor": "Autor",
                    "isbn": "%s",
                    "anoPublicacao": 2020
                }
                """.formatted(isbn);

        String response = mockMvc.perform(post("/api/livros")
                        .with(csrf())
                        .sessionAttr("usuarioId", "user-edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/livros/editar/" + id)
                        .sessionAttr("usuarioId", "user-edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("livro-editar"))
                .andExpect(model().attributeExists("livro"));
    }
}