package com.ortopunkt.analyticsservice.service;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsHttpClient applicationClient;

    public String getMonthlyApplications() {
        // начало текущего месяца
        LocalDate monthAgo = LocalDate.now().withDayOfMonth(1);

        List<ApplicationResponseDto> recent = applicationClient.getAllApplications().stream()
                .filter(app -> app.getCreatedAt() != null && !app.getCreatedAt().isBefore(monthAgo))
                .toList();

        Set<String> tgIds = recent.stream()
                .map(app -> {
                    PatientResponseDto patient = app.getPatient();
                    return patient != null ? patient.getTgId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        sb.append("📝 За <b>месяц</b> было <b>")
                .append(tgIds.size())
                .append("</b> заявок:\n\n");

        Set<String> seen = new HashSet<>();
        for (ApplicationResponseDto app : recent) {
            PatientResponseDto p = app.getPatient();
            if (p != null) {
                String id = p.getTgId();
                if (id != null && seen.add(id)) {
                    String name = p.getName() != null ? p.getName() : "";
                    String username = p.getUsername() != null ? "(@" + p.getUsername() + ")" : "";
                    sb.append("• ").append(name).append(" ").append(username).append("\n");
                }
            }
        }

        return sb.toString();
    }
}
