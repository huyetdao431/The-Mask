package game;

import game.audio.MyAudioPlayer;
import game.entities.Bat;
import game.entities.Character;
import game.entities.Mobs;
import game.map.Map;
import game.map.MapManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.*;


public class Panel  extends JPanel implements KeyListener,ActionListener {

    public final int  tileSize = 24;
    public final int  screenWidth = 720;
    public final int  screenHeight = 720;
    int key = 1;

    public int maxWorldCol;
    public int maxWorldRow;

    private int state = 0;
    final private static int titleState = 0;
    final private static int gameState = 1;
    final private static int finishedState = 2;
    final private static int deadState = 3;
    final private static int levelState = 4;

    private static Image bgImage, bgImage2;

    private int command = 1;

    private MapManager map ;
    private int level = 0;
    public List<List<String>> tiles;

    private Character character;
    private Mobs mobs;

    private int point = 0;
    public int collision = 0;

    Timer collisionTimer;

    MyAudioPlayer bgAudio = new MyAudioPlayer();
    MyAudioPlayer coinCollectionAudio = new MyAudioPlayer();
    MyAudioPlayer startAudio = new MyAudioPlayer();
    MyAudioPlayer deathAudio = new MyAudioPlayer();
    MyAudioPlayer winAudio = new MyAudioPlayer();

    public List<Color> colors = Arrays.asList(Color.decode("#B22222"),Color.decode("#B22222"),Color.decode("#008B8B"),
                                       Color.decode("#8A2BE2"),Color.decode("#4682B4"),Color.decode("#008B8B"));
    public int color = 0;

    public int getRandomColor(int exclude) {
        Random random = new Random();
        if (exclude ==  -1) {
            return 0;
        }
        while (true) {
            int rand = random.nextInt(colors.size());
            if(rand != exclude) {
                return rand;
            }
        }
    }

    public Panel() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        Timer timer = new Timer(1000/60, this);
        timer.start();

        bgImage = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        bgImage2 = new ImageIcon(getClass().getResource("/images/background2.png")).getImage();
        color = getRandomColor(-1);
        collisionTimer = new Timer(1000/60, e -> {
            if (collision == 1) {
                playerDied();
                collision = 0;
            }
        });
        bgAudio.setFile("background");
        bgAudio.playMusic();
        bgAudio.loopMuisc();
        coinCollectionAudio.setFile("getCoin");
        startAudio.setFile("start");
        deathAudio.setFile("death");
        winAudio.setFile("win");
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);

        switch (state) {
            case titleState: {
                drawTitleScreen(g);
                break;
            }
            case gameState: {
                drawGameScreen(g);
                break;
            }
            case finishedState:{
                drawFinishedScreen(g);
                break;
            }
            case deadState: {
                drawDeadScreen(g);
                break;
            }
            case levelState:{
                drawLevelScreen(g);
                break;
            }
        }
    }

    private void drawDeadScreen(Graphics g) {
        g.drawImage(bgImage, 0, 0, 720, 720,  null);
        setBackground(Color.black);
        String text = "You Died";
        int x = screenWidth/2 - tileSize*4;
        int y = tileSize * 5;
        g.setColor(Color.yellow);
        g.setFont(getFont().deriveFont(64f));
        g.drawString(text,x, y);

        drawButton(g, x, screenHeight/2-tileSize*2, "Restart", 1);
        drawButton(g, x, screenHeight/2+tileSize*2, "Main Menu", 2);
    }

    private void drawButton(Graphics g, int x, int y, String s, int cmm) {
        g.setColor(Color.yellow);
        g.fillRect(x,y,tileSize*9, tileSize*2);
        g.setColor(Color.black);
        g.setFont(getFont().deriveFont(24f));
        g.drawString(s,  x + tileSize/2, y + tileSize*3/2);
        if(command == cmm) {
            g.fillRect(x+tileSize/8,y+tileSize/8,tileSize*9-tileSize/4, tileSize*2-tileSize/4);
            g.setColor(Color.yellow);
            g.drawString(s,  x + tileSize/2, y + tileSize*3/2);
        }
    }

    private void drawLevelScreen(Graphics g) {
        g.drawImage(bgImage, 0, 0, 720, 720,  null);
        setBackground(Color.black);
        String text = "Level  "+level;
        int x = screenWidth/2 - tileSize*2;
        int y = tileSize * 5;
        g.setColor(Color.yellow);
        g.setFont(getFont().deriveFont(32f));
        g.drawString(text,x, y);

        text = "You Scored : "+String.valueOf(point);
        g.setColor(Color.yellow);
        g.setFont(getFont().deriveFont(32f));
        g.drawString(text,x-2*tileSize, y+3*tileSize);

        drawButton(g, x - tileSize*2, screenHeight/2-tileSize*2, "Next Level", 1);
        drawButton(g, x - tileSize*2, screenHeight/2+tileSize*2, "Main Menu", 2);
    }

    private void drawFinishedScreen(Graphics g) {
        g.drawImage(bgImage, 0, 0, 720, 720,  null);
        setBackground(Color.black);
        String text = "You Won";
        int x = screenWidth/2 - tileSize*4;
        int y = tileSize * 10;
        g.setColor(Color.yellow);
        g.setFont(getFont().deriveFont(64f));
        g.drawString(text,x, y);
    }

    private void drawGameScreen(Graphics g) {
        map.drawMap(g, character);
        character.draw(g);
        mobs.draw(g);
        drawPoints(g);
    }

    public void drawPoints(Graphics g) {
        int scorex = screenWidth - tileSize*5;
        int scorey = 2*tileSize;
        g.setColor(Color.black);
        g.fillRect(scorex,scorey-tileSize,4*tileSize+10,tileSize+10);
        g.setColor(Color.white);
        g.drawRect(scorex,scorey-tileSize,4*tileSize+10,tileSize+10);
        g.setColor(Color.YELLOW);
        g.setFont(getFont().deriveFont(16f));
        g.drawString("Points : " +String.valueOf(point), scorex+15, scorey);
    }

    private void drawTitleScreen(Graphics g) {
        String text = "THE BOX";
        int x = screenWidth/2 - tileSize*4;
        int y = tileSize * 5;
        g.setColor(Color.YELLOW);
        g.setFont(getFont().deriveFont(48f));
        g.drawString(text,x, y);

        g.drawImage(bgImage2, 0, 0, 720, 720,  null);

        drawButton(g, x, screenHeight/2-tileSize*2, "Start Game", 1);
        drawButton(g, x , screenHeight/2+tileSize*2, "Quit Game", 2);
    }

    private boolean LoadMap() {
        map = new MapManager(this, level);
        tiles = map.GetMap();
        if(tiles == null) {
            return false;
        }
        maxWorldCol = map.getMaxWorldCol();
        maxWorldRow = map.getMaxWorldRow();
        return true;
    }

    private void StartGame() {
        if(!LoadMap()) {
            return;
        }
        color = getRandomColor(color);

        character = new Character(this);
        mobs = new Mobs(character, this);
        mobs.getMobs(tiles);

        state = gameState;
        character.start();
        for(Bat bat : mobs.bats) {
            bat.start();
        }

        collisionTimer.start();

        startAudio.playMusic();
        repaint();
    }

    private void resetGame(){
        map = null;
        tiles = null;
        maxWorldCol = 0;
        maxWorldRow = 0;
        character.setDie(true);
        mobs.isDied();
        collisionTimer.stop();
        point = 0;
        level = 0;

    }

    public void playerDied() {
        character.setDie(true);
        mobs.isDied();
        deathAudio.playMusic();
        point = 0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        state = deadState;
        repaint();
    }

    public void playerWon() {
        character.setDie(true);
        mobs.isDied();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        state = finishedState;
        winAudio.playMusic();
        repaint();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        state = titleState;
                        resetGame();
                        repaint();
                    }
                },
                1200
        );
    }

    public void levelComplete() {
        winAudio.playMusic();
        level++;
        if(level > 3) {
            playerWon();
            return;
        }
        character.setDie(true);
        mobs.isDied();
        state = levelState;
        repaint();
    }

    public void GetPoint(int row,int col) {
        coinCollectionAudio.playMusic();
        tiles.get(row).set(col, "0");
        point++;
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(key == 1) {

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(key == 1) {

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(key == 1) {
            if (state == titleState) {
                if (keyCode == KeyEvent.VK_UP) {
                    command = 1;
                }

                if (keyCode == KeyEvent.VK_DOWN) {
                    command = 2;
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (command == 1) {
                        StartGame();
                    }
                    else{
                        System.exit(0);
                    }
                }
                repaint();
            }
            if (state == deadState) {
                if (keyCode == KeyEvent.VK_UP) {
                    command = 1;
                }

                if (keyCode == KeyEvent.VK_DOWN) {
                    command = 2;
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (command == 1) {
                        StartGame();
                    }
                    else{
                        command = 1;
                        resetGame();
                        state = titleState;
                    }
                }
                repaint();
            }if (state == levelState) {
                if (keyCode == KeyEvent.VK_UP) {
                    command = 1;
                }

                if (keyCode == KeyEvent.VK_DOWN) {
                    command = 2;
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (command == 1) {
                        StartGame();
                    }
                    else{
                        command = 0;
                        resetGame();
                        state = titleState;
                    }
                }
                repaint();
            }
            if(state == gameState) {
                if (keyCode == KeyEvent.VK_UP) {
                    character.moveUp();
                }

                if (keyCode == KeyEvent.VK_DOWN) {
                    character.moveDown();
                }

                if (keyCode == KeyEvent.VK_RIGHT) {
                    character.moveRight();
                }

                if (keyCode == KeyEvent.VK_LEFT) {
                    character.moveLeft();
                }
                repaint();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }
}
