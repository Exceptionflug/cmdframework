package de.exceptionflug.cmdframework.proxy;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ProxyCommand extends Command {

    private final de.exceptionflug.cmdframework.api.Command command;
    private final String noPermMsg;
    private final String inGameOnlyMsg;
    private final ProxyCommandFramework cmd;

    public ProxyCommand(final String name, final ProxyCommandFramework cmd, final de.exceptionflug.cmdframework.api.Command command, final String noPerm, final String inGameOnlyMsg) {
        super(name);
        noPermMsg = noPerm;
        this.cmd = cmd;
        this.command = command;
        this.inGameOnlyMsg = inGameOnlyMsg;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        if(!(commandSender instanceof ProxiedPlayer) && command.inGameOnly()) {
            commandSender.sendMessage(inGameOnlyMsg);
            return;
        }
        if(!command.permission().isEmpty()) {
            if(!commandSender.hasPermission(command.permission())) {
                commandSender.sendMessage(command.noPermMessage().isEmpty() ? noPermMsg : command.noPermMessage());
                return;
            }
        }
        cmd.execute(commandSender, getName(), strings);
    }

}

