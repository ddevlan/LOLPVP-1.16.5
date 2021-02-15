package com.lolpvp.weapons.classes;

import com.lolpvp.weapons.BallerItem;
import com.lolpvp.weapons.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EMP extends BallerItem
{

	public EMP()
	{
		super("EMP", Material.MILK_BUCKET, 1, true, lore(), enchantments(), "emp", "milk");
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{	
		Player player = event.getPlayer();
		if(ItemManager.getInstance().isBallerItem(player.getItemInHand(), this))
		{
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) 
					|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				event.setCancelled(true);
				if (!player.getGameMode().equals(GameMode.CREATIVE)) 
				{
					player.setItemInHand(null);
				}
				
//				CustomProjectile projectile = new ItemProjectile("EMP", player, ItemManager.getInstance().addGlow(ItemManager.getInstance().getItemStack(this)), 0.4F);
//
//				projectile.addTypedRunnable((TypedRunnable<ItemProjectile>) o -> {
//					ParticleEffect.FIREWORKS_SPARK.display(o.getEntity().getLocation());
//					o.getEntity().getWorld().playSound(o.getEntity().getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0F, 200.0F);
//				});
			}
		}	
	}
	
//	@EventHandler
//	public void onHit(CustomProjectileHitEvent event)
//	{
//		Player player = (Player) event.getProjectile().getShooter();
//		if(event.getProjectile().getProjectileName().equalsIgnoreCase("EMP"))
//		{
//			if(((event.getHitType().equals(CustomProjectileHitEvent.HitType.BLOCK) || event.getHitType().equals(CustomProjectileHitEvent.HitType.ENTITY))))
//			{
//				if(event.getProjectile().getShooter() != event.getHitEntity())
//				{
//					ParticleEffect.SPELL.display(event.getProjectile().getEntity().getLocation());
//					event.getProjectile().getEntity().getWorld().playSound(event.getProjectile().getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 10.0F);
//					List<Entity> victims = event.getProjectile().getEntity().getNearbyEntities(2.75D, 2.75D, 2.75D);
//					for (Entity victim : victims)
//					{
//						if ((victim instanceof LivingEntity) && !victim.equals(player))
//						{
//							for (PotionEffect potions : ((LivingEntity)victim).getActivePotionEffects())
//							{
//								((LivingEntity)victim).removePotionEffect(potions.getType());
//							}
//						}
//					}
//				}
//				else
//				{
//					if (!player.getGameMode().equals(GameMode.CREATIVE))
//					{
//						player.getInventory().addItem(ItemManager.getInstance().getItemStack(this));
//					}
//				}
//			}
//		}
//	}
	
	private static List<String> lore()
	{
		return new ArrayList<String>()
		{{
			this.add(ChatColor.ITALIC + "" + ChatColor.DARK_PURPLE + "A throwable bomb that disables your enemies' potion effects!");
			this.add(ChatColor.DARK_GRAY + "Right click to launch.");
		}};
	}
	
	private static HashMap<Enchantment, Integer> enchantments()
	{
		return new HashMap<Enchantment, Integer>()
		{{
		}};
	}
}
