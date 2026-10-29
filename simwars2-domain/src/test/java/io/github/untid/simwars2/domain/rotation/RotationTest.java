package io.github.untid.simwars2.domain.rotation;

import io.github.untid.simwars2.domain.skill.Hit;
import io.github.untid.simwars2.domain.skill.Skill;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RotationTest {

    private static final Skill CORTAR = new Skill(14369, "Cortar",
            List.of(new Hit(0.7)), 0.64);
    private static final Skill DOBLE = new Skill(14370, "Corte doble",
            List.of(new Hit(0.45), new Hit(1.05)), 1.04);
    private static final Skill TRIPLE = new Skill(14371, "Corte triple",
            List.of(new Hit(0.75), new Hit(0.75), new Hit(1.6)), 1.92);

    @Test
    void cycleSeconds_esLaSumaDeLosCasts() {
        var rotation = new Rotation(List.of(CORTAR, DOBLE, TRIPLE));
        assertThat(rotation.cycleSeconds()).isCloseTo(3.6, within(1e-9));
    }

    @Test
    void rechazaListaVaciaONula() {
        assertThatThrownBy(() -> new Rotation(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Rotation(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void laListaEsInmutableYDefensiva() {
        var origen = new ArrayList<>(List.of(CORTAR));
        var rotation = new Rotation(origen);
        origen.add(DOBLE);
        assertThat(rotation.skills()).hasSize(1);
        assertThatThrownBy(() -> rotation.skills().add(TRIPLE))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}