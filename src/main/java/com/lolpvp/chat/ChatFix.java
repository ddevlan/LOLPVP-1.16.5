package com.lolpvp.chat;

import com.lolpvp.core.Core;
import com.lolpvp.utils.MojangAPIUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class ChatFix implements Listener, CommandExecutor
{
	private final Core plugin;
	
	public ChatFix(Core plugin)
	{
		this.plugin = plugin;
	}
	
	public void set(Player player, String[] args)
	{
		FileConfiguration fc = this.plugin.playerData(player);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= args.length - 1; i++) {
			sb.append(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[i]))).append(" ");
		}
		String k = sb.toString();
		if (fc.getString("next-set") != null)
		{
			this.plugin.getChatMethod().setTag(player, fc, k.substring(0, k.length() - 1));
			this.plugin.getChatMethod2().setPrefix(player);
		}
		else
		{
			this.plugin.getChatMethod().quitSet(player, fc, k.substring(0, k.length() - 1));
			this.plugin.getChatMethod2().setPrefix(player);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if ((cmd.getName().equalsIgnoreCase("lolt")) && 
				((sender instanceof Player)))
		{
			Player player = (Player)sender;
			if ((!player.hasPermission("lolpvp.settag")) && (!player.hasPermission("lolpvp.*")) && (!player.isOp()))
			{
				player.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			switch (args.length)
			{
			case 0: 
				player.sendMessage(ChatColor.RED + "Usage: /lolt <tag>");
				return true;
			case 1: 
				set(player, args);
				return true;
			}
			set(player, args);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("loltd"))
		{
			if ((!sender.hasPermission("lolpvp.settag.others")) && (!sender.hasPermission("lolpvp.*")) && (!sender.isOp()))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			switch (args.length)
			{
			case 1: 
				if (MojangAPIUtil.getUUID(Collections.singletonList(args[0])).wasSuccessful())
				{
					UUID uuid = MojangAPIUtil.getUUID(Collections.singletonList(args[0])).getValue().get(args[0]).getUUID();
					FileConfiguration fc = this.plugin.playerData(uuid);
					SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy:HH:mm:ss");
					Date date = new Date();
					fc.set("next-set", format.format(date));
					sender.sendMessage(ChatColor.GREEN + "Reset " + MojangAPIUtil.getUUID(Collections.singletonList(args[0])).getValue().get(args[0]).getName() + " set-tag time to now.");
					try
					{
						fc.save(this.plugin.playerFile(uuid));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Could not find player " + args[0]);
				}
				break;
			default: 
				sender.sendMessage(ChatColor.RED + "Usage: /loltd <player>");
			}
		}
		if (cmd.getName().equalsIgnoreCase("lolto"))
		{
			if ((!sender.hasPermission("lolpvp.settag.others")) && (!sender.hasPermission("lolpvp.*")) && (!sender.isOp()))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			switch (args.length)
			{
			case 0: 
			case 1: 
				sender.sendMessage(ChatColor.RED + "Usage: /lolto <player> <tag>");
				return true;
			}
			if (sender.hasPermission("lolpvp.longtag"))
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= args.length - 1; i++) {
					sb.append(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[i]))).append(" ");
				}
				String k = sb.toString();
				this.plugin.getChatMethod().setTagOther(sender, args[0], k.substring(0, k.length() - 1));
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("loltr"))
		{
			if ((!sender.hasPermission("lolpvp.settag.others")) && (!sender.hasPermission("lolpvp.*")) && (!sender.isOp()))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			if (args.length != 1)
			{
				sender.sendMessage(ChatColor.RED + "Usage: /loltr <player>");
				return true;
			}
			this.plugin.getChatMethod().removeTag(sender, args[0]);
		}
		if (cmd.getName().equalsIgnoreCase("lolm"))
		{
			if ((!sender.hasPermission("lolpvp.setmagic")) && (!sender.hasPermission("lolpvp.*")) && (!sender.isOp()))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			switch (args.length)
			{
			case 0: 
				if ((sender instanceof Player))
				{
					Player player = (Player)sender;
					FileConfiguration fc = this.plugin.playerData(player);
					if (fc.getString("tag") == null)
					{
						sender.sendMessage(ChatColor.RED + "You do not have a tag set.");
						return true;
					}
					String ss = this.plugin.getChatMethod().getPrefix(player);
					String tag = fc.getString("tag");
					if ((fc.getString("magic") == null) || (!fc.getBoolean("magic")))
					{
						player.sendMessage(ChatColor.GREEN + "Your tag has been set to: " + ss + "&k" + tag);
						fc.set("magic", Boolean.TRUE);
						this.plugin.getChatMethod2().setPrefix(player);
					}
					else
					{
						player.sendMessage(ChatColor.GREEN + "Your tag has been set to: " + ss + tag);
						fc.set("magic", Boolean.FALSE);
						this.plugin.getChatMethod2().setPrefix(player);
					}
					try
					{
						fc.save(this.plugin.playerFile(player));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				break;
			case 1: 
				if (!sender.hasPermission("lolpvp.setmagic.others"))
				{
					sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
					return true;
				}
				if (!MojangAPIUtil.getUUID(Collections.singletonList(args[0])).wasSuccessful())
				{
					sender.sendMessage(ChatColor.RED + "Could not find player " + args[0]);
					return true;
				}
				UUID uuid = MojangAPIUtil.getUUID(Collections.singletonList(args[0])).getValue().get(args[0]).getUUID();
				Player player = Bukkit.getPlayer(uuid);
				FileConfiguration fc = this.plugin.playerData(uuid);
				if (fc.getString("tag") == null)
				{
					sender.sendMessage(ChatColor.RED + "You do not have a tag set.");
					return true;
				}
				String ss = this.plugin.getChatMethod().getOfflinePrefix(player, Bukkit.getServer().getWorlds().get(0));
				String tag = fc.getString("tag");
				if ((fc.getString("magic") == null) || (!fc.getBoolean("magic")))
				{
					sender.sendMessage(ChatColor.GRAY + "Set " + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " tag to: " + ss + "&k" + tag);
					fc.set("magic", Boolean.TRUE);
				}
				else
				{
					sender.sendMessage(ChatColor.GRAY + "Set " + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " tag to: " + ss + tag);
					fc.set("magic", Boolean.FALSE);
				}
				try
				{
					fc.save(this.plugin.playerFile(uuid));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			default: 
				sender.sendMessage(ChatColor.RED + "Usage: /lolm");
			}
		}
//		else if(cmd.getName().equalsIgnoreCase("lolnt"))
//		{
//			if(!sender.isOp())
//				return true;
//			switch(args.length)
//			{
//			case 0:
//				if(sender instanceof Player)
//				{
//					this.plugin.getChatMethod().setNameTag((Player)sender);
//					sender.sendMessage(ChatColor.GRAY + "You have set your nametag.");
//					return true;
//				}
//				else
//				{
//					sender.sendMessage(ChatColor.RED + "You must be a player to use this command on yourself.");
//					return true;
//				}
//			case 1:
//				this.plugin.getChatMethod().setNameTag(Bukkit.getPlayer(args[0]));
//				sender.sendMessage(ChatColor.GRAY + "You have set " + ChatColor.AQUA + Bukkit.getPlayer(args[0]).getName() + ChatColor.GRAY + "'s name tag.");
//				return true;
//			default: 
//				sender.sendMessage(ChatColor.RED + "Correct usage: /loltnt <player>");
//				return true;
//			}
//		}
//		else if(cmd.getName().equalsIgnoreCase("lolrnt"))
//		{
//			if(!sender.isOp())
//				return true;
//			switch(args.length)
//			{
//			case 0:
//				if(sender instanceof Player)
//				{
//					this.plugin.getChatMethod().removeNameTag((Player)sender);
//					sender.sendMessage(ChatColor.GRAY + "You have removed your nametag.");
//					return true;
//				}
//				else
//				{
//					sender.sendMessage(ChatColor.RED + "You must be a player to use this command on yourself.");
//					return true;
//				}
//			case 1:
//				this.plugin.getChatMethod().removeNameTag(Bukkit.getPlayer(args[0]));
//				sender.sendMessage(ChatColor.GRAY + "You have removed " + ChatColor.AQUA + Bukkit.getPlayer(args[0]).getName() + ChatColor.GRAY + "'s name tag.");
//				return true;
//			default: 
//				sender.sendMessage(ChatColor.RED + "Correct usage: /lolrnt <player>");
//				return true;
//			}
//		}
		return true;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		Player o = event.getPlayer();
		FileConfiguration fc = this.plugin.playerData(o);
		if ((!Core.permission.getPrimaryGroup(o).equalsIgnoreCase("regular")) &&
				(this.plugin.getChatMethod2().hasTag(o)) && 
				(o.hasPermission("lolpvp.settag")))
		{
			if ((this.plugin.getChatMethod2().filterTag(o).startsWith("null")) || (this.plugin.getChatMethod2().filterTag(o).contains("&8"))) {
				this.plugin.getChatMethod().setTag(o, this.plugin.playerData(o), this.plugin.getChatMethod2().filterTag(o).replace("null", "").replace("&8", ""));
			}
			this.plugin.getChatMethod2().setPrefix(o);
		}
		if (o.getName().length() > 14) {
			o.setPlayerListName(ChatColor.GRAY + o.getName().substring(0, 14));
		} else {
			o.setPlayerListName(ChatColor.GRAY + o.getName());
		}
		if (fc.getString("magic") == null)
		{
			fc.set("magic", Boolean.FALSE);
			fc.set("uuid", o.getUniqueId().toString().replace("-", ""));
			try
			{
				fc.save(this.plugin.playerFile(o));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public String filterMessage(Player player, String m)
	{
		if (player.hasPermission("lolpvp.chatcolor")) {
			return ChatColor.translateAlternateColorCodes('&', m.replace("{TAG}", ""));
		}
		return m.replace("{TAG}", "");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		FileConfiguration fc = this.plugin.playerData(player);
		String group = Core.permission.getPrimaryGroup(player);
		String m = event.getMessage();
		String has = this.plugin.getConfig().getString("groups." + group + ".has-tag");
		String no = this.plugin.getConfig().getString("groups." + group + ".no-tag");
		if (fc.getString("tag") != null)
		{
			String tag = this.plugin.getChatMethod2().filterTag(player);
			if (this.plugin.getChatMethod().getPrefix(player) != null)
			{
				event.setMessage(filterMessage(player, m));
				event.setFormat(ChatColor.translateAlternateColorCodes('&', has.replace("{PLAYER}", player.getName()).replace("{TAG}", tag)) + m.replace("%", "%%"));
			}
			else
			{
				event.setMessage(filterMessage(player, m));
				event.setFormat(ChatColor.translateAlternateColorCodes('&', has.replace("{PLAYER}", player.getName()).replace("{TAG}", tag)) + m.replace("%", "%%"));
			}
		}
		else
		{
			event.setMessage(filterMessage(player, m));
			event.setFormat(ChatColor.translateAlternateColorCodes('&', no.replace("{PLAYER}", player.getName())) + m.replace("%", "%%"));
		}
	}
}