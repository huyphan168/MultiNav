package com.fsoft.multinav;

class Task {
    String file;
    int line;
    int column;
    
    public Task(String file, int line, int column) {
        this.file = file;
        this.line = line;
        this.column = column;
    }
}