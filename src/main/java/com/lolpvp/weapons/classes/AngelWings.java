package com.lolpvp.weapons.classes;

import com.lolpvp.weapons.BallerItem;
import com.lolpvp.weapons.ItemManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Luke on 6/16/2015.
 */
public class AngelWings extends BallerItem
{
    public AngelWings()
    {
        super(ChatColor.AQUA + "Angel Wings", Material.DIAMOND_CHESTPLATE, 1, lore(), enchantments(), "angelwings");
    }
    
    HashMap<String, Location> locs = new HashMap<String, Location>();
	ArrayList<String> hitPlayers = new ArrayList<String>();
	ArrayList<String> FlyingPlayers = new ArrayList<String>();
	
	public boolean checkHeight(Player p, Location loc) 
	{
		if (locs.get(p.getName()) != null) 
		{
			return locs.get(p.getName()).getY() + 20 > loc.getY();
		}
		return false;
	}
	
	@EventHandler
	public void OnPlayerSneak(PlayerToggleSneakEvent e) 
	{
		Player p = e.getPlayer();
		if (ItemManager.getInstance().isBallerItem(p.getInventory().getChestplate(), this)) 
		{
			if (p.getLocation().getBlock().getType() != Material.AIR)
			{
				locs.remove(p.getName());
			}
			if (locs.get(p.getName()) == null) 
			{
				locs.put(p.getName(), p.getLocation());
			}
			if (checkHeight(p, p.getLocation()) && !hitPlayers.contains(p.getName())) 
			{
				p.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).add(new Vector(0,0.5,0)));
    			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1f);

				ParticleEffect.FIREWORKS_SPARK.display(p.getLocation(), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.25f, 1, null, Bukkit.getOnlinePlayers());
    			if (p.getAllowFlight() != true)
    			{
    				p.setAllowFlight(true);
    			}
    			if (!FlyingPlayers.contains(p.getName())) 
    			{
    				FlyingPlayers.add(p.getName());
    			}
			}
		}
	}
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) 
	{
		Player p = e.getPlayer();
		if (ItemManager.getInstance().isBallerItem(p.getInventory().getChestplate(), this) || FlyingPlayers.contains(p.getName())) 
		{
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onArrowHitPlayer(EntityDamageByEntityEvent e) 
	{
		if (e.getDamager() instanceof Arrow) 
		{
			if (e.getEntity() instanceof Player) 
			{
				Player p = (Player) e.getEntity();
				if (ItemManager.getInstance().isBallerItem(p.getInventory().getChestplate(), this)) 
				{
					if (p.getLocation().subtract(new Vector(0,1,0)).getBlock().getType() == Material.AIR) 
					{
						hitPlayers.add(p.getName());
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) 
	{
		Player p = e.getPlayer();
		if (FlyingPlayers.contains(p.getName())) 
		{
			if (!ItemManager.getInstance().isBallerItem(p.getInventory().getChestplate(), this)) 
			{
				p.setAllowFlight(false);
			}
			if (p.getLocation().getBlock().getType() != Material.AIR) 
			{
				p.setAllowFlight(false);
				FlyingPlayers.remove(p.getName());
				hitPlayers.remove(p.getName());
			}
		}
	}
    
    @SuppressWarnings("serial")
	private static List<String> lore()
    {
        return new ArrayList<String>()
        {{
                this.add(ChatColor.GRAY + "Flight I");
                this.add(ChatColor.DARK_GRAY + "Shift to fly!");
            }};
    }

    @SuppressWarnings("serial")
	private static HashMap<Enchantment, Integer> enchantments()
    {
        return new HashMap<Enchantment, Integer>()
        {{
                this.put(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        }};
    }
}
