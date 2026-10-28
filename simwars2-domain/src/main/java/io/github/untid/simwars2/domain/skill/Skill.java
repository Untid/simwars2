package io.github.untid.simwars2.domain.skill;

import java.util.List;

public record Skill(int id, String name, List<Hit> hits, double castSeconds) {
    public Skill {
        if (id <= 0) throw new IllegalArgumentException("id must be > 0");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must be not blank");
        if (hits == null || hits.isEmpty()) throw new IllegalArgumentException("hits must be not empty");
        if (castSeconds <= 0) throw new IllegalArgumentException("castSeconds must be > 0");
        hits = List.copyOf(hits);
    }

    public double totalCoefficient() {
        double total = 0;

        for (Hit h : hits) {
            total += h.coefficient();
        }

        return total;
    }

}
