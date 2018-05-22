package de.exceptionflug.cmdframework.example.proxy;

import de.exceptionflug.cmdframework.api.ICommandFramework;
import de.exceptionflug.cmdframework.proxy.ProxyCommandFramework;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyExamplePlugin extends Plugin {

    private ICommandFramework commandFramework;

    @Override
    public void onEnable() {
        commandFramework = new ProxyCommandFramework(this);
        commandFramework.registerCommands(new ExampleCommand(this));
    }

    public ICommandFramework getCommandFramework() {
        return commandFramework;
    }
}
