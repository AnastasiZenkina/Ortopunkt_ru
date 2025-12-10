package com.ortopunkt.analyticsservice.service.crm;

import com.ortopunkt.analyticsservice.client.AnalyticsClient;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    private AnalyticsClient client;
    private ApplicationsAggregator aggregator;
    private AnalyticsService service;

    @BeforeEach
    void setUp() {
        client = Mockito.mock(AnalyticsClient.class);
        aggregator = Mockito.mock(ApplicationsAggregator.class);

        service = new AnalyticsService(client, aggregator);
    }

    @Test
    void testGetMonthlyApplications() {
        // given
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);

        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setCreatedAt(thisMonth.plusDays(1));

        when(client.getAllApplications()).thenReturn(List.of(dto));
        when(aggregator.aggregate(anyList()))
                .thenReturn(Map.of(
                        "booked", 1,
                        "operatedPaid", 0,
                        "operatedQuota", 0
                ));

        // when
        String report = service.getMonthlyApplications();

        // then
        assertNotNull(report);
        verify(client, times(1)).getAllApplications();
        verify(aggregator, times(1)).aggregate(anyList());
    }
}