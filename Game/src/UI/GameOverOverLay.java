package UI;

import GameState.Gamestate;
import GameState.Playing;
import Levels.LevelManager;
import Main.Game;
import Utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utils.Constants.UI.URMBUTTONS.URM_SIZE;

public class GameOverOverLay {
    Playing playing;
    BufferedImage deadBg;
    private UrmButton menuB,replyB;
    private int bgX,bgY,bgW,bgH;
    public GameOverOverLay(Playing playing){
        this.playing = playing;
        loadBG();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (335 * Game.SCALE);
        int replyX = (int) (440 * Game.SCALE);
        int bY = (int) (195 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replyB = new UrmButton(replyX, bY, URM_SIZE, URM_SIZE, 1);
    }

    private void loadBG() {
        deadBg = LoadSave.GetSpriteAtLas(LoadSave.DEATH_BG);
        bgW =(int) (deadBg.getWidth() * Game.SCALE);
        bgH =(int) (deadBg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW/2;
        bgY =(int)(100*Game.SCALE);
    }

    public void update(){
        menuB.update();
        replyB.update();
    }
    public void render(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.GAME_WIDTH,Game.GAME_HEIGHT);

        g.drawImage(deadBg,bgX,bgY,bgW,bgH,null);
        menuB.render(g);
        replyB.render(g);
    }
    private boolean isIn(UrmButton b,MouseEvent e){
        return b.getBounds().contains(e.getX(),e.getY());
    }
    public void mouseMoved(MouseEvent e){
        replyB.setMouseOver(false);
        menuB.setMouseOver(false);

        if(isIn(menuB,e))
            menuB.setMouseOver(true);
        else if(isIn(replyB,e))
            replyB.setMouseOver(true);
    }
    public void mouseReleased(MouseEvent e){
        if(isIn(menuB,e)) {
            if (menuB.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        }else if(isIn(replyB,e))
            if(replyB.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        menuB.resetBools();
        replyB.resetBools();
    }
    public void mousePressed(MouseEvent e){
        if(isIn(menuB,e))
            menuB.setMousePressed(true);
        else if(isIn(replyB,e))
            replyB.setMousePressed(true);
    }
}
