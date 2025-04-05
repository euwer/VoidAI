package com.voidofaesthetic.voidhelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Voidhelper extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the command
        saveDefaultConfig();
        if (getCommand("soru") != null) {
            String apiKey = getConfig().getString("api-key");
            String model = getConfig().getString("model");
            getCommand("soru").setExecutor(new HelpCommandExecutor(apiKey, model));
        } else {
            getLogger().severe("Command 'soru' not found in plugin.yml");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public class SoruCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // Command logic here
            return true;
        }
    }
}