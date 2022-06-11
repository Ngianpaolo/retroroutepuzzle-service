package io.nigro.retroroutepuzzle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-junit.yml")
class RetroRoutePuzzleApplicationTests {

    @Test
    void contextLoads() {
    }

}
