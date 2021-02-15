package com.lolpvp.weapons.classes;

import com.lolpvp.core.Core;
import com.lolpvp.weapons.BallerItem;
import com.lolpvp.weapons.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MagicFeather extends BallerItem
{
	public MagicFeather() 
	{
		super(ChatColor.AQUA + "Magic Feather", Material.FEATHER, 1, lore(), enchantments(), "magicfeather");
		// TODO Auto-generated constructor stub
	}
	
	ArrayList<UUID> players = new ArrayList<UUID>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) 
	{
		Player p = e.getPlayer();
		if (ItemManager.getInstance().isBallerItem(p.getItemInHand(), this) && !Core.isPlayerInPVP(p)) 
		{
			if (!players.contains(p.getUniqueId())) 
			{
				players.add(p.getUniqueId());
			}
			p.setAllowFlight(true);
			p.setFlying(true);

			ParticleData data = new ParticleData() {
				@Override
				public Object toNMSData() {
					return Material.WHITE_WOOL.createBlockData();
				}
			};

			ParticleEffect.BLOCK_CRACK.display(p.getLocation(), 0.0f, 0.0f, 0.0f, 1, 10, data, Bukkit.getOnlinePlayers());
		}
		if (players.contains(p.getUniqueId())) 
		{
			if (Core.isPlayerInPVP(p) || !ItemManager.getInstance().isBallerItem(p.getItemInHand(), this)) 
			{
				p.setFlying(false);
				p.setAllowFlight(false);
				players.remove(p.getUniqueId());
			}
		}
	}
	
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) 
	{
		Player p = e.getPlayer();
		if (players.contains(p.getUniqueId()) || ItemManager.getInstance().isBallerItem(p.getItemInHand(), this)) 
		{
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("serial")
	private static List<String> lore() 
	{
		return new ArrayList<String>()
		{{
			this.add(ChatColor.GRAY + "Flight I");
			this.add(ChatColor.DARK_GRAY + "Hold to fly. Only works in safezones.");
		}};
	}

	@SuppressWarnings("serial")
	private static HashMap<Enchantment, Integer> enchantments()
	{
		return new HashMap<Enchantment, Integer>()
		{{
			this.put(Enchantment.LOOT_BONUS_MOBS, 1);
		}};
	}
	
}