package qualidadedesoftware.projetoqa.service.impl;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import qualidadedesoftware.projetoqa.dto.LoginRequest;
import qualidadedesoftware.projetoqa.dto.UsuarioCreateRequest;
import qualidadedesoftware.projetoqa.dto.UsuarioResponse;
import qualidadedesoftware.projetoqa.exception.RegraNegocioException;
import qualidadedesoftware.projetoqa.model.Usuario;
import qualidadedesoftware.projetoqa.repository.UsuarioRepository;
import qualidadedesoftware.projetoqa.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponse cadastrar(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraNegocioException("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario(
                null,
                request.nome(),
                request.email(),
                passwordEncoder.encode(request.senha())
        );

        Usuario salvo = usuarioRepository.save(usuario);
        return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    @Override
    public UsuarioResponse autenticar(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new RegraNegocioException("Credenciais inválidas.");
        }

        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    @Override
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}