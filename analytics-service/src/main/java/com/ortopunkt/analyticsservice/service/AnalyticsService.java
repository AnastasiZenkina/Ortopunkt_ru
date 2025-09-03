package com.ortopunkt.analyticsservice.service;

import com.ortopunkt.analyticsservice.client.AnalyticsHttpClient;
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

        // все заявки за месяц
        List<ApplicationResponseDto> recent = applicationClient.getAllApplications().stream()
                .filter(app -> app.getCreatedAt() != null && !app.getCreatedAt().isBefore(monthAgo))
                .toList();

        // уникальные tgId всех написавших
        Set<String> tgIds = recent.stream()
                .map(app -> {
                    PatientResponseDto patient = app.getPatient();
                    return patient != null ? patient.getTgId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int total = tgIds.size();

        // --- Записались ---
        int booked = getTotalBooked(recent);
        double bookedPercent = total == 0 ? 0 : (booked * 100.0 / total);

        // --- Прооперированы ---
        int operatedPaid = getTotalOperatedPaid(recent);
        int operatedQuota = getTotalOperatedQuota(recent);

        double operatedPaidPercent = booked == 0 ? 0 : (operatedPaid * 100.0 / booked);
        double operatedQuotaPercent = booked == 0 ? 0 : (operatedQuota * 100.0 / booked);

        // сборка отчёта
        StringBuilder sb = new StringBuilder();
        sb.append("📝 <b>Заявки за месяц</b>:\n\n")
                .append("Всего написало: <b>").append(total).append("</b> человек\n")
                .append("Записались на консультацию: <b>").append(booked).append("</b>\n")
                .append("Конверсия в запись: ").append(String.format("%.1f", bookedPercent)).append("%\n\n")
                .append("Прооперированы платно: <b>").append(operatedPaid).append("</b> (")
                .append(String.format("%.1f", operatedPaidPercent)).append("% от записавшихся)\n")
                .append("Прооперированы по квоте: <b>").append(operatedQuota).append("</b> (")
                .append(String.format("%.1f", operatedQuotaPercent)).append("% от записавшихся)\n\n");

        // список имён уникальных пользователей
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

    private int getTotalBooked(List<ApplicationResponseDto> recent) {
        return recent.stream()
                .filter(app -> "Записан".equalsIgnoreCase(app.getStatus()))
                .map(app -> {
                    PatientResponseDto patient = app.getPatient();
                    return patient != null ? patient.getTgId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size();
    }

    private int getTotalOperatedPaid(List<ApplicationResponseDto> recent) {
        return recent.stream()
                .filter(app -> "Прооперирован платно".equalsIgnoreCase(app.getStatus()))
                .map(app -> {
                    PatientResponseDto patient = app.getPatient();
                    return patient != null ? patient.getTgId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size();
    }

    private int getTotalOperatedQuota(List<ApplicationResponseDto> recent) {
        return recent.stream()
                .filter(app -> "Прооперирован по квоте".equalsIgnoreCase(app.getStatus()))
                .map(app -> {
                    PatientResponseDto patient = app.getPatient();
                    return patient != null ? patient.getTgId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size();
    }
}