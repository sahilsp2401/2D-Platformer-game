package Levels;

import Main.Game;
import Objects.Potion;
import Objects.Spike;
import Utils.HelpMethods;
import entities.Crabby;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utils.HelpMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<Objects.Container> containers;
    private Point playerSpawn;
    private int lvlTilesWide,maxTilesOffset,maxLvlOffsetX;
    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        calLvlOffset();
        calPlayerSpawn();
    }

    private void createSpikes() {
        spikes = HelpMethods.GetSpikes(img);
    }

    private void createContainers() {
        containers = HelpMethods.GetContainers(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    private void calPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE*maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLvlData(){
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }
    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }
    public  Point getPlayerSpawn(){
        return  playerSpawn;
    }

    public ArrayList<Potion>getPotions(){
        return potions;
    }
    public ArrayList<Objects.Container>getContainers(){
        return containers;
    }
    public ArrayList<Spike>getSpikes(){
        return spikes;
    }
}
