package br.com.vcoroa.accountmanager.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDto {
    private String username;

    private String password;
}
