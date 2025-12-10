package com.ortopunkt.analyticsservice.controller;

import com.ortopunkt.analyticsservice.service.crm.AnalyticsService;
import com.ortopunkt.analyticsservice.service.smm.InstaCommunityService;
import com.ortopunkt.analyticsservice.service.smm.VkCommunityService;
import com.ortopunkt.analyticsservice.service.ads.VkAdsService;
import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class AnalyticsControllerTest {

    private MockMvc mockMvc;

    private AnalyticsService analyticsService;
    private VkCommunityService vkCommunityService;
    private InstaCommunityService instaCommunityService;
    private VkAdsService vkAdsService;

    @BeforeEach
    void setup() {
        analyticsService = Mockito.mock(AnalyticsService.class);
        vkCommunityService = Mockito.mock(VkCommunityService.class);
        instaCommunityService = Mockito.mock(InstaCommunityService.class);
        vkAdsService = Mockito.mock(VkAdsService.class);

        AnalyticsController controller = new AnalyticsController(
                analyticsService,
                vkCommunityService,
                instaCommunityService,
                vkAdsService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getApplicationsAnalytics_returnsReport() throws Exception {
        when(analyticsService.getMonthlyApplications()).thenReturn("test-report");

        mockMvc.perform(get("/api/analytics/applications"))
                .andExpect(status().isOk())
                .andExpect(content().string("test-report"));

        verify(analyticsService, times(1)).getMonthlyApplications();
    }

    @Test
    void updateApplicationFromCrm_callsService() throws Exception {
        mockMvc.perform(post("/api/analytics/applications/update")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk());

        verify(analyticsService, times(1))
                .updateFromCrm(any(ApplicationResponseDto.class));
    }

    @Test
    void getVkCommunityStats_returnsOk() throws Exception {
        PostRequestDto dto = new PostRequestDto();
        when(vkCommunityService.buildVkReport()).thenReturn(dto);

        mockMvc.perform(get("/analytics/social/vk/community"))
                .andExpect(status().isOk());

        verify(vkCommunityService, times(1)).buildVkReport();
    }

    @Test
    void getInstaCommunityStats_returnsOk() throws Exception {
        PostRequestDto dto = new PostRequestDto();
        when(instaCommunityService.buildInstaReport()).thenReturn(dto);

        mockMvc.perform(get("/analytics/social/insta/community"))
                .andExpect(status().isOk());

        verify(instaCommunityService, times(1)).buildInstaReport();
    }

    @Test
    void getVkAdsStats_returnsOk() throws Exception {
        when(vkAdsService.getLastMonthStats()).thenReturn(List.<CampaignResponseDto>of());

        mockMvc.perform(get("/analytics/social/vk/ads"))
                .andExpect(status().isOk());

        verify(vkAdsService, times(1)).getLastMonthStats();
    }
}