package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.request.CampaignRequestDto;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.crm.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService){
        this.campaignService = campaignService;
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDto> create(@RequestBody @Valid CampaignRequestDto dto) {
        CampaignResponseDto saved = campaignService.create(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/latest")
    public ResponseEntity<CampaignResponseDto> getLatestCampaign() {
        return campaignService.getLatestCampaign()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}