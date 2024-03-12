package com.fsoft.multinav;


import com.google.gson.JsonElement;
import java.net.URI;

public class InitializeParams {
    public int processId;
    public String rootPath;
    public URI rootUri;
    public JsonElement initializationOptions;
    public String trace;
}