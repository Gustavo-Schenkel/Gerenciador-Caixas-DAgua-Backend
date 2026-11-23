package br.unipar.gerenciadorcaixas.service

import br.unipar.gerenciadorcaixas.model.CaixaDaAgua
import br.unipar.gerenciadorcaixas.repository.CaixaDaAguaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import br.unipar.gerenciadorcaixas.model.Cor

// Registra a classe como serviço para o Spring gerenciar sua instância.
@Service
class ServiceCaixaDaAgua(
   val repository: CaixaDaAguaRepository
){
    // Salva a entidade; um identificador existente permite atualizar o cadastro.
    fun salvar(caixa : CaixaDaAgua) : ResponseEntity<String>{
        repository.save(caixa)
        // Devolve status 201, indicando criação de registro.
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Caixa de Água foi salva no banco!")
    }

    // Consulta todos os registros no banco.
    fun listar() : ResponseEntity<List<CaixaDaAgua>>{
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll())
    }

    // Busca pelo identificador e lança uma exceção se não encontrar.
    fun buscar(id : Long) : ResponseEntity<CaixaDaAgua>{
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

    // Consulta caixas cujo campo marca contenha o texto informado.
    fun buscarMarca(marca : String): List<CaixaDaAgua>{
        return repository.findByMarcaContainingIgnoreCase(marca)
    }

    // Consulta no banco as caixas da cor selecionada.
    fun buscarCor(cor: Cor): List<CaixaDaAgua> {
        return repository.findByCor(cor)
    }
    // Prepara um valor para ocupar uma coluna do CSV.
    private fun campoCsv(valor: Any?): String {
        var texto = valor?.toString() ?: ""

        // Impede que textos cadastrados sejam interpretados como fórmulas.
        if (texto.trimStart().firstOrNull() in listOf('=', '+', '-', '@')) {
            texto = "'" + texto
        }

        // Protege textos que contenham aspas, ponto e vírgula ou quebra de linha.
        return "\"" + texto.replace("\"", "\"\"") + "\""
    }

    // Consulta as caixas e monta o conteúdo do relatório.
    fun gerarRelatorioCsv(): String {
        val caixas = repository.findAll()

        // StringBuilder permite acrescentar texto ao relatório progressivamente.
        val relatorio = StringBuilder()

        // Ajuda o Excel a reconhecer os acentos do arquivo UTF-8.
        relatorio.append('\uFEFF')

        // Define os nomes das colunas. O ponto e vírgula separa cada coluna.
        relatorio.append(
            "Código;Marca;Modelo;Dimensões;Cor;Material;Formato;Preço\r\n"
        )

        for (caixa in caixas) {
            // Organiza os dados de uma caixa na ordem das colunas.
            val campos = listOf(
                caixa.id,
                caixa.marca,
                caixa.modelo,
                caixa.dimensao.joinToString(" x "),
                caixa.cor,
                caixa.material,
                caixa.formato,
                caixa.preco.toPlainString().replace('.', ',')
            )

            // Converte os valores em colunas e acrescenta uma linha ao arquivo.
            val linha = campos.joinToString(";") {
                campoCsv(it)
            }

            relatorio.append(linha).append("\r\n")
        }

        return relatorio.toString()
    }
 }
