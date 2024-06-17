package com.twarpit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TWarpit extends JavaPlugin implements CommandExecutor, TabCompleter {

    private File warpitFile;
    private FileConfiguration warpitConfig;

    @Override
    public void onEnable() {
        getCommand("warp").setExecutor(this);
        getCommand("warp").setTabCompleter(this);
        getCommand("luowarp").setExecutor(this);
        getCommand("poistawarp").setExecutor(this);

        warpitFile = new File(getDataFolder(), "warpit.yml");
        if (!warpitFile.exists()) {
            warpitFile.getParentFile().mkdirs();
            try {
                warpitFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        warpitConfig = YamlConfiguration.loadConfiguration(warpitFile);

        getLogger().info("TWarpit plugini on käynnistynyt onnistuneesti!");
        getLogger().info("Pluginin on tehnyt Thearex12");
        getLogger().info("Contact: contact@thearex12.com");
        getLogger().info("Source: https://github.com/Thearex/TWarpit");
        getLogger().info("Lisenssi: MIT");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("warp")) {
                if (args.length == 1) {
                    String warpName = args[0];
                    if (warpitConfig.contains(warpName)) {
                        Location loc = warpitConfig.getLocation(warpName);
                        player.teleport(loc);
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Warpattu sijaintiin: " + warpName);
                    } else {
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Warpia ei löytynyt: " + warpName);
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Käyttö: /warp <nimi>");
                }
            } else if (cmd.getName().equalsIgnoreCase("luowarp")) {
                if (args.length == 1) {
                    String warpName = args[0];
                    Location loc = player.getLocation();
                    warpitConfig.set(warpName, loc);
                    saveWarpitConfig();
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Warp luotu: " + warpName);
                } else {
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Käyttö: /luowarp <nimi>");
                }
            } else if (cmd.getName().equalsIgnoreCase("poistawarp")) {
                if (args.length == 1) {
                    String warpName = args[0];
                    if (warpitConfig.contains(warpName)) {
                        warpitConfig.set(warpName, null);
                        saveWarpitConfig();
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Warp poistettu: " + warpName);
                    } else {
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Warpia ei löytynyt: " + warpName);
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TWarpit " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Käyttö: /poistawarp <nimi>");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (args.length == 1) {
                Set<String> warpNames = warpitConfig.getKeys(false);
                List<String> suggestions = new ArrayList<>();
                String currentArg = args[0].toLowerCase();
                for (String warp : warpNames) {
                    if (warp.toLowerCase().startsWith(currentArg)) {
                        suggestions.add(warp);
                    }
                }
                return suggestions;
            }
        }
        return null;
    }

    private void saveWarpitConfig() {
        try {
            warpitConfig.save(warpitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
