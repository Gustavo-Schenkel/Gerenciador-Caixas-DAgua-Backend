package br.unipar.gerenciadorcaixas.repository

import br.unipar.gerenciadorcaixas.model.CaixaDaAgua
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity

import br.unipar.gerenciadorcaixas.model.Cor

// JpaRepository fornece as operações de salvar, listar, buscar e excluir esta entidade.
interface CaixaDaAguaRepository : JpaRepository<CaixaDaAgua, Long>{
        // Pesquisa um trecho da marca, ignorando maiúsculas e minúsculas.
        fun findByMarcaContainingIgnoreCase(marca: String): List<CaixaDaAgua>

        // Pesquisa caixas que possuem exatamente a cor informada.
        fun findByCor(cor: Cor): List<CaixaDaAgua>
}
