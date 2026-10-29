package io.github.untid.simwars2.domain.rotation;

import io.github.untid.simwars2.domain.damage.CritResolver;
import io.github.untid.simwars2.domain.damage.SkillDamage;
import io.github.untid.simwars2.domain.damage.Target;
import io.github.untid.simwars2.domain.skill.Skill;
import io.github.untid.simwars2.domain.stats.StatSheet;

public final class RotationDamage {
    private RotationDamage(){

    }

    public static double cycleDamage(Rotation rotation, double weaponStrength, StatSheet statSheet, Target t, CritResolver critResolver){

        double total = 0;

        for (Skill s : rotation.skills())
        {
            total += SkillDamage.expected(s, weaponStrength, statSheet, t, critResolver);
        }
        return total;
    }

    public static double dps (Rotation rotation, double weaponStrength, StatSheet statSheet, Target t, CritResolver critResolver){

        return cycleDamage(rotation, weaponStrength, statSheet,t,critResolver) / rotation.cycleSeconds();
    }
}
