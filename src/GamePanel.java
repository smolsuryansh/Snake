import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class GamePanel extends JPanel implements ActionListener {

    // Assigning values
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    BufferedImage appleImage;
    boolean showLevelMessage10 = false; // Display message at certain level
    boolean showLevelMessage20 = false; // Display message at certain level
    Timer messageTimer; // Display message at a certain level


    // Creating game ui
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(48, 52, 50));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();


        // Creating an apple using an image
        try {
            appleImage = ImageIO.read(new File("C:\\Users\\cuteb\\OneDrive\\Pictures\\aaple\\apple bhai.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // To start game
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    // To restart game
    public void restartGame(){
        running = false;
        timer.stop();
        bodyParts = 5;
        applesEaten = 0;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame();
        repaint();
    }

    // Graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        // Display a message at a certain score
        if (showLevelMessage10) {
            g.setColor(new Color(97, 97, 97));
            g.setFont(new Font("Dialogue", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Level 10", (SCREEN_WIDTH - metrics.stringWidth("Level 10")) / 2, SCREEN_HEIGHT / 2);
        }
        if (showLevelMessage20) {
            g.setColor(new Color(97, 97, 97));
            g.setFont(new Font("Dialogue", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Level 20!", (SCREEN_WIDTH - metrics.stringWidth("Level 20!")) / 2, SCREEN_HEIGHT / 2);
        }
    }


    public void draw(Graphics g){

        if(running) {


//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.setColor(new Color(90,90,90));
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, 600);
//                g.drawLine(0, i * UNIT_SIZE, 600, i * UNIT_SIZE);
//            }

            if (appleImage != null) {
                g.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);
            }

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(67, 171, 101));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(58, 148, 88));
//                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); // Colorful body
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Dialogue", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    // Apples spawning at random
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    // Directions
    public void move(){

        for(int i= bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    // Creating interactive apples
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            // Display a message after a certain level
            if (applesEaten == 10) {
                showLevelMessage10 = true;
                Timer messageTimer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showLevelMessage10 = false;
                    }
                });
                messageTimer.setRepeats(false);
                messageTimer.start();
            }
            if (applesEaten == 20) {
                showLevelMessage20 = true;
                Timer messageTimer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showLevelMessage20 = false;
                    }
                });
                messageTimer.setRepeats(false);
                messageTimer.start();
            }
            newApple();
        }
    }

    // Collisions
    public void checkCollisions(){
        // Checks head collision with body
        for(int i = bodyParts; i>0; i--){
            if((x[0]==x[i] && y[0]==y[i])){
                running = false;
            }
        }
        // Checks if head touches left border
        if(x[0] < 0){
            running = false;
        }
        // Checks if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // Checks if head touches top border
        if(y[0] < 0){
            running = false;
        }
        // Checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    // Graphics
    public void gameOver (Graphics g){
        // SCORE
        g.setColor(Color.white);
        g.setFont(new Font("Dialogue", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
        // Game over text
        g.setColor(Color.white);
        g.setFont(new Font("Dialogue", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
        // Press Space to restart text
        g.setColor(Color.white);
        g.setFont(new Font("Dialogue", Font.PLAIN, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press SPACE to restart"))/2, 350);
    }

    // Continue running
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    // Keys
    public class MyKeyAdapter extends KeyAdapter {

        private boolean isSpaceKeyPressed = false;
        private boolean isGameRunning = false;

        @Override
        public void keyPressed(KeyEvent e){

            if(e.getKeyCode()==KeyEvent.VK_SPACE && !isSpaceKeyPressed){
                restartGame();
                isGameRunning = true;
                isSpaceKeyPressed = true;
            }

            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                isSpaceKeyPressed = false;
            }
        }

    }
}
