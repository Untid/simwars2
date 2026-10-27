package io.github.untid.simwars2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class DamageCalculatorTest {

    private static final Target GOLEM = new Target(2597);
    private static final StatSheet LOG = new StatSheet(2556, 1961, 1186);

    @Test
    void casoTrivial_aislaLaAritmetica() {
        var stats = new StatSheet(1000, 0, 0);
        double dmg = DamageCalculator.normalHit(1000, stats, 1.0, new Target(1000));
        assertThat(dmg).isEqualTo(1000.0);
    }

    @Test
    void cortar_golpeNormalConArmaMinima_coincideConElLog() {
        double dmg = DamageCalculator.normalHit(900, LOG, 0.7, GOLEM);
        assertThat(Math.floor(dmg)).isEqualTo(620.0);   // log: min 620
    }

    @Test
    void danoEsProporcionalAlCoeficiente() {
        double a = DamageCalculator.normalHit(1000, LOG, 0.5, GOLEM);
        double b = DamageCalculator.normalHit(1000, LOG, 1.0, GOLEM);
        assertThat(b).isCloseTo(2 * a, within(1e-9));
    }

    @Test
    void masArmaduraImplicaMenosDano() {
        double bajo = DamageCalculator.normalHit(1000, LOG, 1.0, new Target(2000));
        double alto = DamageCalculator.normalHit(1000, LOG, 1.0, new Target(3000));
        assertThat(alto).isLessThan(bajo);
    }

    @Test
    void rechazaValoresInvalidos() {
        assertThatThrownBy(() -> new StatSheet(-1, 0, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Target(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}