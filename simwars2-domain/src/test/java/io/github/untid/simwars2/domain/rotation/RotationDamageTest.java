package io.github.untid.simwars2.domain.rotation;

import io.github.untid.simwars2.domain.damage.CritResolver;
import io.github.untid.simwars2.domain.damage.ExpectedValueCritResolver;
import io.github.untid.simwars2.domain.damage.Target;
import io.github.untid.simwars2.domain.skill.Hit;
import io.github.untid.simwars2.domain.skill.Skill;
import io.github.untid.simwars2.domain.stats.StatSheet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RotationDamageTest {

    private static final StatSheet LOG = new StatSheet(2556, 1961, 1186);
    private static final Target GOLEM = new Target(2597);
    private static final CritResolver EV = new ExpectedValueCritResolver();

    private static final Rotation CADENA = new Rotation(List.of(
            new Skill(14369, "Cortar", List.of(new Hit(0.7)), 0.64),
            new Skill(14370, "Corte doble", List.of(new Hit(0.45), new Hit(1.05)), 1.04),
            new Skill(14371, "Corte triple",
                    List.of(new Hit(0.75), new Hit(0.75), new Hit(1.6)), 1.92)));

    @Test
    void cycleDamage_coincideConElModelo() {
        double dmg = RotationDamage.cycleDamage(CADENA, 1000, LOG, GOLEM, EV);
        assertThat(dmg).isCloseTo(8634, within(1.0));
    }

    @Test
    void dps_esDanoDeCicloEntreDuracion() {
        double dps = RotationDamage.dps(CADENA, 1000, LOG, GOLEM, EV);
        assertThat(dps).isCloseTo(2398, within(1.0));
    }

    @Test
    void dps_coincideConElLogDeFrenteConUnaTolerancia() {
        double dps = RotationDamage.dps(CADENA, 1000, LOG, GOLEM, EV);
        // log de frente: 2414 DPS. Diferencia esperada ~0,7 %
        assertThat(dps).isCloseTo(2414, withinPercentage(1.0));
    }
}