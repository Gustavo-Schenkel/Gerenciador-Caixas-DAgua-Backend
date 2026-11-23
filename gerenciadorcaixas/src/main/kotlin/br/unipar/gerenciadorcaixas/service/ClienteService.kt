package br.unipar.gerenciadorcaixas.service

import br.unipar.gerenciadorcaixas.model.Cliente
import br.unipar.gerenciadorcaixas.repository.ClienteRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

// Registra a classe como serviço gerenciado pelo Spring.
@Service
class ClienteService(

    // Acessa os registros de clientes no banco.
    private val repository: ClienteRepository,

    // Recebe o serviço que verifica o CPF e consulta a API externa.
    private val validar: ValidarCpf
) {

    fun salvar(cliente: Cliente): Cliente {

        // Impede o cadastro de nome vazio ou contendo somente espaços.
        if (cliente.nome.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O nome do cliente é obrigatório."
            )
        }

        // Confere os onze dígitos e solicita a aprovação da API.
        // Uma falha de comunicação também interrompe o cadastro.
        if (!validar.validarCpfViaApi(cliente.cpf)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "CPF do cliente não aprovado."
            )
        }

        // Preserva o identificador na edição e padroniza os dados.
        val clienteParaSalvar = Cliente(
            id = cliente.id,
            cpf = cliente.cpf.replace(Regex("[^0-9]"), ""),
            nome = cliente.nome.trim()
        )

        // Persiste o cliente somente depois das verificações.
        return repository.save(clienteParaSalvar)
    }

    // Consulta todos os clientes cadastrados.
    fun listar(): List<Cliente> {
        return repository.findAll()
    }
}