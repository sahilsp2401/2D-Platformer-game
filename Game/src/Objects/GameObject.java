package Objects;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Utils.Constants.ANI_SPEED;
import static Utils.Constants.ObjectConstants.*;

public class GameObject {
    protected int x,y,objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation,active=true;
    protected int aniTick,aniIndex;
    protected int xDrawOffset,yDrawOffset;

    public GameObject(int x,int y,int objType){
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick>=ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objType)) {
                aniIndex = 0;
                if(objType==BARREL||objType==BOX) {
                    doAnimation = false;
                    active =false;
                }
            }
        }
    }

    public void reset(){
        aniIndex =0;
        aniTick = 0;
        active = true;

        if(objType==BARREL||objType==BOX)
            doAnimation = false;
        else
            doAnimation = true;

    }

    protected void initHitBox(int width,int height) {
        hitbox = new Rectangle2D.Float(x,y,(int)(width* Game.SCALE),(int) (height*Game.SCALE));
    }

    public void drawHitBox(Graphics g, int xLvlOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int)(hitbox.x-xLvlOffset),(int)hitbox.y,(int) hitbox.width,(int) hitbox.height);
    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}