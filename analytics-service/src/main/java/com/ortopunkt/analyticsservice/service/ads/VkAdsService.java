package com.ortopunkt.analyticsservice.service.ads;

import com.ortopunkt.analyticsservice.client.VkAdsClient;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VkAdsService {

    private final VkAdsClient vkAdsClient;
    private final VkAdsParser vkAdsParser;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    private List<CampaignResponseDto> cachedStats = List.of();

    @Scheduled(fixedRate = 10000)
    public void refreshCache() {
        cachedStats = loadStats();
        log.info("Кэш VK Ads обновлён: " + cachedStats.size() + " записей");
    }

    public List<CampaignResponseDto> getLastMonthStats() {
        return cachedStats;
    }

    private List<CampaignResponseDto> loadStats() {
        try {
            YearMonth prev = YearMonth.now().minusMonths(1);
            String from = prev.atDay(1).toString();
            String to = prev.atEndOfMonth().toString();

            log.info("Запрос статистики VK Ads за период " + from + " — " + to);

            String json = vkAdsClient.fetchCampaignsJson(from, to).join();
            List<CampaignResponseDto> stats = vkAdsParser.parseCampaigns(json);

            long sum = stats.stream()
                    .map(CampaignResponseDto::getReach)
                    .filter(Objects::nonNull)
                    .mapToLong(Long::longValue)
                    .sum();

            if (!stats.isEmpty()) {
                stats.get(0).setReach(sum);
            }

            List<Long> planIds = stats.stream()
                    .map(CampaignResponseDto::getId)
                    .filter(Objects::nonNull)
                    .filter(id -> id > 0)
                    .distinct()
                    .toList();

            if (!planIds.isEmpty()) {
                String creativesJson = vkAdsClient.fetchCreativeStatsByPlanIds(planIds, from, to);
                Map<String, Map<String, Number>> creativeData = vkAdsParser.parseCreatives(creativesJson);

                Map<String, Number> ctrMap = creativeData.getOrDefault("ctr", Map.of());
                Map<String, Number> subsMap = creativeData.getOrDefault("subs", Map.of());

                LinkedHashMap<String, Double> topCtr = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> topSubs = new LinkedHashMap<>();

                subsMap.entrySet().stream()
                        .filter(e -> e.getValue().intValue() > 0)
                        .sorted((a, b) -> Integer.compare(b.getValue().intValue(), a.getValue().intValue()))
                        .limit(10)
                        .forEach(e -> {
                            String id = e.getKey();
                            int subs = e.getValue().intValue();
                            double ctr = ctrMap.getOrDefault(id, 0d).doubleValue();
                            topCtr.put(id, ctr);
                            topSubs.put(id, subs);
                            log.info("Креатив " + id + " — CTR: " +
                                    String.format(Locale.US, "%.2f", ctr) + "%, подписчики: " + subs);
                        });

                if (!stats.isEmpty()) {
                    CampaignResponseDto dto = stats.get(0);
                    dto.setCreativeStats(topCtr);
                    dto.setCreativeSubs(topSubs);
                }

                if (topSubs.isEmpty()) {
                    log.info("Нет данных за период.");
                }
            } else {
                log.info("Нет активных планов за период.");
            }

            stats.forEach(this::calculateMetrics);
            return stats;

        } catch (Exception e) {
            log.error("Ошибка при сборе статистики VK Ads: " + e.getMessage());
            return List.of();
        }
    }

    private void calculateMetrics(CampaignResponseDto dto) {
        long shows = Optional.ofNullable(dto.getReach()).orElse(0L);
        long clicks = Optional.ofNullable(dto.getImpressions()).orElse(0L);
        long subs = Optional.ofNullable(dto.getSubscribers()).orElse(0L);
        double spent = Optional.ofNullable(dto.getTotalSpent()).orElse(0.0);

        double ctr = shows > 0 ? (double) clicks / shows * 100.0 : 0.0;
        double cpa = subs > 0 ? spent / subs : 0.0;

        dto.setCtr(ctr);
        dto.setCpa(cpa);
    }
}