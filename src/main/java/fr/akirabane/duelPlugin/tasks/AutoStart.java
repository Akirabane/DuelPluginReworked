package fr.akirabane.duelPlugin.tasks;

import fr.akirabane.duelPlugin.Main;
import fr.akirabane.duelPlugin.enumerations.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoStart extends BukkitRunnable {

    private Main main;
    private int timer = 10;

    public AutoStart(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Player player : main.getPlayers()) {
            player.setLevel(timer);
        }

        if (timer == 10) {
            try {
                Bukkit.broadcastMessage(main.getConfig().getString("strings.messages.autoStart.timerMessage").replace("&", "§") + timer + "s");
            } catch (Exception e) {
                Bukkit.broadcastMessage("§cAn error has occured while trying to send the timer message. Please check your config.yml file.");
            }
        } else if (timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
            try {
                Bukkit.broadcastMessage(main.getConfig().getString("strings.messages.autoStart.timerMessage").replace("&", "§") + timer + "s");
            } catch (Exception e) {
                Bukkit.broadcastMessage("§cAn error has occured while trying to send the timer message. Please check your config.yml file.");
            }
        }
        if (timer == 0) {
            try {
                Bukkit.broadcastMessage(main.getConfig().getString("strings.messages.autoStart.timerMessage").replace("&", "§") + "GO !");
            } catch (Exception e) {
                Bukkit.broadcastMessage("§cAn error has occured while trying to send the timer message. Please check your config.yml file.");
            }
            main.setState(GameState.PLAYING);

            for(int i = 0; i < main.getPlayers().size(); i++) {
                Player player = main.getPlayers().get(i);
                Location spawn = main.getSpawns().get(i);

                player.teleport(spawn);
                player.getInventory().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
                player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                player.getInventory().setItem(1, new ItemStack(Material.BOW));
                player.getInventory().setItem(2, new ItemStack(Material.ARROW, 64));
                player.getInventory().setItem(3, new ItemStack(Material.SPLASH_POTION));
                player.getInventory().setItem(4, new ItemStack(Material.SPLASH_POTION));
                player.getInventory().setItem(5, new ItemStack(Material.SPLASH_POTION));
                player.getInventory().setItem(6, new ItemStack(Material.SPLASH_POTION));
                player.getInventory().setItem(7, new ItemStack(Material.SPLASH_POTION));
                player.getInventory().setItem(8, new ItemStack(Material.SPLASH_POTION));
                player.updateInventory();
            }

            GameCycle cycle = new GameCycle(main);
            cycle.runTaskTimer(main, 0, 20);

            cancel();
        }
        timer--;
    }
}
