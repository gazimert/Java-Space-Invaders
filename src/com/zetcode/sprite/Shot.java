package com.zetcode.sprite;

import javax.swing.*;
import java.awt.*;

public class Shot extends Sprite {
    public static int shotNumber = 0;
    public static int bosssShotNumber = 3;
    private final int PLAYER_HEIGHT;
    private final int PLAYER_WIDTH;
    private final int LEVEL;

    public Shot(int x, int y, int playerWidth, int playerHeight, int level) {
        this.LEVEL = level;
        this.PLAYER_WIDTH = playerWidth;
        this.PLAYER_HEIGHT = playerHeight;
        initShot(x, y);

        if(level != 3) shotNumber++;
        else bosssShotNumber ++;
    }

    private void initShot(int x, int y) {
        String shotImg = "src/images/shot.png";
        setWidth(1);
        setHeight(5);
        switch (this.LEVEL) {
            case 1:
                int W_SPACE = PLAYER_WIDTH / 2;
                int H_SPACE = 2;
                setX(x + W_SPACE);
                setY(y + H_SPACE);

                break;
            case 2:
                W_SPACE = shotNumber % 2 == 0 ? 3 : (PLAYER_WIDTH -3);
                setX(x + W_SPACE);
                H_SPACE = PLAYER_HEIGHT / 2;
                setY(y + H_SPACE);
                setWidth(1);
                setHeight(8);
                shotImg = "src/images/shot3.png";

                break;
            case 3:

                switch (bosssShotNumber % 3) {
                    case 0 -> {
                        setX(x);
                        setY(y + (PLAYER_HEIGHT / 2) + 3);
                    }
                    case 1 -> {
                        setX(x + PLAYER_WIDTH / 2);
                        setY(y);
                    }
                    case 2 -> {
                        setX(x + (PLAYER_WIDTH - 2));
                        setY(y + (PLAYER_HEIGHT / 2) + 3);
                    }
                }
                setWidth(2);
                setHeight(8);
                shotImg = "src/images/shot_3.gif";
                break;
        }

        ImageIcon originalIcon = new ImageIcon(shotImg);
        Image image = originalIcon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        setImage(scaledIcon.getImage());

    }
}
