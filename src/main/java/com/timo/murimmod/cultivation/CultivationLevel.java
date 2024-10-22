package com.timo.murimmod.cultivation;

public enum CultivationLevel {
    MORTAL(0),
    QI_CONDENSATION(1),
    FOUNDATION_ESTABLISHMENT(2),
    CORE_FORMATION(3),
    NASCENT_SOUL(4),
    SPIRIT_SEVERING(5),
    VOID_TRIBULATION(6),
    IMMORTAL_ASCENSION(7);

    private final int level;

    CultivationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static CultivationLevel fromLevel(int level) {
        for (CultivationLevel cl : values()) {
            if (cl.level == level) {
                return cl;
            }
        }
        return MORTAL;
    }
}
