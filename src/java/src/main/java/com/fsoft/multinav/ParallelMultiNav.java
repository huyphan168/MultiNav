package com.fsoft.multinav;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParallelMultiNav {
    private Path workspaceRoot;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    public ParallelMultiNav(String workspace){
        this.workspaceRoot = Paths.get(workspace);
    }

    public List<String> execute(List<Task> tasks) throws InterruptedException, ExecutionException {
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        
        for (Task task : tasks) {
            CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
                // Create a new Navigator instance for each task to avoid shared state issues
                Navigator localNavigator = get_navigator(this.workspaceRoot);
                var suggestions = localNavigator.doGoto(task.file, task.line, task.column);
                return suggestions;
            }, threadPool);
            
            futures.add(future);
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        List<String> results = new ArrayList<>();
        for (CompletableFuture<List<String>> future : futures) {
            results.addAll(future.get());
        }
        
        return results;
    }

    private static Navigator get_navigator(Path workspaceRoot) {
        var init = new InitializeParams();
        init.rootUri = workspaceRoot.toUri();
        var navigator = new Navigator();
        navigator.initialize(init);
        return navigator;
    }
}

