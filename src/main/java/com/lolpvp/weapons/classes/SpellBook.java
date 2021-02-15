package com.lolpvp.weapons.classes;

import com.lolpvp.core.Core;
import com.lolpvp.weapons.BallerItem;
import com.lolpvp.weapons.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpellBook extends BallerItem 
{
	Core plugin;
	public SpellBook(Core core)
	{
		super(ChatColor.AQUA + "Spell Book", Material.BOOK, 1, lore(), enchantments(), "spellbook");
		plugin = core;
		// TODO Auto-generated constructor stub
	}
	
	ArrayList<String> lore = new ArrayList<String>();

	HashMap<Player, Integer> hashmap = new HashMap<Player, Integer>();

	public void replenishMana(Player p, ItemStack i) 
	{
		if (ItemManager.getInstance().isBallerItemName(i, this)) 
		{
			final ItemMeta meta = i.getItemMeta();
			String total = meta.getLore().get(0).replace(meta.getLore().get(0), ChatColor.GRAY + "Evil Spells: 5");
			lore.clear();
			lore.add(total);
			lore.add(ChatColor.DARK_GRAY + "A magic spellbook!");
			meta.setLore(lore);
			i.setItemMeta(meta);
			p.updateInventory();
		}
	}
	
	public void useMana(Player p, ItemStack i) 
	{
		ItemMeta meta = i.getItemMeta();
		String number = meta.getLore().get(0).replace("Evil Spells: ", "");
		String total = meta.getLore().get(0).replace(meta.getLore().get(0), ChatColor.GRAY + "Evil Spells: " + (Integer.parseInt(ChatColor.stripColor(number)) - 1));
		lore.clear();
		lore.add(total);
		lore.add(ChatColor.DARK_GRAY + "A magic spellbook!");
		meta.setLore(lore);
		i.setItemMeta(meta);
		p.updateInventory();
	}
	
	public void throwSpellBook(Player p) 
	{
//		final ItemProjectile item = new ItemProjectile("SpellBook", p, p.getItemInHand(), 1);
//		new BukkitRunnable()
//		{
//			public void run()
//			{
//				if (!item.getEntity().isDead() && !item.getEntity().isOnGround())
//				{
//					ParticleEffect.CRIT_MAGIC.display(item.getEntity().getLocation());
//				} else {
//					cancel();
//                }
//			}
//		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) 
	{
		final Player p = e.getPlayer();
		if (ItemManager.getInstance().isBallerItemName(p.getItemInHand(), this)) 
		{
			if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) 
			{
				if (!hashmap.containsKey(p)) 
				{
					hashmap.put(p, 5);
				}
				if (hashmap.get(p) != 0) 
				{
					throwSpellBook(p);
					useMana(p, p.getItemInHand());
					hashmap.put(p, hashmap.get(p) - 1);
					if (hashmap.get(p) == 0) 
					{
						p.sendMessage(ChatColor.RED + "You are out of mana! Please keep this item in your inventory to regenerate.");
						new BukkitRunnable()
						{
							Integer seconds = 0;

							public void run()
							{
								if(seconds.intValue() < 5)
								{
									seconds = seconds.intValue() + 1;
								} else {
									for (ItemStack it : p.getInventory().getContents()) 
									{
										replenishMana(p, it);
									}
									p.sendMessage(ChatColor.GREEN + "Your mana has been replenished!");
									hashmap.remove(p);
									cancel();
								}
							}
						}.runTaskTimer(plugin, 0L, 80L);
					}
				} else {
					p.sendMessage(ChatColor.RED + "You are out of mana! Please keep this item in your inventory to regenerate.");
                }
			}
		}
	}

//	@EventHandler
//	public void onProjectileHit(CustomProjectileHitEvent e)
//	{
//		if (e.getProjectile().getProjectileName().equals("SpellBook"))
//		{
//			if (e.getHitType().equals(HitType.ENTITY))
//			{
//				if (e.getHitEntity().equals(e.getProjectile().getShooter()))
//				{
//					e.setCancelled(true);
//					return;
//				}
//				Damageable entity = e.getHitEntity();
//
//				if(e.getHitEntity() instanceof Player)
//				{
//					Player player = (Player)e.getHitEntity();
//
//					if(Core.isPlayerInPVP(player))
//					{
//						if(!IceBlade.froze.contains(player.getName()))
//						{
//							if(!Essentials.getPlugin(Essentials.class).getUser(player).isGodModeEnabled())
//							{
//								entity.setHealth(entity.getHealth() > 2 ? entity.getHealth() - 2 : 0);
//								entity.playEffect(EntityEffect.HURT);
//								entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 1f);
//							}
//						}
//						else
//						{
//							e.getProjectile().getShooter().sendMessage(ChatColor.RED + player.getName() + " is already under a Freeze Spell! You cannot use 2 spells on the same player!");
//						}
//					}
//				}
//				else
//				{
//					entity.setHealth(entity.getHealth() > 2 ? entity.getHealth() - 2 : 0);
//					entity.playEffect(EntityEffect.HURT);
//					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 1f);
//				}
//			}
//		}
//	}
	
	@SuppressWarnings("serial")
	private static List<String> lore() 
	{
		return new ArrayList<String>()
		{{
			this.add(ChatColor.GRAY + "Evil Spells: 5");
			this.add(ChatColor.DARK_GRAY + "A magic spellbook!");
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
