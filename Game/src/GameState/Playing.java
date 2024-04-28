package GameState;

import Levels.LevelManager;
import Main.Game;
import Objects.ObjectManager;
import UI.GameOverOverLay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import Utils.LoadSave;
import entities.EnemyManager;
import entities.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Utils.Constants.Environment.*;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay   pauseOverlay;
    private GameOverOverLay gameOverOverLay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused=false;

    private int xLvlOffset;
    private int leftBorder=(int)(0.2*Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8*Game.GAME_WIDTH);

    private int maxLvlOffsetX;

    private boolean gameOver;
    private boolean lvlCompleted;
    private BufferedImage bgImg,bigCloud,smallClouds;
    private int[] smallCloudsPos;
    private Random random = new Random();
    private boolean playerDying;

    public Playing(Game game) {
        super(game);
        initClasses();
        bgImg = LoadSave.GetSpriteAtLas(LoadSave.PLAYING_BG);
        bigCloud = LoadSave.GetSpriteAtLas(LoadSave.BIG_CLOUDS);
        smallClouds = LoadSave.GetSpriteAtLas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for(int i=0;i<smallCloudsPos.length;i++)
            smallCloudsPos[i] = (int)(90*Game.SCALE)+random.nextInt((int)(100*Game.SCALE));

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel(){
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
    }
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(200,200,(int) (64* Game.SCALE),(int) (40*Game.SCALE),this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverLay = new GameOverOverLay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if(paused){
            pauseOverlay.update();
        }else if(lvlCompleted){
            levelCompletedOverlay.update();
        }else if(gameOver){
            gameOverOverLay.update();
        }else if(playerDying){
            player.update();
        } else{
            levelManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(),player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int)player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if(diff>rightBorder)
            xLvlOffset+=diff-rightBorder;
        else if(diff<leftBorder)
            xLvlOffset+=diff-leftBorder;

        if(xLvlOffset>maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if(xLvlOffset<0)
            xLvlOffset = 0;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(bgImg,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
        drawClouds(g);
        levelManager.render(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.render(g,xLvlOffset);
        objectManager.render(g,xLvlOffset);
        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT);
            pauseOverlay.render(g);
        } else if (gameOver) {
            gameOverOverLay.render(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.render(g);
        }
    }

    private void drawClouds(Graphics g) {
        for (int i=0;i<3;i++)
            g.drawImage(bigCloud,i*BIG_CLOUD_WIDTH-(int) (xLvlOffset*0.3),(int)(204*Game.SCALE),BIG_CLOUD_WIDTH,BIG_CLOUD_HEIGHT,null);

        for (int i=0;i<smallCloudsPos.length;i++)
            g.drawImage(smallClouds,SMALL_CLOUD_WIDTH*4*i-(int) (xLvlOffset*0.7),smallCloudsPos[i],SMALL_CLOUD_WIDTH,SMALL_CLOUD_HEIGHT,null);
    }

    public void resetAll(){
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();

    }
    public void  setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){
        objectManager.checkObjectHit(attackbox);
    }
    public void checkSpikesTouched(Player player){
        objectManager.checkSpikesTouched(player);
    }

    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    public void setLevelCompleted(boolean levelCompleted){
        this.lvlCompleted = levelCompleted;
        if(levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver){
            if(e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {


        if(!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }else
            gameOverOverLay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }else
            gameOverOverLay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {


        if (!gameOver){
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }else
            gameOverOverLay.mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e){
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }

    }

    public void unpauseGame(){
        paused = false;
    }
    public void windowFocusLost(){
        player.resetDirBooleans();
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }
    public Player getPlayer(){
        return  player;
    }
    public LevelManager getLevelManager(){
        return levelManager;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
