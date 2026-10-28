package io.github.untid.simwars2.domain;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class SkillTest {

    private static Skill cortar() {
        return new Skill(14369, "Cortar", List.of(new Hit(0.7)), 0.64);
    }

    @Test
    void coeficienteTotalEsLaSumaDeLosGolpes() {
        var triple = new Skill(14371, "Corte triple",
                List.of(new Hit(0.75), new Hit(0.75), new Hit(1.6)), 1.92);
        assertThat(triple.totalCoefficient()).isCloseTo(3.1, within(1e-9));
    }

    @Test
    void laListaDeGolpesEsInmutableYDefensiva() {
        var origen = new ArrayList<>(List.of(new Hit(0.7)));
        var skill = new Skill(1, "X", origen, 1.0);
        origen.add(new Hit(5.0));
        assertThat(skill.hits()).hasSize(1);
        assertThatThrownBy(() -> skill.hits().add(new Hit(1.0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void rechazaDatosInvalidos() {
        assertThatThrownBy(() -> new Hit(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Skill(1, "X", List.of(), 1.0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Skill(1, "X", List.of(new Hit(1)), 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Skill(1, " ", List.of(new Hit(1)), 1.0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}