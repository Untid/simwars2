package io.github.untid.simwars2.domain.damage;

public record Target(int armor) {
    public Target {
        if (armor <= 0) throw new IllegalArgumentException("armor must be > 0");
    }
}