package br.com.vcoroa.accountmanager.controllers;

import br.com.vcoroa.accountmanager.entities.Conta;
import br.com.vcoroa.accountmanager.helper.ContaTestHelper;
import br.com.vcoroa.accountmanager.response.ContaValorPeriodoResponse;
import br.com.vcoroa.accountmanager.services.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContaControllerTest {

    @InjectMocks
    ContaController contaController;

    @Mock
    ContaService contaService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @MockBean
    private PageRequest pageRequest;
    private PageRequest pageRequestWithSorting;
    private Page<Conta> contasPage;
    private List<Conta> contas;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contaController).build();

        // Configura o ObjectMapper para usar o JavaTimeModule
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @Test
    public void shouldCreateConta_whenValidContaIsGiven() throws Exception {

        Conta contaAPagarFake = ContaTestHelper.buildContaFake();

        when(contaService.createConta(any(Conta.class))).thenReturn(contaAPagarFake);

        String contaJson = objectMapper.writeValueAsString(contaAPagarFake);

        mockMvc.perform(post("/api/contas")
            .contentType(MediaType.APPLICATION_JSON)
                .content(contaJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valor").value(contaAPagarFake.getValor()))
                    .andExpect(jsonPath("$.descricao").value(contaAPagarFake.getDescricao()))
                    .andExpect(jsonPath("$.tipo").value(contaAPagarFake.getTipo()));
    }

    @Test
    public void shouldUpdateConta_whenValidContaAndIdAreGiven() throws Exception {
        Conta contaFake = ContaTestHelper.buildContaFake();

        when(contaService.updateConta(anyLong(), any(Conta.class))).thenReturn(contaFake);

        String contaJson = objectMapper.writeValueAsString(contaFake);

        mockMvc.perform(put("/api/contas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(contaFake.getValor()))
                .andExpect(jsonPath("$.descricao").value(contaFake.getDescricao()))
                .andExpect(jsonPath("$.tipo").value(contaFake.getTipo()));
    }

    @Test
    public void shouldAlterarSituacao_whenValidIdAndSituacaoAreGiven() throws Exception {
        mockMvc.perform(patch("/api/contas/1/situacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"PAGA\""))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldFetchAllTheContasInDatabase() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(1, 10);
        LocalDate dataVencimento = LocalDate.now();
        String descricao = "Teste";
        Integer tipo = 1; // Tipo de conta 1
        Page<Conta> contas = mock(Page.class);
        when(contaService.getContas(pageable, dataVencimento, descricao)).thenReturn(contas);

        // Act
        mockMvc.perform(get("/api/contas?page=1&size=10&sort=valor,asc&dataVencimento=" + dataVencimento + "&descricao=" + descricao + "&tipo=" + tipo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert
        verify(contaService).getContas(pageable, dataVencimento, descricao);
        verifyNoMoreInteractions(contaService);
    }

    @Test
    public void testGetValorTotalPagoPorPeriodo() throws Exception {
        LocalDate inicio = LocalDate.now().minusDays(10);
        LocalDate fim = LocalDate.now();
        Double valorTotal = 1000.0;
        ContaValorPeriodoResponse valorTotalResponse = new ContaValorPeriodoResponse(valorTotal);

        when(contaService.obterValorTotalPagoPorPeriodo(any(LocalDate.class), any(LocalDate.class))).thenReturn(valorTotalResponse);

        mockMvc.perform(get("/api/contas/valor-total-pago?inicio=" + inicio.toString() + "&fim=" + fim.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotalPago").value(valorTotal));
    }

    @Test
    public void shouldReturnConta_whenValidIdIsGiven() throws Exception {
        Conta contaAPagarFake = ContaTestHelper.buildContaFake();

        when(contaService.getContaById(anyLong())).thenReturn(Optional.of(contaAPagarFake));

        mockMvc.perform(get("/api/contas/1")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(contaAPagarFake.getValor()))
                .andExpect(jsonPath("$.descricao").value(contaAPagarFake.getDescricao()))
                .andExpect(jsonPath("$.tipo").value(contaAPagarFake.getTipo()));
    }
}