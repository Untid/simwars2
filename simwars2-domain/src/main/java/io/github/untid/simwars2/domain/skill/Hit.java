package io.github.untid.simwars2.domain.skill;

public record Hit(double coefficient) {

    public Hit {
        if (coefficient <= 0) throw new IllegalArgumentException("Coefficient must be > 0");
    }
}
