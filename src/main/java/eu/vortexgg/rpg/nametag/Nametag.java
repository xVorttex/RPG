package eu.vortexgg.rpg.nametag;

import com.google.common.collect.Lists;

import eu.vortexgg.rpg.packet.ScoreboardTeamPacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Nametag {

    String name, prefix, suffix;
    ScoreboardTeamPacket packet;

    Nametag(String name, String prefix, String suffix) {
	this.name = name;
	this.prefix = prefix;
	this.suffix = suffix;
	this.packet = new ScoreboardTeamPacket(name, prefix, suffix, Lists.newArrayList(), 0);
    }

}
