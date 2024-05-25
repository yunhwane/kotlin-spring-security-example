package com.example.testcode
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.test.context.junit.jupiter.SpringExtension
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestConstructor


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class ApiTest: DescribeSpec() {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup


    init {
        beforeSpec {
            if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
                RestAssured.port = port
            }
        }

        beforeTest {
            databaseCleanup.afterPropertiesSet()
            databaseCleanup.execute()
        }
    }
}