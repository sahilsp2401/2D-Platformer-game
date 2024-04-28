package UI;

import GameState.Gamestate;
import GameState.Playing;
import Main.Game;
import Utils.LoadSave;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static Utils.Constants.UI.PauseButtons.*;
import static Utils.Constants.UI.URMBUTTONS.*;
import static Utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage bgImg;
    private int bgX,bgY,bgW,bgH;

    private AudioOptions   audioOptions;
    private UrmButton menuB,replyB,unpauseB;


    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBG();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();

    }



    private void createUrmButtons() {
        int menuX = (int)(313*Game.SCALE);
        int replyX = (int)(387*Game.SCALE);
        int unpauseX = (int)(462*Game.SCALE);
        int bY = (int) (325*Game.SCALE);

        menuB = new UrmButton(menuX,bY,URM_SIZE,URM_SIZE,2);
        replyB = new UrmButton(replyX,bY,URM_SIZE,URM_SIZE,1);
        unpauseB = new UrmButton(unpauseX,bY,URM_SIZE,URM_SIZE,0);
    }



    private void loadBG() {
        bgImg = LoadSave.GetSpriteAtLas(LoadSave.PAUSE_BG);
        bgW =(int) (bgImg.getWidth() * Game.SCALE);
        bgH =(int) (bgImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW/2;
        bgY =(int) (25* Game.SCALE);
    }

    public void update(){

        menuB.update();
        replyB.update();
        unpauseB.update();
        audioOptions.update();
    }

    public void render(Graphics g){
        //BG
        g.drawImage(bgImg,bgX,bgY,bgW,bgH,null);
        //URM Buttons
        menuB.render(g);
        replyB.render(g);
        unpauseB.render(g);
        audioOptions.render(g);
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e,menuB))
            menuB.setMousePressed(true);
        else if (isIn(e,replyB))
            replyB.setMousePressed(true);
        else if (isIn(e,unpauseB))
            unpauseB.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e,menuB)) {
            if (menuB.isMousePressed()) {
                playing.setGameState(Gamestate.MENU);
                playing.resetAll();
                playing.unpauseGame();
            }
        }
        else if (isIn(e,replyB)) {
            if (replyB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }
        }
        else if (isIn(e,unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        }else
            audioOptions.mouseReleased(e);

        menuB.resetBools();
        replyB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replyB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e,menuB))
            menuB.setMouseOver(true);
        else if(isIn(e,replyB))
            replyB.setMouseOver(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);

    }
    private boolean isIn(MouseEvent e,PauseButton b){
        return  b.getBounds().contains(e.getX(),e.getY());

    }
}
