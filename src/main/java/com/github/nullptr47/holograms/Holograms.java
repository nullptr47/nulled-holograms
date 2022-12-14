package com.github.nullptr47.holograms;

import com.github.nullptr47.holograms.impl.CraftHologram18;
import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public class Holograms {

    public static Hologram newHologram(Location location, List<String> text) {

        return new CraftHologram18(location.getX(), location.getY(), location.getZ(),
                location.getPitch(), location.getYaw(), location.getWorld(), text);

    }

    public static Hologram newHologram(Location location, String... text) {

        return new CraftHologram18(location.getX(), location.getY(), location.getZ(),
                location.getPitch(), location.getYaw(), location.getWorld(), Arrays.asList(text));

    }

}
