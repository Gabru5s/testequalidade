package QualidadeDeSoftware.ProjetoQA;

import org.springframework.boot.SpringApplication;

public class TestProjetoQaApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProjetoQaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
