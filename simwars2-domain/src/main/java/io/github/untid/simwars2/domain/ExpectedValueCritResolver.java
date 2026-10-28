package io.github.untid.simwars2.domain;

public final class ExpectedValueCritResolver implements CritResolver {

    @Override
    public double resolve(double normalDamage, double critChance, double critMultiplier) {
        return normalDamage * (1 + critChance * (critMultiplier - 1));
    }
}
