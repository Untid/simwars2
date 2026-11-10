package io.github.untid.simwars2.infrastructure.gw2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.untid.simwars2.domain.skill.Hit;
import io.github.untid.simwars2.domain.skill.Skill;
import io.github.untid.simwars2.domain.skill.SkillCatalogPort;

import java.util.*;


public class Gw2JsonSkillCatalog implements SkillCatalogPort {

    private final Map<Integer, JsonNode> skillsById;
    private final Map<Integer, Double> castSecondsById;

    private Gw2JsonSkillCatalog(Map<Integer, JsonNode> skillsById, Map<Integer, Double> castSecondsById) {
        if (skillsById == null) throw new IllegalArgumentException("skillsById must be not null");
        if (castSecondsById == null) throw new IllegalArgumentException("castSecondsById must be not null");

        this.skillsById = Map.copyOf(skillsById);
        this.castSecondsById = Map.copyOf(castSecondsById);

    }

    public static SkillCatalogPort fromJson(String skillsJson, String overridesJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<Integer, JsonNode> skillsMap = new HashMap<>();
            JsonNode skillsRoot = mapper.readTree(skillsJson);
            if (skillsRoot.isArray()) {
                for (JsonNode node : skillsRoot) {
                    skillsMap.put(node.get("id").asInt(), node);
                }
            }

            Map<Integer, Double> overridesMap = new HashMap<>();
            JsonNode overridesRoot = mapper.readTree(overridesJson);
            if (overridesRoot.isObject()) {
                for (Map.Entry<String, JsonNode> entry : overridesRoot.properties()) {
                    overridesMap.put(Integer.parseInt(entry.getKey()), entry.getValue().asDouble());
                }
            }

            return new Gw2JsonSkillCatalog(skillsMap, overridesMap);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error al parsear el JSON de habilidades u overrides", e);
        }

    }


    @Override
    public Optional<Skill> findById(int id) {

        if (!skillsById.containsKey(id)) {
            return Optional.empty();
        }

        if (!castSecondsById.containsKey(id)) {
            throw new IllegalStateException("No castSeconds override for skill " + id);
        }

        JsonNode node = skillsById.get(id);
        double castSeconds = castSecondsById.get(id);
        String name = node.has("name") ? node.get("name").asText() : "";

        List<Hit> hits = new ArrayList<>();
        JsonNode facts = node.get("facts");

        if (facts != null && facts.isArray()) {
            for (JsonNode fact : facts) {
                if ("Damage".equals(fact.path("type").asText())) {
                    int hitCount = fact.path("hit_count").asInt();
                    double dmgMultiplier = fact.path("dmg_multiplier").asDouble();

                    for (int i = 0; i < hitCount; i++) {
                        hits.add(new Hit(dmgMultiplier));
                    }
                }
            }
        }

        if (hits.isEmpty()) {
            throw new IllegalStateException("Skill " + id + " doesn't have valid hits");
        }

        return Optional.of(new Skill(id, name, hits, castSeconds));
    }
}
