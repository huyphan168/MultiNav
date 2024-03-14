package com.fsoft.multinav;
import java.util.*;
import java.nio.file.Paths;
import com.google.gson.*;
import java.nio.file.Path;
import java.net.URI;

public class Navigator {
    private Path workspaceRoot;
    public static DefinitionProvider navigator;
    private JavaCompilerService cacheCompiler;
    private JsonObject cacheSettings;
    private boolean modifiedBuild = true;
    private JsonObject settings = new JsonObject();
    private FindResource fr;

    JavaCompilerService compiler() {
        if (needsCompiler()) {
            cacheCompiler = createCompiler();
            cacheSettings = settings;
            modifiedBuild = false;
        }
        return cacheCompiler;
    }

    public void initialize(InitializeParams params) {
        this.workspaceRoot = Paths.get(params.rootUri);
        this.fr = new FindResource(Paths.get(params.rootUri));
        FileStore.setWorkspaceRoots(Set.of(Paths.get(params.rootUri)));
    }

    private boolean needsCompiler() {
        if (modifiedBuild) {
            return true;
        }
        if (!settings.equals(cacheSettings)) {
            return true;
        }
        return false;
    }

    public Optional<List<Location>> navigate(TextDocumentPositionParams position) {
        if (!FileStore.isJavaFile(position.textDocument.uri)) return Optional.empty();
        var file = Paths.get(position.textDocument.uri);
        var line = position.position.line + 1;
        var column = position.position.character + 1;
        var found = new DefinitionProvider(compiler(), file, line, column).find();
        if (found == DefinitionProvider.NOT_SUPPORTED) {
            return Optional.empty();
        }
        return Optional.of(found);
    }

    private JavaCompilerService createCompiler() {
        Objects.requireNonNull(workspaceRoot, "Can't create compiler because workspaceRoot has not been initialized");

        var externalDependencies = externalDependencies();
        var classPath = classPath();
        var addExports = addExports();
        // If classpath is specified by the user, don't infer anything
        if (!classPath.isEmpty()) {
            return new JavaCompilerService(classPath, docPath(), addExports);
        }
        // Otherwise, combine inference with user-specified external dependencies
        else {
            var infer = new InferConfig(workspaceRoot, externalDependencies);
            classPath = infer.classPath();
            var docPath = infer.buildDocPath();

            return new JavaCompilerService(classPath, docPath, addExports);
        }
    }

    private Set<Path> classPath() {
        if (!settings.has("classPath")) return Set.of();
        var array = settings.getAsJsonArray("classPath");
        var paths = new HashSet<Path>();
        for (var each : array) {
            paths.add(Paths.get(each.getAsString()).toAbsolutePath());
        }
        return paths;
    }

    private Set<String> addExports() {
        if (!settings.has("addExports")) return Set.of();
        var array = settings.getAsJsonArray("addExports");
        var strings = new HashSet<String>();
        for (var each : array) {
            strings.add(each.getAsString());
        }
        return strings;
    }

    private Set<String> externalDependencies() {
        if (!settings.has("externalDependencies")) return Set.of();
        var array = settings.getAsJsonArray("externalDependencies");
        var strings = new HashSet<String>();
        for (var each : array) {
            strings.add(each.getAsString());
        }
        return strings;
    }

    private Set<Path> docPath() {
        if (!settings.has("docPath")) return Set.of();
        var array = settings.getAsJsonArray("docPath");
        var paths = new HashSet<Path>();
        for (var each : array) {
            paths.add(Paths.get(each.getAsString()).toAbsolutePath());
        }
        return paths;
    }

    public List<String> doGoto(String file, int row, int column) {
            return doGoto(file, row, column, false);
        }
    
    private List<String> doGoto(String file, int row, int column, boolean includeColumn) {
        TextDocumentIdentifier document = new TextDocumentIdentifier();

        document.uri = fr.uri(file);

        Position position = new Position();

        position.line = row - 1;
        position.character = column - 1;

        TextDocumentPositionParams p = new TextDocumentPositionParams();

        p.textDocument = document;
        p.position = position;

        var locations = navigate(p).orElse(List.of());
        var strings = new ArrayList<String>();
        for (var l : locations) {
            var fileName = path(l.uri).getFileName();
            var start = l.range.start;
            if (includeColumn) {
                strings.add(String.format("%s:%d,%d", fileName, start.line + 1, start.character + 1));
            } else {
                strings.add(String.format("%s:%d", fileName, start.line + 1));
            }
        }
        return strings;
    }
    private Path path(URI uri) {
        switch (uri.getScheme()) {
            case "file":
                return Paths.get(uri);
            case "jar":
                return Paths.get(uri.getSchemeSpecificPart().substring("file://".length()));
            default:
                throw new RuntimeException("Don't know what to do with " + uri.getScheme());
        }
    }
}
