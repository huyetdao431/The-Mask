package game.entities;


import game.audio.MyAudioPlayer;
import game.Panel;

import javax.swing.*;
import java.awt.*;

public class Character extends Thread {
    private int worldX;
    private int worldY;
    private final int screenX;
    private final int screenY;

    game.Panel panel;

    private int move;
    private String direction;
    private int point;
    private boolean die;

    private boolean trapCollisionTop, trapCollisionBottom, trapCollisionLeft, trapCollisionRight;

    private Image up, down, left, right, thornUp, thornDown, thornLeft, thornRight;

    private final MyAudioPlayer charAudio = new MyAudioPlayer();
    private final MyAudioPlayer spikesAudio = new MyAudioPlayer();

    public Character(Panel panel) {
        this.panel = panel;
        getSourceImage();
        direction = "down";
        trapCollisionTop = false;
        trapCollisionBottom = false;
        trapCollisionLeft = false;
        trapCollisionRight = false;
        screenX = panel.screenWidth / 2;
        screenY = panel.screenHeight / 2;
        worldX = panel.tileSize * 11;
        worldY = (panel.maxWorldRow - 6) * panel.tileSize;
        move = 0;
        point = 0;
        die = false;
        charAudio.setFile("landing");
        spikesAudio.setFile("spikesAttack");
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    public void getSourceImage() {
        up = new ImageIcon(getClass().getResource("/images/up.png")).getImage();
        down = new ImageIcon(getClass().getResource("/images/down.png")).getImage();
        left = new ImageIcon(getClass().getResource("/images/left.png")).getImage();
        right = new ImageIcon(getClass().getResource("/images/right.png")).getImage();
        thornUp = new ImageIcon(getClass().getResource("/images/thorn_up.png")).getImage();
        thornDown = new ImageIcon(getClass().getResource("/images/thorn_down.png")).getImage();
        thornLeft = new ImageIcon(getClass().getResource("/images/thorn_left.png")).getImage();
        thornRight = new ImageIcon(getClass().getResource("/images/thorn_right.png")).getImage();
    }

    @Override
    public void run() {
        super.run();
        while (!die) {
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int wCol = worldX / panel.tileSize;
            int wRow = worldY / panel.tileSize;

            if (panel.tiles.get(wRow).get(wCol).equals("9")) {
                panel.levelComplete();
                move = 0;
            }

            if (wCol >0 && panel.tiles.get(wRow).get(wCol - 1).equals("5")) {
                if (move == 1) {
                    move = 0;
                    point = 0;
                }
                trapCollisionLeft = true;
            } else if (wCol <= panel.maxWorldCol && panel.tiles.get(wRow).get(wCol + 1).equals("5")) {
                if (move == 2) {
                    move = 0;
                    point = 0;
                }
                trapCollisionRight = true;
            } else if (wRow >0 && panel.tiles.get(wRow - 1).get(wCol).equals("5")) {
                if (move == 3) {
                    move = 0;
                    point = 0;
                }
                trapCollisionTop = true;
            } else if (wRow <=panel.maxWorldRow && panel.tiles.get(wRow + 1).get(wCol).equals("5")) {
                if (move == 4) {
                    move = 0;
                    point = 0;
                }
                trapCollisionBottom = true;
            }

            if (trapCollisionTop) {
                trapActivated(wCol, wRow);
                trapCollisionTop = false;
            }
            else if (trapCollisionBottom) {
                trapActivated(wCol, wRow);
                trapCollisionBottom = false;
            }
            else if (trapCollisionLeft) {
                trapActivated(wCol, wRow);
                trapCollisionLeft = false;
            } else if (trapCollisionRight) {
                trapActivated(wCol, wRow);
                trapCollisionRight = false;
            }

            if (move == 1) {
                if (panel.tiles.get(wRow).get(wCol - 1).equals("1")) {
                    point = 0;
                    move = 0;
                    charAudio.playMusic();
                } else if (panel.tiles.get(wRow).get(wCol - 1).equals("2")) {
                    panel.collision = 1;
                    move = 0;
                } else if (panel.tiles.get(wRow).get(wCol - 1).equals("3")) {
                    panel.GetPoint(wRow, wCol - 1);
                } else {
                    worldX = worldX - panel.tileSize;
                    if (point < 4) {
                        point++;
                    }
                }
            } else if (move == 2) {
                if (panel.tiles.get(wRow).get(wCol + 1).equals("1")) {
                    point = 0;
                    move = 0;
                    charAudio.playMusic();
                } else if (panel.tiles.get(wRow).get(wCol + 1).equals("2")) {
                    panel.collision = 1;
                    move = 0;
                } else if (panel.tiles.get(wRow).get(wCol + 1).equals("3")) {
                    panel.GetPoint(wRow, wCol + 1);
                } else {
                    worldX = worldX + panel.tileSize;
                    if (point < 4) {
                        point++;
                    }
                }
            } else if (move == 3) {
                if(panel.tiles.get(wRow-1).get(wCol).equals("1")){
                    move = 0;
                    point = 0;
                    charAudio.playMusic();
                } else if (panel.tiles.get(wRow - 1).get(wCol).equals("2")) {
                    panel.collision = 1;
                    move = 0;
                } else if (panel.tiles.get(wRow - 1).get(wCol).equals("3")) {
                    panel.GetPoint(wRow - 1, wCol);
                } else {
                    worldY = worldY - panel.tileSize;
                    if (point < 4) {
                        point++;
                    }
                }
            } else if (move == 4) {
                if (panel.tiles.get(wRow + 1).get(wCol).equals("1")) {
                    move = 0;
                    point = 0;
                    charAudio.playMusic();
                } else if (panel.tiles.get(wRow + 1).get(wCol).equals("2")) {
                    panel.collision = 1;
                    move = 0;
                } else if (panel.tiles.get(wRow + 1).get(wCol).equals("3")) {
                    panel.GetPoint(wRow + 1, wCol);
                } else {
                    worldY = worldY + panel.tileSize;
                    if (point < 4) {
                        point++;
                    }
                }
            }
        }
    }

    private void trapActivated(int col, int row) {
        new Thread(() -> {
            try {
                Thread.sleep(300);
                panel.tiles.get(row).set(col, "2");
                spikesAudio.playMusic();

                int colChar = worldX/panel.tileSize;
                int rowChar = worldY/panel.tileSize;
                if(panel.tiles.get(rowChar).get(colChar).equals("2")) {
                    spikesAudio.stopMusic();
                    panel.collision = 1;
                    move = 0;
                }
                Thread.sleep(1000);

                panel.tiles.get(row).set(col, "0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void draw(Graphics g) {
        drawCharacter(g);
        drawCharacterTail(g);
    }

    public void drawCharacter(Graphics g) {
        g.setColor(Color.yellow);
        Image image = null;
        switch (direction) {
            case "up":
                image = up;
                break;
            case "down":
                image = down;
                break;
            case "left":
                image = left;
                break;
            case "right":
                image = right;
                break;
        }
        g.drawImage(image, screenX, screenY, panel.tileSize, panel.tileSize, null);
    }


    public void drawCharacterTail(Graphics g) {
        if (move == 1) {
            if (point >= 1) {
                if (point >= 2) {
                    g.fillRect(screenX + panel.tileSize, screenY,
                            (point - 2) * panel.tileSize, panel.tileSize / 3 - 3);
                }

                g.fillRect(screenX + panel.tileSize, screenY + (panel.tileSize / 3),
                        point * panel.tileSize, panel.tileSize / 3);
                if (point >= 3) {
                    g.fillRect(screenX + panel.tileSize, screenY + 2 * (panel.tileSize / 3) + 3,
                            (point - 3) * panel.tileSize, panel.tileSize / 3 - 3);
                }
            }
        } else if (move == 2) {
            if (point >= 1) {
                if (point >= 2) {
                    g.fillRect(screenX - (point - 2) * panel.tileSize, screenY,
                            (point - 2) * panel.tileSize, panel.tileSize / 3 - 3);
                }

                g.fillRect(screenX - point * panel.tileSize, screenY + (panel.tileSize / 3),
                        point * panel.tileSize, panel.tileSize / 3);
                if (point >= 3) {
                    g.fillRect(screenX - (point - 3) * panel.tileSize, screenY + 2 * (panel.tileSize / 3) + 3,
                            (point - 3) * panel.tileSize, panel.tileSize / 3 - 3);
                }
            }
        } else if (move == 3) {
            if (point >= 1) {
                if (point >= 2) {
                    g.fillRect(screenX, screenY + panel.tileSize,
                            (panel.tileSize / 3) - 3, (point - 2) * panel.tileSize);
                }

                g.fillRect(screenX + panel.tileSize / 3, screenY + panel.tileSize,
                        panel.tileSize / 3, point * panel.tileSize);
                if (point >= 3) {
                    g.fillRect(screenX + (2 * panel.tileSize) / 3 + 3, screenY + panel.tileSize,
                            (panel.tileSize / 3) - 3, (point - 3) * panel.tileSize);
                }
            }
        } else if (move == 4) {
            if (point >= 1) {
                if (point >= 2) {
                    g.fillRect(screenX, screenY - (point - 2) * panel.tileSize,
                            (panel.tileSize / 3) - 3, (point - 2) * panel.tileSize);
                }

                g.fillRect(screenX + panel.tileSize / 3, screenY - point * panel.tileSize,
                        panel.tileSize / 3, point * panel.tileSize);
                if (point >= 3) {
                    g.fillRect(screenX + (2 * panel.tileSize) / 3 + 3, screenY - (point - 3) * panel.tileSize,
                            (panel.tileSize / 3) - 3, (point - 3) * panel.tileSize);
                }
            }
        }
    }

    public void moveUp() {
        move = 3;
        direction = "up";
    }

    public void moveDown() {
        move = 4;
        direction = "down";
    }

    public void moveLeft() {
        move = 1;
        direction = "left";
    }

    public void moveRight() {
        move = 2;
        direction = "right";
    }
}