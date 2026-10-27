package io.github.untid.simwars2.domain;

public record Target(int armor) {
    public Target {
        if (armor <= 0) throw new IllegalArgumentException("armor must be > 0");
    }
}