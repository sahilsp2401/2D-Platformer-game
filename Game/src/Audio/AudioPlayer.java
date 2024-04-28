package Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {

    public static int MENU = 0;
    public static int LEVEL1 = 1;
    public static int LEVEL2 = 2;
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int ATTACK = 3;
    public static int LVLCOMPLETE = 4;

    private Clip[] songs,effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute,effectMute;

    public AudioPlayer(){
        loadSongs();
        loadEffects();
        playSong(MENU);
    }

    private void loadSongs(){
        String[] names = {"menu","level1","level2"};
        songs = new Clip[names.length];
        for(int i=0;i<songs.length;i++)
            songs[i] = getClip(names[i]);
    }
    private void loadEffects(){
        String[] names = {"die","jump","gameover","attack","lvlcompleted"};
        effects = new Clip[names.length];
        for(int i=0;i<effects.length;i++)
            effects[i] = getClip(names[i]);

        updateEffectsVolume();
    }
    private Clip getClip(String name){
        URL url = getClass().getResource("/audio/"+name+".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);

            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }
    public void stopSong(){
        if(songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }
    public void setLevelSong(int lvlIndex){
        if(lvlIndex%2==0)
            playSong(LEVEL1);
        else
            playSong(LEVEL2);
    }

    public void lvlCompleted(){
        stopSong();
        playEffect(LVLCOMPLETE);
    }
    public void playEffect(int effect){
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }
    public void playSong(int song){
        stopSong();
        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleSongMute(){
        this.songMute = !songMute;
        for(Clip c : songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectsMute(){
        this.effectMute = !effectMute;
        for (Clip c:effects){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if(!effectMute)
            playEffect(JUMP);
    }

    private void updateSongVolume(){
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range*volume)+gainControl.getMinimum();
        gainControl.setValue(gain);
    }
    private void updateEffectsVolume(){
        for(Clip c:effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
