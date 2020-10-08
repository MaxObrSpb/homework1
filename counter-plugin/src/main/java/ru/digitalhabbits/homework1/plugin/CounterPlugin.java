package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CounterPlugin
        implements PluginInterface {

    private static final Pattern SPLITTER = Pattern.compile("(\\b[a-zA-Z][a-zA-Z.0-9-]*\\b)");

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        int wordsAmount = 0;
        Matcher matcher = SPLITTER.matcher(text);
        while(matcher.find()){
            wordsAmount++;
        }
        return new StringBuilder(String.valueOf(text.lines().count()))
                .append(";")
                .append(wordsAmount)
                .append(";")
                .append(text.length())
                .toString();
    }

}
