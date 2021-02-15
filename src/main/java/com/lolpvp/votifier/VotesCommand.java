package com.lolpvp.votifier;

import com.lolpvp.core.Core;
import com.lolpvp.core.Permissions;
import com.lolpvp.utils.UUIDFetcher;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedMap;
import java.util.UUID;
import java.util.logging.Level;

public class VotesCommand implements CommandExecutor {

    private final Core plugin;

    public VotesCommand(Core instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("votes")) {
                FileConfiguration playerData;
                if (args.length == 0) {
                    playerData = this.plugin.playerData(player);
                    player.sendMessage(ChatColor.AQUA + "10" + ChatColor.GRAY + " votes: " + ChatColor.AQUA + "10 diamonds.");
                    player.sendMessage(ChatColor.AQUA + "25" + ChatColor.GRAY + " votes: " + ChatColor.AQUA + "$10,000 ingame money.");
                    player.sendMessage(ChatColor.AQUA + "50" + ChatColor.GRAY + " votes: " + ChatColor.AQUA + "100 diamonds.");
                    player.sendMessage(ChatColor.AQUA + "75" + ChatColor.GRAY + " votes: " + ChatColor.AQUA + "$100,000 ingame money.");
                    player.sendMessage(ChatColor.AQUA + "100" + ChatColor.GRAY + " votes: " + ChatColor.AQUA + "50 OP Apples.");
                    player.sendMessage(ChatColor.AQUA + "125" + ChatColor.GRAY + " votes: " + ChatColor.AQUA + "$25 donation voucher.");
                    int votes = playerData.getInt("votes");
                    player.sendMessage(ChatColor.GRAY + "Total votes for this month: " + ChatColor.AQUA + votes);
                } else if (!(args[0].equalsIgnoreCase("top") || args[0].equalsIgnoreCase("reset"))) {
                    OfflinePlayer offlinePlayer = null;
                    String otherPlayer = args[0];
                    try {
                        offlinePlayer = this.plugin.getServer().getOfflinePlayer(UUIDFetcher.getUUIDOf(otherPlayer));
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.plugin.getLogger().log(Level.WARNING, "Couldn't find UUID of the Username: " + otherPlayer);
                    }
                    if (player.hasPermission(Permissions.VOTES_OTHERS.toString()) && offlinePlayer != null) {
                        this.plugin.playerData(offlinePlayer);
                    }
                } else if (args[0].equalsIgnoreCase("top")) {
                    SortedMap<UUID, Integer> sortedMap = this.plugin.getVotesManager().sortVotes();
                    int rank = 1;
                    for (UUID votedPlayers : sortedMap.keySet()) {
                        String message = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("votestop"))
                                .replace("{RANK}", Integer.valueOf(rank).toString())
                                .replace("{PLAYER}", Objects.requireNonNull(this.plugin.getServer().getOfflinePlayer(votedPlayers).getName()))
                                .replace("{VOTES}", sortedMap.get(votedPlayers).toString()));
                        player.sendMessage(message);
                        rank++;
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    player.sendMessage(ChatColor.GREEN + "Everyone's votes has been reset.");
                    File users = new File(this.plugin.getDataFolder(), "userdata");
                    if (users.exists()) {
                        Arrays.stream(Objects.requireNonNull(users.listFiles())).forEach(file -> {
                            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                            config.set("votes", 0);
                            config.set("pending-commands", null);
                            try {
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                                this.plugin.getLogger().log(Level.WARNING, "Couldn't save " + player.getName() + "'s data file.");
                            }
                        });
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

