package com.fsoft.multinav;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static Navigator navigator;

    public static void main(String[] arg) {
        String file = "/org/javacs/example/Goto.java";
        navigator = get_navigator(Paths.get("/home/anhnt446/MultiNav/src/java/src/test/examples"));
        var suggestions = navigator.doGoto(file, 17, 14);
        assertThat(suggestions, hasItem("Goto.java:40"));
    }

    private static Navigator get_navigator(Path workspaceRoot) {
        var init = new InitializeParams();
        init.rootUri = workspaceRoot.toUri();
        var navigator = new Navigator();
        navigator.initialize(init);
        return navigator;
    }
}