package br.unipar.gerenciadorcaixas.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

// Faz o Spring criar e disponibilizar este serviço para outras classes,
// como PessoaService, que o utiliza antes de salvar uma pessoa.
@Service
class ValidarCpf {

    // Chave que identifica sua conta e autoriza as consultas na API.
    private val token = "f6d9c9592d262749bc83081335fda4e9"

    // Endereço ao qual será acrescentado o CPF consultado.
    private val url = "https://api.cpf-brasil.org/cpf"

    // Limita a espera: até 5 segundos para conectar e
    // até 10 segundos para ler a resposta do serviço externo.
    private val configuracao = SimpleClientHttpRequestFactory().apply {
        setConnectTimeout(5000)
        setReadTimeout(10000)
    }

    // RestTemplate envia requisições HTTP e converte as respostas recebidas.
    private val restTemplate = RestTemplate(configuracao)

    // Retorna true somente quando a API aprova a consulta.
    // Retorna false para CPF rejeitado e lança uma exceção se houver falha no serviço.
    fun validarCpfViaApi(cpf: String): Boolean {

        // A expressão regular identifica tudo que não é número.
        // replace remove esses caracteres, permitindo receber CPF com pontos e traço.
        val cpfLimpo = cpf.replace(Regex("[^0-9]"), "")

        // Esta conferência é local: evita consultar a API com um CPF incompleto.
        // Ter onze dígitos não comprova validade; a API fará a próxima verificação.
        if (cpfLimpo.length != 11) {
            println("CPF rejeitado: deve conter 11 dígitos.")
            return false
        }

        // Os cabeçalhos acompanham a requisição:
        // X-API-Key autentica a consulta e Accept solicita resposta em JSON.
        val headers = HttpHeaders().apply {
            set("X-API-Key", token)
            set("Accept", "application/json")
        }

        // HttpEntity reúne os dados enviados na requisição.
        // Void indica que não há corpo; estamos enviando somente os cabeçalhos.
        val requisicao = HttpEntity<Void>(headers)

        try {

            // Executa GET no endereço /cpf/NUMERO.
            // Map::class.java orienta a conversão do JSON em um mapa de chaves e valores.
            val resposta = restTemplate.exchange(
                "$url/$cpfLimpo",
                HttpMethod.GET,
                requisicao,
                Map::class.java
            )

            // body contém o JSON convertido. Uma resposta sem corpo
            // não fornece confirmação suficiente para aprovar o CPF.
            val corpo = resposta.body

            // Exige três condições: HTTP 200, success verdadeiro e data como mapa.
            // Isso evita considerar qualquer resposta HTTP 200 como aprovação.
            val aprovado =
                resposta.statusCode == HttpStatus.OK &&
                        corpo?.get("success") == true &&
                        corpo["data"] is Map<*, *>

            println("Consulta externa concluída. CPF aprovado: $aprovado")

            // PessoaService usa este resultado para permitir ou impedir o salvamento.
            return aprovado

        } catch (erro: HttpClientErrorException) {

            // Captura respostas HTTP 4xx da API.
            // Nesta consulta, 400 ou 404 representam CPF inválido ou não encontrado.
            if (
                erro.statusCode == HttpStatus.BAD_REQUEST ||
                erro.statusCode == HttpStatus.NOT_FOUND
            ) {
                println("CPF rejeitado pela API.")
                return false
            }

            // Outros erros 4xx podem indicar problema de autenticação ou de cota.
            // Interrompe a operação sem apresentar essa falha como CPF aprovado.
            println("API recusou a consulta: HTTP ${erro.statusCode.value()}")

            throw ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "A API recusou a consulta. Confira o token e a cota."
            )

        } catch (erro: RestClientException) {

            // Trata falhas como conexão indisponível, tempo de espera esgotado
            // ou erro ao processar a resposta. HTTP 503 informa indisponibilidade.
            println("Falha na comunicação com a API de CPF.")

            throw ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Não foi possível consultar a API de CPF."
            )
        }
    }
}