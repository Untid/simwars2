package io.github.untid.simwars2.domain;

public record StatSheet(int power, int precision, int ferocity) {
    public StatSheet {
        if (power < 0) throw new IllegalArgumentException("power must be > 0");
        if (precision < 0) throw new IllegalArgumentException("precision must be > 0");
        if (ferocity < 0) throw new IllegalArgumentException("ferocity must be > 0");
    }
}