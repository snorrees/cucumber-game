package com.cucumber.workshop;

public class ResourceDirectory {
    public final String dir;

    public ResourceDirectory(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return dir;
    }
}
