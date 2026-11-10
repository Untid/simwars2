package io.github.untid.simwars2.infrastructure.gw2;

import io.github.untid.simwars2.domain.damage.ExpectedValueCritResolver;
import io.github.untid.simwars2.domain.damage.Target;
import io.github.untid.simwars2.domain.rotation.Rotation;
import io.github.untid.simwars2.domain.rotation.RotationDamage;
import io.github.untid.simwars2.domain.skill.SkillCatalogPort;
import io.github.untid.simwars2.domain.stats.StatSheet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CatalogRotationIntegrationTest {
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
                {"type":"Damage","hit_count":1,"dmg_multiplier":1.6}]}
            ]""";

    private static final String OVERRIDES = """
            {"14369":0.64,"14370":1.04,"14371":1.92}""";

    private final SkillCatalogPort catalog =
            Gw2JsonSkillCatalog.fromJson(SKILLS, OVERRIDES);

    private final StatSheet statSheet = new StatSheet(2556, 1961, 1186);
    private final Target target = new Target(2597);
    private final ExpectedValueCritResolver critResolver = new ExpectedValueCritResolver();

    private Rotation cadena() {
        return new Rotation(List.of(
                catalog.findById(14369).orElseThrow(),
                catalog.findById(14370).orElseThrow(),
                catalog.findById(14371).orElseThrow()));
    }

    @Test
    void cycleSeconds_vale3coma6() {
        double resultado = cadena().cycleSeconds();

        assertThat(resultado).isCloseTo(3.6, within(1e-9));
    }

    @Test
    void cycleDamage_coincideConElModelo() {
        double resultado = RotationDamage.cycleDamage(cadena(), 1000, statSheet, target, critResolver);

        assertThat(resultado).isCloseTo(8634, within(1.0));
    }

    @Test
    void dps_coincideConElModelo() {
        double resultado = RotationDamage.dps(cadena(), 1000, statSheet, target, critResolver);

        assertThat(resultado).isCloseTo(2398, within(1.0));
    }


}
