package br.ccom.zup.edu

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

import javax.inject.Inject

// testando o desligar o grpc
@Controller // servico embaracado do grpc que subimos com esta aplicação
class GrpcServerController(@Inject val grpcServer: GrpcEmbeddedServer) {

    @Get("/grpc/stop")
    fun stop(): HttpResponse<String> {
        grpcServer.stop()

        return HttpResponse.ok("is running? ${grpcServer.isRunning}")
    }
}