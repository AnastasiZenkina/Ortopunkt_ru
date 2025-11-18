package com.ortopunkt.ai.topic;

public enum Topic {
    FLATFOOT("flatfoot"),
    HALLUX_VALGUS("hallux valgus"),
    FIRST_JOINT_PROSTHESIS("first joint prosthesis"),
    GANGLION("ganglion"),
    DUPUYTREN("dupuytren"),
    ENDOPROSTHESIS("endoprosthesis"),
    REPEAT("repeat"),
    SYMPTOM("symptom"),
    MORTON("morton"),
    HAGLUND("haglund"),
    RHEUMATOID("rheumatoid"),
    HEEL_SPUR("heel spur"),
    QUOTA("quota"),
    PAID("paid"),
    REGION("region"),
    ONLINE("online"),
    REHAB("rehab"),
    AGE("age"),
    PARTNER("partner"),
    COMMON("common");

    private final String key;

    Topic(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}