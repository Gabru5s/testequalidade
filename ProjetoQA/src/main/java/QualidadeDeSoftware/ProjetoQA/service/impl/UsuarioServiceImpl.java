package QualidadeDeSoftware.ProjetoQA.service.impl;

import QualidadeDeSoftware.ProjetoQA.dto.LoginRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioResponse;
import QualidadeDeSoftware.ProjetoQA.exception.RegraNegocioException;
import QualidadeDeSoftware.ProjetoQA.model.Usuario;
import QualidadeDeSoftware.ProjetoQA.repository.UsuarioRepository;
import QualidadeDeSoftware.ProjetoQA.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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