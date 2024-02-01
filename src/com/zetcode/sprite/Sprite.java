package com.zetcode.sprite;

import javax.swing.*;
import java.awt.*;

public class Sprite {

    private boolean visible;
    private Image image;
    private boolean dying;
    private int level = 0;
    private int width;
    private int height;

    private Timer timer;
    private int showHideCount;
    private boolean immortal = false; // ölümsüzlük
    private int unitTime = 0;
    private int health = 2;

    int x;
    int y;
    int dx;
    int dy;

    public Sprite() {

        visible = true;
    }

    public void die() {

        visible = false;
    }

    public boolean isVisible() {

        return visible;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }


    public void setImage(Image image) {

        this.image = image;
    }

    public Image getImage() {

        return image;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public int getY() {

        return y;
    }

    public int getX() {

        return x;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setDying(boolean dying) {

        this.health--;
        if(this.health <= 0) {
            this.dying = dying;
        }
    }

    public boolean isDying() {

        return this.dying;
    }

    public void updateImg(int level) {

        setImage(this.image);
    }

    public void removeImg() {
        setImage(null);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public void immortalTimer(int level) {

        if (!immortal) {
            showHideCount = 0;
            int DELAY = 200;
            // 0.2 saniye, milisaniye cinsinden
            timer = new Timer(DELAY, e -> {
                unitTime++;

                if (showHideCount < 8) {
                    if (unitTime % 2 == 0) {
                        updateImg(level);
                    } else removeImg();

                    showHideCount++;
                } else {
                    timer.stop();
                    immortal = false;
                    unitTime = 0;
                }
            });
            timer.start();
            immortal = true;
        }
    }

    public void drawHealthBar(Graphics g) {
        g.setColor(Color.GREEN);

        int barWidth = getWidth();
        int barHeight = 2;
        int barX = getX();
        int barY = getY() - barHeight - 5;

        g.fillRect(barX, barY, barWidth, barHeight);

        g.setColor(Color.RED);

        int remainingWidth = (int) (barWidth * ((double) this.getHealth() /( this.level+1))); // varsayılan can değeri için getMaxHealth() kullanabilirsin
        g.fillRect(barX + remainingWidth, barY, barWidth - remainingWidth, barHeight);
    }

}


