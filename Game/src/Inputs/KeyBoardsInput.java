package Inputs;
import GameState.Gamestate;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class KeyBoardsInput implements KeyListener {

    private GamePanel gamePanel;

    public KeyBoardsInput(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().keyPressed(e);
            default:
                break;
        }

    }
}
