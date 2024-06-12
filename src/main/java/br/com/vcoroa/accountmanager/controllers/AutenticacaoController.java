package br.com.vcoroa.accountmanager.controllers;

import br.com.vcoroa.accountmanager.dtos.UsuarioDto;
import br.com.vcoroa.accountmanager.entities.Usuario;
import br.com.vcoroa.accountmanager.response.LoginResponse;
import br.com.vcoroa.accountmanager.response.UsuarioResponse;
import br.com.vcoroa.accountmanager.services.AutenticacaoService;
import br.com.vcoroa.accountmanager.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacao")
@Tag(name = "Autenticação", description = "API para gerenciamento e autenticação de usuários")
public class AutenticacaoController {

    private final JwtService jwtService;
    private final AutenticacaoService autenticacaoService;
    private final AuthenticationManager authenticationManager;

    public AutenticacaoController(
        JwtService jwtService,
        AutenticacaoService autenticacaoService,
        AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.autenticacaoService = autenticacaoService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Registra um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida")})
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody UsuarioDto usuarioDto) {
        UsuarioResponse usuarioCriado = autenticacaoService.registrar(usuarioDto);
        return new ResponseEntity<>(usuarioCriado, HttpStatus.CREATED);
    }

    @Operation(summary = "Autentica um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UsuarioDto usuarioDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDto.getUsername(), usuarioDto.getPassword())
        );

        UserDetails userDetails = autenticacaoService.loadUserByUsername(usuarioDto.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }
}
