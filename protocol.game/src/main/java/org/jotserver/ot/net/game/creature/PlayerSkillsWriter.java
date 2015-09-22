package org.jotserver.ot.net.game.creature;

import org.jotserver.net.CData;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.Skills;
import org.jotserver.ot.model.player.Skills.Skill;
import org.jotserver.ot.model.player.Skills.Type;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerSkillsWriter extends AbstractWriter {

    private static final int OPBYTE = 0xA1;
    private Skills skills;


    public PlayerSkillsWriter(Player player) {
        this(player, player.getSkills());
    }

    public PlayerSkillsWriter(Player receiver, Skills skills) {
        super(receiver);
        this.skills = skills;
    }

    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, OPBYTE); // Skills
        // Skillevel, percent
        for(Type type : Skills.Type.values()) {
            Skill skill = skills.getSkill(type);
            writeSkill(out, skill);
        }
    }

    private void writeSkill(OutputStream out, Skill skill) throws IOException {
        CData.writeByte(out, skill.getLevel());
        CData.writeByte(out, skill.getPercent());
    }

}
