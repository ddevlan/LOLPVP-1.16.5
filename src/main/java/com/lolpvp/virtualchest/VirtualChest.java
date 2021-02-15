package com.lolpvp.virtualchest;

import com.lolpvp.core.Core;
import com.lolpvp.utils.MojangAPIUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class VirtualChest implements CommandExecutor
{
	
	private final Core plugin;
	
	public VirtualChest(Core instance)
	{
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			if(commandLabel.equalsIgnoreCase("chest2"))
			{
				if(player.hasPermission("lolpvp.chest2"))
				{
					if(args.length == 0)
					{
						String s = player.getName().toLowerCase();
				        String name = ChatColor.RED + player.getName();
				        Inventory inv = Bukkit.getServer().createInventory(null, 54, name);
				        VirtualChestManager.getInstance().load(s, inv, player);
				        VirtualChestManager.getInstance().in.add(player.getName());	
					}
					else if(args.length == 1)
					{
						if(player.hasPermission("lolpvp.chest2.others"))
						{
							String s = args[0].toLowerCase();

							AtomicReference<String> name = new AtomicReference<>(s);
							AtomicReference<UUID> uuid = new AtomicReference<>();

							MojangAPIUtil.getUUIDAsyncWithCallBack((successful, result, exception) -> {
								MojangAPIUtil.Profile profile = result.get(s);

								if (successful) {
									uuid.set(profile.getUUID());
									name.set(profile.getName());
								} else {
									sender.sendMessage(ChatColor.RED + "Error: " + exception);
								}

							});

							if (uuid.get() != null)
							{
								FileConfiguration fc = this.plugin.playerData(uuid.get());
								if (fc.getConfigurationSection("chest.") != null)
								{
									String displayName = ChatColor.RED + name.get();
									Inventory inv = Bukkit.getServer().createInventory(null, 54, displayName);
									VirtualChestManager.getInstance().load(s, inv, player);
									VirtualChestManager.getInstance().in.add(player.getName());
								}
								else
								{
									player.sendMessage(ChatColor.AQUA + name.get() + ChatColor.RED + " does not have a chest.");
								}
							}
							else
							{
								player.sendMessage(ChatColor.AQUA + args[0] + ChatColor.RED + " is not a player!");
							}	
						}
						else
						{
							player.sendMessage(ChatColor.RED + "You do not have permission for this command.");	
						}	
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				}
			}
			else if(commandLabel.equalsIgnoreCase("clearchest2"))
			{
				if(player.hasPermission("lolpvp.clearchest2"))
				{
					if (args.length == 0) 
					{
						VirtualChestManager.getInstance().clearChest(player, player.getName());
					} 
					
					if(args.length == 1 && player.hasPermission("lolpvp.chearchest2.others")) 
					{	
						if(MojangAPIUtil.getUUID(Collections.singletonList(args[0])).getValue().get(args[0]) != null)
						{
							VirtualChestManager.getInstance().clearChest(player, args[0]);	
						}
						else
						{
							player.sendMessage(ChatColor.AQUA + args[0] + ChatColor.RED + " is not a player!");
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You do not have permission for this command.");	
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You do not have permission for this command.");	
				}
			}
		}
		return false;
	}
}
