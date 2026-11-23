package br.unipar.gerenciadorcaixas.controller

import br.unipar.gerenciadorcaixas.model.Pessoa
import br.unipar.gerenciadorcaixas.service.PessoaService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

// Permite configurar o acesso do frontend a estas rotas pelo navegador.
@CrossOrigin(
    // Permite acesso de qualquer origem nas requisições sem credenciais.
    origins = ["*"],

    )


// Registra a controladora que recebe as requisições HTTP.
@Controller
// Define o prefixo das rotas desta controladora.
@RequestMapping("/pessoa")
class PessoaController (
    // Recebe o serviço que executa as operações da aplicação.
    private val servico: PessoaService
) {
    // Define uma rota GET para consultar dados.
    @GetMapping("/listar")
    // Encaminha a consulta de todos os registros para o serviço.
    fun listar() : ResponseEntity<List<Pessoa>>{
        return servico.listar()
    }

    // Define uma rota GET para consultar dados.
    @GetMapping("/buscar/{id}")
    // Recebe o identificador na URL e solicita o registro ao serviço.
    fun buscar(@PathVariable id : Long) : ResponseEntity<Pessoa>{
        return servico.buscar(id)
    }

    // Define uma rota POST para receber e salvar dados.
    @PostMapping("/salvar")
    // Recebe o JSON de uma pessoa e solicita seu salvamento.
    fun salvarPessoa(@RequestBody caixa: Pessoa) : ResponseEntity<String>{
        return servico.salvar(caixa)
    }

    // Define uma rota DELETE para excluir dados.
    @DeleteMapping("/excluir/{id}")
    // Recebe o identificador na URL e solicita a exclusão.
    fun excluir(@PathVariable id : Long) : ResponseEntity<Void>{
        return servico.excluir(id)

    }

    // Define uma rota GET para consultar dados.
    @GetMapping("/buscar/cpf/{cpf}")
    // Pesquisa pelo CPF recebido na URL.
    fun buscarMarca(@PathVariable cpf: String) : ResponseEntity<List<Pessoa>>{
        return servico.buscarCpf(cpf)
    }
}
