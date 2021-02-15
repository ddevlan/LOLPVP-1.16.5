package com.lolpvp.perkbooks;

import com.lolpvp.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;



public class PerkBookCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player)sender;
            if (command.getName().equalsIgnoreCase("redeem"))
            {
                if (!player.getItemInHand().getType().equals(Material.AIR)) {

                    if (PerkBookManager.getInstance().isPerkBook(player.getItemInHand())) {

                        String[] regex = ((BookMeta)player.getItemInHand().getItemMeta()).getPage(1).split(" "); byte b; int i; String[] arrayOfString1;
                        for (i = (arrayOfString1 = regex).length, b = 0; b < i; ) { String perk = arrayOfString1[b];

                            for (PerkBook perkbook : PerkBookManager.getPerkBooks()) {

                                if (perk.equalsIgnoreCase(perkbook.getPerk()))
                                {
                                    PerkBookManager.getInstance().redeemPerkBook(player, perkbook);
                                }
                            }

                            b++; }

                    } else {
                        player.sendMessage(ChatColor.RED + "That's not a perkbook!");
                        return true;
                    }

                } else {

                    player.sendMessage(ChatColor.RED + "You must be holding a Perk Book.");
                    return true;
                }
            }

            if (command.getName().equalsIgnoreCase("pgive") && player.isOp()) {
                if (args.length >= 1) {
                    if (PerkBookManager.getInstance().getPerkBookByName(args[0]) != null) {
                        for (PerkBook perkbook : PerkBookManager.getPerkBooks()) {
                            if (args.length == 1) {
                                if (args[0].equalsIgnoreCase(perkbook.getPerk())) {
                                    PerkBookManager.getInstance().givePerkBook(player, PerkBookManager.getInstance().getPerkBookByName(args[0]));
                                    player.sendMessage(ChatColor.GREEN + "You have received a " + ChatColor.AQUA + Core.getInstance().getPerkBookManager().getPerkBookByName(args[0]).getPerk() + " Perk Book" + ChatColor.GREEN + "!");

                                }
                            } else {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                    player.sendMessage(ChatColor.RED + "Player not found.");
                                    return false;
                                }
                                player.sendMessage(ChatColor.GREEN + "You have given " + target.getDisplayName() + ChatColor.GREEN +  " a " + ChatColor.AQUA + Core.getInstance().getPerkBookManager().getPerkBookByName(args[0]).getPerk() + " Perk Book" + ChatColor.GREEN + "!");
                                target.sendMessage(ChatColor.GREEN + "You have received a " + ChatColor.AQUA + Core.getInstance().getPerkBookManager().getPerkBookByName(args[0]).getPerk() + " Perk Book" + ChatColor.GREEN + "!");
                                PerkBookManager.getInstance().givePerkBook(target, PerkBookManager.getInstance().getPerkBookByName(args[0]));
                            }
                        }
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Perk Book not found.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /pgive <book> [player]");
                }
            }
        }
        return false;
    }
}
