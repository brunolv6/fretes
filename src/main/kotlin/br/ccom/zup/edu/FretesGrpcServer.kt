package br.ccom.zup.edu

import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
// se baseará nas classes stubs geradas a partir do *.proto
class FretesGrpcServer : FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    // sobreescrevendo o método que usaremos ou exposeremos
    override fun calculaFrete(request: CalculoFreteRequest?, responseObserver: StreamObserver<CalculoFreteResponse>?) {
        logger.info("Calculando frete para $request")

        val cep = request!!.cep

        if(cep == null || cep.isBlank()){
            val error = Status.INVALID_ARGUMENT
                                .withDescription("CEP deve ser informado")
                                .asRuntimeException()

            responseObserver?.onError(error)

            return
        }

        if(!cep.matches("[0-9]{5}-[\\d]{3}".toRegex())){
            responseObserver?.onError(Status.INVALID_ARGUMENT
                                .withDescription("CEP inválido")
                                .augmentDescription("formato esperado 99999-999")
                                .asRuntimeException())

            return
        }

        // simula erro de seguranca
        if(cep.endsWith("333")){
            val statusProto: com.google.rpc.Status = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.getNumber()) // com.google.rpc.Code
                .setMessage("sem permissao para acessar recuros")
                .addDetails(Any.pack(ErrorDetails.newBuilder() // any do protobuf
                    .setCode(401).setMessage("token expirado")
                    .build()))
                .build()

            responseObserver?.onError(StatusProto.toStatusRuntimeException(statusProto))

            return
        }

        var valor = 0.0
        try {
            valor = Random.nextDouble(0.0, 140.0)
            if(valor > 100.0){
                throw IllegalStateException("Erro Inesperado simulado") // do kotlin o erro
            }
        } catch (e: Exception) {
            responseObserver?.onError(Status.INTERNAL
                .withDescription(e.message)
                .withCause(e) // não será enviado ao cliente, apenas anexado ao Status
                .asRuntimeException())
            return
        }

        val response = CalculoFreteResponse.newBuilder()
            .setCep(request?.cep)
            .setValor(Random.nextDouble(0.0, 140.0))
            .build()

        logger.info("Frete calculado $response")

        responseObserver?.onNext(response)

        responseObserver?.onCompleted()
    }
}