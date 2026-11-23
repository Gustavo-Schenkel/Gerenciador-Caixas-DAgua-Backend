package br.unipar.gerenciadorcaixas.controller

import br.unipar.gerenciadorcaixas.model.Cliente
import br.unipar.gerenciadorcaixas.service.ClienteService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// Recebe requisições HTTP e devolve dados no corpo da resposta.
@RestController
// Define o prefixo das rotas desta controladora.
@RequestMapping("/cliente")
// Permite configurar o acesso do frontend a estas rotas pelo navegador.
@CrossOrigin(origins = ["*"])
class ClienteController(
    // Recebe o serviço que executa as operações da aplicação.
    private val service: ClienteService
) {
    // Define uma rota POST para receber e salvar dados.
    @PostMapping("/salvar")
    // Recebe os dados em JSON e os encaminha para salvar.
    fun salvar(@RequestBody cliente: Cliente): ResponseEntity<Cliente> {
        // Devolve status 201, indicando criação de registro.
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.salvar(cliente))
    }

    // Define uma rota GET para consultar dados.
    @GetMapping("/listar")
    // Encaminha a consulta de todos os registros para o serviço.
    fun listar(): List<Cliente> {
        return service.listar()
    }
}
