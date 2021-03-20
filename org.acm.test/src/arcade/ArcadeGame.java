package arcade;/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 *
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import acm.util.SoundClip;
import java.awt.*;
import java.awt.event.*;

public class ArcadeGame extends GraphicsProgram {
    // Hello
    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    /**
     * Dimensions of game board (usually the same)
     */
    private static final int WIDTH = APPLICATION_WIDTH;
    //    private static final int HEIGHT = APPLICATION_HEIGHT;
    private SoundClip jump;
    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

//    /** Offset of the paddle up from the bottom */
//    private static final int PADDLE_Y_OFFSET = 30;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    //    /** Number of rows of bricks */
    private static final int NBRICK_ROWS = 10;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;
    private static final int PAUSE = 5;

    /** Offset of the top brick row from the top */
    private static final int BRICK_Y_OFFSET = 70;
    /**
     * Number of turns
     */
    private static int time = 3;
    private static int N_TURNS = 2;
    private int lives = N_TURNS;
    private int countBricks;
    private int numberOfBricks;
    private static int counter = 3;
    private double vx, vy;
    private GRect rocket;
    private GOval ball;
    private static final int DELAY = 5000;
    private GLabel cred;
    private static final int Y_SPEED = 8;

    /* Method: run() */

    /**
     * Runs the Breakout program.
     */
    SoundClip game1;
    public void run() {

        this.setSize(APPLICATION_WIDTH + 100, APPLICATION_HEIGHT);
        addKeyListeners();
        menu();
        setupTheGame();
        creatingPaddle();
        GLabel label1;
        GLabel label2;
        game1 = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\end.wav");
        while (game) {
            game1.setVolume(0.25);
            game1.loop();
            if (ball != null) {
                ball.move(vx, vy);
            }
            movePowerUp();
            checkCollision();
            hitTheBrick();
            hitTheRocket();
            checkForWinner();
            label2 = new GLabel("Current life: " + N_TURNS);
            label2.setFont("Ancient Modern Tales-26");
            label2.setColor(Color.red);
            add(label2, 50, 50);

            label1 = new GLabel("Points: " + 3 * countBricks);
            label1.setFont("Ancient Modern Tales-26");
            label1.setColor(Color.red);
            add(label1, 250, 50);
            pause(PAUSE);
            remove(label1);
            remove(label2);
        }
        removeAll();


    }

    public static void main(String[] args) {

        new ArcadeGame().start(args);

    }

    private void checkForWinner() {
        GLabel label2;
        if (countBricks >= numberOfBricks) {
            game = false;
            game1.stop();
            counter++;
            GImage win = new GImage("win.gif");
            win.scale(0.5, 0.9);
            add(win);
            label2 = new GLabel("You win.Total points: " + 3 * countBricks);
            label2.setFont("Ancient Modern Tales-26");
            label2.setColor(Color.blue);
            add(label2, getWidth() / 2.0 - 100, 50);
            SoundClip win1 = new SoundClip("arcade/win.wav");
            win1.setVolume(0.8);
            win1.play();
            pause(DELAY);
            removeAll();
            GImage image = new GImage("bluemoon.png");
            image.scale(0.3, 0.6);
            add(image);
            while (time > 0) {
                beginning = new GLabel("Congratulation! ");
                beginning.setFont("Ancient Modern Tales-36");
                beginning.setColor(Color.red);
                add(beginning, getWidth() / 2.0 - 100, getHeight() / 2.0 - 75);
                label2 = new GLabel("Next level with");
                label2.setFont("Ancient Modern Tales-36");
                label2.setColor(Color.red);
                add(label2, getWidth() / 2.0 - 100, getHeight() / 2.0 - 25);
                GLabel label3 = new GLabel("extra ball speed in: " + time);
                label3.setFont("Ancient Modern Tales-36");
                label3.setColor(Color.red);
                add(label3, getWidth() / 2.0 - 100, getHeight() / 2.0 + 25);
                time -= 1;
                pause(1000);
                remove(label3);
            }
            removeAll();
            this.setSize(APPLICATION_WIDTH + 100, APPLICATION_HEIGHT);
            menu = true;
            ball = null;
            menu();
            ball = null;
            time = 3;
            game = true;
            this.setSize(APPLICATION_WIDTH + 15, APPLICATION_HEIGHT);
            GImage back = new GImage("back.png");
            back.scale(1, 1);
            add(back);
            ball = null;
            rocket = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
            rocket.setFilled(true);
            rocket.setColor(Color.blue);
            add(rocket, 160, 500);
            vx = r_gen.nextDouble(1.0, counter);
            if (r_gen.nextBoolean(0.5)) vx = -vx;
            vy = counter;
            creatingPaddle();

        }
        if (countBricks < numberOfBricks && N_TURNS == 0) {
            game1.stop();
            removeAll();
            counter = 3;
            this.setSize(875, 600);
            GImage lose = new GImage("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\lose.gif");
            lose.scale(1.8, 2);
            add(lose);
            label2 = new GLabel("You lost. Total points: " + 3 * countBricks);
            label2.setFont("Ancient Modern Tales-36");
            label2.setColor(Color.red);
            add(label2, getWidth() / 2.0 - 125, 50);
            SoundClip lost = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\lost.wav");
            lost.setVolume(0.35);
            lost.play();
            game1 = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\end.wav");
            menu = true;
            ball = null;
            N_TURNS = lives;
            pause(DELAY);
            this.setSize(APPLICATION_WIDTH + 100, APPLICATION_HEIGHT);
            lost.stop();
            menu();
            removeAll();
            N_TURNS = lives;
            ball = null;
            setupTheGame();
            creatingPaddle();

        }
    }


    public void credits() {
        GLabel beginning1 = new GLabel("This game was created");
        beginning1.setFont("Ancient Modern Tales-36");
        beginning1.setColor(Color.red);
        add(beginning1, getWidth() / 2.0 - 125, getHeight());
        GLabel beginning2 = new GLabel("by Miracle and Smfun12.");
        beginning2.setFont("Ancient Modern Tales-30");
        beginning2.setColor(Color.red);
        add(beginning2, getWidth() / 2.0 - 105, getHeight() + 45);
        GLabel beginning3 = new GLabel("We hope you'll enjoy it.");
        beginning3.setFont("Ancient Modern Tales-36");
        beginning3.setColor(Color.red);
        add(beginning3, getWidth() / 2.0 - 115, getHeight() + 90);
        GLabel exit = new GLabel("To enter the menu press 'Esc'");
        exit.setFont("Ancient Modern Tales-18");
        exit.setColor(Color.red);
        add(exit,0,20);
        while (credits) {
            beginning1.sendToFront();
            beginning2.sendToFront();
            beginning3.sendToFront();
            exit.sendToFront();
            beginning1.move(0, -Y_SPEED);
            if (beginning1.getY() <= 0) {
                beginning1.setLocation(getWidth() / 2.0 - 125, getHeight());

            }
            beginning2.move(0, -Y_SPEED);
            if (beginning2.getY() <= 0) {
                beginning2.setLocation(getWidth() / 2.0 - 105, getHeight());

            }
            beginning3.move(0, -Y_SPEED);
            if (beginning3.getY() <= 0) {
                beginning3.setLocation(getWidth() / 2.0 - 115, getHeight());

            }

            pause(50);

        }


    }

    public void menu() {
        GImage back = new GImage("arcade/back.png");
        back.scale(1, 1);
        add(back);
        beginning = new GLabel("Welcome to our Arcanoid game");
        beginning.setFont("Ancient Modern Tales-26");
        beginning.setColor(Color.red);
        add(beginning, getWidth() / 2.0 - 125, 50);

        GLabel beginning4 = new GLabel("To launch the game press 'Play'");
        beginning4.setFont("Ancient Modern Tales-26");
        beginning4.setColor(Color.red);
        add(beginning4, getWidth() / 2.0 - 125, 110);

        GLabel beginning2 = new GLabel("To add the ball, click on the screen");
        beginning2.setFont("Ancient Modern Tales-26");
        beginning2.setColor(Color.RED);
        add(beginning2, getWidth() / 2.0 - 125, 170);
        playGame = new GRect(175, 100);
        playGame.setFilled(true);
        playGame.setColor(Color.gray);

        credit = new GRect(175, 100);
        credit.setFilled(true);
        credit.setColor(Color.gray);

        start = new GLabel("Play");
        start.setFont("Ancient Modern Tales-26");
        start.setColor(Color.black);
        add(start, getWidth() / 2.0 - 10, getHeight() / 2.0 - 10);
        cred = new GLabel("Credits");
        cred.setFont("Ancient Modern Tales-26");

        cred.setColor(Color.black);
        add(cred, getWidth() / 2.0 - 15, getHeight() / 2.0 + 85);
        addMouseListeners();
        menu1 = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\menu.wav");
        menu1.setVolume(0.3);
        menu1.loop();
        while (menu) {

            add(playGame, getWidth() / 2.0 - 75, getHeight() / 2.0 - 75);
            start.sendToFront();
            add(credit, getWidth() / 2.0 - 75, getHeight() / 2.0 + 26);
            cred.sendToFront();
            if (credits) {
                credits();
            }

        }

    }

    public void setupTheGame() {
        this.setSize(APPLICATION_WIDTH + 15, APPLICATION_HEIGHT);
        GImage back = new GImage("arcade/back.png");
        back.scale(1, 1);
        add(back);
        rocket = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
        rocket.setFilled(true);
        rocket.setColor(Color.blue);
        add(rocket, 160, 500);

        vx = r_gen.nextDouble(1.0, 3.0);
        if (r_gen.nextBoolean(0.5)) vx = -vx;
        vy = 3;
    }

    private void checkCollision() {
        if (ball != null) {
            if (ball.getX() >= 0 && ball.getY() + BALL_RADIUS <= getHeight())
                vy *= -1;
            if (ball.getX() >= 0 && ball.getY() >= 0) {
                vx *= -1;
                vy *= -1;
            }
            if (ball.getX() + BALL_RADIUS <= getWidth() && ball.getY() > 0)
                vx *= -1;
            if (ball.getY() + BALL_RADIUS > getHeight() - (int) vy) {
                remove(ball);
                SoundClip fall = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\fall.wav");
                fall.setVolume(0.7);
                fall.play();
                ball = null;
//            add(ball,getWidth()/2.0, getHeight()/2.0);
                N_TURNS -= 1;
                if (N_TURNS == 0) {
                    game = false;
                }
            }
        }
    }

    public void creatingPaddle() {
        countBricks = 0;
        numberOfBricks = 0;
        int height = BRICK_Y_OFFSET;
        for (int i = 0; i < NBRICK_ROWS; i++) {
            height += BRICK_HEIGHT + BRICK_SEP;
            int width = 0;
            for (int j = 0; j < NBRICKS_PER_ROW; j++) {
                GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
                brick.setFilled(true);
                brick.setColor(Color(i));
                add(brick, width, height);
                width += BRICK_WIDTH + BRICK_SEP;
                numberOfBricks++;
            }
        }
    }

    private Color Color(int i) {
        if (i >= 0 && i < 2)
            return Color.RED;
        if (i >= 2 && i < 4)
            return Color.ORANGE;
        if (i >= 4 && i < 6)
            return Color.YELLOW;
        if (i >= 6 && i < 8)
            return Color.GREEN;
        else
            return Color.CYAN;
    }

    private boolean isItBrick(GObject collision) {
        if (collision != null) {
            if (collision.getWidth() == BRICK_WIDTH && collision.getHeight() == BRICK_HEIGHT) {
                int o = r_gen.nextInt(0,35);
                if (o == 0 || o == 35){
                    if (rect == null){
                        rect = new GImage("arcade/powerup.png");
                        rect.scale(0.04,0.04);
                        add(rect,collision.getX(),collision.getY());}
                    remove(collision);
                    SoundClip collision2 = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\collision.wav");
                    collision2.setVolume(0.4);
                    collision2.play();

                }
                remove(collision);
                SoundClip collision2 = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\collision.wav");
                collision2.setVolume(0.4);
                collision2.play();

                countBricks++;
                vy *= (-1);
                return true;
            }
        }
        return false;
    }

    public void hitTheBrick() {
        if (ball != null) {
            GObject collision;
            collision = getElementAt(ball.getX() + BALL_RADIUS / 2.0, ball.getY());
            if (isItBrick(collision))
                return;
            collision = getElementAt(ball.getX(), ball.getY());
            if (isItBrick(collision))
                return;
            collision = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
            if (isItBrick(collision))
                return;
            collision = getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
            if (isItBrick(collision))
                return;
            collision = getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
            isItBrick(collision);
        }
    }

    public void movePowerUp(){
        int y_SPEED = 1;
        if (rect!=null){
            if (rect.getX()>= rocket.getX()-PADDLE_WIDTH && rect.getX()<=rocket.getX()+PADDLE_WIDTH+60 && rect.getY()+30==rocket.getY()){
                remove(rect);
                powerUp = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\powerup.wav");
                powerUp.setVolume(0.1);
                powerUp.play();
                rect = null;
                N_TURNS ++;

            }
            if (rect != null){
                if (rect.getY()+10<=APPLICATION_HEIGHT){
                    rect.move(0,y_SPEED);
                }else{
                    remove(rect);
                    rect = null;
                }
            }
        }
    }
    public void hitTheRocket() {
        if (ball != null) {
            GObject collision = getElementAt(ball.getX(), ball.getY());
            GObject collision1 = getElementAt(ball.getX() + ball.getWidth(), ball.getY());
            GObject collision2 = getElementAt(ball.getX(), ball.getY() + ball.getHeight());
            GObject collision3 = getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight());
            if (collision == rocket) {
                if (ball.getY() + ball.getHeight() > rocket.getY()) {
                    double dif = (ball.getY() + ball.getHeight()) - rocket.getY();
                    ball.move(0, -dif);

                }
                jump = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\jump.wav");
                jump.setVolume(0.3);
                jump.play();
                vy *= (-1);
            } else if (collision1 == rocket) {
                if (ball.getY() + ball.getHeight() > rocket.getY()) {
                    double dif = (ball.getY() + ball.getHeight()) - rocket.getY();
                    ball.move(0, -dif);

                }
                jump = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\jump.wav");
                jump.setVolume(0.3);
                jump.play();
                vy *= (-1);
            } else if (collision2 == rocket) {
                if (ball.getY() + ball.getHeight() > rocket.getY()) {
                    double dif = (ball.getY() + ball.getHeight()) - rocket.getY();
                    ball.move(0, -dif);

                }
                jump = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\jump.wav");
                jump.setVolume(0.3);
                jump.play();
                vy *= (-1);
            } else if (collision3 == rocket) {
                if (ball.getY() + ball.getHeight() > rocket.getY()) {
                    double dif = (ball.getY() + ball.getHeight()) - rocket.getY();
                    ball.move(0, -dif);

                } jump = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\jump.wav");
                jump.setVolume(0.3);
                jump.play();
                vy *= (-1);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {

        if (game) {

            if (ball == null) {
                ball = new GOval(BALL_RADIUS, BALL_RADIUS);
                ball.setFilled(true);
                ball.setColor(Color.black);
                add(ball, getWidth() / 2.0, getHeight() / 2.0);
            }
        }
        if (menu && !credits && !game) {
            if (e.getX() >= playGame.getX() && e.getX() <= playGame.getX() + 175 && e.getY() >= playGame.getY() && e.getY() <= playGame.getY() + 100) {
                SoundClip click = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\bitclick.aiff");
                click.setVolume(1);
                click.play();
                menu = false;
                menu1.stop();
                removeAll();
                game = true;
                ball = null;
                N_TURNS = lives;

            }
        }
        if (!credits && !game) {
            if (e.getX() >= credit.getX() && e.getX() <= credit.getX() + 175 && e.getY() >= credit.getY() && e.getY() <= credit.getY() + 100) {
                SoundClip click = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\bitclick.aiff");
                click.setVolume(1);
                click.play();
                credits = true;
                ball = null;
                removeAll();
                GImage back = new GImage("arcade/back.png");
                back.scale(1, 1);
                add(back);


            }

        }
    }

    public void keyPressed(KeyEvent e) {
        if (!game) {
            ball = null;
            if (credits) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    SoundClip click = new SoundClip("C:\\Users\\sasha\\IdeaProjects\\arcade\\org.acm.test\\src\\arcade\\bitclick.aiff");
                    click.setVolume(1);
                    click.play();
                    credits = false;
                    removeAll();
                    GImage back = new GImage("arcade/back.png");
                    back.scale(1, 1);
                    add(back);
                    beginning = new GLabel("Welcome to our Arcanoid game");
                    beginning.setFont("Ancient Modern Tales-26");
                    beginning.setColor(Color.red);
                    add(beginning, getWidth() / 2.0 - 125, 50);

                    GLabel beginning4 = new GLabel("To launch the game press 'Play'");
                    beginning4.setFont("Ancient Modern Tales-26");
                    beginning4.setColor(Color.red);
                    add(beginning4, getWidth() / 2.0 - 125, 110);

                    GLabel beginning2 = new GLabel("To add the ball, click on the screen");
                    beginning2.setFont("Ancient Modern Tales-26");
                    beginning2.setColor(Color.RED);
                    add(beginning2, getWidth() / 2.0 - 125, 170);
                    playGame = new GRect(175, 100);
                    playGame.setFilled(true);
                    playGame.setColor(Color.gray);

                    credit = new GRect(175, 100);
                    credit.setFilled(true);
                    credit.setColor(Color.gray);

                    start = new GLabel("Play");
                    start.setFont("Ancient Modern Tales-26");
                    start.setColor(Color.black);
                    add(start, getWidth() / 2.0 - 10, getHeight() / 2.0 - 10);
                    cred = new GLabel("Credits");
                    cred.setFont("Ancient Modern Tales-26");

                    cred.setColor(Color.black);
                    add(cred, getWidth() / 2.0 - 15, getHeight() / 2.0 + 85);

                }
            }

        }
    }

    public void mouseMoved(MouseEvent e) {

        if (!menu && !credits) {
            int x_SPEED = 8;
            while (e.getX() > rocket.getX() && rocket.getX() + PADDLE_WIDTH <= APPLICATION_WIDTH) {
                rocket.move(x_SPEED, 0);
            }
            while (e.getX() < rocket.getX() && rocket.getX() >= 0) {
                rocket.move(-x_SPEED, 0);
            }
        }
        if (menu && !game && !credits) {
            if (e.getX() >= playGame.getX() && e.getX() <= playGame.getX() + 175 && e.getY() >= playGame.getY() && e.getY() <= playGame.getY() + 100) {

                playGame.setColor(Color.lightGray);
            } else {

                playGame.setColor(Color.gray);
            }
            if (e.getX() >= credit.getX() && e.getX() <= credit.getX() + 175 && e.getY() >= credit.getY() && e.getY() <= credit.getY() + 100) {

                credit.setColor(Color.lightGray);
            } else {

                credit.setColor(Color.gray);
            }

        }

    }
    private SoundClip powerUp;
    private SoundClip menu1;
    private GImage rect;
    private GRect playGame;
    private GLabel start;
    private GLabel beginning;
    private GRect credit;
    private boolean game = false;
    private boolean menu = true;
    private boolean credits = false;
    private RandomGenerator r_gen = RandomGenerator.getInstance();
}
