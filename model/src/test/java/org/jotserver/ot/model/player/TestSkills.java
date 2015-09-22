package org.jotserver.ot.model.player;


import static org.junit.Assert.*;

import org.jotserver.ot.model.player.Skills.Skill;
import org.junit.Before;
import org.junit.Test;

public class TestSkills {

    private Skills skills;

    @Before
    public void setUp() throws Exception {
        skills = new Skills();
    }

    @Test
    public void allSkillsAreLevelTenForNewSkills() {
        for(Skills.Type type : Skills.Type.values()) {
            Skill skill = skills.getSkill(type);
            assertNotNull(skill);
            assertEquals(type.toString(), 10, skill.getLevel());
        }
    }

    @Test
    public void allSkillsHaveZeroTriesForNewSkills() {
        for(Skills.Type type : Skills.Type.values()) {
            Skill skill = skills.getSkill(type);
            assertNotNull(skill);
            assertEquals(type.toString(), 0, skill.getTries());
        }
    }

    @Test
    public void canSetSkill() {
        Skills.Skill skill = new Skills.Skill(100);
        skills.setSkill(Skills.Type.CLUB, skill);
        assertSame(skill, skills.getSkill(Skills.Type.CLUB));
    }

    @Test
    public void canAddSkillTries() {
        skills.addTries(Skills.Type.SHIELD, 8);
        assertEquals(8, skills.getSkill(Skills.Type.SHIELD).getTries());
    }

    @Test
    public void skillHasLevelAndTries() {
        Skills.Skill skill = new Skills.Skill(15, 12);
        assertEquals(15, skill.getLevel());
        assertEquals(12, skill.getTries());
    }

    @Test
    public void canSetSkillLevelAndTries() {
        Skills.Skill skill = new Skills.Skill();
        skill.setLevel(15);
        skill.setTries(12);
        assertEquals(15, skill.getLevel());
        assertEquals(12, skill.getTries());
    }

    @Test
    public void canSetSkillPercent() {
        Skills.Skill skill = new Skills.Skill();
        skill.setPercent(86);
        assertEquals(86, skill.getPercent());
    }


}
