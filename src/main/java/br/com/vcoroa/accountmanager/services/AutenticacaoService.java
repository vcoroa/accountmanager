package br.com.vcoroa.accountmanager.services;

import br.com.vcoroa.accountmanager.dtos.UsuarioDto;
import br.com.vcoroa.accountmanager.entities.Usuario;
import br.com.vcoroa.accountmanager.repositories.UsuarioRepository;
import br.com.vcoroa.accountmanager.response.UsuarioResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse registrar(UsuarioDto usuarioDto) {
        Usuario novoUsuario = Usuario.builder()
            .username(usuarioDto.getUsername())
            .password(passwordEncoder.encode(usuarioDto.getPassword()))
            .build();
        novoUsuario = usuarioRepository.save(novoUsuario);

        return UsuarioResponse.builder()
            .username(novoUsuario.getUsername())
            .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

}