package com.lolpvp.weapons;

import com.lolpvp.weapons.classes.IronManSuit;
import com.lolpvp.weapons.classes.SnowArmor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

import java.util.ArrayList;
import java.util.List;

public class BallerItemTask extends BukkitRunnable
{
	private Player player;
	private final BallerItem item;
	private List<Player> players;
	public BallerItemTask(Player player, BallerItem item)
	{
		this.player = player;
		this.item = item;
	}
	
	public BallerItemTask(ArrayList<Player> players, BallerItem item)
	{
		this.players = players;
		this.item = item;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub 
		if(item.equals(ItemManager.getInstance().getItemByName("rabbitsfoot")))
		{
			if(player.getEquipment().getBoots() != null && ItemManager.getInstance().isBallerItem(player.getEquipment().getBoots(), ItemManager.getInstance().getItemByName("rabbitsfoot")))
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2));
			}
			else
			{
				this.cancel();
			}
		}
		else if(item.equals(ItemManager.getInstance().getItemByName("snowarmor")))
		{
			SnowArmor snowarmor = (SnowArmor)item;
			if(snowarmor.isSnowArmor(player.getEquipment().getArmorContents()))
			{
				ParticleEffect.SNOW_SHOVEL.display(player.getLocation().add(new Vector(0, 1, 0)), 0.0f, 1.0f, 0.0f, 1, 1, null, Bukkit.getOnlinePlayers());
			}
			else
			{
				this.cancel();
			}
		}
		else if(item.equals(ItemManager.getInstance().getItemByName("ironmansuit")))
		{
			IronManSuit ironManSuit = (IronManSuit)item;
			if(ironManSuit.isWearingSuit(player.getEquipment().getArmorContents()))
			{
				ParticleEffect.SNOW_SHOVEL.display(player.getLocation().add(new Vector(0, 1, 0)), 0.0f, 1.0f, 0.0f, 1, 1, null, Bukkit.getOnlinePlayers());

				ParticleData data = new ParticleData() {
					@Override
					public Object toNMSData() {
						return Material.LAVA.createBlockData();
					}
				};
				ParticleEffect.BLOCK_CRACK.display(player.getLocation(), 0.0f, 0.0f, 0.0f, 1, 10, data, Bukkit.getOnlinePlayers());
			}
			else
			{
				this.cancel();
			}
		}
		else if(item.equals(ItemManager.getInstance().getItemByName("bunnyears")))
		{
			if(player.getEquipment().getHelmet() != null && ItemManager.getInstance().isBallerItem(player.getEquipment().getHelmet(), ItemManager.getInstance().getItemByName("bunnyears")))
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2));
			}
			else
			{
				this.cancel();
			}
		}
		else if(item.equals(ItemManager.getInstance().getItemByName("snowman")))
		{
			for(Player p : players)
			{
				for(int i = 0; i < 20; i++)
				{
					p.getWorld().playEffect(p.getLocation().add(0.0, 1.0, 0.0), Effect.STEP_SOUND, 80);
					p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 80);
				}
			}
		}
	}

}
