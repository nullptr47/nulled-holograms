package com.github.nullptr47.holograms.impl;

import com.github.nullptr47.holograms.Hologram;
import lombok.val;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author nullptr47
 * @version 1.0 | 1.8_R3
 * @see Hologram
 */
public class CraftHologram18 extends Hologram {

    private final World world;
    private int[] id;

    /**
     * constructor method
     * @param x location x
     * @param y location y
     * @param z location z
     * @param pitch location pitch
     * @param yaw location yaw
     * @param world location world
     * @param lines text to be displayed
     */
    public CraftHologram18(double x, double y, double z, float pitch, float yaw, World world, List<String> lines) {

        super(x, y, z, pitch, yaw, lines);

        this.world = world;
        this.id = new int[lines.size()];

    }

    /**
     * Display the hologram for all players. (server-side holograms)
     */
    public void display() {

        double additional = 0;
        int address = 0;

        for (int index = getLines().size() - 1; index >= 0; index--) {

            String text = getLines().get(index);

            if (text.isEmpty()) { additional += 0.25; continue; }

            ArmorStand armorStand = (ArmorStand) world.spawnEntity(new Location(world, x, (y + additional), z), EntityType.ARMOR_STAND);

            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(text);
            armorStand.setSmall(true);

            id[address++] = armorStand.getEntityId();

            additional += 0.25;

        }

    }

    /**
     * Display the hologram for certain players only. (client-sided holograms)
     * @param players who will see the hologram
     */
    public void displayTo(Player... players) {

        double additional = 0;
        int address = 0;

        for (String text : getLines()) {

            int entityId = (int) (Math.random() * Integer.MAX_VALUE);
            CraftArmorStand armorStand = new CraftArmorStand((CraftServer) Bukkit.getServer(), new EntityArmorStand(((CraftWorld) world).getHandle()));
            PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity();

            armorStand.setCustomName(text);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);

            try {
                FieldUtils.writeDeclaredField(spawnPacket, "a", entityId, true);
                FieldUtils.writeDeclaredField(spawnPacket, "j", 78, true);
            }
            catch (Exception exception) { exception.printStackTrace(); }

            spawnPacket.a(MathHelper.floor(x * 32.0D));
            spawnPacket.b(MathHelper.floor((y - additional) * 32.0D));
            spawnPacket.c(MathHelper.floor(z * 32.0D));
            spawnPacket.d(MathHelper.floor(pitch * 256.0F / 360.0F));
            spawnPacket.e(MathHelper.floor(yaw * 256.0F / 360.0F));

            PacketPlayOutEntityMetadata dataPacket = new PacketPlayOutEntityMetadata(entityId, armorStand.getHandle().getDataWatcher(), false);

            for (Player player : players) {

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnPacket);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(dataPacket);

            }

            additional += 0.25;
            id[address++] = entityId;

        }

    }

    public void changeDisplayTo(Player[] players, String... lines) {

        changeDisplayTo(players, Arrays.asList(lines));

    }

    /**
     * changes the text for selected players.
     * @implNote: text must have the same lines amount.
     * @param players who will receive update
     */
    public void changeDisplayTo(Player[] players, List<String> lines) {

        for (int index = 0; index < id.length; index++) {

            int entityId = id[index];
            String text = lines.get(index);
            CraftArmorStand armorStand = new CraftArmorStand((CraftServer) Bukkit.getServer(), new EntityArmorStand(((CraftWorld) world).getHandle()));

            armorStand.setCustomName(text);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);

            PacketPlayOutEntityMetadata dataPacket = new PacketPlayOutEntityMetadata(entityId, armorStand.getHandle().getDataWatcher(), false);

            for (Player player : players)
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(dataPacket);

        }

    }

    public void changeLine(int line, String display) {

        for (Entity entity : world.getEntities())
            if (entity.getEntityId() == id[line]) entity.setCustomName(display);

    }

    public void setLines(List<String> lines) {

        id = new int[lines.size()];
        super.lines = lines;

    }

    /**
     * removes the hologram (Only works for server-side holograms)
     */
    public void remove() {

        for (Entity entity : world.getEntitiesByClasses(CraftArmorStand.class))
            for (Integer entityId : id) if (entity.getEntityId() == entityId) entity.remove();

    }

    /**
     * removes the hologram (Only works for server-side holograms)
     */
    public void removeFor(Player... players) {

        for (Player player : players) {

            PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(id);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyPacket);

        }

    }

}
