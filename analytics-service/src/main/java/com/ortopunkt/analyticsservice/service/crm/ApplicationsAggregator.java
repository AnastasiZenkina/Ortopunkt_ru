package com.ortopunkt.analyticsservice.service.crm;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ApplicationsAggregator {

    public Map<String, Integer> aggregate(List<ApplicationResponseDto> recent) {
        Map<String, Integer> result = new HashMap<>();
        result.put("booked", countBooked(recent));
        result.put("operatedPaid", countOperatedPaid(recent));
        result.put("operatedQuota", countOperatedQuota(recent));
        return result;
    }

    private int countBooked(List<ApplicationResponseDto> apps) {
        return countByStatus(apps, s -> containsAny(s, "ЗАПИС", "BOOK"));
    }

    private int countOperatedPaid(List<ApplicationResponseDto> apps) {
        return countByPaymentStatus(apps, "PAID");
    }

    private int countOperatedQuota(List<ApplicationResponseDto> apps) {
        return countByPaymentStatus(apps, "QUOTA");
    }

    private int countByStatus(List<ApplicationResponseDto> apps, java.util.function.Predicate<String> statusMatch) {
        return apps.stream()
                .filter(a -> statusMatch.test(norm(a.getStatus())))
                .map(this::uniqueKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size();
    }

    private int countByPaymentStatus(List<ApplicationResponseDto> apps, String code) {
        return apps.stream()
                .filter(a -> code.equalsIgnoreCase(a.getPaymentStatus()))
                .map(this::uniqueKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size();
    }

    private String uniqueKey(ApplicationResponseDto app) {
        PatientResponseDto p = app.getPatient();
        if (p != null && p.getTgId() != null) return "tg:" + p.getTgId();
        if (p != null && p.getId() != null) return "patient:" + p.getId();
        if (app.getId() != null) return "app:" + app.getId();
        return null;
    }

    private String norm(String s) {
        return s == null ? "" : s.trim().toUpperCase(Locale.ROOT);
    }

    private boolean containsAny(String s, String... parts) {
        for (String p : parts) if (s.contains(p)) return true;
        return false;
    }
}