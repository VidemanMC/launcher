package ru.videmanmc.launcher.model.value.files.ignored;

public interface ExcludePredicate {

    String WILDCARD = "*";

    boolean isMatch(String pathSubString);

}
