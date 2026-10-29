package io.github.untid.simwars2.domain.stats;

import io.github.untid.simwars2.domain.damage.DamageCalculator;
import io.github.untid.simwars2.domain.damage.Target;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CritModelTest {

    private static final StatSheet LOG = new StatSheet(2556, 1961, 1186);

    @Test
    void multiplicadorSinFerocity_es1punto5() {
        assertThat(CritModel.critMultiplier(new StatSheet(0, 0, 0)))
                .isCloseTo(1.5, within(1e-9));
    }

    @Test
    void multiplicadorConTuPanel_es229() {
        assertThat(CritModel.critMultiplier(LOG))
                .isCloseTo(2.29, within(0.001));        // panel: 229.0 %
    }

    @Test
    void probabilidadConTuPanel_es5076() {
        assertThat(CritModel.critChance(LOG))
                .isCloseTo(0.5076, within(0.0001));     // panel: 50.76 %
    }

    @Test
    void probabilidadTienePisoEnCero() {
        assertThat(CritModel.critChance(new StatSheet(0, 500, 0))).isEqualTo(0.0);
    }

    @Test
    void probabilidadTieneTechoEnCien() {
        assertThat(CritModel.critChance(new StatSheet(0, 5000, 0))).isEqualTo(1.0);
    }

    @Test
    void cortar_criticoConArmaMaxima_coincideConElLog() {
        double normal = DamageCalculator.normalHit(1100, LOG, 0.7, new Target(2597));
        double crit = normal * CritModel.critMultiplier(LOG);
        assertThat(Math.floor(crit)).isEqualTo(1735.0);  // log: max 1735
    }
}