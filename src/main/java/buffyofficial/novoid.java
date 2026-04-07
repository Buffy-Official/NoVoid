package buffyofficial;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class novoid extends JavaPlugin implements Listener {

    private List<String> enabledWorlds;
    private final String prefix = ChatColor.translateAlternateColorCodes('&', "&5&lNo&2&lVoid &7>> ");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadLocalConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("NoVoid started");
    }

    @Override
    public void onDisable() {
        getLogger().info("NoVoid disabled");
    }

    private void reloadLocalConfig(){
        reloadConfig();
        FileConfiguration config = getConfig();
        enabledWorlds = config.getStringList("enabled-worlds");
    }

    @EventHandler
    public void onPlayerFall(PlayerMoveEvent event){
        Player p = event.getPlayer();
        World w = p.getWorld();

        if (!enabledWorlds.contains(w.getName())){
            return;
        }

        if (p.getLocation().getY() < 0){
            Location spawn = w.getSpawnLocation().add(0.5, 0, 0.5);
            p.teleport(spawn);
            p.setFallDistance(0f);
            p.sendMessage(prefix + ChatColor.GREEN + "Saved from the void!");
        }
    }

    @EventHandler
    public void onVoidDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player p)) return;

        World w = p.getWorld();

        if (!enabledWorlds.contains(w.getName())){
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setCancelled(true);
            Location spawn = w.getSpawnLocation().add(0.5, 0, 0.5);
            p.teleport(spawn);
            p.setFallDistance(0f);
            p.sendMessage(prefix + ChatColor.GREEN + "Saved from the void!");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args){
        if (command.getName().equalsIgnoreCase("novoid")){
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
                reloadConfig();
                sender.sendMessage(prefix + ChatColor.YELLOW + "Configuration reloaded!");
            }else{
                sender.sendMessage(prefix + ChatColor.YELLOW + "Usage: /novoid reload");
            }
            return true;
        }
        return false;
    }
}
