package com.zetcode;

import com.zetcode.sound.Sound;
import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Boss;
import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private List<Alien> aliens;
    private Boss boss;
    private Player player;
    private List<Shot> shots;

    private int direction = -1;

    private boolean isWin = false;
    private boolean gameDelayStart = false;
    private boolean inGame = true;

    private Timer timer;
    private int gameLevel = 1;
    private Sound sound;


    public Board() {

        gameStart();
    }

    private void initBoard() {

        sound = new Sound();
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.setInitialDelay(Commons.INITIAL_DELAY);
        timer.start();
    }


    private void gameInit() {

        sound.soundStart();
        aliens = new ArrayList<>();
        shots = new ArrayList<>();
        boss = new Boss(125, Commons.BORDER_TOP + 20);
        boss.setHealth(200);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {

                var alien = new Alien(Commons.ALIEN_INIT_X + 38 * j,
                        Commons.ALIEN_INIT_Y + 50 * i, 1);
                alien.setLevel(1);
                aliens.add(alien);
            }
        }


        player = new Player(shots);
        player.setLevel(1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    playMusic();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {
            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
                alien.drawHealthBar(g);
            }

            if (alien.isDying() && !alien.isImmortal()) {

                alien.die();
            }
        }
    }

    private void drawBoss(Graphics g) {

        if (gameLevel == 3) {

            if (boss.isVisible()) {
                g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
                boss.drawHealthBar(g);
            }

            if (boss.isDying() && !boss.isImmortal()) {

                boss.die();
                inGame = false;
                isWin = true;

            }
        }

    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            player.drawHealthBar(g);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {


        for (Shot shot : shots) {
            if (shot.isVisible()) {

                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }

        Alien.Bomb b = boss.getBomb();

        if (!b.isDestroyed()) {

            g.drawImage(b.getImage(), b.getX(), b.getY(), this);
        }
    }

    private void drawExplosion(Graphics g, int x, int y, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(Commons.EXPL_IMG_PATH);
        Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        g.drawImage(scaledIcon.getImage(), x, y, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        //ImageIcon gameStartIcon = new ImageIcon("src/images/game-start.gif");

        ImageIcon imageIcon = new ImageIcon("src/images/space-invaders-background.png");

//        if (!gameDelayStart) {
//            g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
//            //g.drawImage(gameStartIcon.getImage(), Commons.BORDER_LEFT - 10 , Commons.BORDER_TOP - 40, getWidth() - Commons.BORDER_RIGHT, Commons.GROUND, this);
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(Commons.INITIAL_DELAY - 1000);
//                        gameDelayStart = true;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
//
//        } else {
//            doDrawing(g);
//        }

        doDrawing(g);

        g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);


    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

            if (gameLevel == 3) {
                drawBoss(g);
            }

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        ImageIcon gameOverIcon = isWin ? new ImageIcon("src/images/game-win.gif"): new ImageIcon("src/images/game-over.gif");
        g.drawImage(gameOverIcon.getImage(), Commons.BORDER_LEFT - 40, Commons.BORDER_TOP - 50, getWidth() - Commons.BORDER_RIGHT, Commons.GROUND, this);
    }

    private void gameStart() {
        String imagePath = "src/images/game-start.png";
        int imgW = 590; // Resmin genişliği
        int imgH = 375; // Resmin yüksekliği

        // Resmi yükseklik ve genişlik özellikleriyle ImageIcon olarak yükle
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath)
                .getImage()
                .getScaledInstance(imgW, imgH, Image.SCALE_DEFAULT));

        // JPanel oluştur
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        // Resmi göstermek için bir JLabel oluştur
        JLabel label = new JLabel();
        label.setIcon(imageIcon);
        panel.add(label, BorderLayout.CENTER);

        // Metin eklemek için bir JLabel ekle
        JLabel textLabel = new JLabel("Atarimizin başına geçerek oyuna başlayabilirsiniz.");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Yazı tipini ve boyutunu değiştir
        textLabel.setForeground(new Color(135,79,147)); // Yazı rengini değiştir
        panel.add(textLabel, BorderLayout.NORTH);

        // Buton eklemek için bir JButton ekle
        Object[] options = {"Atarinin başına geç"};
        int option = JOptionPane.showOptionDialog(
                JOptionPane.getRootFrame(),
                panel,
                "HOŞGELDİNİZ",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        // Kullanıcı 'Atarinin başına geç' butonuna tıkladığında
        if (option == JOptionPane.OK_OPTION) {
            initBoard();
            gameInit();
            System.out.println("atariya geçildi");
        }
    }

    private void removeAliens() {
        aliens.removeIf(alien -> !alien.isVisible() && (alien.getBomb().isDestroyed())); // görünmeyen ve mermileri yok olmus olan uzaylıları sil
    }

    // Level arası geçişlerde ekrana bir messagebox gelir
//    private void changeLevelInfo(int seviye) {
//        String mesaj = "Tebrikler! Seviye " + seviye + "'e ulaştınız!";
//        JOptionPane.showMessageDialog(this, mesaj, "Seviye Atla", JOptionPane.INFORMATION_MESSAGE);
//    }


    private void updateAlien() {

        for (Alien alien : aliens) {
            int x = alien.getX();
            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {

                direction = -1;

                for (Alien a2 : aliens) {

                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {

                direction = 1;

                for (Alien a : aliens) {

                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        // aliens

        for (Alien alien : aliens) {
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Commons.GROUND - alien.getHeight()) {
                    inGame = false;
                    gameOver(getGraphics());
                }

                // level artışında uzaylıların hızını arttır
                if (direction == -1) {
                    alien.act(direction - player.getLevel() + 1);
                } else if (direction == 1) {
                    alien.act(direction + player.getLevel() - 1);
                }
            }
        }

        removeAliens();
    }


    private void updateBoss() {
        if (gameLevel != 3)
            return;

        int x = boss.getX();

        if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT - 65 && direction != -1) {

            direction = -1;
        }

        if (x <= Commons.BORDER_LEFT && direction != 1) {

            direction = 1;
        }

        if (boss.isVisible()) {
            if (direction == -1) {
                boss.act(direction - player.getLevel() - 2);
            } else if (direction == 1) {
                boss.act(direction + player.getLevel() + 2);
            }
        }

        var generator = new Random();

        int shot = generator.nextInt(10);
        Alien.Bomb bomb = boss.getBomb();

        if (shot == Commons.CHANCE && boss.isVisible() && bomb.isDestroyed()) {

            sound.soundFireball();
            bomb.setDestroyed(false);
            bomb.setX(boss.getX());
            bomb.setY(boss.getY());
        }

        int bombX = bomb.getX();
        int bombY = bomb.getY();
        int bombW = bomb.getWidth();
        int bombH = bomb.getHeight();

        int playerX = player.getX();
        int playerY = player.getY();
        int playerW = player.getWidth();
        int playerH = player.getHeight();

        if (player.isVisible() && !bomb.isDestroyed()) {

            if (bombX + bombW >= playerX &&
                    bombX <= playerX + playerW &&
                    bombY + bombH >= playerY &&
                    bombY <= playerY + playerH) {

                if (player.getHealth() > 1) {
                    sound.soundHit();
                }

                if (!player.isImmortal() && player.getHealth() <= 1) {
                    sound.soundLose();
                    ImageIcon originalIcon = new ImageIcon(Commons.EXPL_IMG_PATH);

                    Image image = originalIcon.getImage().getScaledInstance(playerW, playerH, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(image);

                    player.setImage(scaledIcon.getImage());
                }

                player.setDying(true);
                bomb.setDestroyed(true);
            }
        }

        if (!bomb.isDestroyed()) {

            bomb.setY(bomb.getY() + 4);

            if (bomb.getY() >= Commons.GROUND) {

                bomb.setDestroyed(true);
            }
        }
    }

    private void updateShot() {
        for (Shot shot : shots) {
            if (shot.isVisible()) {

                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Alien alien : aliens) {

                    int alienX = alien.getX();
                    int alienY = alien.getY();

                    if (alien.isVisible() && shot.isVisible() && !(alien.isImmortal())) {
                        if (shotX >= (alienX)
                                && shotX <= (alienX + alien.getWidth())
                                && shotY >= (alienY)
                                && shotY <= (alienY + alien.getHeight())) {

                            // 1 den büyük olmasının sebebi hit ve destroy seslerinin çakışmasını önlemek
                            if (alien.getHealth() > 1) {
                                sound.soundHit();
                            }

                            if (alien.isImmortal()) return;


                            if (alien.getHealth() <= 1) {
                                sound.soundDestroy();
                                ImageIcon originalIcon = new ImageIcon(Commons.EXPL_IMG_PATH);

                                Image image = originalIcon.getImage().getScaledInstance(alien.getWidth(), alien.getHeight(), Image.SCALE_DEFAULT);
                                ImageIcon scaledIcon = new ImageIcon(image);

                                //Bu kısım ölme durumunda uygulanacak kodları gösteriyor
                                alien.setImage(scaledIcon.getImage());
                            }
                            alien.setDying(true);

                            // eğer ölmezse de shot' ın resmi güncellenecek
                            shot.die();
                        }
                    }
                }

                // boss hasar kontrolü
                if (gameLevel == 3) {

                    int bossX = boss.getX();
                    int bossY = boss.getY();

                    if (boss.isVisible() && shot.isVisible() && !(boss.isImmortal())) {
                        if (shotX >= (bossX)
                                && shotX <= (bossX + boss.getWidth())
                                && shotY >= (bossY)
                                && shotY <= (bossY + boss.getHeight())) {


                            if (boss.getHealth() <= 1) {
                                ImageIcon originalIcon = new ImageIcon(Commons.EXPL_IMG_PATH);

                                Image image = originalIcon.getImage().getScaledInstance(boss.getWidth(), boss.getHeight(), Image.SCALE_DEFAULT);
                                ImageIcon scaledIcon = new ImageIcon(image);

                                //Bu kısım ölme durumunda uygulanacak kodları gösteriyor
                                boss.setImage(scaledIcon.getImage());

                                player.setLevel(4);
                            }
                            boss.setDying(true);

                            // eğer ölmezse de shot' ın resmi güncellenecek
                            shot.die();
                        }
                    }
                }

                if (Shot.bosssShotNumber % 60 == 0) {

                    int initY = boss.getY();
                    int alienHeight = boss.getHeight();

                    if (aliens.toArray().length > 0) {

                        for (Alien alien : aliens) {
                            if (alien.isVisible()) {
                                initY = alien.getY();
                                alienHeight = alien.getHeight();
                            }
                        }
                    }

                    for (int j = 0; j < 6; j++) {

                        Alien alien;

                        if ((Shot.bosssShotNumber / 60) % 2 == 0) {
                            alien = new Alien(Commons.ALIEN_INIT_X + 38 * j,
                                    initY + alienHeight + 20, 1);
                            alien.setLevel(1);
                            alien.updateImg(1);

                        } else {
                            alien = new Alien(Commons.ALIEN_INIT_X + 55 * j,
                                    initY + alienHeight + 20, 2);
                            alien.setLevel(2);
                            alien.updateImg(2);
                        }


                        aliens.add(alien);
                    }

                    Shot.bosssShotNumber += 3;
                }

                int y = shot.getY();
                y -= (4 + player.getLevel());

                if (y < Commons.BORDER_TOP) {
                    shot.die();
                } else {
                    shot.setY(y);
                }
            }
        }
        // görünmeyen mermileri sil
        player.removeShots(shots);

    }
    private void update() {

        if (aliens.isEmpty() && player.getLevel() == 2 && gameLevel != 2) {
            gameLevel++;
            //changeLevelInfo(2);
            LevelTwo levelTwo = new LevelTwo();
            aliens = levelTwo.generateAliens();
        }
        else if (aliens.isEmpty() && player.getLevel() == 3 && gameLevel != 3) {
            gameLevel++;
            player.setLevel(3);
            player.updateImg(3);
            //changeLevelInfo(3);
            System.out.println("Boss fight");

        }

        if (aliens.toArray().length == 0 && player.getLevel() == 4) {

            inGame = false;
            timer.stop();
            stopMusic();
            isWin = true;
            gameOver(getGraphics());
        }

        // player
        player.act();

        if (aliens.toArray().length == 0) {

            if (player.getLevel() != 2 && player.getLevel() != 3) {
                player.setLevel(2);
                player.updateImg(2);
            }
            else if (player.getLevel() == 2) {

                player.setLevel(3);
                player.updateImg(3);

            }
        }
    }

    private void updateBombs() {
        var generator = new Random();

        for (Alien alien : aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            int playerW = player.getWidth();
            int playerH = player.getHeight();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + playerW)
                        && bombY >= (playerY)
                        && bombY <= (playerY + playerH)) {

                    if (player.getHealth() > 1) {
                        sound.soundHit();
                    }

                    if (!player.isImmortal() && player.getHealth() <= 1) {
                        sound.soundLose();
                        ImageIcon originalIcon = new ImageIcon(Commons.EXPL_IMG_PATH);

                        Image image = originalIcon.getImage().getScaledInstance(playerW, playerH, Image.SCALE_DEFAULT);
                        ImageIcon scaledIcon = new ImageIcon(image);

                        player.setImage(scaledIcon.getImage());
                    }

                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Commons.GROUND + playerH + 20) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void updateCollision() {
        if (!player.isImmortal()) {
            for (Alien alien : aliens) {
                if (alien.isVisible() && !alien.isImmortal()) {
                    int alienX = alien.getX();
                    int alienY = alien.getY();
                    int playerY = player.getY();
                    int playerX = player.getX();

                    if ((player.getX() >= (alienX)
                            && playerX <= (alienX + alien.getWidth())
                            && playerY >= (alienY)
                            && playerY <= (alienY + alien.getHeight()))) {

                        ImageIcon originalIcon = new ImageIcon(Commons.EXPL_IMG_PATH);

                        Image image = originalIcon.getImage().getScaledInstance(alien.getWidth(), alien.getHeight(), Image.SCALE_DEFAULT);
                        ImageIcon scaledIcon = new ImageIcon(image);

                        alien.setImage(scaledIcon.getImage());
                        if (!player.isImmortal()) {
                            player.setImage(scaledIcon.getImage());
                        }

                        player.setDying(true);
                        drawExplosion(getGraphics(), alienX, alienY, alien.getWidth(), alien.getHeight());
                        drawExplosion(getGraphics(), playerX, playerY, player.getWidth(), player.getHeight());

                        gameOver(getGraphics());

                        sound.soundDestroy();
                    }
                }
            }
        }
    }

    private void doGameCycle() {
        updateBoss();
        updateAlien();
        updateShot();
        updateBombs();
        updateCollision();
        update();
        repaint();
    }

    public void playMusic() {
        sound.playMusic();
        sound.loopMusic();
    }

    public void stopMusic() {
        sound.stopMusic();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);

            if (!inGame || e.getKeyCode() != KeyEvent.VK_SPACE) {
                return;
            }

            sound.soundShoot();

            int x = player.getX();
            int y = player.getY();
            int playerW = player.getWidth();
            int playerH = player.getHeight();

            int level = player.getLevel();

            for (int i = 0; i < level; i++) {
                Shot shot = new Shot(x, y, playerW, playerH, level);
                player.addShot(shot);
            }
        }
    }
}
