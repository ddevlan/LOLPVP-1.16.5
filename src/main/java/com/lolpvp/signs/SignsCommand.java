package com.lolpvp.signs;

import com.lolpvp.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignsCommand implements CommandExecutor {

    private final Core plugin;

    public SignsCommand(Core instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getLabel().equalsIgnoreCase("commandsign")) {
            if (!(commandSender instanceof Player)) {
                Player player = (Player) commandSender;
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Invalid arguments.");
                    return false;
                } else {
                    if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c") && args.length >= 4) {
                        int price = -1337;
                        try {
                            price = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "Error: " + e);
                            player.sendMessage(ChatColor.RED + "Correct usage: /commandsign <create|addcommand> <command> [price]");
                        }

                        if (price <= 0) {
                            player.sendMessage(ChatColor.RED + "Invalid number or too low of a number.");
                        }

                        if((player.getTargetBlock(null, 6).getType().name().contains("SIGN")) || (player.getTargetBlock(null, 6).getType().name().contains("_WALL_SIGN"))) {
                            Sign sign = (Sign)(player.getTargetBlock(null, 6)).getState();
                            this.plugin.getSignsManager().createCommandSign(sign, args[1].replace("/", ""), price);
                            this.plugin.getSignsManager().reloadSignData();
                            player.sendMessage(ChatColor.GREEN + "Created command sign successfully.");
                        } else {
                            player.sendMessage(ChatColor.RED + "You must be looking at a sign.");
                        }
                    } else if (args[0].equalsIgnoreCase("ac") || args[0].equalsIgnoreCase("addcommand")) {
                        if((player.getTargetBlock(null, 6).getType().name().contains("SIGN")) || (player.getTargetBlock(null, 6).getType().name().contains("_WALL_SIGN"))) {
                            Sign sign = (Sign)(player.getTargetBlock(null, 6)).getState();
                            if(this.plugin.getSignsManager().isCommandSign(sign)) {
                                this.plugin.getSignsManager().addCommandToSign(sign, args[1].replace("/", ""));
                                player.sendMessage(ChatColor.GREEN + "Added command to sign successfully.");
                                this.plugin.getSignsManager().reloadSignData();
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Correct usage: /commandsign <create|addcommand> <command> [price]");
                    }
                }
            }
        }
        return false;
    }

}
