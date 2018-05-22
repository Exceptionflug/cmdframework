package de.exceptionflug.cmdframework.example.proxy;

import de.exceptionflug.cmdframework.api.Command;
import de.exceptionflug.cmdframework.api.Completer;
import de.exceptionflug.cmdframework.api.ICommand;
import de.exceptionflug.cmdframework.api.ICommandContext;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class ExampleCommand implements ICommand {

    private final ProxyExamplePlugin plugin;

    public ExampleCommand(final ProxyExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(name = "example", inGameOnly = true)
    public void example(final ICommandContext commandContext) {
        final ProxiedPlayer p = commandContext.getSender(ProxiedPlayer.class);
        p.sendMessage("§6Exceptionflug CommandFramework 2018");
        p.sendMessage("§6Mache mal /example test [Args] :)");
    }

    @Command(name = "example.test", inGameOnly = true)
    public void exampleTest(final ICommandContext commandContext) {
        final ProxiedPlayer p = commandContext.getSender(ProxiedPlayer.class);
        if(commandContext.getArgumentCount() == 0) {
            p.sendMessage("§cDu hast leider keine Argumente für mich :(");
        } else {
            p.sendMessage("Du hast angegeben: §6"+commandContext.getJoinedString(0));
        }
    }

    @Completer(name = "example")
    public List<String> complete(final ICommandContext commandContext) {
        final List<String> ret = new ArrayList<>();
        final StringBuilder label = new StringBuilder(commandContext.getCommandLabel());
        for (final String arg : commandContext.getArguments()) {
            label.append(" ").append(arg);
        }
        for (final String currentLabel : plugin.getCommandFramework().getCommandLabels()) {
            String current = currentLabel.replace('.', ' ');
            if (current.contains(label.toString())) {
                current = current.substring(current.indexOf(' ') != -1 ? current.indexOf(' ') : current.length()).trim();
                current = current.substring(0, current.indexOf(' ') != -1 ? current.indexOf(' ') : current.length()).trim();
                if (!ret.contains(current)) {
                    ret.add(current);
                }
            }
        }
        return ret;
    }

}
