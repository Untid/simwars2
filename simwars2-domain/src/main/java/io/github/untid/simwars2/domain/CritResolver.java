package io.github.untid.simwars2.domain;

public interface CritResolver {

    double resolve(double normalDamage, double critChance, double critMultiplier);
}
