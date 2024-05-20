package game.entities;

import game.Panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mobs {
    public List<Bat> bats;
    Panel panel;
    Character character;

    public Mobs(Character character, Panel panel) {
        this.character = character;
        this.panel = panel;
        bats = new ArrayList<>();
    }

    public void getMobs(List<List<String>> tiles) {
        for(int i = 0; i< tiles.size(); i++) {
            for(int j = 0; j< tiles.get(0).size(); j++) {
                if(tiles.get(i).get(j).equals("6")) {
                    Bat bat = new Bat(character, panel, j, i);
                    bats.add(bat);
                }
            }
        }
    }

    public void draw(Graphics g) {
        for(Bat bat : bats) {
            bat.draw(g);
        }
    }

    public void isDied() {
        for (Bat bat: bats) {
            bat.setDie(true);
        }
    }

}
