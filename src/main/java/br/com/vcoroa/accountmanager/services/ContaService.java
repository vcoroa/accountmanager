package br.com.vcoroa.accountmanager.services;

import br.com.vcoroa.accountmanager.entities.Conta;
import br.com.vcoroa.accountmanager.entities.TipoConta;
import br.com.vcoroa.accountmanager.repositories.ContaRepository;
import br.com.vcoroa.accountmanager.response.ContaValorPeriodoResponse;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta createConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Conta updateConta(Long id, Conta conta) {
        Conta existingConta = contaRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        existingConta.setDescricao(conta.getDescricao());
        existingConta.setValor(conta.getValor());
        existingConta.setDataVencimento(conta.getDataVencimento());
        existingConta.setDataPagamento(conta.getDataPagamento());
        existingConta.setSituacao(conta.getSituacao());
        return contaRepository.save(existingConta);
    }

    public void alterarSituacao(Long id, String situacao) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        conta.setSituacao(situacao);
        contaRepository.save(conta);
    }

    public Page<Conta> getContas(Pageable pageable, LocalDate dataVencimento, String descricao) {
        return contaRepository.findAllByDataVencimentoAndDescricaoAndTipo(pageable, dataVencimento, descricao, TipoConta.A_PAGAR.getId());
    }

    public Optional<Conta> getContaById(Long id) {
        return contaRepository.findById(id);
    }

    public Double getValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim) {
        return contaRepository.findAll().stream()
                .filter(conta -> conta.getTipo() == 1)
                .filter(conta -> conta.getDataPagamento() != null && !conta.getDataPagamento().isBefore(inicio) && !conta.getDataPagamento().isAfter(fim))
                .mapToDouble(Conta::getValor)
                .sum();
    }

    public ContaValorPeriodoResponse obterValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim) {
        Double valorTotal = getValorTotalPagoPorPeriodo(inicio, fim);
        return new ContaValorPeriodoResponse(valorTotal);
    }

    @Async
    public void importarContasCsv(File csvFile) throws IOException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                .build()) {

            String[] nextLine;
            List<Conta> chunk = new ArrayList<>();
            int chunkSize = 100;  // Ajuste o tamanho do chunk conforme necessário

            while ((nextLine = reader.readNext()) != null) {
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(nextLine[0]));
                conta.setDataPagamento(LocalDate.parse(nextLine[1]));
                conta.setValor(Double.parseDouble(nextLine[2]));
                conta.setDescricao(nextLine[3]);
                conta.setSituacao(nextLine[4]);
                conta.setTipo(Integer.parseInt(nextLine[5]));
                chunk.add(conta);

                if (chunk.size() == chunkSize) {
                    processChunk(chunk);
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                processChunk(chunk);
            }
        } catch (IOException | CsvValidationException e) {
            // Log ou trate a exceção conforme necessário
            e.printStackTrace();
        }
    }

    @Async
    private void processChunk(List<Conta> chunk) {
        contaRepository.saveAll(chunk);
    }

}