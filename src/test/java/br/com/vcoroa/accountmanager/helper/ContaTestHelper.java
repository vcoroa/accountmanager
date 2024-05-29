package br.com.vcoroa.accountmanager.helper;

import br.com.vcoroa.accountmanager.entities.Conta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContaTestHelper {

    public static Conta buildContaFake() {
        return Conta.builder()
            .valor(100D)
            .dataVencimento(LocalDate.parse("2024-02-14"))
            .dataPagamento(LocalDate.parse("2024-02-12"))
            .descricao("Conta de luz fev.")
            .tipo(1)
            .build();
    }

    public static Conta buildContaFakeContaWithId(Long id) {
        return Conta.builder()
            .valor(100D)
            .dataVencimento(LocalDate.parse("2024-02-14"))
            .dataPagamento(LocalDate.parse("2024-02-12"))
            .descricao("Conta de luz fev.")
            .tipo(1)
            .id(id)
            .build();
    }

    public static List<Conta> buildContasFake() {
        List<Conta> contas = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            Conta conta = Conta.builder()
                .valor(100D * i)
                .dataVencimento(LocalDate.parse("2024-02-14"))
                .dataPagamento(LocalDate.parse("2024-02-12"))
                .descricao("Conta de luz fev. " + i)
                .tipo(1)
                .id(i)
                .build();
            contas.add(conta);
        }
        return contas;
    }

}
