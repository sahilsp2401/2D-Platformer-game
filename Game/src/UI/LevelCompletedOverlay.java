package UI;

import GameState.Gamestate;
import GameState.Playing;
import Main.Game;
import Utils.LoadSave;
import static Utils.Constants.UI.URMBUTTONS.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelCompletedOverlay {

    private Playing playing;
    private UrmButton menu,next;
    private BufferedImage img;
    private int bgX,bgY,bgW,bgH;
    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int)(330*Game.SCALE);
        int nextX = (int)(445*Game.SCALE);
        int y = (int)(195*Game.SCALE);
        next = new UrmButton(nextX,y,URM_SIZE,URM_SIZE,0);
        menu = new UrmButton(menuX,y,URM_SIZE,URM_SIZE,2);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtLas(LoadSave.COMPLETED_IMG);
        bgW = (int)(img.getWidth()* Game.SCALE);
        bgH = (int)(img.getHeight()* Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgW/2;
        bgY = (int) (75*Game.SCALE);
    }

    public void update(){
        menu.update();
        next.update();
    }
    public void render(Graphics g){
        g.drawImage(img,bgX,bgY,bgW,bgH,null);
        next.render(g);
        menu.render(g);
    }

    private boolean isIn(UrmButton b,MouseEvent e){
        return b.getBounds().contains(e.getX(),e.getY());
    }
    public void mouseMoved(MouseEvent e){
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(menu,e))
            menu.setMouseOver(true);
        else if(isIn(next,e))
            next.setMouseOver(true);
    }
    public void mouseReleased(MouseEvent e){
        if(isIn(menu,e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        }else if(isIn(next,e))
            if(next.isMousePressed()) {
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        menu.resetBools();
        next.resetBools();
    }
    public void mousePressed(MouseEvent e){
        if(isIn(menu,e))
            menu.setMousePressed(true);
        else if(isIn(next,e))
            next.setMousePressed(true);
    }
}
