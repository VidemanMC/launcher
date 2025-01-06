package ru.videmanmc.launcher.model.value.files.ignored;

public class DirectoryPredicate implements ExcludePredicate {


    @Override
    public boolean isMatch(String pathSubString) {
        return pathSubString.indexOf("/") < pathSubString.length();
    }
}
