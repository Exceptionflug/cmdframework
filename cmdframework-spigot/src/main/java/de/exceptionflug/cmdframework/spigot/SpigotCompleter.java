package de.exceptionflug.cmdframework.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpigotCompleter implements TabCompleter {

    private final Map<String, Entry<Method, Object>> completers = new HashMap<>();

    public void addCompleter(final String label, final Method m, final Object obj) {
        completers.put(label, new AbstractMap.SimpleEntry<>(m, obj));
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        for (int i = args.length; i >= 0; i--) {
            final StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                if (!args[x].equals("") && !args[x].equals(" ")) {
                    buffer.append("." + args[x].toLowerCase());
                }
            }
            String cmdLabel = buffer.toString();
            if (completers.containsKey(cmdLabel)) {
                final Entry<Method, Object> entry = completers.get(cmdLabel);
                try {
                    final List<String> labelParts = (List<String>) entry.getKey().invoke(entry.getValue(), new SpigotCommandContext(sender, args, label, cmdLabel.split("\\.").length-1));
                    if (labelParts == null || labelParts.size() == 0) {
                        return null;
                    }
                    return labelParts;
                } catch (final IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
