package br.unipar.gerenciadorcaixas.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

// Identifica esta classe como uma entidade persistida no banco de dados.
@Entity
 class CaixaDaAgua(
   // Define a chave primária da entidade.
   @Id
    // Gera o identificador automaticamente usando uma sequência do banco.
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // O identificador pode ficar nulo antes do cadastro.
    val id: Long? = null,
   val marca: String,
   val modelo: String,
   // MutableList<Double> armazena medidas decimais em uma lista que pode ser alterada.
   val dimensao: MutableList<Double>,
   val cor: Cor,
   val material: Material,
   val formato: String,
   // BigDecimal representa valores decimais, adequado para dinheiro.
   val preco: BigDecimal,

   )
