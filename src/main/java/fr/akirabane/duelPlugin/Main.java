package fr.akirabane.duelPlugin;

import fr.akirabane.duelPlugin.enumerations.GameState;
import fr.akirabane.duelPlugin.listeners.DamageListener;
import fr.akirabane.duelPlugin.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private List<Player> players = new ArrayList<>();
    private List<Location> spawn = new ArrayList<>();
    private GameState currentState;

    @SuppressWarnings("static-access")
    @Override
    public void onEnable() {
        getLogger().info("DuelPlugin has been enabled");

        saveDefaultConfig();
        setState(currentState.WAITING);

        World world = Bukkit.getWorld("world");
        spawn.add(new Location(world, -12.5f, 137.5f, -30.5f, -180.0f, 0.0f));
        spawn.add(new Location(world, -16.5f, 137.5f, -38.5f, 0.0f, 0.0f));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(this), this);
        pm.registerEvents(new DamageListener(this), this);
    }

    public void setState(GameState currentState) {
        this.currentState = currentState;
    }

    public boolean isState(GameState currentState) {
        return this.currentState == currentState;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Location> getSpawns() {
        return spawn;
    }

    public void eliminate(Player player) {
        if(players.contains(player)) players.remove(player);
        try {
            player.kickPlayer(getConfig().getString("strings.messages.main.eliminated").replace("&", "§"));
        } catch (Exception e) {
            player.kickPlayer("§cYou have been eliminated.");
        }
        checkWin();
    }

    public void checkWin() {
        if (players.size() == 1) {
            Player winner = players.get(0);
            try {
                Bukkit.broadcastMessage(winner.getName() + getConfig().getString("strings.messages.main.whoWon").replace("&", "§"));
            } catch (Exception e) {
                Bukkit.broadcastMessage(winner.getName() + " has won the game.");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                winner.kickPlayer(getConfig().getString("strings.messages.main.win").replace("&", "§"));
            } catch (Exception e) {
                winner.kickPlayer("§cYou have won the game.");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bukkit.shutdown();
        }
    }

}
