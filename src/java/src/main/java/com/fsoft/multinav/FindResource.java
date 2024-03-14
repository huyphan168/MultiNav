package com.fsoft.multinav;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Find java sources in maven-project */
public class FindResource {

    public final String home;

    public FindResource(Path _home){
        home = _home.toString();
    }

    public URI uri(String resourcePath) {
        var path = path(resourcePath);
        return path.toUri();
    }

    public Path path(String resourcePath) {
        if (resourcePath.startsWith("/")) resourcePath = resourcePath.substring(1);
        return Paths.get(home).resolve(resourcePath).toAbsolutePath().normalize();
    }
}