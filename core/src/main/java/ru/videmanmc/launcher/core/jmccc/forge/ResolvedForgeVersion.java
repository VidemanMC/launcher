package ru.videmanmc.launcher.core.jmccc.forge;


import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Full copy of org.to2mbn.jmccc.mcdownloader.provider.forge.ResolvedForgeVersion
 */
@Getter
public class ResolvedForgeVersion implements Serializable {

    private static final Pattern FORGE_VERSION_PATTERN_1 = Pattern.compile("^([\\w.\\-]+)-[Ff]orge\\1?-([\\w.\\-]+)$");

    private static final Pattern FORGE_VERSION_PATTERN_2 = Pattern.compile("^([\\w.\\-]+)-[Ff]orge([\\w.\\-]+)$");

    private static final Pattern FORGE_VERSION_PATTERN_3 = Pattern.compile("^Forge([\\w.\\-]+)$");

    private static final long serialVersionUID = 1L;

    private String forgeVersion;

    private String minecraftVersion;

    public ResolvedForgeVersion(String forgeVersion, String minecraftVersion) {
        this.forgeVersion = forgeVersion;
        this.minecraftVersion = minecraftVersion;
    }

    public static ResolvedForgeVersion resolve(String version) {
        Matcher matcher = FORGE_VERSION_PATTERN_1.matcher(version);
        String forgeVersion;
        String mcversion;
        if (matcher.matches()) {
            forgeVersion = matcher.group(2);
            mcversion = matcher.group(1);
            return new ResolvedForgeVersion(forgeVersion, mcversion);
        } else {
            matcher = FORGE_VERSION_PATTERN_2.matcher(version);
            if (matcher.matches()) {
                forgeVersion = matcher.group(2);
                mcversion = matcher.group(1);
                return new ResolvedForgeVersion(forgeVersion, mcversion);
            } else {
                matcher = FORGE_VERSION_PATTERN_3.matcher(version);
                if (matcher.matches()) {
                    forgeVersion = matcher.group(1);
                    return new ResolvedForgeVersion(forgeVersion, (String) null);
                } else {
                    return null;
                }
            }
        }
    }

    public String getVersionName() {
        return this.minecraftVersion + "-" + this.forgeVersion;
    }

    public String toString() {
        return this.getVersionName();
    }

    public int hashCode() {
        return Objects.hash(this.forgeVersion, this.minecraftVersion);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof ResolvedForgeVersion)) {
            return false;
        } else {
            ResolvedForgeVersion another = (ResolvedForgeVersion) obj;
            return Objects.equals(this.forgeVersion, another.forgeVersion) && Objects.equals(this.minecraftVersion, another.minecraftVersion);
        }
    }
}
