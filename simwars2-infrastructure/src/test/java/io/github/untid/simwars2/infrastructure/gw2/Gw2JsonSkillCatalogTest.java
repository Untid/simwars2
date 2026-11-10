package io.github.untid.simwars2.infrastructure.gw2;

import io.github.untid.simwars2.domain.skill.Hit;
import io.github.untid.simwars2.domain.skill.Skill;
import io.github.untid.simwars2.domain.skill.SkillCatalogPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Gw2JsonSkillCatalogTest {

    private static final String SKILLS = """
            [
              {"id":14369,"name":"Cortar","facts":[
                {"type":"Range","value":130},
                {"type":"Damage","hit_count":1,"dmg_multiplier":0.7},
                {"type":"Number","value":3}]},
              {"id":14370,"name":"Corte doble","facts":[
                {"type":"Damage","hit_count":1,"dmg_multiplier":0.45},
                {"type":"Damage","hit_count":1,"dmg_multiplier":1.05}]},
              {"id":14371,"name":"Corte triple","facts":[
                {"type":"Damage","hit_count":2,"dmg_multiplier":0.75},
                {"type":"Damage","hit_count":1,"dmg_multiplier":1.6}]},
              {"id":99999,"name":"Sin override","facts":[
                {"type":"Damage","hit_count":1,"dmg_multiplier":1.0}]},
              {"id":88888,"name":"Sin daño","facts":[
                {"type":"Range","value":130}]}
            ]""";

    private static final String OVERRIDES = """
            {"14369":0.64,"14370":1.04,"14371":1.92,"88888":1.0}""";

    private final SkillCatalogPort catalog =
            Gw2JsonSkillCatalog.fromJson(SKILLS, OVERRIDES);

    @Test
    void cortar_unGolpe() {
        Skill s = catalog.findById(14369).orElseThrow();
        assertThat(s.name()).isEqualTo("Cortar");
        assertThat(s.hits()).containsExactly(new Hit(0.7));
    }

    @Test
    void corteDoble_dosGolpesEnOrden() {
        Skill s = catalog.findById(14370).orElseThrow();
        assertThat(s.hits()).containsExactly(new Hit(0.45), new Hit(1.05));
    }

    @Test
    void corteTriple_expandeHitCount() {
        Skill s = catalog.findById(14371).orElseThrow();
        assertThat(s.hits()).containsExactly(
                new Hit(0.75), new Hit(0.75), new Hit(1.6));
    }

    @Test
    void ignoraHechosQueNoSonDano() {
        Skill s = catalog.findById(14369).orElseThrow();
        assertThat(s.hits()).hasSize(1);
    }

    @Test
    void castSecondsSaleDelOverride() {
        assertThat(catalog.findById(14371).orElseThrow().castSeconds())
                .isEqualTo(1.92);
    }

    @Test
    void idInexistente_devuelveVacio() {
        assertThat(catalog.findById(1)).isEmpty();
    }

    @Test
    void sinOverride_fallaConMensajeConElId() {
        assertThatThrownBy(() -> catalog.findById(99999))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("99999");
    }

    @Test
    void sinHechosDeDano_fallaConMensajeConElId() {
        assertThatThrownBy(() -> catalog.findById(88888))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("88888");
    }
}