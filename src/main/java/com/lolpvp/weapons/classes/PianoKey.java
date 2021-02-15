package com.lolpvp.weapons.classes;

import com.earth2me.essentials.Essentials;
import com.lolpvp.core.Core;
import com.lolpvp.utils.Cooldowns;
import com.lolpvp.weapons.BallerItem;
import com.lolpvp.weapons.ItemManager;
import me.confuser.barapi.BarAPI;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PianoKey extends BallerItem
{
	Core plugin;
	public PianoKey(Core instance) 
	{
		super(ChatColor.AQUA + "Piano Key", Material.DIAMOND_SWORD, 1, lore(), enchantments(), "pianokey");
		this.plugin = instance;
		// TODO Auto-generated constructor stub
	}
	
	ArrayList<Inventory> inventory = new ArrayList<Inventory>();
	ArrayList<String> lore = new ArrayList<String>();
	
	public ArrayList<Location> getCircle(Location center, double radius, int amount)
	{
        World world = center.getWorld();
        double increment = (2*Math.PI)/amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++){
        double angle = i*increment;
        double x = center.getX() + (radius * Math.cos(angle));
        double z = center.getZ() + (radius * Math.sin(angle));
        locations.add(new Location(world, x, center.getY() + 1, z));
        }
        return locations;
    }
	
    public static void playRecord(Player p, Location loc, Integer record)
    {
    	p.getWorld().playEffect(loc, Effect.RECORD_PLAY, record);
    }
   
    public static void stopRecord(Player p, Location loc)
    {
		p.getWorld().playEffect(loc, Effect.RECORD_PLAY, 0);
    }
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) 
	{
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) 
		{
			Player p = (Player) e.getDamager();
			Player player = (Player) e.getEntity();
			
			if (ItemManager.getInstance().isBallerItemName(p.getItemInHand(), this)) 
			{
				ParticleEffect.NOTE.display(player.getLocation(), 0, 1, 0, 1, 2, null, Bukkit.getOnlinePlayers());
				ParticleEffect.NOTE.display(player.getLocation(), 0, 1, 1, 1, 2, null, Bukkit.getOnlinePlayers());
				ParticleEffect.NOTE.display(player.getLocation(), 1, 1, 0, 1, 2, null, Bukkit.getOnlinePlayers());
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) 
	{
		Player p = e.getEntity();
		if (p.getKiller() instanceof Player) 
		{
			Player player = p.getKiller();
			if (ItemManager.getInstance().isBallerItemName(player.getItemInHand(), this)) 
			{
				if (player.getItemInHand().getItemMeta().getLore().get(3) != null) 
				{
					String id = player.getItemInHand().getItemMeta().getLore().get(3).replace(ChatColor.DARK_GRAY + "Selected Song: ", "");
					for (final Player other: Bukkit.getOnlinePlayers()) 
					{
						if (other != null && other != p) 
						{
							final Location loc = other.getLocation().clone();
							playRecord(other, loc, Integer.parseInt(ChatColor.stripColor(id)));
							plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() 
							{
								public void run() 
								{
									stopRecord(other, loc);
								}
							}, 100L);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void OnRightClick(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (ItemManager.getInstance().isBallerItemName(p.getItemInHand(), this)) 
		{
			if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) 
			{
				if (p.isSneaking()) 
				{
					Inventory inv = Bukkit.getServer().createInventory(p, 9, ChatColor.RED + "Piano Key Song Customizer");
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_3));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_4));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_5));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_6));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_7));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_8));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_9));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_10));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_11));
					inv.addItem(new ItemStack(Material.LEGACY_RECORD_12));
					if (!inventory.contains(inv)) 
					{
						inventory.add(inv);
					}
					p.openInventory(inv);
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void OnRecordClick(InventoryClickEvent e) 
	{
		if (inventory.contains(e.getInventory())) 
		{
			if (e.getWhoClicked() instanceof Player) 
			{
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_3) || e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_4)
						|| e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_5) || e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_6)
						|| e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_7) || e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_8)
						|| e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_9) || e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_10)
						|| e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_11) || e.getCurrentItem().getType().equals(Material.LEGACY_RECORD_12))
				{
					ItemMeta meta = p.getItemInHand().getItemMeta();
					lore.clear();
					lore.addAll(meta.getLore());
					String string = lore.get(3).replace(lore.get(3), ChatColor.DARK_GRAY + "Selected Song: " + e.getCurrentItem().getType().getId());
					lore.remove(3);
					lore.add(string);
					meta.setLore(lore);
					p.getItemInHand().setItemMeta(meta);
				}
				p.updateInventory();
				inventory.remove(e.getInventory());
				p.closeInventory();
			}
		}
	}
	@EventHandler
	public void onShift(PlayerToggleSneakEvent e)
	{
		final Player p = e.getPlayer();
		final ItemStack i = p.getItemInHand();

		if (ItemManager.getInstance().isBallerItemName(i, this)) 
		{
			if(Cooldowns.tryCooldown(p, "pianokey", 3 * 1000))
			{
				new BukkitRunnable()
				{
					Integer seconds = 0;

					public void run()
					{
						if(p.isSneaking())
						{
							if(seconds.intValue() <= 10)
							{
								float bar = seconds.floatValue() * 10.0F;
								BarAPI.setMessage(p, ChatColor.AQUA + "" + ChatColor.BOLD + "Music Radius: " + seconds + ".0", bar);
								seconds = seconds.intValue() + 1;
							} else {
								activate(p, seconds);
								cancel();
							}
						}
						else
						{
							if(seconds.intValue() > 1)
							{
								activate(p, seconds.intValue());
							} else {
								BarAPI.removeBar(p);
							}
							cancel();
						}
					}
				}.runTaskTimer(plugin, 0L, 20L);	
			}
			else
			{
				Long lol = Cooldowns.getCooldown(p, "pianokey");
				int bbb = lol.intValue() / 1000;
				p.sendMessage(this.getName() + ChatColor.RED + " is on cooldown for " + bbb + " seconds.");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void activate(final Player p, final Integer seconds)
	{
		final World world = p.getWorld();
		BarAPI.removeBar(p);
		if (Core.isPlayerInPVP(p) && !InvisRing.in.contains(p.getName())) 
		{
			for (Location loc : getCircle(p.getLocation(), seconds.doubleValue(), 10)) 
    		{
				ParticleEffect.NOTE.display(loc);
    		}
			List<Entity> victims = p.getNearbyEntities(seconds.doubleValue(), seconds.doubleValue(), seconds.doubleValue());
			for (final Entity victimz: victims) 
			{
				if (victimz instanceof Player) 
				{
					Damageable victim = (Damageable) victimz;
					if (Core.isPlayerInPVP((Player)victim)) 
					{
						new BukkitRunnable()
						{
							int i = 0;
							@Override
							public void run()
							{
								if(i < 15)
								{
									victimz.getWorld().playSound(victimz.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.2f * (i + seconds));
									for(int j = 0; j < 2; j++)
										ParticleEffect.NOTE.display(victimz.getLocation());
								}
								else
								{
									cancel();
								}
								i++;
							}
						}.runTaskTimer(plugin, 0L, 2L);
						
						if(!Essentials.getPlugin(Essentials.class).getUser((Player)victim).isGodModeEnabled())
						{
							if (victim.getHealth() > 4) 
							{
								victim.setHealth(victim.getHealth() - 4);
								victim.playEffect(EntityEffect.HURT);
							} else {
								victim.playEffect(EntityEffect.HURT);
								victim.setHealth(0);
							}	
						}
					}
				}
			}
		} else {
			p.sendMessage(ChatColor.RED + "You can only use this in PVP enabled areas!");
		}
	}
	
	@SuppressWarnings("serial")
	private static List<String> lore() 
	{
		return new ArrayList<String>()
		{{
			this.add(ChatColor.GRAY + "Music I");
			this.add(ChatColor.DARK_GRAY + "Hold shift to play music.");
			this.add(ChatColor.DARK_GRAY + "Hold shift and right click air to select a song.");
			this.add(ChatColor.DARK_GRAY + "Selected Song: ");
		}};
	}

	@SuppressWarnings("serial")
	private static HashMap<Enchantment, Integer> enchantments()
	{
		return new HashMap<Enchantment, Integer>()
		{{
			this.put(Enchantment.DAMAGE_ALL, 10);
			this.put(Enchantment.DAMAGE_UNDEAD, 10);
		}};
	}
}
