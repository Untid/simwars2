package io.github.untid.simwars2.domain.damage;

public interface CritResolver {

    double resolve(double normalDamage, double critChance, double critMultiplier);
}
