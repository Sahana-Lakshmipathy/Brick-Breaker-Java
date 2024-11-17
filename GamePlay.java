package BrickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    // Constants for game settings
    private static final int INITIAL_PLAYER_X = 310;
    private static final int INITIAL_BALL_X = 120;
    private static final int INITIAL_BALL_Y = 350;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 8;
    private static final int BORDER_THICKNESS = 3;
    private static final int PADDLE_Y_POSITION = 550;
    private static final int GAME_WIDTH = 700; // 692
    private static final int GAME_HEIGHT = 595;
    private static final int PLAYER_MOVE_DISTANCE = 20;
    private static final int MAX_PLAYER_X = 550;
    private static final int MIN_PLAYER_X = 10;

    // Game state variables
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    // Timer and delay for ball movement
    private Timer timer;
    private int delay = 8;

    // Player and ball position
    private int playerX = INITIAL_PLAYER_X;
    private int ballposX = INITIAL_BALL_X;
    private int ballposY = INITIAL_BALL_Y;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    // Constructor
    public GamePlay() {
        initGame();
    }

    // Initializes game settings
    private void initGame() {
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    // Paints the game elements
    @Override
    public void paint(Graphics g) {

        drawBackground(g);
        map.draw((Graphics2D)g);
        drawBorders(g);
        drawPaddle(g);
        drawBall(g);
        drawScores(g);


        if (totalBricks <= 0){
            gameWin(g);
        }
        if (ballposY > 570){
            gameOver(g);
        }
    }
    // Handle the success situation
    private void gameWin(Graphics g){
        play = false;
        ballXdir = 0;
        ballYdir = 0;
        g.setColor(Color.RED);
        g.setFont(new Font("serif", Font.BOLD,35));
        g.drawString("Game Over!! You win! Score:" + score, 130, 300);
        g.setColor(Color.GREEN);
        g.setFont(new Font("serif", Font.BOLD,25));
        g.drawString("Press enter to restart!", 235, 350);
    }



    // Handle the GameOver Situation
    private void gameOver(Graphics g){
        play = false;
        ballXdir = 0;
        ballYdir = 0;
        g.setColor(Color.RED);
        g.setFont(new Font("serif", Font.BOLD,35));
        g.drawString("Game Over!!  Score:" + score, 170, 300);
        g.setColor(Color.GREEN);
        g.setFont(new Font("serif", Font.BOLD,25));
        g.drawString("Press enter to restart!", 235, 350);
    }

    private void drawScores(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 570,30);
    }


    // Draws the game background
    private void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, GAME_WIDTH, GAME_HEIGHT);
    }

    // Draws the game borders
    private void drawBorders(Graphics g) {
        g.setColor(Color.YELLOW);
        /*
        * g.fillRect(0,0,3,592);
        * g.fillRect(0,0,692, 3);
        * g.fillRect(691,0,3,592);
        * */
        // Left border
        g.fillRect(0, 0, BORDER_THICKNESS, GAME_HEIGHT);

        // Top border
        g.fillRect(0, 0, GAME_WIDTH, BORDER_THICKNESS);

        // Right border
        g.fillRect(GAME_WIDTH - BORDER_THICKNESS, 0, BORDER_THICKNESS, GAME_HEIGHT);

    }

    // Draws the paddle
    private void drawPaddle(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(playerX, PADDLE_Y_POSITION, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    // Draws the ball
    private void drawBall(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(ballposX, ballposY, BALL_SIZE, BALL_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {

            if (new Rectangle(ballposX, ballposY,BALL_SIZE,BALL_SIZE).intersects(playerX, PADDLE_Y_POSITION, PADDLE_WIDTH, PADDLE_HEIGHT)){
                ballYdir = -ballYdir;
            }

            A: for(int i = 0; i < map.bricks.length;i++){
                for(int j = 0; j < map.bricks[0].length; j++){
                    if(map.bricks[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX,ballposY,BALL_SIZE,BALL_SIZE);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks -= 1;
                            score += 15;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width ){
                                ballXdir = - ballXdir;
                            } else {
                                ballYdir = - ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;

            // Ball collision with walls
            if (ballposX <= 0 || ballposX + BALL_SIZE >= GAME_WIDTH) {
                ballXdir = -ballXdir; // Horizontal bounce
            }
            if (ballposY <= 0) {
                ballYdir = -ballYdir; // Vertical bounce
            }
        }
        repaint();
    }

    // Handles key press events
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                movePaddleRight();
                break;
            case KeyEvent.VK_LEFT:
                movePaddleLeft();
                break;
            case KeyEvent.VK_ENTER:
                if (!play){
                    restartGame();
                }
        }
    }

    //Restart the game
    private void restartGame(){
        play = true;
        ballposX = INITIAL_BALL_X;
        ballposY = INITIAL_BALL_Y;
        ballXdir = -1;
        ballYdir = -2;
        playerX = INITIAL_PLAYER_X;
        score = 0;
        totalBricks = 21;
        map = new MapGenerator(3,7);
        repaint();
    }

    // Moves the paddle right
    private void movePaddleRight() {
        if (playerX < MAX_PLAYER_X) {
            playerX += PLAYER_MOVE_DISTANCE;
        }
        play = true;
    }

    // Moves the paddle left
    private void movePaddleLeft() {
        if (playerX > MIN_PLAYER_X) {
            playerX -= PLAYER_MOVE_DISTANCE;
        }
        play = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for this game
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this game
    }


}
