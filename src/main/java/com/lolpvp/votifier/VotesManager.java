package com.lolpvp.votifier;

import com.lolpvp.core.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class VotesManager {

    private final Core plugin;

    public VotesManager(Core instance) {
        this.plugin = instance;
        loadVotes();
    }

    public void checkVote(UUID uuid) {
        FileConfiguration fc = this.plugin.playerData(uuid);
        if (fc.get("votes") == null) {
            fc.set("votes", 1);
        }
        else {
            fc.set("votes", fc.getInt("votes") + 1);
        }
        if (this.plugin.getConfig().getStringList("votes." + fc.getInt("votes") + "-votes") != null) {
            giveReward(this.plugin.getServer().getPlayer(uuid));
        }
        try {
            fc.save(this.plugin.playerFile(uuid));
        } catch (IOException e) {
            e.printStackTrace();
            this.plugin.getLogger().log(Level.WARNING, "Couldn't save " + this.plugin.getServer().getOfflinePlayer(uuid).getName() + "'s player data.");
        }
    }

    protected void giveReward(Player player) {
        FileConfiguration fc = this.plugin.playerData(player.getUniqueId());
        if (player != null) {
            if(this.plugin.getConfig().getConfigurationSection("votes." + fc.getInt("votes") + "-votes") != null) {
                for (String rewardCommands : this.plugin.getConfig().getStringList("votes." + fc.getInt("votes") + "-votes.commands")) {
                    String command = rewardCommands.replace("{PLAYER}", player.getName());
                    this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), command);
                }
            }
        } else {
            fc.set("pending-command", this.plugin.getConfig().getStringList("votes." + fc.getInt("votes") + "-votes.commands"));
        }
    }

    public final Map<UUID, Integer> votes = new HashMap<>();

    public SortedMap<UUID, Integer> sortVotes() {
        SortedMap<UUID, Integer> sortedVotes = new TreeMap<UUID, Integer>(new ValueComparator(votes));
        sortedVotes.putAll(votes);
        return sortedVotes;
    }

    public void loadVotes() {
        File users = new File(this.plugin.getDataFolder(), "userdata");
        if (users.exists()) {
            Arrays.stream(Objects.requireNonNull(users.listFiles())).forEach(file -> {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                if (users.getName().contains(".yml")) {
                    UUID uuid = UUID.fromString(users.getName().replace(".yml", ""));
                    votes.put(uuid, config.getInt("votes"));
                }
            });
        }
    }

    private class ValueComparator implements Comparator<UUID> {
        Map<UUID, Integer> base;

        public ValueComparator(Map<UUID, Integer> base) {
            this.base = base;
        }

        @Override
        public int compare(UUID a, UUID b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
