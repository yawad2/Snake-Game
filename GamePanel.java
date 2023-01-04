package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    static final int width = 600;
    static final int height = 600;
    static final int unitSize = 25;//how big the game objects are
    static final int gameUnit= (width*height)/unitSize;
    static final int delay = 75;
    final int x[] = new int [gameUnit]; //holds all x-coordinates of the snake's body
    final int y[] = new int [gameUnit]; //holds all y-coordinates of the snake's body
    int body = 6; //snake's body will start with 6 parts
    int score;
    int appleX;
    int appleY;
    char direction = 'R'; //snake begins by moving to the right direction
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //creating a grid
        if(running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);
            for (int i = 0; i < body; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255),random.nextInt(255) ));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("InkFree", Font.BOLD,30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score,(width - metrics.stringWidth("Score: " + score))/2,g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)width/unitSize)*unitSize;
        appleY = random.nextInt((int)height/unitSize)*unitSize;

    }
    public void move (){
        for(int i= body;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
                y[0]=y[0]-unitSize;
                break;
            case 'D':
                y[0]=y[0]+unitSize;
                break;
            case 'L':
                x[0]=x[0]-unitSize;
                break;
            case 'R':
                x[0]=x[0]+unitSize;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == appleX)&&(y[0] == appleY)){
            body++;
            score++;
            newApple();
        }
    }
    public void checkCollisions(){
        //checks if head collides with body
        for(int i=body;i>0;i--){
            if((x[0] == x[i])&&(y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches left border
        if(x[0]<0){
            running = false;
        }
        //check if head touches right border
        if(x[0]>width){
            running = false;
        }
        //check if head touches top border
        if(y[0]<0){
            running = false;
        }
        //check if head touches bottom border
        if(y[0]>height){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("InkFree", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(width - metrics.stringWidth("Game Over"))/2,height/2); //writes "Game Over" in the center of the screen
        //score
        g.setColor(Color.RED);
        g.setFont(new Font("InkFree", Font.BOLD,30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score,(width - metrics2.stringWidth("Score: " + score))/2,g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();

        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
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
    }
}
