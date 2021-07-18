package br.ccom.zup.edu

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

        val response = CalculoFreteResponse.newBuilder()
            .setCep(request?.cep)
            .setValor(Random.nextDouble(0.0, 140.0))
            .build()

        logger.info("Frete calculado $response")

        responseObserver?.onNext(response)

        responseObserver?.onCompleted()
    }
}