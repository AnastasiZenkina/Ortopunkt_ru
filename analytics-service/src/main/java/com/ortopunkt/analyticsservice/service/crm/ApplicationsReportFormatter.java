package com.ortopunkt.analyticsservice.service.crm;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;

import java.util.*;

public class ApplicationsReportFormatter {

    public static String format(List<ApplicationResponseDto> apps,
                                int booked,
                                int operatedPaid,
                                int operatedQuota) {

        int total = getUniqueTgIds(apps).size();
        double bookedPercent = total == 0 ? 0 : (booked * 100.0 / total);
        double operatedPaidPercent = booked == 0 ? 0 : (operatedPaid * 100.0 / booked);
        double operatedQuotaPercent = booked == 0 ? 0 : (operatedQuota * 100.0 / booked);

        StringBuilder sb = new StringBuilder();
        sb.append("📝 <b>Заявки за месяц</b>:\n\n")
                .append("Всего написало: <b>").append(total).append("</b> человек\n")
                .append("Записались на консультацию: <b>").append(booked).append("</b>\n")
                .append("Конверсия в запись: ").append(String.format("%.1f", bookedPercent)).append("%\n\n")
                .append("Прооперированы платно: <b>").append(operatedPaid).append("</b> (")
                .append(String.format("%.1f", operatedPaidPercent)).append("% от записавшихся)\n")
                .append("Прооперированы по квоте: <b>").append(operatedQuota).append("</b> (")
                .append(String.format("%.1f", operatedQuotaPercent)).append("% от записавшихся)\n\n");

        Set<String> seen = new HashSet<>();
        for (ApplicationResponseDto app : apps) {
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

    private static Set<String> getUniqueTgIds(List<ApplicationResponseDto> apps) {
        Set<String> tgIds = new HashSet<>();
        for (ApplicationResponseDto app : apps) {
            if (app.getPatient() != null && app.getPatient().getTgId() != null) {
                tgIds.add(app.getPatient().getTgId());
            }
        }
        return tgIds;
    }
}