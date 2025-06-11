package com.dom;

import com.dom.service.CakeConsumerService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        ApplicationContext context = Micronaut.run(Application.class, args);
        Environment env = context.getEnvironment();
        log.info("Application started in environments: {}", env.getActiveNames());

        // Load our cakes
        CakeConsumerService service = context.getBean(CakeConsumerService.class);
        service.loadCakes();
    }
}
