package br.unipar.gerenciadorcaixas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// Ativa a configuração automática e a descoberta dos componentes do Spring Boot.
@SpringBootApplication
class GerenciadorcaixasApplication

// Inicia a execução da aplicação.
fun main(args: Array<String>) {
	runApplication<GerenciadorcaixasApplication>(*args)
}
