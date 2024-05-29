package br.com.vcoroa.accountmanager.services;

import br.com.vcoroa.accountmanager.entities.Conta;
import br.com.vcoroa.accountmanager.repositories.ContaRepository;
import br.com.vcoroa.accountmanager.response.ContaValorPeriodoResponse;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    private ContaService contaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        contaService = new ContaService(contaRepository);
    }

    @Test
    public void createConta_createsContaSuccessfully() {
        Conta conta = new Conta();
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta result = contaService.createConta(conta);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    public void updateConta_updatesContaSuccessfully() {
        Conta conta = new Conta();
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta result = contaService.updateConta(1L, conta);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    public void getContas_returnsContasSuccessfully() {
        Pageable pageable = mock(Pageable.class);
        LocalDate dataVencimento = LocalDate.now();
        String descricao = "Teste";
        Integer tipo = 1;
        Page<Conta> contas = mock(Page.class);
        when(contaRepository.findAllByDataVencimentoAndDescricaoAndTipo(pageable, dataVencimento, descricao, tipo)).thenReturn(contas);

        Page<Conta> result = contaService.getContas(pageable, dataVencimento, descricao);

        assertEquals(contas, result);
        verify(contaRepository, times(1)).findAllByDataVencimentoAndDescricaoAndTipo(pageable, dataVencimento, descricao, tipo);
    }

    @Test
    public void obterValorTotalPagoPorPeriodo_returnsValorTotalPagoPorPeriodoSuccessfully() {
        LocalDate inicio = LocalDate.now().minusDays(10);
        LocalDate fim = LocalDate.now();
        Double valorTotal = 1000.0;

        Conta conta1 = mock(Conta.class);
        when(conta1.getValor()).thenReturn(500.0);
        when(conta1.getDataPagamento()).thenReturn(LocalDate.now().minusDays(5));

        Conta conta2 = mock(Conta.class);
        when(conta2.getValor()).thenReturn(500.0);
        when(conta2.getDataPagamento()).thenReturn(LocalDate.now().minusDays(3));

        List<Conta> contas = Arrays.asList(conta1, conta2);

        when(contaRepository.findAll()).thenReturn(contas);

        ContaValorPeriodoResponse response = contaService.obterValorTotalPagoPorPeriodo(inicio, fim);

        assertEquals(valorTotal, response.getValorTotalPago());
    }

    @Test
    public void importarContasCsv_importsContasSuccessfully() throws IOException, CsvValidationException {
        // Cria um arquivo CSV temporário
        File csvFile = File.createTempFile("contas", ".csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("2024-02-14,2024-02-12,100,Conta de luz,1,1");
            writer.println("2024-03-14,2024-03-12,200,Conta de água,2,2");
            writer.println("2024-04-14,2024-04-12,300,Conta de gás,3,3");
        }

        contaService.importarContasCsv(csvFile);

        verify(contaRepository, atLeastOnce()).saveAll(anyList());

        // Deleta o arquivo temporário após o teste
        csvFile.delete();
    }
}
