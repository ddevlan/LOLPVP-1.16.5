package com.lolpvp.utils;
import net.minecraft.server.v1_16_R3.*;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
 
public class CustomEntityFirework extends EntityFireworks {
    Player[] players;
 
    public CustomEntityFirework(World world, Player... p) {
        super(EntityTypes.FIREWORK_ROCKET, world);
        players = p;
        this.a(0.25F, 0.25F);
    }
 
    boolean gone = false;

    @Override
    protected void x() {
        super.x();
    }



    public boolean h() {
        if (gone) {
            return false;
        }
 
        if (!this.world.captureBlockStates) {
            gone = true;
 
            if (players != null) {
                if (players.length > 0) {
                    for (Player player : players) {
                        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 17));
                    }
 
                    this.die();
                    return true;
                }
            }
 
            world.broadcastEntityEffect(this, (byte) 17);
            this.die();
        }
        return true;
    }
 
    public static void spawn(Location location, FireworkEffect effect, Player... players) {
        try {
            CustomEntityFirework firework = new CustomEntityFirework(((CraftWorld) location.getWorld()).getHandle(), players);
            FireworkMeta meta = ((Firework) firework.getBukkitEntity()).getFireworkMeta();
            meta.addEffect(effect);
            ((Firework) firework.getBukkitEntity()).setFireworkMeta(meta);
            firework.setPosition(location.getX(), location.getY(), location.getZ());
 
            if ((((CraftWorld) location.getWorld()).getHandle()).addEntity(firework)) {
                firework.setInvisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}