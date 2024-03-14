package com.fsoft.multinav;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import com.fsoft.multinav.ParallelMultiNav;
// public class Main {
//     private static Navigator navigator;

//     public static void main(String[] arg) {
//         String file = "/org/javacs/example/Goto.java";
//         navigator = get_navigator(Paths.get("/home/anhnt446/MultiNav/src/java/src/test/examples"));
//         var suggestions = navigator.doGoto(file, 17, 14);
//         assertThat(suggestions, hasItem("Goto.java:40"));
//     }

//     private static Navigator get_navigator(Path workspaceRoot) {
//         var init = new InitializeParams();
//         init.rootUri = workspaceRoot.toUri();
//         var navigator = new Navigator();
//         navigator.initialize(init);
//         return navigator;
//     }
// }

public class Main {
    private static ParallelMultiNav Nav;

    public static void main(String[] arg) throws InterruptedException{
        try {
            String file = "/org/javacs/example/Goto.java";
            Nav = new ParallelMultiNav("/home/anhnt446/MultiNav/src/java/src/test/examples");
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Task task = new Task(file, 17, 14);
                tasks.add(task);
            }
            List<String> suggestions = Nav.execute(tasks);
            assertThat(suggestions, hasItem("Goto.java:40"));
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}