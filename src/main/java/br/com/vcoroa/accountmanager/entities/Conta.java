package br.com.vcoroa.accountmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity(name = "contas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "descricao", nullable = false)
    @NotEmpty(message = "Descricao é obrigatória")
    private String descricao;

    @Column(name = "situacao")
    private String situacao;

    @Column(name = "tipo", nullable = false)
    private int tipo;

    @Transient
    public TipoConta getTipoConta() {
        return TipoConta.getById(tipo);
    }

}