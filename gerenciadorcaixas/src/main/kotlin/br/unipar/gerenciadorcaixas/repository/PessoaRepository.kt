package br.unipar.gerenciadorcaixas.repository

import br.unipar.gerenciadorcaixas.model.Pessoa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

// Identifica um componente de acesso ao banco de dados.
@Repository
// JpaRepository fornece as operações de salvar, listar, buscar e excluir esta entidade.
interface PessoaRepository : JpaRepository<Pessoa, Long> {
    // Declara a consulta por igualdade no campo CPF.
    fun findByCpf(cpf: String): ResponseEntity<List<Pessoa>>
}
