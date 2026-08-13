package br.com.fullcycle.application.hexagonal;

import br.com.fullcycle.application.infrastructure.Main;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("tests")
@SpringBootTest(classes = Main.class)
public abstract class IntegrationTest {
}
