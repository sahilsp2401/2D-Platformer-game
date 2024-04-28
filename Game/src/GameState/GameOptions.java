package GameState;

import Main.Game;
import UI.AudioOptions;
import UI.PauseButton;
import UI.UrmButton;
import Utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utils.Constants.UI.URMBUTTONS.URM_SIZE;

public class GameOptions extends State implements Statemethods {
    private AudioOptions audioOptions;
    private BufferedImage bg_Img,optionsBgImg;
    private int bgX,bgY,bgW,bgH;
    private UrmButton menuB;
    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int)(387*Game.SCALE);
        int menuY = (int)(325*Game.SCALE);

        menuB = new UrmButton(menuX,menuY,URM_SIZE,URM_SIZE,2);
    }

    private void loadImgs() {
        bg_Img = LoadSave.GetSpriteAtLas(LoadSave.Main_Menu_BG);
        optionsBgImg = LoadSave.GetSpriteAtLas(LoadSave.OPTION_MENU);

        bgW = (int)(optionsBgImg.getWidth()*Game.SCALE);
        bgH = (int)(optionsBgImg.getHeight()*Game.SCALE);
        bgX = Game.GAME_WIDTH /2 - bgW/2;
        bgY = (int)(33*Game.SCALE);
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(bg_Img,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
        g.drawImage(optionsBgImg,bgX,bgY,bgW,bgH,null);

        menuB.render(g);
        audioOptions.render(g);
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    if(isIn(e,menuB))
        menuB.setMousePressed(true);
    else
        audioOptions.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e,menuB)) {
            if (menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        }
        else
              audioOptions.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        if(isIn(e,menuB))
            menuB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);

        menuB.resetBools();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    private boolean isIn(MouseEvent e, PauseButton b){
        return  b.getBounds().contains(e.getX(),e.getY());

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
