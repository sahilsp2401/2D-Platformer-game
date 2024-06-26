package GameState;

import Main.Game;
import UI.MenuButton;
import Utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage bgImage,bg_Image_Ninja,ninja;
    private int menuX,menuY,menuWidth,menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBG();
    }

    private void loadBG() {
        bgImage = LoadSave.GetSpriteAtLas(LoadSave.MENU_BG);
        bg_Image_Ninja = LoadSave.GetSpriteAtLas(LoadSave.Main_Menu_BG);
        menuWidth = (int)(bgImage.getWidth()* Game.SCALE);
        menuHeight = (int)(bgImage.getHeight()*Game.SCALE);
        menuX = Game.GAME_WIDTH/2 - menuWidth/2;
        menuY = (int)(45*Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH/2,(int)(150*Game.SCALE),0,Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2,(int)(220*Game.SCALE),1,Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH/2,(int)(290*Game.SCALE),2,Gamestate.QUIT);
    }

    @Override
    public void update() {
        for(MenuButton mb : buttons){
            mb.update();
        }
    }


    @Override
    public void render(Graphics g) {
        g.drawImage(bg_Image_Ninja,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
        g.drawImage(bgImage,menuX,menuY,menuWidth,menuHeight,null);
        //g.drawImage(ninja,menuX+18,menuY-95,400,100,null);

        for(MenuButton mb : buttons){
            mb.render(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                if(mb.isMousePressed())
                    mb.applyGamestate();
                if(mb.getState()==Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton mb : buttons){
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
            mb.setMouseOver(false);
        for (MenuButton mb : buttons)
            if(isIn(e,mb)) {
                mb.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
