package integration.testcontainer;

import com.academy.fintech.origination.Application;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class,
        GrpcServerFactoryAutoConfiguration.class,
        GrpcClientAutoConfiguration.class,
        Application.class})
public class ServiceIntegrationConfig {
}
