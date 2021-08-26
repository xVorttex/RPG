package eu.vortexgg.rpg.scoreboard;

import com.google.common.collect.*;
import eu.vortexgg.rpg.packet.ScoreboardScorePacket;
import eu.vortexgg.rpg.packet.ScoreboardTeamPacket;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore.EnumScoreboardAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VScoreboard {

    public static final String SCOREBOARD_TITLE = "RPG";

    Player player;
    Objective objective;
    Map<String, Integer> displayedScores = Maps.newHashMap();
    Set<String> sentTeamCreates = Sets.newHashSet(), recentlyUpdatedScores = Sets.newHashSet(), usedBaseScores = Sets.newHashSet();
    Map<String, String> scorePrefixes = Maps.newHashMap(), scoreSuffixes = Maps.newHashMap();
    StringBuilder separateScoreBuilder = new StringBuilder();
    List<String> separateScores = Lists.newArrayList();
    String[] prefixScoreSuffix = new String[3];
    ThreadLocal<List<String>> localList = ThreadLocal.withInitial(ArrayList::new);

    public VScoreboard(Player player) {
        this.player = player;
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("Scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
    }

    public void update() {
        List<String> lines = localList.get();
        if (!lines.isEmpty())
            lines.clear();

        ScoreboardManager.getScores(lines, player);
        recentlyUpdatedScores.clear();
        usedBaseScores.clear();

        int nextValue = lines.size();
        if (nextValue > 16)
            lines.subList(0, 16);

        if (!objective.getDisplayName().equals(SCOREBOARD_TITLE))
            objective.setDisplayName(SCOREBOARD_TITLE);

        for (String line : lines) {
            if (line.length() > 48)
                throw new IllegalArgumentException("Line is too long! Offending line: " + line);

            String[] separated = separate(line, usedBaseScores);
            String prefix = separated[0], score = separated[1], suffix = separated[2];

            recentlyUpdatedScores.add(score);
            if (!sentTeamCreates.contains(score))
                createAndAddMember(score);
            if (!displayedScores.containsKey(score) || displayedScores.get(score) != nextValue)
                setScore(score, nextValue);
            if (!scorePrefixes.containsKey(score) || !scorePrefixes.get(score).equals(prefix)
                    || !scoreSuffixes.get(score).equals(suffix))
                updateScore(score, prefix, suffix);

            nextValue--;
        }

        for (final String displayedScore : ImmutableSet.copyOf(displayedScores.keySet())) {
            if (recentlyUpdatedScores.contains(displayedScore))
                continue;
            removeScore(displayedScore);
        }
    }

    private void createAndAddMember(String scoreTitle) {
        new ScoreboardTeamPacket(scoreTitle, "_", "_", ImmutableList.of(), 0).sendToPlayer(player);
        new ScoreboardTeamPacket(scoreTitle, ImmutableList.of(scoreTitle), 3).sendToPlayer(player);
        sentTeamCreates.add(scoreTitle);
    }

    private void setScore(String score, int value) {
        new ScoreboardScorePacket(score, objective.getName(), value, EnumScoreboardAction.CHANGE).sendToPlayer(player);
        displayedScores.put(score, value);
    }

    private void removeScore(String score) {
        displayedScores.remove(score);
        scorePrefixes.remove(score);
        scoreSuffixes.remove(score);
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(new PacketPlayOutScoreboardScore(score));
    }

    private void updateScore(String score, String prefix, String suffix) {
        scorePrefixes.put(score, prefix);
        scoreSuffixes.put(score, suffix);
        new ScoreboardTeamPacket(score, prefix, suffix, null, 2).sendToPlayer(player);
    }

    private String[] separate(String line, Collection<String> usedBaseScores) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        String prefix = "", score = "", suffix = "";
        separateScores.clear();
        separateScoreBuilder.setLength(0);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '*' || (separateScoreBuilder.length() == 16 && separateScores.size() < 3)) {
                separateScores.add(separateScoreBuilder.toString());
                separateScoreBuilder.setLength(0);
                if (c == '*')
                    continue;
            }
            separateScoreBuilder.append(c);
        }
        separateScores.add(separateScoreBuilder.toString());
        switch (separateScores.size()) {
            case 1:
                score = separateScores.get(0);
                break;
            case 2:
                score = separateScores.get(0);
                suffix = separateScores.get(1);
                break;
            case 3:
                prefix = separateScores.get(0);
                score = separateScores.get(1);
                suffix = separateScores.get(2);
                break;
            default:
                Bukkit.getLogger().warning("Failed to separate scoreboard line. Input: " + line);
                break;
        }
        if (usedBaseScores.contains(score)) {
            if (score.length() <= 14) {
                for (ChatColor chatColor : ChatColor.values()) {
                    String possibleScore = chatColor + score;
                    if (!usedBaseScores.contains(possibleScore)) {
                        score = possibleScore;
                        break;
                    }
                }
            } else {
                Bukkit.getLogger().warning("Found a scoreboard base collision to shift: " + score);
            }
        }
        usedBaseScores.add(score);
        prefixScoreSuffix[0] = prefix;
        prefixScoreSuffix[1] = score;
        prefixScoreSuffix[2] = suffix;
        return prefixScoreSuffix;
    }

}
