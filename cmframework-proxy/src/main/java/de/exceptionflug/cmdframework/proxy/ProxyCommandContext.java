package de.exceptionflug.cmdframework.proxy;

import de.exceptionflug.cmdframework.api.ICommandContext;
import net.md_5.bungee.api.CommandSender;

public class ProxyCommandContext implements ICommandContext {

    private final CommandSender sender;
    private final String[] args;
    private final String commandLabel;

    public ProxyCommandContext(final CommandSender sender, final String[] args, final String commandLabel, final int subCommand) {
        final String[] modArgs = new String[args.length - subCommand];
        for (int i = 0; i < args.length - subCommand; i++) {
            modArgs[i] = args[i + subCommand];
        }
        final StringBuilder buffer = new StringBuilder();
        buffer.append(commandLabel);
        for (int x = 0; x < subCommand; x++) {
            buffer.append(".").append(args[x]);
        }
        final String cmdLabel = buffer.toString();
        this.sender = sender;
        this.args = modArgs;
        this.commandLabel = cmdLabel;
    }

    @Override
    public <T> T getSender(final Class<T> type) {
        if(type.isAssignableFrom(sender.getClass())) {
            return (T) sender;
        }
        return null;
    }

    @Override
    public String[] getArguments() {
        return args;
    }

    @Override
    public String getCommandLabel() {
        return commandLabel;
    }
}
