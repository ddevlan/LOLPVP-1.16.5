package com.lolpvp.weapons.classes;

import com.lolpvp.weapons.BallerItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WitchsMagic extends BallerItem
{

	public WitchsMagic() 
	{
		super(ChatColor.RESET + "" + ChatColor.AQUA + "Witch's Magic", Material.POTION, 16388, 100, lore(), enchantments(), "witchsmagic", "witchmagic", "wm");
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("serial")
	private static List<String> lore()
	{
		return new ArrayList<String>()
		{{
			this.add(ChatColor.RED + "Blindness (0:10)");
			this.add(ChatColor.RED + "Slowness (0:10)");
			this.add(ChatColor.RED + "Confusion (0:10)");
		}};
	}
	
	@SuppressWarnings("serial")
	private static HashMap<Enchantment, Integer> enchantments()
	{
		return new HashMap<Enchantment, Integer>()
		{{
		}};
	}
	
//	@EventHandler
//	public void onInteract(PlayerInteractEvent event)
//	{
//		if (ItemManager.getInstance().isBallerItem(event.getPlayer().getItemInHand(), this) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
//			event.setCancelled(true);
//			if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
//				event.getPlayer().getInventory().remove(ItemManager.getInstance().getItemStackWithMeta(this, 1));
//			}
//			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 1.0F);
//			@SuppressWarnings("unused")
//			CustomProjectile projectile = new ItemProjectile("witchsmagic",
//					event.getPlayer(), ItemManager.getInstance().getItemStackWithMeta(this, 1), 0.4F);
//		}
//	}
	
//	@EventHandler
//	public void onHit(CustomProjectileHitEvent event)
//	{
//		if (((event.getHitType().equals(CustomProjectileHitEvent.HitType.BLOCK))
//				|| (event.getHitType().equals(CustomProjectileHitEvent.HitType.ENTITY)))
//				&& (event.getProjectile().getProjectileName().equalsIgnoreCase("witchsmagic")))
//		{
//			event.getProjectile().getEntity().getWorld().playEffect(event.getProjectile().getEntity().getLocation(), Effect.POTION_BREAK, 4);
//			List<Entity> victims = event.getProjectile().getEntity().getNearbyEntities(1.5, 1.5, 1.5);
//			for (Entity victim : victims) {
//				if ((victim instanceof LivingEntity))
//				{
//					((LivingEntity) victim).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 0));
//					((LivingEntity) victim).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0));
//					((LivingEntity) victim).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0));
//					((LivingEntity) victim).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 33 * 20, 0));
//				}
//			}
//		}
//	}
}
