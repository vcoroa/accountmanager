package br.com.vcoroa.accountmanager.controllers;

import br.com.vcoroa.accountmanager.entities.Conta;
import br.com.vcoroa.accountmanager.response.ContaValorPeriodoResponse;
import br.com.vcoroa.accountmanager.services.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contas a Pagar", description = "API para gerenciamento de contas a pagar")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Operation(summary = "Cria uma nova conta a pagar", description = "Cria uma nova conta a pagar no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta a pagar criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Conta> create(@Valid @RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.createConta(conta));
    }

    @Operation(summary = "Atualiza uma conta a pagar existente", description = "Atualiza uma conta a pagar existente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta a pagar atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
            @ApiResponse(responseCode = "404", description = "Conta a pagar não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Conta> updateConta(@PathVariable Long id, @RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.updateConta(id, conta));
    }

    @Operation(summary = "Altera a situação de uma conta a pagar", description = "Altera a situação de uma conta a pagar no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Situação da conta a pagar alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
            @ApiResponse(responseCode = "404", description = "Conta a pagar não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/situacao")
    public ResponseEntity<Void> alterarSituacao(@PathVariable Long id, @RequestBody String situacao) {
        contaService.alterarSituacao(id, situacao);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtém todas as contas a pagar", description = "Obtém todas as contas a pagar do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contas a pagar encontradas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<Page<Conta>> getContas(
            @RequestParam(required = false) LocalDate dataVencimento,
            @RequestParam(required = false) String descricao,
            Pageable pageable) {
        return ResponseEntity.ok(contaService.getContas(pageable, dataVencimento, descricao));
    }

    @Operation(summary = "Obtém uma conta a pagar por ID", description = "Obtém uma conta a pagar por ID do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta a pagar encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta a pagar não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Conta> getContaById(@PathVariable Long id) {
        Optional<Conta> conta = contaService.getContaById(id);
        return conta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtém o valor total pago por período", description = "Obtém o valor total pago por período do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total pago por período obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/valor-total-pago")
    public ResponseEntity<ContaValorPeriodoResponse> getValorTotalPagoPorPeriodo(@RequestParam("inicio") String inicio, @RequestParam("fim") String fim) {
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        return ResponseEntity.ok(contaService.obterValorTotalPagoPorPeriodo(dataInicio, dataFim));
    }

    @Operation(summary = "Importa contas a pagar a partir de um arquivo CSV", description = "Importa contas a pagar a partir de um arquivo CSV para o sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contas a pagar importadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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