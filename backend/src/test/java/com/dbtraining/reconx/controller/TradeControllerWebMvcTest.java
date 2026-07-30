package com.dbtraining.reconx.controller;

import com.dbtraining.reconx.dto.TradeMapper;
import com.dbtraining.reconx.dto.TradeRequest;
import com.dbtraining.reconx.repository.entity.Trade;
import com.dbtraining.reconx.security.JwtAuthenticationFilter;
import com.dbtraining.reconx.security.JwtTokenProvider;
import com.dbtraining.reconx.security.SecurityConfig;
import com.dbtraining.reconx.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class TradeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeMapper tradeMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(roles = "TRADER")
    void create_whenAuthenticatedAsTrader_returns201() throws Exception {
        TradeRequest req = new TradeRequest("T-123", "CP-A", "IBM", BigDecimal.TEN, BigDecimal.valueOf(150), LocalDate.now());
        Trade mockTrade = new Trade("T-123", "CP-A", "IBM", BigDecimal.TEN, BigDecimal.valueOf(150), LocalDate.now());
        
        when(tradeService.create(any(), any())).thenReturn(mockTrade);

        mockMvc.perform(post("/v1/trades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_whenUnauthenticated_returns401() throws Exception {
        TradeRequest req = new TradeRequest("T-123", "CP-A", "IBM", BigDecimal.TEN, BigDecimal.valueOf(150), LocalDate.now());

        mockMvc.perform(post("/v1/trades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void create_whenAuthenticatedAsViewer_returns403() throws Exception {
        TradeRequest req = new TradeRequest("T-123", "CP-A", "IBM", BigDecimal.TEN, BigDecimal.valueOf(150), LocalDate.now());

        mockMvc.perform(post("/v1/trades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}
