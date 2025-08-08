package com.ortopunkt.medicalcrm.controller;
import com.ortopunkt.medicalcrm.dto.CampaignRequestDto;
import com.ortopunkt.dto.CampaignResponseDto;
import com.ortopunkt.medicalcrm.entity.Campaign;
import com.ortopunkt.medicalcrm.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        CampaignResponseDto saved = campaignService.create(dto);
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id){
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
