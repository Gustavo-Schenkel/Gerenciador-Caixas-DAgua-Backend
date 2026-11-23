package br.unipar.gerenciadorcaixas.model

import jakarta.persistence.Entity
import java.math.BigDecimal

// Define uma entidade que será persistida no banco.
@Entity
class Funcionario(

    // Identificador e CPF serão recebidos pela classe Pessoa.
    id: Long = 0L,
    cpf: String,

    // Nome do funcionário.
    val nome: String,

    // BigDecimal representa o valor monetário do salário.
    val salario: BigDecimal = BigDecimal.ZERO

// Herda o identificador e o CPF de Pessoa.
) : Pessoa(id, cpf)