package de.exceptionflug.cmdframework.spigot;

import de.exceptionflug.cmdframework.api.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SpigotCommand extends Command {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

    private final de.exceptionflug.cmdframework.api.Command command;
    private final ICommand commandObject;
    private final String noPermMsg;
    private final String inGameOnlyMsg;
    private final CommandExecutor executor;
    protected SpigotCompleter completer;

    protected SpigotCommand(final String name, final de.exceptionflug.cmdframework.api.Command command, final CommandExecutor executor, final ICommand commandObject, final String noPermMsg, final String inGameOnlyMsg) {
        super(name);
        this.command = command;
        this.executor = executor;
        this.commandObject = commandObject;
        this.noPermMsg = noPermMsg;
        this.inGameOnlyMsg = inGameOnlyMsg;
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String commandLabel, final String[] args) {
        if(!(commandSender instanceof Player) && command.inGameOnly()) {
            commandSender.sendMessage(inGameOnlyMsg);
            return false;
        }
        if(!command.permission().isEmpty()) {
            if(!commandSender.hasPermission(command.permission())) {
                commandSender.sendMessage(command.noPermMessage().isEmpty() ? noPermMsg : command.noPermMessage());
                return false;
            }
        }
        executor.onCommand(commandSender, this, commandLabel, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        List<String> completions = null;
        try {
            if (completer != null) {
                completions = completer.onTabComplete(sender, this, alias, args);
            }
        } catch (final Throwable throwable) {
            sender.sendMessage("§cAn exception occurred while executing this tab completer:");
            sender.sendMessage("§cCompleter: §6" + alias + " (" + commandObject.getClass().getName() + ")");
            sender.sendMessage("§cException: §6" + throwable.getClass().getName());
            sender.sendMessage("§cTimestamp: §6" + DATE_FORMAT.format(new Date()));
            sender.sendMessage("§cPlease report this issue to a staff member.");
            throwable.printStackTrace();
        }

        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }
        return completions;
    }
}
