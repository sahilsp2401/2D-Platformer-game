package Levels;

import GameState.Gamestate;
import Main.Game;
import Utils.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.TILES_SIZE;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    public int lvlIndex=0;
    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex>=levels.size()){
            lvlIndex=0;
            System.out.println("No more Levels!!!");
            Gamestate.state = Gamestate.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }
    private void buildAllLevels() {
        BufferedImage [] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img:allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtLas(LoadSave.LEVEl_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int j=0;j<4;j++){
            for(int i=0;i<12;i++)
            {
                int index = j*12 +i;
                levelSprite[index] = img.getSubimage(i*32,j*32,32,32);
            }
        }
    }

    public void render(Graphics g,int lvlOffset){
        for(int j=0;j<Game.TILES_IN_HEIGHT;j++)
            for (int i=0;i<levels.get(lvlIndex).getLvlData()[0].length;i++){
                int index = levels.get(lvlIndex).getSpriteIndex(i,j);
                g.drawImage(levelSprite[index],TILES_SIZE*i-lvlOffset,TILES_SIZE*j,TILES_SIZE,TILES_SIZE,null);
            }
    }
    public void update(){

    }

    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }

    public int getLvlIndex(){
        return lvlIndex;
    }


}
