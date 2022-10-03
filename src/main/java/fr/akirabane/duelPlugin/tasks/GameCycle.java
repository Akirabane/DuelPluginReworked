package fr.akirabane.duelPlugin.tasks;

import fr.akirabane.duelPlugin.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCycle extends BukkitRunnable {

    private Main main;
    private int timer = 300;

    public GameCycle(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        if(timer == 0) {
            cancel();
        }
        timer--;
    }
}
