package br.unipar.gerenciadorcaixas.controller

import br.unipar.gerenciadorcaixas.model.CaixaDaAgua
import br.unipar.gerenciadorcaixas.model.Cor
import br.unipar.gerenciadorcaixas.model.Material
import br.unipar.gerenciadorcaixas.service.ServiceCaixaDaAgua
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController


// Permite configurar o acesso do frontend a estas rotas pelo navegador.
@CrossOrigin(
    // Permite acesso de qualquer origem nas requisições sem credenciais.
    origins = ["*"],

)

// Registra a controladora que recebe as requisições HTTP.
@Controller
// Define o prefixo das rotas desta controladora.
@RequestMapping("/caixa")
class ControllerCaixaDaAgua(
    // Recebe o serviço que executa as operações da aplicação.
    private val servico : ServiceCaixaDaAgua
){
    // Define uma rota GET para consultar dados.
    @GetMapping("/listar")
    // Encaminha a consulta de todos os registros para o serviço.
    fun listar() : ResponseEntity<List<CaixaDaAgua>>{
        return servico.listar()
    }

    // Define uma rota GET para consultar dados.
    @GetMapping("/buscar/{id}")
    // Recebe o identificador na URL e solicita o registro ao serviço.
    fun buscar(@PathVariable id : Long) : ResponseEntity<CaixaDaAgua>{
        return servico.buscar(id)
    }

    // Define uma rota POST para receber e salvar dados.
    @PostMapping("/salvar")
    // Recebe o JSON de uma caixa e solicita seu salvamento.
    fun salvarCaixaDaAgua(@RequestBody caixa: CaixaDaAgua) : ResponseEntity<String>{
        return servico.salvar(caixa)
    }

    // Define uma rota DELETE para excluir dados.
    @DeleteMapping("/excluir/{id}")
    // Recebe o identificador na URL e solicita a exclusão.
    fun excluir(@PathVariable id : Long) : ResponseEntity<Void>{
        return servico.excluir(id)

    }

    // Diferencia a busca por marca da busca por identificador.
    @GetMapping("/buscar/marca/{marca}")
    // Pesquisa caixas pela marca recebida na URL.
    fun buscarMarca(@PathVariable marca: String) : ResponseEntity<List<CaixaDaAgua>>{
        return ResponseEntity.ok(servico.buscarMarca(marca))
    }

    // Recebe a cor pela URL e devolve as caixas encontradas.
    @GetMapping("/buscar/cor/{cor}")
    fun buscarCor(
        @PathVariable("cor") cor: Cor
    ): ResponseEntity<List<CaixaDaAgua>> {
        return ResponseEntity.ok(servico.buscarCor(cor))
    }
    // Disponibiliza o relatório no endereço /caixa/relatorio.
    @GetMapping("/relatorio", produces = ["text/csv"])
    fun baixarRelatorio(): ResponseEntity<ByteArray> {

        // Converte o relatório textual em bytes para enviar como arquivo.
        val arquivo = servico.gerarRelatorioCsv()
            .toByteArray(Charsets.UTF_8)

        return ResponseEntity.ok()

            // attachment orienta o navegador a baixar o arquivo.
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=caixas.csv"
            )

            // Informa que o conteúdo é CSV com caracteres em UTF-8.
            .contentType(
                MediaType("text", "csv", Charsets.UTF_8)
            )

            .body(arquivo)
    }

}


