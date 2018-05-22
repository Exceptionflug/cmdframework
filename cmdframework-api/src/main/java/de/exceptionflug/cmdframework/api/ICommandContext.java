package de.exceptionflug.cmdframework.api;

public interface ICommandContext {

    <T> T getSender(Class<T> type);
    String[] getArguments();
    String getCommandLabel();

    default int getArgumentCount() {
        return getArguments().length;
    }

    default int getArgumentInt(final int i) throws NumberFormatException {
        return Integer.parseInt(getArgument(i));
    }

    default double getArgumentDouble(final int i) throws NumberFormatException {
        return Double.parseDouble(getArgument(i));
    }

    default String getArgument(final int i) {
        return getArguments()[i];
    }

    default String getJoinedString(final int i) {
        final StringBuilder buffer = new StringBuilder(getArgument(i));
        for (int ii = i + 1; ii < getArgumentCount(); ++ii) {
            buffer.append(" ").append(getArgument(ii));
        }
        return buffer.toString();
    }


}
