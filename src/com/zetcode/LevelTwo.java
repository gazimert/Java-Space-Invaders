package com.zetcode;

import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;

import java.util.ArrayList;
import java.util.List;

public class LevelTwo {
    private Player player;
    private List<Alien> aliens;
    private List<Shot> shots;

    public LevelTwo() {

    }

    public List<Alien> generateAliens(){
        aliens = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {

                var alien = new Alien(Commons.ALIEN_INIT_X + 48 * j,
                        Commons.ALIEN_INIT_Y + 55 * i, 2);
                alien.setLevel(2);
                alien.updateImg(alien.getLevel());
                aliens.add(alien);
            }
        }

        return aliens;
    }

    public void run(){

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public void setAliens(List<Alien> aliens) {
        this.aliens = aliens;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }
}
