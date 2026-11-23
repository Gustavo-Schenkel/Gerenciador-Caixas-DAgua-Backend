package br.unipar.gerenciadorcaixas.service

import br.unipar.gerenciadorcaixas.model.Funcionario
import br.unipar.gerenciadorcaixas.repository.FuncionarioRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

// Registra a classe como serviço gerenciado pelo Spring.
@Service
class FuncionarioService(

    // Acessa os registros de funcionários no banco.
    private val repository: FuncionarioRepository,

    // Reutiliza o serviço de validação e consulta externa de CPF.
    private val validar: ValidarCpf
) {

    fun salvar(funcionario: Funcionario): Funcionario {

        // Rejeita um nome vazio ou contendo somente espaços.
        if (funcionario.nome.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O nome do funcionário é obrigatório."
            )
        }

        // Aceita salário zero, mas impede valores negativos.
        if (funcionario.salario < BigDecimal.ZERO) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O salário não pode ser negativo."
            )
        }

        // O funcionário só será salvo se o CPF passar pela API.
        if (!validar.validarCpfViaApi(funcionario.cpf)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "CPF do funcionário não aprovado."
            )
        }

        // Monta o registro com CPF sem pontuação e nome sem espaços nas extremidades.
        val funcionarioParaSalvar = Funcionario(
            id = funcionario.id,
            cpf = funcionario.cpf.replace(Regex("[^0-9]"), ""),
            nome = funcionario.nome.trim(),
            salario = funcionario.salario
        )

        return repository.save(funcionarioParaSalvar)
    }

    // Consulta todos os funcionários cadastrados.
    fun listar(): List<Funcionario> {
        return repository.findAll()
    }
}