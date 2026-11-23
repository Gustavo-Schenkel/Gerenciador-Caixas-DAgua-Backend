package br.unipar.gerenciadorcaixas.repository

import br.unipar.gerenciadorcaixas.model.Funcionario
import org.springframework.data.jpa.repository.JpaRepository

// JpaRepository fornece as operações de salvar, listar, buscar e excluir esta entidade.
interface FuncionarioRepository : JpaRepository<Funcionario, Long>
