package fr.akirabane.duelPlugin.listeners;

import fr.akirabane.duelPlugin.Main;
import fr.akirabane.duelPlugin.enumerations.GameState;
import fr.akirabane.duelPlugin.tasks.AutoStart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {
    private Main main;

    public PlayerListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location spawn = new Location(Bukkit.getWorld("world"), 8.5f, 137.5f, -36.5f, 0.0f, 0.0f);
        player.teleport(spawn);

        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);

        if (main.isState(GameState.PLAYING)) {
            event.setJoinMessage(null);
            try {
                player.kickPlayer(main.getConfig().getString("strings.messages.playerListener.statePlayingKickMessage").replace("&", "§"));
            } catch (Exception e) {
                player.kickPlayer("§cThe game is already started.");
            }
        } else if (main.isState(GameState.FINISH)) {
            event.setJoinMessage(null);
            try {
                event.setJoinMessage(main.getConfig().getString("strings.messages.playerListener.joinMessage").replace("&", "§").replace("%player%", player.getName()));
                main.getPlayers().add(player);
            } catch (Exception e) {
                player.kickPlayer("§cThe game is already finished.");
            }
        }

        if(!main.getPlayers().contains(player)) main.getPlayers().add(player);
        player.setGameMode(GameMode.ADVENTURE);
        try {
            event.setJoinMessage(player.getName() + " " + main.getConfig().getString("strings.messages.playerListener.joinMessage").replace("&", "§") + "<" + main.getPlayers().size() + "/" + Bukkit.getMaxPlayers() + ">");
        } catch (Exception e) {
            event.setJoinMessage(player.getName() + " §ahas joined the game. <" + main.getPlayers().size() + "/" + Bukkit.getMaxPlayers() + ">");
        }

        if(main.isState(GameState.WAITING) && main.getPlayers().size() == 2) {
            AutoStart start = new AutoStart(main);
            start.runTaskTimer(main, 0, 20);
            main.setState(GameState.STARTING);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(main.getPlayers().contains(player)) {
            main.getPlayers().remove(player);
        }
        event.setQuitMessage(null);
        main.checkWin();
    }
}
