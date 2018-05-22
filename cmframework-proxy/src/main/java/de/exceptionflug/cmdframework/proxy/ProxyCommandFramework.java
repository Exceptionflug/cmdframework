package de.exceptionflug.cmdframework.proxy;

import de.exceptionflug.cmdframework.api.Command;
import de.exceptionflug.cmdframework.api.Completer;
import de.exceptionflug.cmdframework.api.ICommand;
import de.exceptionflug.cmdframework.api.ICommandFramework;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public final class ProxyCommandFramework implements ICommandFramework {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

    private final Plugin plugin;
    private final Map<String, Entry<Method, Object>> stringEntryMap = new HashMap<>();
    private String noPermissionMessage = "Unknown command. Type \"/help\" for help.";
    private String inGameOnlyMessage = "This command can only be executed ingame.";

    public ProxyCommandFramework(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String[] getCommandLabels() {
        return stringEntryMap.keySet().toArray(new String[0]);
    }

    @Override
    public void registerCommand(final Command command, final Method method, final ICommand commandObject) {
        final List<String> labels = new ArrayList<>();
        labels.add(command.name());
        labels.addAll(Arrays.asList(command.aliases()));
        for(final String label : labels) {
            if(stringEntryMap.containsKey(label))
                continue;
            stringEntryMap.put(label, new SimpleEntry<>(method, commandObject));
            final String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
            ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new ProxyCommand(cmdLabel, this, command, noPermissionMessage, inGameOnlyMessage));
        }
    }

    @Override
    public void registerCompleter(final Completer completer, final Method method, final ICommand commandObject) {
        final List<String> labels = new ArrayList<>();
        labels.add(completer.name());
        labels.addAll(Arrays.asList(completer.aliases()));
        for(final String label : labels) {
            new ProxyCompleter(method, label, commandObject, plugin);
        }
    }

    @Override
    public void setInGameOnlyMessage(final String msg) {
        inGameOnlyMessage = msg;
    }

    @Override
    public void setNoPermissionMessage(final String msg) {
        noPermissionMessage = msg;
    }

    public void execute(final CommandSender commandSender, final String label, final String[] args) {
        for (int i = args.length; i >= 0; i--) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            final String cmdLabel = buffer.toString();
            if (stringEntryMap.containsKey(cmdLabel)) {
                final Method method = stringEntryMap.get(cmdLabel).getKey();
                final Object methodObject = stringEntryMap.get(cmdLabel).getValue();
                try {
                    method.invoke(methodObject, new ProxyCommandContext(commandSender, args, label, cmdLabel.split("\\.").length - 1));
                } catch (final Exception e) {
                    commandSender.sendMessage("§cAn exception occurred while executing this command:");
                    commandSender.sendMessage("§cCommand: §6"+cmdLabel+" ("+methodObject.getClass().getName()+")");
                    commandSender.sendMessage("§cException: §6"+e.getClass().getName());
                    commandSender.sendMessage("§cTimestamp: §6"+DATE_FORMAT.format(new Date()));
                    commandSender.sendMessage("§cPlease report this issue to a staff member.");
                    e.printStackTrace();
                }
                return;
            }
        }
        commandSender.sendMessage("§cThis command is not handled. What a shame...");
    }
}
