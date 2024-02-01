package com.zetcode.sprite;

import com.zetcode.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends Sprite {

    private final List<Shot> SHOTS;
    private String playerImgPath;

    public Player(List<Shot> shots) {
        initPlayer();
        immortalTimer(this.getLevel());
        this.SHOTS = shots;
    }

    private void initPlayer() {
        updateImg(1);
        setX(Commons.PLAYER_INIT_X);
        setY(Commons.PLAYER_INIT_Y);
    }
    @Override
    public void updateImg(int level) {

        setHealth(level + 2);
        switch (level) {
            case 1 -> {
                this.playerImgPath = "src/images/player.png";
                this.setWidth(30);
                this.setHeight(30);
            }
            case 2 -> {
                this.playerImgPath = "src/images/player2.png";
                this.setWidth(40);
                this.setHeight(40);
                setHealth(getHealth() - 1);
            }
            case 3 -> {
                this.playerImgPath = "src/images/player3.png";
                this.setWidth(45);
                this.setHeight(45);
                setHealth(getHealth() - 1);
            }
        }
        ImageIcon originalIcon = new ImageIcon(playerImgPath);
        Image image = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        setImage(scaledIcon.getImage());
    }

    public void act() {
        x += dx;
        y += dy;

        if (x <= 2) {
            x = 2;
        }

        if (x > Commons.BOARD_WIDTH - Commons.BORDER_RIGHT) {

            x = Commons.BORDER_LEFT;
        }

        if (x < Commons.BORDER_LEFT) {
            x = Commons.BOARD_WIDTH - Commons.BORDER_RIGHT;
        }

        if (y < Commons.BORDER_TOP) {

            y = Commons.BORDER_TOP;
        }

        if (y > Commons.GROUND) {

            y = Commons.GROUND;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int playerSpeed = 1 + this.getLevel();

        if (key == KeyEvent.VK_LEFT) {
            dx = -playerSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = playerSpeed;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -playerSpeed;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = playerSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(level);
        immortalTimer(level);
        System.out.println("init Timer Çalıştır");
    }

    public void addShot(Shot shot) {
        this.SHOTS.add(shot);
    }

    public void removeShots(List<Shot> shots) {

        shots.removeIf(shot -> !shot.isVisible());
    }

    @Override
    public void setDying(boolean dying) {
        if (this.isImmortal()) return;

        super.setDying(dying);
    }
}
