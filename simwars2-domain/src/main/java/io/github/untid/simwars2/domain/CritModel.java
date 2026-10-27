package io.github.untid.simwars2.domain;

public final class CritModel {
    private CritModel() {}

    public static double critChance(StatSheet s) {

        double raw = ((s.precision() - 895) / 21.0) / 100;

        return Math.max(0.0, Math.min(1.0, raw));
    }

    public static double critMultiplier(StatSheet s) {

        return 1.5 + (s.ferocity() / 1500.0);
    }
}
