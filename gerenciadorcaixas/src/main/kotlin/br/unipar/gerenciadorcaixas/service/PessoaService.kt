package br.unipar.gerenciadorcaixas.service

import br.unipar.gerenciadorcaixas.model.Pessoa
import br.unipar.gerenciadorcaixas.repository.PessoaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

// Registra a classe como serviço para o Spring gerenciar sua instância.
@Service
class PessoaService (
    // Recebe o repositório usado para acessar o banco.
    private val repository: PessoaRepository,
    // Recebe o serviço responsável pela consulta de CPF.
    private val validar : ValidarCpf
){
        // Salva a entidade; um identificador existente permite atualizar o cadastro.
        fun salvar(pessoa : Pessoa) : ResponseEntity<String> {
            // Só segue para salvar se a função de consulta devolver verdadeiro.
            if (validar.validarCpfViaApi(pessoa.cpf.toString())) {
                println("Cpf validado com sucesso!")
                repository.save(pessoa)
                // Devolve status 201, indicando criação de registro.
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Pessoa foi salva no banco!")
            } else {
                println("Cpf invalido!")
                // Devolve status 409 quando o CPF é rejeitado pela função de validação.
                return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }
        }

        // Consulta todos os registros no banco.
        fun listar() : ResponseEntity<List<Pessoa>>{
            return ResponseEntity.status(HttpStatus.OK).body(repository.findAll())
        }

        // Busca pelo identificador e lança uma exceção se não encontrar.
        fun buscar(id : Long) : ResponseEntity<Pessoa>{
            val resposta = repository.findById(id).orElseThrow{
                NoSuchElementException("O $id digitado não é válido.")
            }
            return ResponseEntity.status(HttpStatus.OK).
            body(resposta)
        }

        // Exclui o registro pelo identificador.
        fun excluir(id : Long) : ResponseEntity<Void>{
            repository.deleteById(id)
            // Devolve status 204: sucesso sem conteúdo no corpo.
            return ResponseEntity.noContent().build()

        }

        // Encaminha a busca por CPF ao repositório.
        fun buscarCpf(cpf : String): ResponseEntity<List<Pessoa>>{
            return repository.findByCpf(cpf)
        }
}

