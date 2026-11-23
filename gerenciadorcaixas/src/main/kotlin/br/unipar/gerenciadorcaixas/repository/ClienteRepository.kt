package br.unipar.gerenciadorcaixas.repository

import br.unipar.gerenciadorcaixas.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository

// JpaRepository fornece as operações de salvar, listar, buscar e excluir esta entidade.
interface ClienteRepository : JpaRepository<Cliente, Long>
