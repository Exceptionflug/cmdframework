package de.exceptionflug.cmdframework.example.spigot;

import de.exceptionflug.cmdframework.api.ICommandFramework;
import de.exceptionflug.cmdframework.spigot.SpigotCommandFramework;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotExamplePlugin extends JavaPlugin {

    private ICommandFramework commandFramework;

    @Override
    public void onEnable() {
        commandFramework = new SpigotCommandFramework(this);
        commandFramework.registerCommands(new ExampleCommand(this));
    }

    public ICommandFramework getCommandFramework() {
        return commandFramework;
    }
}
