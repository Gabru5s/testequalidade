package qualidadedesoftware.projetoqa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestProjetoQaApplication {

    public static void main(String[] args) {
        SpringApplication.from(ProjetoQaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
