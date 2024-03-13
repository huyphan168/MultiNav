package com.fsoft.multinav;

import java.util.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MultiNav {
    public static Navigator navigator;
    
    public MultiNav(String workspace){
        navigator = get_navigator(Paths.get(workspace));
    }

    public static List<String> execute (String file, int line, int column) {
        var suggestions = navigator.doGoto(file, line, column);
        return suggestions;
    }

    private static Navigator get_navigator(Path workspaceRoot) {
        var init = new InitializeParams();
        init.rootUri = workspaceRoot.toUri();
        var navigator = new Navigator();
        navigator.initialize(init);
        return navigator;
    }
}