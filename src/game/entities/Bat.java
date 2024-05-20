package game.entities;

import game.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Bat extends Thread{
    private boolean die;
    Character character;
    game.Panel panel;
    private final int col;
    private final int row;
    private int x;
    private int y;
    private int speed;
    private String direction ;
    private final Image image;

    public Bat(Character character, Panel panel, int col, int row) {
        this.character = character;
        this.panel = panel;
        this.col = col;
        this.row = row;
        image = new ImageIcon(getClass().getResource("/images/bat.png")).getImage();
        x = col *panel.tileSize;
        y = row * panel.tileSize;
        speed = 3;
        die = false;
        direction = getRandomTypeOfMovement();
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    public String getRandomTypeOfMovement() {
        Random random= new Random();
        int randomNumber = random.nextInt(2);
        if(randomNumber == 0) return "upAndDown";
        else return "leftAndRight";
    }

    @Override
    public void run() {
        super.run();
        while(!die) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            move(direction);

            if (character.getWorldX() < x + panel.tileSize &&
                character.getWorldX() + panel.tileSize > x &&
                character.getWorldY() < y + panel.tileSize &&
                character.getWorldY() + panel.tileSize > y) {
                panel.collision = 1;
                break;
            }
        }
    }

    public void move(String direction) {
        if(direction.equals("leftAndRight")) {
            x+=speed;
            int colKiller = x/ panel.tileSize;
            if(colKiller-1 >=0 && colKiller+1 < panel.maxWorldCol) {
                String left = panel.tiles.get(row).get(colKiller);
                String right = panel.tiles.get(row).get(colKiller+1);
                if(left.equals("1") || right.equals("1")) {
                    speed = -speed;
                }
            }
        } else {
            y+=speed;
            int rowKiller = y/ panel.tileSize;
            if(rowKiller -1 >=0 && rowKiller+1 < panel.maxWorldRow) {
                String top = panel.tiles.get(rowKiller).get(col);
                String bottom = panel.tiles.get(rowKiller+1).get(col);
                if(top.equals("1") || bottom.equals("1")) {
                    speed = -speed;
                }
            }
        }
    }

    public void draw(Graphics g) {
        int mapscreenx = x - character.getWorldX() + character.getScreenX();
        int mapscreeny = y - character.getWorldY() + character.getScreenY();
        g.drawImage(image, mapscreenx, mapscreeny, panel.tileSize, panel.tileSize, null);
    }
}
