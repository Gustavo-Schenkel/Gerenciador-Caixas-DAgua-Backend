package br.unipar.gerenciadorcaixas.model

import jakarta.persistence.Entity

// Identifica esta classe como uma entidade persistida no banco de dados.
@Entity
class Cliente(
    id: Long = 0L,
    cpf: String,
    val nome: String
// Herda de Pessoa e envia o identificador e o CPF ao construtor da classe mãe.
) : Pessoa(id, cpf)
