package com.ortopunkt.ai.config;

import com.ortopunkt.ai.topic.Topic;
import java.util.Map;

public class AiDictionaries {

    public static final Map<Topic, String> TOPIC_DESCRIPTIONS = Map.ofEntries(
            Map.entry(Topic.FLATFOOT, "плоскостопие, уплощение стопы, стельки, обувь, ортопедия, стопа болит, походка"),
            Map.entry(Topic.HALLUX_VALGUS, "шишка на ноге, косточка, сустав большого пальца, вальгус, косточка возле пальца, деформация пальца"),
            Map.entry(Topic.FIRST_JOINT_PROSTHESIS, "артродез пальца ноги, эндопротез пальца, операция на плюснефаланговом суставе"),
            Map.entry(Topic.GANGLION, "гигрома, шишка на кисти, жидкость, рука, суставная сумка"),
            Map.entry(Topic.DUPUYTREN, "болезнь Дюпюитрена, пальцы, контрактура, сгибание"),
            Map.entry(Topic.ENDOPROSTHESIS, "тазобедренный сустав, коленный сустав, замена сустава, эндопротезирование, протез сустава, операция по замене"),
            Map.entry(Topic.REPEAT, "повторная операция, не помогло, повторно, было уже вмешательство"),
            Map.entry(Topic.SYMPTOM, "боль, жжение, отёк, покраснение, симптом, ощущение, дискомфорт"),
            Map.entry(Topic.MORTON, "неврома Мортона, стопа, боль между пальцами, жжение под пальцами"),
            Map.entry(Topic.HAGLUND, "деформация Хаглунда, ахилл, бугор, пятка, воспаление пяточной области"),
            Map.entry(Topic.RHEUMATOID, "ревматоидный артрит, воспалённые суставы, деформация суставов, боли в мелких суставах"),
            Map.entry(Topic.HEEL_SPUR, "пяточная шпора, фасциит, боль в пятке, шип, плантарный фасциит"),
            Map.entry(Topic.QUOTA, "квота, бесплатно, операция по полису, госпрограмма"),
            Map.entry(Topic.PAID, "платно, стоимость, цена операции, сколько стоит"),
            Map.entry(Topic.REGION, "город, выезд, где вы находитесь, локация, место приёма"),
            Map.entry(Topic.ONLINE, "онлайн-консультация, удалённо, по переписке, дистанционно"),
            Map.entry(Topic.REHAB, "реабилитация, восстановление, стельки, ортопедия, после операции"),
            Map.entry(Topic.AGE, "возраст, старше 60, пожилой, можно ли мне, возрастной пациент"),
            Map.entry(Topic.PARTNER, "реклама, сотрудничество, партнёрство, совместные проекты")
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