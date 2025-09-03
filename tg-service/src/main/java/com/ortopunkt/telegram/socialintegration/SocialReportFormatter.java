package com.ortopunkt.telegram.socialintegration;

import org.springframework.stereotype.Component;

@Component
public class SocialReportFormatter {

    public String formatDoctorReport(String vkAds, String vkCommunity, String insta) {
        return "👨‍⚕️ Отчёт для врача:\n" +
                "VK Ads: " + vkAds + "\n" +
                "VK Community: " + vkCommunity + "\n" +
                "Instagram: " + insta;
    }

    public String formatSmmReport(String vkCommunity, String insta) {
        return "🎨 Отчёт для SMM:\n" +
                "VK Community: " + vkCommunity + "\n" +
                "Instagram: " + insta;
    }

    public String formatTargetReport(String vkAds) {
        return "🎯 Отчёт для таргетолога:\n" +
                "VK Ads: " + vkAds;
    }
}