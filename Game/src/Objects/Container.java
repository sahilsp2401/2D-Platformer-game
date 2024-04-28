package Objects;

import Main.Game;

import static Utils.Constants.ObjectConstants.*;

public class Container extends GameObject{
    public Container(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox() {
        if(objType== BOX){
            initHitBox(25,18);
            xDrawOffset = (int)(Game.SCALE*7);
            yDrawOffset = (int)(Game.SCALE*12);
        }else {
            initHitBox(23,25);
            xDrawOffset=(int)(Game.SCALE*8);
            yDrawOffset =(int)(Game.SCALE*5);
        }
        hitbox.y += yDrawOffset +(int)(Game.SCALE *2);
        hitbox.x += xDrawOffset/2;
    }

    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }
}
