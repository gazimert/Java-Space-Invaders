package com.zetcode.sprite;

import javax.swing.*;
import java.awt.*;

public class Boss extends Alien{

    public Boss(int x, int y) {

        super(x, y, 3);
        initBoss(x,y);
    }

    private void initBoss(int x, int y) {

        this.x = x;
        this.y = y;

        updateImg(3);
    }

    @Override
    public void updateImg(int level) {

        String alienImgPath = "src/images/alien2.png";
        this.setWidth(100);
        this.setHeight(100);

        ImageIcon originalIcon = new ImageIcon(alienImgPath);
        Image image = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        setImage(scaledIcon.getImage());
    }


    @Override
    public void drawHealthBar(Graphics g) {
            g.setColor(Color.GREEN);

            int barWidth = getWidth();
            int barHeight = 2;
            int barX = getX();
            int barY = getY() - barHeight - 5;

            g.fillRect(barX, barY, barWidth, barHeight);

            g.setColor(Color.RED);

            int remainingWidth = (int) (barWidth * ((double) this.getHealth() /200)); // varsayılan can değeri için getMaxHealth() kullanabilirsin
            g.fillRect(barX + remainingWidth, barY, barWidth - remainingWidth, barHeight);
    }

    @Override
    public void setDying(boolean dying) {

        super.setDying(dying);
    }
}
