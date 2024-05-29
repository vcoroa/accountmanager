package br.com.vcoroa.accountmanager.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaValorPeriodoResponse {
    private Double valorTotalPago;
}
