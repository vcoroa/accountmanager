package br.com.vcoroa.accountmanager.controllers;

import br.com.vcoroa.accountmanager.entities.Conta;
import br.com.vcoroa.accountmanager.response.ContaValorPeriodoResponse;
import br.com.vcoroa.accountmanager.services.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> create(@Valid @RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.createConta(conta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> updateConta(@PathVariable Long id, @RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.updateConta(id, conta));
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<Void> alterarSituacao(@PathVariable Long id, @RequestBody String situacao) {
        contaService.alterarSituacao(id, situacao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Conta>> getContas(
            @RequestParam(required = false) LocalDate dataVencimento,
            @RequestParam(required = false) String descricao,
            Pageable pageable) {
        return ResponseEntity.ok(contaService.getContas(pageable, dataVencimento, descricao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> getContaById(@PathVariable Long id) {
        Optional<Conta> conta = contaService.getContaById(id);
        return conta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/valor-total-pago")
    public ResponseEntity<ContaValorPeriodoResponse> getValorTotalPagoPorPeriodo(@RequestParam("inicio") String inicio, @RequestParam("fim") String fim) {
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        return ResponseEntity.ok(contaService.obterValorTotalPagoPorPeriodo(dataInicio, dataFim));
    }

    @PostMapping("/importar")
    public ResponseEntity<Void> importarContasCsv(@RequestParam("file") MultipartFile file) {
        try {
            File csvFile = convertMultiPartToFile(file);
            contaService.importarContasCsv(csvFile);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}