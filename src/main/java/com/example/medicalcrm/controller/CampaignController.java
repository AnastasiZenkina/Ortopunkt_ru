package com.example.medicalcrm.controller;
import com.example.medicalcrm.dto.CampaignRequestDto;
import com.example.medicalcrm.dto.CampaignResponseDto;
import com.example.medicalcrm.entity.Campaign;
import com.example.medicalcrm.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService){
        this.campaignService = campaignService;
    }

    @GetMapping
    public List <Campaign> getAllCampaigns(){
        return campaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable Long id){
        return campaignService.getCampaignById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDto> create(@RequestBody @Valid CampaignRequestDto dto) {
        Campaign saved = campaignService.create(dto);
        return ResponseEntity.ok(CampaignResponseDto.fromEntity(saved));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id){
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
