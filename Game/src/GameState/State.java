package GameState;

import Audio.AudioPlayer;
import Main.Game;
import UI.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;
    public State(Game game){
        this.game =game;
    }

    public Game getGame(){
        return game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(),e.getY());
    }

    public void setGameState(Gamestate state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }
        Gamestate.state = state;
    }
}
