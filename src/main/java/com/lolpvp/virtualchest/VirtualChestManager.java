package com.lolpvp.virtualchest;

import com.lolpvp.core.Core;
import com.lolpvp.utils.MojangAPIUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class VirtualChestManager 
{
	private static Core plugin;
	private static VirtualChestManager instance;

	public final HashSet<String> in = new HashSet<String>();
	
	public static void setup(Core core)
	{
		plugin = core;
	}
	
	public void load(String s, Inventory inv, Player player)
	{
		AtomicReference<String> name = new AtomicReference<>(s);
		AtomicReference<UUID> uuid = new AtomicReference<>();

		MojangAPIUtil.getUUIDAsyncWithCallBack((successful, result, exception) -> {
			MojangAPIUtil.Profile profile = result.get(s);

			if (successful) {
				uuid.set(profile.getUUID());
				name.set(profile.getName());
			} else {
				exception.printStackTrace();
			}

		});

		FileConfiguration fc = plugin.playerData(uuid.get());
		if (fc.getConfigurationSection("chest.") != null) {
			for (String ss : fc.getConfigurationSection("chest.").getKeys(false)) {
				inv.setItem(Integer.parseInt(ss), fc.getItemStack("chest." + ss));
			}
		}
		player.openInventory(inv);
	}

	public void save(InventoryView inv)
	{
		String s = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', inv.getTitle())).toLowerCase();

		AtomicReference<String> name = new AtomicReference<>(s);
		AtomicReference<UUID> uuid = new AtomicReference<>();

		MojangAPIUtil.getUUIDAsyncWithCallBack((successful, result, exception) -> {
			MojangAPIUtil.Profile profile = result.get(s);

			if (successful) {
				uuid.set(profile.getUUID());
				name.set(profile.getName());
			} else {
				exception.printStackTrace();
			}

		});


		FileConfiguration fc = plugin.playerData(uuid.get());
		int slot = 0;
		for (ItemStack stack : inv.getTopInventory().getContents())
		{
			fc.set("chest." + slot, stack);
			slot++;
		}
		try
		{
			fc.save(plugin.playerFile(uuid.get()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clearChest(Player player, String s)
	{
		AtomicReference<String> name = new AtomicReference<>(s);
		AtomicReference<UUID> uuid = new AtomicReference<>();

		MojangAPIUtil.getUUIDAsyncWithCallBack((successful, result, exception) -> {
			MojangAPIUtil.Profile profile = result.get(s);

			if (successful) {
				uuid.set(profile.getUUID());
				name.set(profile.getName());
			} else {
				exception.printStackTrace();
				uuid.set(null);
			}

		});

		if (uuid.get() != null)
		{
			FileConfiguration fc = plugin.playerData(uuid.get());
			if (fc.getConfigurationSection("chest.") != null)
			{
				fc.set("chest", null);
				try
				{
					fc.save(plugin.playerFile(uuid.get()));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				player.sendMessage(ChatColor.GRAY + "Cleared " + ChatColor.AQUA + name + ChatColor.GRAY + "'s chest.");
			}
			else
			{
				player.sendMessage(ChatColor.AQUA + name.get() + ChatColor.RED + "does not have a chest.");
			}
		}
		else
		{
			player.sendMessage(ChatColor.AQUA + name.get() + ChatColor.RED + " is not a player!");
		}
	}
	
	public static VirtualChestManager getInstance()
	{
		if(instance == null)
			instance = new VirtualChestManager();
		return instance;
	}
}
