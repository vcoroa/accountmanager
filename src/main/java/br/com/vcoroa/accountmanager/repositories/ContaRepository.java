package br.com.vcoroa.accountmanager.repositories;

import br.com.vcoroa.accountmanager.entities.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Page<Conta> findAllByDataVencimentoAndDescricaoAndTipo(Pageable pageable, LocalDate dataVencimento, String descricao, Integer tipo);
}