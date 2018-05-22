package de.exceptionflug.cmdframework.api;

import java.lang.reflect.Method;
import java.util.List;

public interface ICommandFramework {

    default void registerCommands(final ICommand command) {
        final Method[] methods = command.getClass().getMethods();
        for(final Method method : methods) {
            if(method.isAnnotationPresent(Command.class)) {
                final Class[] params = method.getParameterTypes();
                if(params.length != 1) {
                    throw new IllegalStateException("The method "+method.getName()+" in "+command.getClass().getName()+" cannot be registered: Invalid parameter count.");
                } else {
                    if(!params[0].equals(ICommandContext.class)) {
                        throw new IllegalStateException("The method "+method.getName()+" in "+command.getClass().getName()+" cannot be registered: Invalid parameter type.");
                    }
                    registerCommand(method.getDeclaredAnnotation(Command.class), method, command);
                }
            } else if(method.isAnnotationPresent(Completer.class)) {
                final Class[] params = method.getParameterTypes();
                if(params.length != 1) {
                    throw new IllegalStateException("The method "+method.getName()+" in "+command.getClass().getName()+" cannot be registered: Invalid parameter count.");
                } else {
                    if(!params[0].equals(ICommandContext.class)) {
                        throw new IllegalStateException("The method "+method.getName()+" in "+command.getClass().getName()+" cannot be registered: Invalid parameter type.");
                    }
                    if(method.getReturnType() != List.class) {
                        throw new IllegalStateException("The method "+method.getName()+" in "+command.getClass().getName()+" cannot be registered: Invalid return type.");
                    }
                    registerCompleter(method.getDeclaredAnnotation(Completer.class), method, command);
                }
            }
        }
    }

    String[] getCommandLabels();
    void registerCommand(Command command, Method method, ICommand commandObject);
    void registerCompleter(Completer completer, Method method, ICommand commandObject);
    void setInGameOnlyMessage(String msg);
    void setNoPermissionMessage(String msg);

}
