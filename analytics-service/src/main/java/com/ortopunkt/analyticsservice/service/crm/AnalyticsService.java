package com.ortopunkt.analyticsservice.service.crm;

import com.ortopunkt.analyticsservice.client.AnalyticsClient;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsClient applicationClient;
    private final ApplicationsAggregator aggregator;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    public String getMonthlyApplications() {
        LocalDate monthAgo = LocalDate.now().withDayOfMonth(1);
        log.info("Формирование отчёта по заявкам с " + monthAgo);

        List<ApplicationResponseDto> recent = applicationClient.getAllApplications().stream()
                .filter(app -> app.getCreatedAt() != null && !app.getCreatedAt().isBefore(monthAgo))
                .toList();

        Map<String, Integer> counts = aggregator.aggregate(recent);

        String report = ApplicationsReportFormatter.format(
                recent,
                counts.get("booked"),
                counts.get("operatedPaid"),
                counts.get("operatedQuota")
        );

        log.info("Отчёт по заявкам сформирован. Всего заявок: " + recent.size());
        return report;
    }

    public void updateFromCrm(ApplicationResponseDto dto) {
        log.info("Обновление данных по заявке из CRM: id=" + dto.getId() + ", статус=" + dto.getStatus());
        try {
            getMonthlyApplications();
        } catch (Exception e) {
            log.error("Ошибка при обновлении данных из CRM: " + e.getMessage());
        }
    }
}