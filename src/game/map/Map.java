package game.map;

import java.io.File;

public class Map {
    private int level = 0;

    public Map(int level) {
        this.level = level;
    }

    public File getFile() {
        File file;
        if(level == 0) {
            file = new File("res/maps/level1.txt");
        }
        else if(level == 1) {
            file = new File("res/maps/level2.txt");
        }

        else if(level == 2) {
            file = new File("res/maps/level3.txt");
        }

        else if(level == 3) {
            file = new File("res/maps/level4.txt");
        }
        else {
            file = null;
        }
        return file;
    }

}
