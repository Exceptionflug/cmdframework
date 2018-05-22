package de.exceptionflug.cmdframework.proxy;

import de.exceptionflug.cmdframework.api.ICommand;
import de.exceptionflug.cmdframework.api.ICommandContext;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public final class ProxyCompleter implements Listener {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

    private final Method method;
    private final String label;
    private final ICommand commandObject;

    public ProxyCompleter(final Method method, final String label, final ICommand commandObject, final Plugin plugin) {
        this.method = method;
        this.label = label;
        this.commandObject = commandObject;
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onTab(final TabCompleteEvent e) {
        try {
            final String[] cursorSplit = e.getCursor().substring(1).split(" ");
            if(!label.toLowerCase().startsWith(cursorSplit[0].toLowerCase()))
                return;
            if(!label.equalsIgnoreCase(cursorSplit[0])) {
                e.getSuggestions().add("/"+label);
                return;
            }
            final String[] finalArgs = new String[cursorSplit.length-1];
            for (int i = 1; i < cursorSplit.length; i++) {
                finalArgs[i-1] = cursorSplit[i];
            }
            final ICommandContext commandContext = new ProxyCommandContext((CommandSender) e.getSender(), finalArgs, label, label.split("\\.").length - 1);
            e.getSuggestions().addAll((Collection<? extends String>) method.invoke(commandObject, commandContext));
        } catch (final Throwable throwable) {
            ((CommandSender)e.getSender()).sendMessage("§cAn exception occurred while executing this tab completer:");
            ((CommandSender)e.getSender()).sendMessage("§cCompleter: §6"+ label +" ("+commandObject.getClass().getName()+")");
            ((CommandSender)e.getSender()).sendMessage("§cException: §6"+throwable.getClass().getName());
            ((CommandSender)e.getSender()).sendMessage("§cTimestamp: §6"+DATE_FORMAT.format(new Date()));
            ((CommandSender)e.getSender()).sendMessage("§cPlease report this issue to a staff member.");
            throwable.printStackTrace();
        }

    }

}
