package com.example.service;

import com.example.service.grpc.GreetingRequest;
import com.example.service.grpc.GreetingResponse;
import com.example.service.grpc.GreetingsGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }


    @Bean
    @GlobalServerInterceptor
    AuthenticationProcessInterceptor authenticationProcessInterceptor(GrpcSecurity security) throws Exception {
        return security
                .authorizeRequests(a -> a.methods("Greetings/hello").authenticated())
                .authorizeRequests(a -> a.methods("grpc.*/*").permitAll())
                .oauth2ResourceServer(a -> a.jwt(Customizer.withDefaults()))
                .build();
    }

}

@Service
class GreetingsImpl extends GreetingsGrpc.GreetingsImplBase {

    @Override
    public void hello(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        var user = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        responseObserver
                .onNext(GreetingResponse.newBuilder().setMessage("Hello " + user)
                        .build());
        responseObserver.onCompleted();
    }
}