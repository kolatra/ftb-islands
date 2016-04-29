package com.kolatra.ftbislands.util;

import java.io.File;

public class IslandLoader {

    public void loadIslandFromFile(File island) {
        if (!island.getName().endsWith(".island")) {
            return;
        }
    }
}
