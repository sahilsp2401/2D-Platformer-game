package Objects;

import GameState.Playing;
import Levels.Level;
import Utils.LoadSave;
import entities.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utils.Constants.ObjectConstants.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs,containerImgs;
    private BufferedImage spikeImgs;
    private ArrayList<Potion> potions;
    private ArrayList<Container> containers;
    private ArrayList<Spike> spikes;
    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Player player){
        for(Spike s:spikes)
            if(s.getHitbox().intersects(player.getHitbox()))
                player.kill();

    }

    public void checkObjectTouched(Rectangle2D.Float hitbox){
        for(Potion p : potions)
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }
    public void applyEffectToPlayer(Potion p){
        if(p.getObjType()==RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }
    public void checkObjectHit(Rectangle2D.Float attackbox){
        for(Container c : containers)
            if(c.isActive()&&!c.doAnimation){
                if(c.getHitbox().intersects(attackbox)){
                    c.setAnimation(true);
                    int type =0;
                    if(c.getObjType()==BARREL)
                        type = 1;

                    potions.add(new Potion((int) (c.getHitbox().x+c.getHitbox().width/2),
                            (int)(c.getHitbox().y-c.getHitbox().height/2),
                            type));
                    return;
                }
            }
    }


    public void loadObjects(Level newLevel){
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtLas(LoadSave.POTION_ATLAS);
        potionImgs =  new BufferedImage[2][7];
        for(int j=0;j<potionImgs.length;j++)
            for (int i=0;i<potionImgs[j].length;i++)
                potionImgs[j][i] = potionSprite.getSubimage(12*i,16*j,12,16);

       BufferedImage containerSprite = LoadSave.GetSpriteAtLas(LoadSave.OBJECTS_ATLAS);
        containerImgs =  new BufferedImage[2][8];
        for(int j=0;j<containerImgs.length;j++)
            for (int i=0;i<containerImgs[j].length;i++)
                containerImgs[j][i] = containerSprite.getSubimage(40*i,30*j,40,30);

        spikeImgs = LoadSave.GetSpriteAtLas(LoadSave.TRAP_ATLAS);
    }

    public void update(){
        for(Potion p : potions)
            if(p.isActive())
                p.update();

        for(Container c : containers)
            if(c.isActive())
                c.update();
    }

    public void render(Graphics g,int xLvlOffset){
        drawPotions(g,xLvlOffset);
        drawContainers(g,xLvlOffset);
        drawTraps(g,xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s:spikes)
            g.drawImage(spikeImgs,(int)(s.getHitbox().x-xLvlOffset),
                    (int)(s.getHitbox().y-s.getyDrawOffset()),SPIKE_WIDTH,SPIKE_HEIGHT,null);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for(Container c : containers)
            if(c.isActive()){
                int type=0;
                if(c.getObjType()==BARREL)
                    type = 1;

                g.drawImage(containerImgs[type][c.getAniIndex()],
                        (int)(c.getHitbox().x-c.getxDrawOffset()-xLvlOffset),
                        (int)(c.getHitbox().y-c.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p : potions)
            if(p.isActive()){
                int type=0;
                if(p.getObjType()==RED_POTION)
                    type =1;

                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int)(p.getHitbox().x-p.getxDrawOffset()-xLvlOffset),
                        (int)(p.getHitbox().y-p.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,null);
            }
    }

    public void resetAllObjects(){

        loadObjects(playing.getLevelManager().getCurrentLevel());

        for(Potion p : potions)
            p.reset();
        for(Container c : containers)
            c.reset();
    }
}
