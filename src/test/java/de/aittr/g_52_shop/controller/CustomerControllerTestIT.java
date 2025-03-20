package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
class CustomerControllerTestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${key.access}")
    private String accessPhrase;

    @Autowired
    private CustomerRepository repository;

    private final String BEARER_PREFIX = "Bearer ";

    private CustomerDto testCustomer;
    private String adminAccessToken;
    private SecretKey accessKey;

    @BeforeEach
    public void setUp() {
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        adminAccessToken = generateAdminAccessToken();
        testCustomer = createTestCustomer();
    }

    // Тест проверяет доступность и корректность объектов пользователей, получаемых по запросу
    // GET на endpoint /customers/all

    @Test
    @Order(1)
    public void checkRequestForAllCustomersWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<CustomerDto[]> response = restTemplate.exchange(
                "/customers/all", HttpMethod.GET, request, CustomerDto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody(), "Response body should not be null");

        for (CustomerDto customer : response.getBody()) {
            assertNotNull(customer.getId(), "Product id should not be null");
            assertNotNull(customer.getName(), "Customer name should not be null");
            assertNotNull(customer.getCart(), "Customer cart should not be null");
        }
    }

    // Тест проверяет корректность сохранения объектов пользователей при помощи запроса POST на endpoint /customers

    @Test
    @Order(2)
    public void  checkSuccessWhileSavingCustomerWithAdminToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<CustomerDto> request = new HttpEntity<>(testCustomer, headers);

        ResponseEntity<CustomerDto> response = restTemplate.exchange(
                "/customers", HttpMethod.POST, request, CustomerDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");

        CustomerDto savedCustomer = response.getBody();

        assertNotNull(savedCustomer, "Saved customer should not be null");
        assertNotNull(savedCustomer.getId(), "Saved customer id should not be null");
        assertEquals(testCustomer.getName(), savedCustomer.getName(), "Customer name is incorrect");

        repository.deleteById(savedCustomer.getId());

    }

    // Тест проверяет корректность сохранения товара в корзину покупателя при помощи запроса PUT на endpoint /customers

    @Test
    @Order(3)
    public void checkSuccessWhileAddingProductToCustomerCart() {

        Long customerId = 6L;
        Long productId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/customers/" + customerId + "/add-product/" + productId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected HTTP status");
    }


    // Вспомогательные методы

    // Создаёт тестовый объект покупателя

    private CustomerDto createTestCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setName("John Doe");
        return customer;
    }

    // Возвращает токен администратора

    private String generateAdminAccessToken () {
        Role role = new Role();
        role.setTitle("ROLE_ADMIN");

        return BEARER_PREFIX + Jwts.builder()
                .subject("TestAdmin")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("roles", Set.of(role))
                .signWith(accessKey)
                .compact();
    }

}

// TODO: проверить и разобрать третий тест!!!
/*
Вот этот тест вообще не будет корректно работать. Где гарантия, что когда тест запускается,
в БД действительно существует покупатель с идентификатором 6 и продукт с идентификатором 1?
Такой гарантии нет, этот тест будет падать, причём падать необоснованно.
 */