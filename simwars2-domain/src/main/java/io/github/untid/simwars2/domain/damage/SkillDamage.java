package io.github.untid.simwars2.domain.damage;

import io.github.untid.simwars2.domain.skill.Hit;
import io.github.untid.simwars2.domain.skill.Skill;
import io.github.untid.simwars2.domain.stats.CritModel;
import io.github.untid.simwars2.domain.stats.StatSheet;

public final class SkillDamage {
    private SkillDamage() {
    }

    public static double expected(Skill s, double weaponStrength, StatSheet statSheet, Target t, CritResolver critResolver) {

        double total = 0;
        double p = CritModel.critChance(statSheet);
        double m = CritModel.critMultiplier(statSheet);

        for (Hit h : s.hits()) {
            double normal = DamageCalculator.normalHit(weaponStrength, statSheet, h.coefficient(), t);

            total += critResolver.resolve(normal, p, m);
        }

        return total;
    }
}
