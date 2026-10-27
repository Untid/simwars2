package io.github.untid.simwars2.domain;

public final class DamageCalculator {
    private DamageCalculator(){}

    public static double normalHit(double weaponStrength, StatSheet s, double coefficient, Target t) {

        return (weaponStrength * s.power() * coefficient) / t.armor();
    }
}
