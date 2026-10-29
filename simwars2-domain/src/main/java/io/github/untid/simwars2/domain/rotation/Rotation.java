package io.github.untid.simwars2.domain.rotation;

import io.github.untid.simwars2.domain.skill.Skill;

import java.util.List;

public record Rotation(List<Skill> skills) {
    public Rotation{
        if (skills == null || skills.isEmpty()) throw new IllegalArgumentException("skills must be not empty");
        skills = List.copyOf(skills);
    }

    public double cycleSeconds(){

        double totalCastSeconds = 0;

        for(Skill s : skills){

            totalCastSeconds += s.castSeconds();
        }

        return totalCastSeconds;
    }

}
