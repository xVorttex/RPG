package eu.vortexgg.rpg.command;

import eu.vortexgg.rpg.RPG;
import eu.vortexgg.rpg.config.Perm;
import eu.vortexgg.rpg.quest.Quest;
import eu.vortexgg.rpg.quest.QuestManager;
import eu.vortexgg.rpg.quest.quester.Quester;
import eu.vortexgg.rpg.util.BukkitUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommands implements CommandExecutor {

    final QuestManager questManager;

    public QuestCommands(RPG plugin) {
        this.questManager = QuestManager.get();
        plugin.getCommand("quester").setExecutor(this);
        plugin.getCommand("quest").setExecutor(new QuestCommand());
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (Perm.ADMIN.hasPermission(s)) {
            if (args.length == 0) {
                s.sendMessage(BukkitUtil.formatHelp("НПС Квестовщики"));
                s.sendMessage("/quester setnpc <айди> <нпсАйди>");
                s.sendMessage("/quester create <айди> <имя(Цветное)>");
                return true;
            } else {
                switch (args[0].toLowerCase()) {
                    case "setnpc": {
                        if (args.length == 3) {
                            Quester quester = questManager.getQuesters().get(args[1]);
                            if (quester == null) {
                                s.sendMessage("§cКвестер '" + args[1] + "' не найден");
                                return true;
                            }

                            int in = Integer.parseInt(args[2]);
                            NPC npc = CitizensAPI.getNPCRegistry().getById(in);
                            if (npc == null) {
                                s.sendMessage("§cНПС не найдено.");
                                return true;
                            }

                            quester.setNpc(npc.getUniqueId());
                            if (s instanceof Player)
                                quester.spawnNPC(((Player) s).getLocation());
                        }
                        return false;
                    }
                    case "create": {
                        if (args.length == 3) {
                            String id = args[1].toLowerCase();

                            if (questManager.getQuesters().containsKey(id)) {
                                s.sendMessage("§cТакой квестер уже существует!");
                                return false;
                            }

                            Quester quester = new Quester();
                            quester.setId(id);
                            quester.setDisplayName(BukkitUtil.color(args[2]));

                            questManager.getQuesters().put(id, quester);

                            s.sendMessage("§fВы §aуспешно §fсоздали квестера " + quester.getDisplayName() + "§f, ID: §7" + id);

                        }
                        return false;
                    }
                }

            }
        }
        return false;
    }

    private class QuestCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender s, Command cmd, String arg, String[] args) {
            if (Perm.ADMIN.hasPermission(s)) {
                if (args.length == 0) {
                    s.sendMessage(BukkitUtil.formatHelp("Квесты"));
                    s.sendMessage("/quest create <айди>");
                    return true;
                } else {
                    switch (args[0].toLowerCase()) {
                        case "create": {
                            if (args.length == 2) {
                                String id = args[1].toLowerCase();

                                if (questManager.getQuests().containsKey(id)) {
                                    s.sendMessage("§cТакой квест уже существует!");
                                    return false;
                                }

                                Quest quest = new Quest();
                                quest.setId(id);

                                questManager.getQuests().put(id, quest);

                                s.sendMessage("§fВы §aуспешно §fсоздали квест! ID: " + id);
                                s.sendMessage("Используйте §7/quest " + id + " §fдля изменения.");
                            }
                            return false;
                        }
                        default: {
                            Quest quest = questManager.getQuests().get(args[0].toLowerCase());

                            if (quest == null) {
                                s.sendMessage("§cТакой квест не существует!");
                                return false;
                            }

                            quest.getQuestMenu().open((Player) s);

                        }

                    }
                }
            }
            return false;
        }

    }

}
