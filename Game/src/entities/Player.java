package entities;

import Audio.AudioPlayer;
import GameState.Playing;
import Main.Game;
import Utils.LoadSave;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static Utils.HelpMethods.*;
import static Utils.Constants.PlayerConstants.*;
import static Utils.Constants.*;
import static Utils.HelpMethods.GetEntityXPosNextToWall;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private boolean left,right,jump;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 5 * Game.SCALE;
    private boolean moving =false,idle=true,attacking = false;

    //Jumping/Gravity
    private float jumpSpeed = -2.75f *Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    //Status_barUI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int)(192*Game.SCALE);
    private int statusBarHeight = (int)(58*Game.SCALE);
    private int statusBarX = (int)(10*Game.SCALE);
    private int statusBarY = (int)(10*Game.SCALE);

    private int healthBarWidth = (int)(150*Game.SCALE);
    private int healthBarHeight = (int)(4*Game.SCALE);
    private int healthBarXStart = (int)(34*Game.SCALE);
    private int healthBarYStart = (int)(14*Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    private Playing playing;
    private boolean attackChecked;
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = Game.SCALE * 0.75f;
        loadAnimations();
        initHitBox(20,28);
        initAttackBox();
    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(25*Game.SCALE),(int)(20*Game.SCALE));
        resetAttackBox();
    }

    public void update(){
        updateHealthBar();
        if(currentHealth<=0) {
            if(state != DEAD){
                state = DEAD;
                aniTick = 0;
                aniIndex =0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            }else if(aniIndex==GetSpriteAmount(DEAD)-1 &&aniTick>=ANI_SPEED-1){
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            }else
                updateAnimation();

            return;
        }
        updateAttackBox();

        updatePos();
        if(moving||idle) {
            checkPotionTouched();
            checkSpikesTouched();
        }
        if(attacking)
            checkAttack();
        updateAnimation();
        setAnimation();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex!=1)
            return;
        attackChecked = true;

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ATTACK);
    }

    public void render(Graphics g,int lvlOffset){
        g.drawImage(animations[state][aniIndex],
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int)(hitbox.y-yDrawOffset),
                width*flipW,height,null);
        //drawHitBox(g,lvlOffset);
        //drawAttackBox(g,lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        //BackGround UI
        g.drawImage(statusBarImg,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        //Health UI
        g.setColor(Color.red);
        g.fillRect(healthBarXStart+statusBarX,healthBarYStart+statusBarY,healthWidth,healthBarHeight);
        //
        g.setColor(Color.yellow);
        g.fillRect(((int)(44*Game.SCALE))+statusBarX,((int)(34*Game.SCALE))+statusBarY,(int)(104*Game.SCALE),(int)(2*Game.SCALE));
    }

    private void updateAttackBox() {
        if(right&&left){
            if(flipW==1)
                attackBox.x = hitbox.x + hitbox.width+(int)(Game.SCALE*3)-15;
            else
                attackBox.x = hitbox.x - hitbox.width-(int)(Game.SCALE*3)+8;
        }else if(right){
            attackBox.x = hitbox.x + hitbox.width+(int)(Game.SCALE*3)-15;
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width-(int)(Game.SCALE*3)+8;
        }
        attackBox.y = hitbox.y +(Game.SCALE *10);
    }
    private void updateHealthBar() {
        healthWidth = (int)((currentHealth/(float)maxHealth) * healthBarWidth);
    }
    private void updateAnimation() {
        aniTick++;
        if(aniTick>=ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex>=GetSpriteAmount(state)){
                aniIndex =0;
                attacking =false;
                attackChecked = false;
            }
        }
    }
    private void setAnimation() {
        int startAni = state;
        if(moving)
            state = RUNNING;
        else
            state = IDLE;

        if(inAir){
            if(airSpeed<0)
                state = JUMP;
            else
                state = FALLING;
        }
        if(attacking){
            state = ATTACK;
        }
        if(startAni != state)
            resetAniTick();
    }

    private void resetAniTick() {
        aniTick =0;
        aniIndex=0;
    }

    private void updatePos() {

        moving = false;
        if(jump)
            jump();
        if(!inAir)
            if((right&&left)||(!left&&!right))
                    return;

        float xspeed=0;

        if(left) {
            xspeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xspeed += walkSpeed;
            flipX =0;
            flipW =1;
        }

        if(!inAir)
            if(!IsEntityOnFloor(hitbox,lvlData))
                inAir = true;
        if(inAir){
            if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXpos(xspeed);
            }else {
                hitbox.y = GetEntityYPosUnderRooforAboveFloor(hitbox,airSpeed);
                if(airSpeed>0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXpos(xspeed);
            }
        }else
            updateXpos(xspeed);

        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXpos(float xspeed) {
        if(CanMoveHere(hitbox.x+xspeed,hitbox.y,hitbox.width, hitbox.height, lvlData)){
            hitbox.x+=xspeed;
        }else{
            hitbox.x = GetEntityXPosNextToWall(hitbox,xspeed);
        }

    }

    public void changeHealth(int value){
        currentHealth += value;
        if(currentHealth<=0){
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth>=maxHealth)
            currentHealth = maxHealth;
    }

    public void kill(){
        currentHealth = 0;
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtLas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[8][6];
            for(int j=0;j<animations.length;j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = img.getSubimage( i*131, j*80, 128, 80);
                }
            }
            statusBarImg = LoadSave.GetSpriteAtLas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox,lvlData)){
            inAir =true;
        }
    }
    public void resetDirBooleans(){
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }
    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }

    public void resetAll(){
        resetDirBooleans();
        inAir =false;
        attacking =false;
        moving =false;
        airSpeed=0f;
        state = IDLE;
        currentHealth = maxHealth;
        flipW =1;
        flipX = 0;

        hitbox.x = x;
        hitbox.y = y;
        resetAttackBox();

        if (!IsEntityOnFloor(hitbox,lvlData)){
            inAir =true;
        }
    }

    private void resetAttackBox(){
        if(flipW==1)
            attackBox.x = hitbox.x + hitbox.width+(int)(Game.SCALE*3)-15;
        else
            attackBox.x = hitbox.x - hitbox.width-(int)(Game.SCALE*3)+8;
    }

    public void changePower(int bluePotionValue) {
        System.out.println("Add Power!!!");
    }
}
