package br.unipar.gerenciadorcaixas.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

// Identifica esta classe como uma entidade persistida no banco de dados.
@Entity
// JOINED salva os dados da mãe e dos filhos em tabelas ligadas pelo mesmo identificador.
@Inheritance(strategy = InheritanceType.JOINED)
// open permite que Cliente e Funcionario herdem de Pessoa.
open class Pessoa(
    // Define a chave primária da entidade.
    @Id
    // Gera o identificador automaticamente usando uma sequência do banco.
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0L,
    val cpf: String
)
