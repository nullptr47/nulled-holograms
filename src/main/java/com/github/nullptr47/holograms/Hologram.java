package com.github.nullptr47.holograms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
public abstract class Hologram {

    protected double x, y, z;
    protected float yaw, pitch;
    @Getter @Setter private List<String> lines;

    public abstract void display();
    public abstract void displayTo(Player... players);
    public abstract void changeDisplayTo(Player[] players, String... lines);
    public abstract void changeLine(int line, String display);
    public abstract void remove();

}
