package com.cricketcraft.ftbisland.util;

import java.io.File;

public class IslandLoader {

    public void loadIslandFromFile(File island) {
        if(!island.getName().endsWith(".island")) {
            return;
        }
    }
}
