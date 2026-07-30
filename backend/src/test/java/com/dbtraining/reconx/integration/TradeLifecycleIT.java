package com.dbtraining.reconx.integration;

import com.dbtraining.reconx.dto.LoginRequest;
import com.dbtraining.reconx.dto.LoginResponse;
import com.dbtraining.reconx.dto.TradeRequest;
import com.dbtraining.reconx.dto.TradeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TradeLifecycleIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFullTradeLifecycle() {
        // 1. Login as trader
        LoginRequest loginReq = new LoginRequest("trader@reconx.com", "trader123");
        ResponseEntity<LoginResponse> loginRes = restTemplate.postForEntity("/auth/login", loginReq, LoginResponse.class);
        assertThat(loginRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = loginRes.getBody().token();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // 2. Create trade
        TradeRequest tradeReq =
                                new TradeRequest(
                                    "ABC-20260730-0002",
                                    1L,
                                    1L,
                                    "EQUITY",
                                    "BUY",
                                    BigDecimal.valueOf(100),
                                    BigDecimal.valueOf(150),
                                    LocalDate.now()
                                );
        HttpEntity<TradeRequest> createEntity = new HttpEntity<>(tradeReq, headers);
        ResponseEntity<TradeResponse> createRes = restTemplate.postForEntity("/v1/trades", createEntity, TradeResponse.class);
        
        assertThat(createRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = createRes.getHeaders().getLocation().toString();

        // 3. Get list of trades and verify it's there
        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<String> getRes = restTemplate.exchange("/v1/trades", HttpMethod.GET, getEntity, String.class);
        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRes.getBody()).contains("T-IT-1");

        // 4. Soft delete
        Long tradeId = createRes.getBody().id();
        ResponseEntity<Void> deleteRes = restTemplate.exchange("/v1/trades/" + tradeId, HttpMethod.DELETE, getEntity, Void.class);
        assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 5. Verify it's no longer in the list
        ResponseEntity<String> getResAfterDelete = restTemplate.exchange("/v1/trades", HttpMethod.GET, getEntity, String.class);
        assertThat(getResAfterDelete.getBody()).doesNotContain("\"tradeRef\":\"T-IT-1\"");
    }
}
