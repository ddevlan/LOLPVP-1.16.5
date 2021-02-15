package com.lolpvp.chat;

import com.lolpvp.core.Core;
import com.lolpvp.utils.MojangAPIUtil;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.lolpvp.core.Core.has;

public class ChatMethod
{
	private final Core plugin;

	public ChatMethod(Core plugin)
	{
		this.plugin = plugin;
	}

	public String getPrefix(Player player)
	{
		String kk = player.hasPermission("lolpvp.gray") ? "&7" : "";
		kk = player.hasPermission("lolpvp.darkred") ? "&4" : kk;
		kk = player.hasPermission("lolpvp.black") ? "&0" : kk;
		kk = player.hasPermission("lolpvp.green") ? "&a" : kk;
		kk = player.hasPermission("lolpvp.red") ? "&c" : kk;
		kk = player.hasPermission("lolpvp.gold") ? "&6" : kk;
		return kk;
	}
	
	public ChatColor getPrefixChatColor(Player player)
	{
		ChatColor kk = player.hasPermission("lolpvp.gray") ? ChatColor.GRAY : ChatColor.WHITE;
		kk = player.hasPermission("lolpvp.darkred") ? ChatColor.DARK_RED : kk;
		kk = player.hasPermission("lolpvp.black") ? ChatColor.BLACK : kk;
		kk = player.hasPermission("lolpvp.green") ? ChatColor.GREEN : kk;
		kk = player.hasPermission("lolpvp.red") ? ChatColor.RED : kk;
		kk = player.hasPermission("lolpvp.gold") ? ChatColor.GOLD : kk;
		return kk;
	}

	public String getOfflinePrefix(OfflinePlayer player, World w)
	{
		User user = Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId());
		assert user != null;
		String kk = has(user, "lolpvp.gray") ? "&7" : "";
		kk = has(user, "lolpvp.darkred") ? "&4" : kk;
		kk = has(user, "lolpvp.black") ? "&0" : kk;
		kk = has(user, "lolpvp.green") ? "&a" : kk;
		kk = has(user, "lolpvp.red") ? "&c" : kk;
		kk = has(user, "lolpvp.gold") ? "&6" : kk;
		return kk;
	}

	public Date add(String[] parts)
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int seconds = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		int hours = Integer.parseInt(parts[2]);
		int days = Integer.parseInt(parts[3]);
		int months = Integer.parseInt(parts[4]);
		int years = Integer.parseInt(parts[5]);
		calendar.add(13, seconds);
		calendar.add(12, minutes);
		calendar.add(11, hours);
		calendar.add(5, days);
		calendar.add(2, months);
		calendar.add(1, years);
		return calendar.getTime();
	}

	public void quitSet(Player player, FileConfiguration fc, String tag)
	{
		if (tagCheck(player, tag))
		{
			SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy:HH:mm:ss");
			String[] parts = this.plugin.getConfig().getString("groups." + Core.permission.getPrimaryGroup(player) + ".next-set-time").split(":");
			String finalone = format.format(add(parts));
			player.sendMessage(ChatColor.GREEN + "Your tag has been set to: " + getPrefix(player) + tag);
			fc.set("magic", Boolean.FALSE);
			fc.set("next-set", finalone);
			fc.set("tag", tag);
			try
			{
				fc.save(this.plugin.playerFile(player));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean tagCheck(Player player, String tag)
	{
		if ((player.hasPermission("lolpvp.longtag")) && (!player.hasPermission("lolpvp.owner")))
		{
			int i = this.plugin.getConfig().getInt("max-longtag-length");
			if (tag.length() > i)
			{
				player.sendMessage(ChatColor.RED + "That tag is too long.");
				return false;
			}
		}
		else if ((!player.hasPermission("lolpvp.longtag")) && (!player.hasPermission("lolpvp.owner")))
		{
			int i = this.plugin.getConfig().getInt("max-normaltag-length");
			if (tag.length() > i)
			{
				player.sendMessage(ChatColor.RED + "That tag is too long.");
				return false;
			}
		}
		if (!player.hasPermission("lolpvp.owner")) {
			for (String ss : this.plugin.getConfig().getStringList("blocked-tags")) {
				if (tag.toLowerCase().contains(ss.toLowerCase()))
				{
					player.sendMessage(ChatColor.RED + "That tag contains an illegal word.");
					return false;
				}
			}
		}
		if (!tag.matches("[a-zA-Z0-9$+# ]*"))
		{
			player.sendMessage(ChatColor.RED + "That tag contains an illegal word.");
			return false;
		}
		return true;
	}

	public String getMonthForInt(int num)
	{
		String month = "null";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if ((num >= 0) && (num <= 11)) {
			month = months[num];
		}
		return month;
	}
	
//	public void setNameTag(Player player)
//	{
//		NametagAPI.setPrefix(player.getName(), this.getPrefix(player));
//	}
//	
//	public void removeNameTag(Player player)
//	{
//		NametagAPI.resetNametag(player.getName());
//	}

	public void setTag(Player player, FileConfiguration fc, String tag)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy:HH:mm:ss");
		String seconds, minutes, hours, pm, days, months, years;
		if (tagCheck(player, tag)) {
			try
			{
				Date d = format.parse(fc.getString("next-set"));
				Date date = new Date();
				if ((date.after(d)) || (player.hasPermission("lolpvp.settag.bypass")))
				{
					String[] parts = this.plugin.getConfig().getString("groups." + Core.permission.getPrimaryGroup(player) + ".next-set-time").split(":");
					String finalone = format.format(add(parts));
					String kk = getPrefix(player) + tag;
					player.sendMessage(ChatColor.GREEN + "Your tag has been set to: " + kk);
					fc.set("tag", tag);
					fc.set("magic", Boolean.FALSE);
					fc.set("next-set", finalone);
					try
					{
						fc.save(this.plugin.playerFile(player));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					String[] parts = fc.getString("next-set").split(":");
					seconds = parts[5];
					minutes = parts[4];
					if (Integer.parseInt(parts[3]) > 12)
					{
						hours = Integer.toString(Integer.parseInt(parts[3]) - 12);
						pm = "PM";
					}
					else
					{
						hours = parts[3];
						pm = "AM";
					}
					days = parts[0];
					months = getMonthForInt(Integer.parseInt(parts[1]) - 1);
					years = parts[2];
					player.sendMessage(ChatColor.RED + "You cannot set a tag until: " + months + " " + days + ", " + years + "; " + hours + ":" + minutes + ":" + seconds + " " + pm);
					player.sendMessage(ChatColor.RED + "Upgrade your rank at " + ChatColor.LIGHT_PURPLE + "www.LOLPVP.com " + ChatColor.RED + " to upgrade your tag faster!");
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void setTagOther(CommandSender sender, String s, String tag)
	{
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

		if (Bukkit.getServer().getPlayer(uuid.get()) != null)
		{
			uuid.set(Objects.requireNonNull(Bukkit.getPlayer(s)).getUniqueId());
			name.set(Objects.requireNonNull(Bukkit.getServer().getPlayer(s)).getName());
		}
		else
		{
			name.set(MojangAPIUtil.getSkinData(uuid.get()).getValue().getName());
		}

		if (!this.plugin.playerFile(uuid.get()).exists()) {
			try {
				this.plugin.playerFile(uuid.get()).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED + "Error: " + e);
			}
		}

		FileConfiguration fc = (this.plugin.playerData(uuid.get()));
		if (!tag.matches("[a-zA-Z0-9$+# ]*"))
		{
			sender.sendMessage(ChatColor.RED + "That tag contains an illegal symbol.");
			return;
		}
		fc.set("tag", tag);
		fc.set("magic", Boolean.FALSE);
		if (Bukkit.getServer().getPlayer(name.get()) != null)
		{
			sender.sendMessage(ChatColor.GRAY + "Set " + ChatColor.AQUA + name.get() + ChatColor.GRAY + "'s tag to: " + getOfflinePrefix(Bukkit.getPlayer(name.get()), Bukkit.getServer().getWorlds().get(0)) + tag);
			this.plugin.getChatMethod2().setPrefix(Bukkit.getServer().getPlayer(name.get()));
		}
		else
		{
			sender.sendMessage(ChatColor.GRAY + "Set " + ChatColor.AQUA + name.get() + ChatColor.GRAY + "'s tag to: " + getOfflinePrefix(Bukkit.getOfflinePlayer(name.get()), Bukkit.getServer().getWorlds().get(0)) + tag);
		}
		try
		{
			fc.save(this.plugin.playerFile(uuid.get()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void removeTag(Player player)
	{
		FileConfiguration fc = this.plugin.playerData(player);
		fc.set("tag", null);
		fc.set("magic", null);
		try
		{
			fc.save(this.plugin.playerFile(player));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void removeTag(CommandSender sender, String s)
	{
		AtomicReference<String> name = new AtomicReference<>(s);
		AtomicReference<UUID> uuid = new AtomicReference<>();

		MojangAPIUtil.getUUIDAsyncWithCallBack((successful, result, exception) -> {
			MojangAPIUtil.Profile profile = result.get(s);

			if (successful) {
				uuid.set(profile.getUUID());
				name.set(profile.getName());

				UUID uid = profile.getUUID();
				FileConfiguration fc = this.plugin.playerData(uid);
				fc.set("tag", null);
				fc.set("magic", null);
				sender.sendMessage(ChatColor.RED + "Reset " + profile.getName() + "'s tag.");
				try
				{
					fc.save(this.plugin.playerFile(uid));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Could not find player: " + s);
				sender.sendMessage(ChatColor.RED + "Error: " + exception);
			}
		});
	}

}
