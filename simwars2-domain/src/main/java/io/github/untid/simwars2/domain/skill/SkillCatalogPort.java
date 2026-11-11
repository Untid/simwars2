package io.github.untid.simwars2.domain.skill;

import java.util.Optional;

public interface SkillCatalogPort {
    Optional<Skill> findById(int id);
}
