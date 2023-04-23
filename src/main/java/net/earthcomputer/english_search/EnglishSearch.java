package net.earthcomputer.english_search;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class EnglishSearch {
    public static BiFunction<String, String, String> englishTranslations = Language.getInstance()::getOrDefault;

    public static Component toEnglish(Component component) {
        ComponentContents contents = component.getContents();
        if (contents instanceof TranslatableContents translatable) {
            Object[] args = translatable.getArgs().clone();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Component c) {
                    args[i] = toEnglish(c);
                }
            }
            StringBuilder sb = new StringBuilder();
            new TranslatableContents(translatable.getKey(), translatable.getFallback(), args).decomposeTemplate(
                englishTranslations.apply(translatable.getKey(), Objects.requireNonNullElse(translatable.getFallback(), translatable.getKey())),
                text -> text.visit(s -> {
                    sb.append(s);
                    return Optional.empty();
                })
            );
            contents = new LiteralContents(sb.toString());
        }
        List<Component> siblings = new ArrayList<>(component.getSiblings().size());
        for (Component sibling : component.getSiblings()) {
            siblings.add(toEnglish(sibling));
        }
        return new MutableComponent(contents, siblings, component.getStyle());
    }
}
