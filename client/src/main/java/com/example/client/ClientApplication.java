package com.example.client;

import com.example.service.grpc.GreetingRequest;
import com.example.service.grpc.GreetingsGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.grpc.client.ImportGrpcClients;
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@ImportGrpcClients(basePackageClasses = GreetingsGrpc.class, target = "localhost:9090")
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    private final SecurityContextHolderStrategy strategy = SecurityContextHolder
            .getContextHolderStrategy();

    @Bean
    GrpcChannelBuilderCustomizer<?> grpcChannelBuilderCustomizer(OAuth2AuthorizedClientManager authorizedClientManager) {
        return GrpcChannelBuilderCustomizer
                .matching("localhost:9090", builder -> builder.intercept(
                        new BearerTokenAuthenticationInterceptor(() -> this.token(authorizedClientManager))));
    }

    String token(OAuth2AuthorizedClientManager authorizedClientManager) {
        var authenticatedUser = this.strategy.getContext().getAuthentication();
        if (authenticatedUser instanceof OAuth2AuthenticationToken auth2AuthenticationToken) {
            var clientId = auth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauthAuthorizationRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientId)
                    .principal(authenticatedUser)
                    .build();
            var authorized = authorizedClientManager.authorize(oauthAuthorizationRequest);
            return Objects.requireNonNull(authorized).getAccessToken().getTokenValue();
        }
        return null;
    }
}

@Controller
@ResponseBody
class GrpcClientController {

    private final GreetingsGrpc.GreetingsBlockingStub stub;

    GrpcClientController(GreetingsGrpc.GreetingsBlockingStub stub) {
        this.stub = stub;
    }

    @GetMapping("/")
    String greet() {
        return this.stub.hello(GreetingRequest
                        .newBuilder()
                        .setName("World")
                        .build())
                .getMessage();
    }
}
