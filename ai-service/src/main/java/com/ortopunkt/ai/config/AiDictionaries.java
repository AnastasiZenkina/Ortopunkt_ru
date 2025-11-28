package com.ortopunkt.ai.config;

import com.ortopunkt.ai.topic.Topic;
import java.util.Map;

public class AiDictionaries {

    public static final Map<Topic, String> TOPIC_DESCRIPTIONS = Map.ofEntries(
            Map.entry(Topic.FLATFOOT,
                    "плоскостоп, уплощен стоп, стельк, обув, ортопед, стоп бол, походк"),
            Map.entry(Topic.HALLUX_VALGUS,
                    "шишк на ног, шишк, косточк, сустав больш пальц, вальгус, деформац пальц"),
            Map.entry(Topic.FIRST_JOINT_PROSTHESIS,
                    "артродез пальц ног, эндопротез пальц, операц на плюснефалангов сустав"),
            Map.entry(Topic.GANGLION,
                    "гигром, шишк на кист, жидкост, рук, суставн сумк"),
            Map.entry(Topic.DUPUYTREN,
                    "болезн дюпюитрен, пальц, контрактур, сгибан"),
            Map.entry(Topic.ENDOPROSTHESIS,
                    "тазобедрен сустав, коленн сустав, замен сустав, эндопротез, протез сустав"),
            Map.entry(Topic.REPEAT,
                    "повторн операц, не помогл, повторно, было уже вмешат"),
            Map.entry(Topic.SYMPTOM,
                    "бол, жжени, отёк, покраснен, симптом, ощущен, дискомфорт"),
            Map.entry(Topic.MORTON,
                    "невром мортона, стоп, бол между пальц, жжени под пальц"),
            Map.entry(Topic.HAGLUND,
                    "деформац хаглунд, ахилл, бугор, пятк, воспален пяточн област"),
            Map.entry(Topic.RHEUMATOID,
                    "ревматоидн артрит, воспалён сустав, деформац сустав, бол в мелк сустав"),
            Map.entry(Topic.HEEL_SPUR,
                    "пяточн шпор, фасциит, бол в пятк, шип, плантарн фасциит"),
            Map.entry(Topic.QUOTA,
                    "квот, бесплат, операц по полис, госпрограм"),
            Map.entry(Topic.PAID,
                    "платн, стоимост, цен операц, сколько стoит"),
            Map.entry(Topic.REGION,
                    "город, выезд, где вы находитесь, локац, мест приём"),
            Map.entry(Topic.ONLINE,
                    "онлайн-консульт, удалённ, по переписк, дистанцион"),
            Map.entry(Topic.REHAB,
                    "реабилитац, восстановлен, стельк, ортопед, после операц"),
            Map.entry(Topic.AGE,
                    "возраст, старш 60, пожил, можно ли мне, возрастн пациент"),
            Map.entry(Topic.PARTNER,
                    "реклам, сотрудничеств, партнёрств, совместн проект")
    );

    public static final Map<String, String> HYPOTHESES = Map.ofEntries(
            Map.entry("interest", "Пациент спрашивает про лечение или хочет лечиться"),
            Map.entry("wants_paid", "Пациент спрашивает цену, сколько стоит, говорит что платно"),
            Map.entry("wants_free", "Пациент пишет про квоту или бесплатно"),
            Map.entry("mentions_no_money", "Пациент пишет что нет денег или дорого"),
            Map.entry("asks_about_discount", "Пациент спрашивает про скидку или акции"),
            Map.entry("anxious", "Пациент переживает, тревожится, боится"),
            Map.entry("aggressive", "Пациент раздражён, недоволен, злится"),
            Map.entry("ready_for_surgery", "Пациент пишет что готов на операцию"),
            Map.entry("wants_consultation", "Пациент хочет консультацию врача"),
            Map.entry("gathering_info", "Пациент просто узнаёт информацию"),
            Map.entry("not_ready", "Пациент пишет что пока не готов лечиться")
    );
}