package com.jschapin.dregs;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ExampleLambda {

    record Config(String message, Integer statusCode) {
        Config {
            Objects.requireNonNull(message);
            statusCode = Optional.ofNullable(statusCode).orElse(200);
        }
    }

    final Config config;


    public ExampleLambda() {
        this(System.getenv());
    }

    public ExampleLambda(Map<String, String> environment) {
        this.config = MappedRecord.fromMap(environment, Config.class);
    }

    public APIGatewayProxyResponseEvent handler(APIGatewayProxyRequestEvent event) {
        return new APIGatewayProxyResponseEvent()
                .withBody(config.message)
                .withStatusCode(config.statusCode);
    }

}
