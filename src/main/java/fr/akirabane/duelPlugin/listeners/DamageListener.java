package fr.akirabane.duelPlugin.listeners;

import fr.akirabane.duelPlugin.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private Main main;

    public DamageListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity victim = event.getEntity();
        if(victim instanceof Player) {
            Player player = (Player) victim;
            if(player.getHealth() <= event.getDamage()) {
                event.setDamage(0);
                main.eliminate(player);
            }
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {

        Entity victim = event.getEntity();
        if(victim instanceof Player) {
            Player player = (Player) victim;
            Entity damager = event.getDamager();
            Player killer = player;

            if(player.getHealth() <= event.getDamage()) {
                if(damager instanceof Player) killer = (Player)damager;
                if(damager instanceof Arrow) {
                    Arrow arrow = (Arrow) damager;
                    if(arrow.getShooter() instanceof Player) killer = (Player) arrow.getShooter();
                }

                try {
                    killer.sendMessage(main.getConfig().getString("strings.messages.damageListeners.killedMessage").replace("&", "§") + victim.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.setDamage(0);
                main.eliminate(player);
            }
        }
    }
}
