package com.zetcode.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound {

    private Player player;

    private final String shoot;
    private final String hit;
    private final String destroy;
    private final String lose;
    private final String start;
    private final String fireball;
    private final String music;

    public Sound() {
        this.shoot = "/com/zetcode/sound/shoot.mp3";
        this.hit = "/com/zetcode/sound/hit.mp3";
        this.destroy = "/com/zetcode/sound/destroy.mp3";
        this.lose = "/com/zetcode/sound/wrong-buzzer.mp3";
        this.start = "/com/zetcode/sound/game-start.mp3";
        this.fireball = "/com/zetcode/sound/fireball-1.mp3";
        this.music = "/com/zetcode/sound/background-music2.mp3";
    }

    public void playMusic(){
        play(music);
    }

    public void loopMusic(){
        try {
            InputStream stream = getClass().getResourceAsStream(music);
            player = new Player(new BufferedInputStream(stream));
            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        if (player != null) {
            player.close();
        }
    }

    public void soundShoot(){
        play(shoot);
    }

    public void soundHit(){
        play(hit);
    }

    public void soundDestroy(){
        play(destroy);
    }

    public void soundLose(){
        play(lose);
    }

    public void soundStart(){
        play(start);
    }

    public void soundFireball(){
        play(fireball);
    }

    private void play(String filename){
        try {
            InputStream stream = getClass().getResourceAsStream(filename);
            player = new Player(new BufferedInputStream(stream));
            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
