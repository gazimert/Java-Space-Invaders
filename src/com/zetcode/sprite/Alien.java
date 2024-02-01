package com.zetcode.sprite;

import javax.swing.*;
import java.awt.*;

public class Alien extends Sprite {
    private String alienImgPath;
    private Bomb bomb;

    public Alien(int x, int y, int level) {

        immortalTimer(this.getLevel());
        initAlien(x, y, level);
    }

    private void initAlien(int x, int y, int level) {

        this.x = x;
        this.y = y;


        bomb = new Bomb(x, y, level);

        updateImg(1);
    }

    @Override
    public void updateImg(int level) {
        switch (level) {
            case 1 -> {
                this.alienImgPath = "src/images/alien.png";
                this.setWidth(30);
                this.setHeight(30);
            }
            case 2 -> {
                this.alienImgPath = "src/images/alien2.png";
                this.setWidth(40);
                this.setHeight(40);
                setHealth(3);
            }
        }
        ImageIcon originalIcon = new ImageIcon(alienImgPath);
        Image image = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        setImage(scaledIcon.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    @Override
    public void setDying(boolean dying) {
        if (this.isImmortal()) return;

        super.setDying(dying);
    }

    public Bomb getBomb() {

        return bomb;
    }

    public static class Bomb extends Sprite {
        private String bombImg = "src/images/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y, int level) {

            initBomb(x, y, level);
        }

        private void initBomb(int x, int y, int level) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            updateImg(level);
        }

        @Override
        public void updateImg(int level) {
            switch (level) {
                case 1 -> {
                    this.bombImg = "src/images/shot2.png";
                    this.setWidth(1);
                    this.setHeight(5);
                }
                case 2 -> {
                    this.bombImg = "src/images/shot.png";
                    this.setWidth(1);
                    this.setHeight(7);
                }
                case 3 -> {
                    this.bombImg = "src/images/bossBomb.gif";
                    this.setWidth(25);
                    this.setHeight(84);
                }
            }
            ImageIcon originalIcon = new ImageIcon(bombImg);
            Image image = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
            ImageIcon scaledIcon = new ImageIcon(image);
            setImage(scaledIcon.getImage());
        }


        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
