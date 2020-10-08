package ru.digitalhabbits.homework1.plugin;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrequencyDictionaryPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        Map<String, Integer> map = new HashMap<>();
        Pattern SPLITTER = Pattern.compile("(\\b[a-zA-Z][a-zA-Z.0-9-]*\\b)");
        Matcher matcher = SPLITTER.matcher(text);

        while (matcher.find()) {
            String word = matcher.group().trim().toLowerCase();
            if (!map.containsKey(word)){
                map.put(word, 1);
            } else {
                map.put(word, map.get(word) + 1);
            }
        }

//        Set<Pair<Integer, String>> pairs = new TreeSet<>(Collections.reverseOrder());
        Set<Pair<Integer, String>> pairs = new TreeSet<>((o1, o2) -> o1.getRight().compareTo(o2.getRight()));
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            pairs.add(new ImmutablePair<>(entry.getValue(), entry.getKey()));
        }
        StringBuilder sb = new StringBuilder("");
        for (Pair pair: pairs) {
            sb.append(pair.getRight())
                    .append(" ")
                    .append(pair.getLeft())
                    .append("\n");
        }
        return sb.toString();
    }
}