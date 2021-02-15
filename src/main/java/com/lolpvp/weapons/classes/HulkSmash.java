package com.lolpvp.weapons.classes;

import com.lolpvp.core.Core;
import com.lolpvp.utils.Cooldowns;
import com.lolpvp.weapons.BallerItem;
import com.lolpvp.weapons.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HulkSmash extends BallerItem{

	public HulkSmash() 
	{
		super(ChatColor.AQUA + "Hulk Smash", Material.SLIME_BALL, 1, lore(), enchantments(), "hulksmash");
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("serial")
	private static HashMap<Enchantment, Integer> enchantments() {
		// TODO Auto-generated method stub
		return new HashMap<Enchantment, Integer>()
		{{
			this.put(Enchantment.DURABILITY, 1);
		}};
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(ItemManager.getInstance().isBallerItem(p.getItemInHand(), this))
			{
				if(Core.isPlayerInPVP(p))
				{
					if(Cooldowns.tryCooldown(p, "hulk", 5 * 1000))
					{
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2f, 1f);
						List<Entity> victims = p.getNearbyEntities(10, 10, 10);
						for (final Entity victim: victims) 
						{
							if (victim instanceof LivingEntity) 
							{
								LivingEntity player = (LivingEntity) victim;
								player.setVelocity(new Vector(0,1.2,0));
							}
						}	
						
						int radius = 10;
						final Block block = p.getLocation().getBlock(); //placed block	
						
						for (int x = -(radius); x <= radius; x += 2)
						{
							for (int z = -(radius); z <= radius; z += 2)
							{
								if(!block.getRelative(x,-1,z).getType().equals(Material.AIR))
								{
									int finalZ = z;
									int finalX = x;
									ParticleData data = new ParticleData() {
										@Override
										public Object toNMSData() {
											return block.getRelative(finalX, -1, finalZ).getBlockData();
										}
									};

									ParticleEffect.BLOCK_CRACK.display(block.getRelative(x, 0, z).getLocation(), 0.0f, 0.0f, 0.0f, 1, 1, data, Bukkit.getOnlinePlayers());
								}
							}
						}
					}
					else
					{
						Long lol = Cooldowns.getCooldown(p, "hulk");
						int bbb = lol.intValue() / 1000;
						p.sendMessage(this.getName() + ChatColor.RED + " is on cooldown for " + bbb + " seconds.");
					}
				}
			}	
		}
	}

	
	
	@SuppressWarnings("serial")
	private static List<String> lore()
	{
		return new ArrayList<String>()
		{{
			this.add(ChatColor.DARK_GRAY + "Right click to smash!");
			this.add(ChatColor.DARK_GRAY + "Avengers: Age Of Ultron Collectable Item.");
		}};
	}
}
