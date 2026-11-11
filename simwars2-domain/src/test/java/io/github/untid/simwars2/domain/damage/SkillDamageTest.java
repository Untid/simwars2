package io.github.untid.simwars2.domain.damage;

import io.github.untid.simwars2.domain.condition.Vulnerability;
import io.github.untid.simwars2.domain.skill.Hit;
import io.github.untid.simwars2.domain.skill.Skill;
import io.github.untid.simwars2.domain.stats.CritModel;
import io.github.untid.simwars2.domain.stats.StatSheet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SkillDamageTest {

    private static final StatSheet LOG = new StatSheet(2556, 1961, 1186);
    private static final Target GOLEM = new Target(2597);
    private static final CritResolver EV = new ExpectedValueCritResolver();

    private static final Skill CORTAR = new Skill(14369, "Cortar",
            List.of(new Hit(0.7)), 0.64);
    private static final Skill DOBLE = new Skill(14370, "Corte doble",
            List.of(new Hit(0.45), new Hit(1.05)), 1.04);
    private static final Skill TRIPLE = new Skill(14371, "Corte triple",
            List.of(new Hit(0.75), new Hit(0.75), new Hit(1.6)), 1.92);

    @Test
    void valorEsperadoSinCritico_equivaleAlGolpeNormal() {
        var stats = new StatSheet(1000, 0, 0);
        double normal = DamageCalculator.normalHit(1000, stats, 0.7, new Target(1000));
        double esperado = new ExpectedValueCritResolver().resolve(normal, 0.0, 1.5);
        assertThat(esperado).isEqualTo(normal);
    }

    @Test
    void valorEsperadoConCriticoSeguro_equivaleAlGolpeCritico() {
        double esperado = new ExpectedValueCritResolver().resolve(100.0, 1.0, 2.0);
        assertThat(esperado).isCloseTo(200.0, within(1e-9));
    }

    @Test
    void cadenaCompleta_coincideConElModelo() {
        double total = 0;
        for (Skill s : List.of(CORTAR, DOBLE, TRIPLE)) {
            total += SkillDamage.expected(s, 1000, LOG, GOLEM, EV);
        }
        assertThat(total).isCloseTo(8634, within(1.0));
    }

    @Test
    void cadenaCompleta_coincideConElLogDeFrenteConUnaTolerancia() {
        double total = 0;
        for (Skill s : List.of(CORTAR, DOBLE, TRIPLE)) {
            total += SkillDamage.expected(s, 1000, LOG, GOLEM, EV);
        }
        // log de frente: 8690 por cadena. Tolerancia 1 %
        assertThat(total).isCloseTo(8690, withinPercentage(1.0));
    }

    @Test
    void sinVulnerabilidad_equivaleALaSobrecargaAntigua() {
        double antigua = SkillDamage.expected(TRIPLE, 1000, LOG, GOLEM, EV);
        double nueva = SkillDamage.expected(TRIPLE, 1000, LOG, GOLEM, EV,
                new Vulnerability(0));
        assertThat(nueva).isEqualTo(antigua);
    }

    @Test
    void conVeinticincoStacks_escalaElDanoUnVeinticincoPorCiento() {
        double base = SkillDamage.expected(TRIPLE, 1000, LOG, GOLEM, EV);
        double vuln = SkillDamage.expected(TRIPLE, 1000, LOG, GOLEM, EV,
                new Vulnerability(25));
        assertThat(vuln).isCloseTo(base * 1.25, within(1e-9));
    }

    @Test
    void cortar_conVeinticincoStacks_coincideConElLog() {
        //log con 25 stacks de Vulnerabilidad, parche X
        double min = DamageCalculator.normalHit(900, LOG, 0.7, GOLEM)
                * new Vulnerability(25).multiplier();
        double max = DamageCalculator.normalHit(1100, LOG, 0.7, GOLEM)
                * CritModel.critMultiplier(LOG)
                * new Vulnerability(25).multiplier();
        assertThat(Math.floor(min)).isEqualTo(775.0);
        assertThat(Math.floor(max)).isEqualTo(2169.0);
    }
}