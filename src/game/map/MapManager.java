package game.map;

import game.Panel;
import game.entities.Character;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {
    private int maxWorldCol;
    private int maxWorldRow;
    Panel panel;
    Map map;
    List<List<String>> tiles;
    private Image thornUp, thornDown, thornLeft, thornRight;


    public MapManager (Panel panel, int level) {
        this.panel = panel;
        map = new Map(level);
        maxWorldRow = 0;
        maxWorldCol = 0;
        getSourceImage();
    }
    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public void getSourceImage() {
        thornUp = new ImageIcon(getClass().getResource("/images/thorn_up.png")).getImage();
        thornDown = new ImageIcon(getClass().getResource("/images/thorn_down.png")).getImage();
        thornLeft = new ImageIcon(getClass().getResource("/images/thorn_left.png")).getImage();
        thornRight = new ImageIcon(getClass().getResource("/images/thorn_right.png")).getImage();
    }

    public List<List<String>> GetMap() {

        File file = map.getFile();
        if(file == null) {
            return null;
        }

        List<List<String>> list = fileReader(file);

        tiles = new ArrayList<>();
        tiles.addAll(list);
        maxWorldCol = tiles.get(0).size();
        maxWorldRow = tiles.size();
        return tiles;
    }

    public List<List<String>> fileReader(File file) {
        List<List<String>> array = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                array.add(Arrays.asList(temp));
            }
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    public void drawMap(Graphics g, Character character) {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < maxWorldCol && worldRow < maxWorldRow) {
            int worldx = worldCol * panel.tileSize;
            int worldy = worldRow * panel.tileSize;

            //screen x coordinate is same as worldx coordinate and world is not moved in x axis
            int mapscreenx = worldx - character.getWorldX() + character.getScreenX();
            int mapscreeny = worldy - character.getWorldY() + character.getScreenY();

            if (tiles.get(worldRow).get(worldCol).equals("1")) {
                g.setColor(panel.colors.get(panel.color));
                if (worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow - 1).get(worldCol);
                    if (top.equals("0") || top.equals("3") || top.equals("2") || top.equals("9") || top.equals("5") || top.equals("6")) {
                        g.fillRect(mapscreenx, mapscreeny, panel.tileSize, panel.tileSize / 3);
                    }
                }
                if (worldRow + 1 <= maxWorldRow - 1) {
                    String bottom = tiles.get(worldRow + 1).get(worldCol);
                    if (bottom.equals("0") || bottom.equals("3") || bottom.equals("2") || bottom.equals("9") || bottom.equals("5") || bottom.equals("6")) {
                        g.fillRect(mapscreenx, mapscreeny + panel.tileSize - panel.tileSize / 3, panel.tileSize, panel.tileSize / 3);
                    }
                }
                if (worldCol - 1 >= 0) {
                    String left = tiles.get(worldRow).get(worldCol - 1);
                    if (left.equals("0") || left.equals("3") || left.equals("2") || left.equals("9") || left.equals("5") || left.equals("6")) {
                        g.fillRect(mapscreenx, mapscreeny, panel.tileSize / 3, panel.tileSize);
                    }
                }
                if (worldCol + 1 <= maxWorldCol - 1) {
                    String right = tiles.get(worldRow).get(worldCol + 1);
                    if (right.equals("0") || right.equals("3") || right.equals("2") || right.equals("9") || right.equals("5") || right.equals("6")) {
                        g.fillRect(mapscreenx + panel.tileSize - panel.tileSize / 3, mapscreeny, panel.tileSize / 3, panel.tileSize);
                    }
                }
                if (worldCol + 1 <= maxWorldCol - 1 && worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow - 1).get(worldCol);
                    String right = tiles.get(worldRow).get(worldCol + 1);
                    String cross = tiles.get(worldRow - 1).get(worldCol + 1);
                    if (top.equals("1") && right.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx + panel.tileSize - panel.tileSize / 3, mapscreeny, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
                if (worldCol + 1 <= maxWorldCol - 1 && worldRow + 1 <= maxWorldRow - 1) {
                    String bottom = tiles.get(worldRow + 1).get(worldCol);
                    String right = tiles.get(worldRow).get(worldCol + 1);
                    String cross = tiles.get(worldRow + 1).get(worldCol + 1);
                    if (bottom.equals("1") && right.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx + panel.tileSize - panel.tileSize / 3, mapscreeny + panel.tileSize - panel.tileSize / 3, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
                if (worldCol - 1 >= 0 && worldRow + 1 <= maxWorldRow - 1) {
                    String bottom = tiles.get(worldRow + 1).get(worldCol);
                    String left = tiles.get(worldRow).get(worldCol - 1);
                    String cross = tiles.get(worldRow + 1).get(worldCol - 1);
                    if (bottom.equals("1") && left.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx, mapscreeny + panel.tileSize - panel.tileSize / 3, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
                if (worldCol - 1 >= 0 && worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow - 1).get(worldCol);
                    String left = tiles.get(worldRow).get(worldCol - 1);
                    String cross = tiles.get(worldRow - 1).get(worldCol - 1);
                    if (top.equals("1") && left.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx, mapscreeny, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
            }
            else if (tiles.get(worldRow).get(worldCol).equals("5")) {
                g.setColor(Color.decode("#00FFFF"));
                if (worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow - 1).get(worldCol);
                    if (top.equals("0") || top.equals("3") || top.equals("2") || top.equals("9")) {
                        g.fillRect(mapscreenx, mapscreeny, panel.tileSize, panel.tileSize / 3);
                    }
                }
                if (worldRow + 1 <= maxWorldRow - 1) {
                    String bottom = tiles.get(worldRow + 1).get(worldCol);
                    if (bottom.equals("0") || bottom.equals("3") || bottom.equals("2") || bottom.equals("9")) {
                        g.fillRect(mapscreenx, mapscreeny + panel.tileSize - panel.tileSize / 3, panel.tileSize, panel.tileSize / 3);
                    }
                }
                if (worldCol - 1 >= 0) {
                    String left = tiles.get(worldRow).get(worldCol - 1);
                    if (left.equals("0") || left.equals("3") || left.equals("2") || left.equals("9")) {
                        g.fillRect(mapscreenx, mapscreeny, panel.tileSize / 3, panel.tileSize);
                    }
                }
                if (worldCol + 1 <= maxWorldCol - 1) {
                    String right = tiles.get(worldRow).get(worldCol + 1);
                    if (right.equals("0") || right.equals("3") || right.equals("2") || right.equals("9")) {
                        g.fillRect(mapscreenx + panel.tileSize - panel.tileSize / 3, mapscreeny, panel.tileSize / 3, panel.tileSize);
                    }
                }
                if (worldCol + 1 <= maxWorldCol - 1 && worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow - 1).get(worldCol);
                    String right = tiles.get(worldRow).get(worldCol + 1);
                    String cross = tiles.get(worldRow - 1).get(worldCol + 1);
                    if (top.equals("1") && right.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx + panel.tileSize - panel.tileSize / 3, mapscreeny, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
                if (worldCol + 1 <= maxWorldCol - 1 && worldRow + 1 <= maxWorldRow - 1) {
                    String bottom = tiles.get(worldRow + 1).get(worldCol);
                    String right = tiles.get(worldRow).get(worldCol + 1);
                    String cross = tiles.get(worldRow + 1).get(worldCol + 1);
                    if (bottom.equals("1") && right.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx + panel.tileSize - panel.tileSize / 3, mapscreeny + panel.tileSize - panel.tileSize / 3, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
                if (worldCol - 1 >= 0 && worldRow + 1 <= maxWorldRow - 1) {
                    String bottom = tiles.get(worldRow + 1).get(worldCol);
                    String left = tiles.get(worldRow).get(worldCol - 1);
                    String cross = tiles.get(worldRow + 1).get(worldCol - 1);
                    if (bottom.equals("1") && left.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx, mapscreeny + panel.tileSize - panel.tileSize / 3, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
                if (worldCol - 1 >= 0 && worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow - 1).get(worldCol);
                    String left = tiles.get(worldRow).get(worldCol - 1);
                    String cross = tiles.get(worldRow - 1).get(worldCol - 1);
                    if (top.equals("1") && left.equals("1") && !cross.equals("1")) {
                        g.fillRect(mapscreenx, mapscreeny, panel.tileSize / 3, panel.tileSize / 3);
                    }
                }
            }
            else if (tiles.get(worldRow).get(worldCol).equals("2")) {
                if(worldRow - 1 >= 0) {
                    String top = tiles.get(worldRow-1).get(worldCol);
                    String bottom = tiles.get(worldRow+1).get(worldCol);
                    if (top.equals("1") && bottom.equals("0") || top.equals("5") || bottom.equals("3")) {
                        g.drawImage(thornDown, mapscreenx, mapscreeny, panel.tileSize, panel.tileSize, null);
                    }
                }
                if(worldRow + 1 <= panel.maxWorldRow - 1) {
                    String top = tiles.get(worldRow-1).get(worldCol);
                    String bottom = tiles.get(worldRow+1).get(worldCol);
                    if (bottom.equals("1") && top.equals("0") || bottom.equals("5") || top.equals("3")) {
                        g.drawImage(thornUp, mapscreenx, mapscreeny, panel.tileSize, panel.tileSize, null);
                    }
                }
                if(worldCol - 1 >= 0) {
                    String left = tiles.get(worldRow).get(worldCol-1);
                    String right = tiles.get(worldRow).get(worldCol+1);
                    if (left.equals("1") && right.equals("0") || left.equals("5") || right.equals("3")) {
                        g.drawImage(thornRight, mapscreenx, mapscreeny, panel.tileSize, panel.tileSize, null);
                    }
                }
                if(worldCol + 1 <= panel.maxWorldCol - 1) {
                    String left = tiles.get(worldRow).get(worldCol-1);
                    String right = tiles.get(worldRow).get(worldCol+1);
                    if (right.equals("1") && left.equals("0") || right.equals("5") || left.equals("3")) {
                        g.drawImage(thornLeft, mapscreenx, mapscreeny, panel.tileSize, panel.tileSize, null);
                    }
                }
            }
            else if (tiles.get(worldRow).get(worldCol).equals("3")) {
                g.setColor(Color.yellow);
                g.fillOval(mapscreenx + (panel.tileSize / 4), mapscreeny + (panel.tileSize / 4), panel.tileSize / 4, panel.tileSize / 4);
            }
            else if (tiles.get(worldRow).get(worldCol).equals("9")) {
                g.setColor((Color.yellow));
                g.fillRect(mapscreenx, mapscreeny, panel.tileSize, panel.tileSize);
                g.setColor(Color.black);
                g.fillRect(mapscreenx + panel.tileSize / 8, mapscreeny + panel.tileSize / 8, panel.tileSize / 4 * 3, panel.tileSize / 4 * 3);
                g.setColor((Color.yellow));
                g.fillRect(mapscreenx + panel.tileSize / 4, mapscreeny + panel.tileSize / 4, panel.tileSize / 2, panel.tileSize / 2);
                g.setColor(Color.black);
                g.fillRect(mapscreenx + panel.tileSize / 3, mapscreeny + panel.tileSize / 3, panel.tileSize / 3, panel.tileSize / 3);
            }
            worldCol++;
            if (worldCol == panel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

}
