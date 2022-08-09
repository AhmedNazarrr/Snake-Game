import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakePanel extends JPanel implements ActionListener {

    final int SCREEN_WIDTH = 600;
    final int SCREEN_HEIGHT = 600;
    final int UNIT_SIZE = 25;
    final int GAME_UNITS = (this.SCREEN_WIDTH * this.SCREEN_HEIGHT)
            / this.UNIT_SIZE;
    final int DELAY = 75; // the higher delay number the slower game

    final int[] x = new int[this.GAME_UNITS];
    final int[] y = new int[this.GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;

    int appleX;
    int appleY;

    char direction = 'R'; // R,L,U,D correspond to right, left, up, down
    boolean running = false;
    Timer timer;
    Random random;

    SnakePanel() { // constructor
        this.random = new Random();
        this.setPreferredSize(
                new Dimension(this.SCREEN_WIDTH, this.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true); // ????
        this.addKeyListener(new myKeyAdapter());
        this.startGame();
    }

    public void startGame() {
        this.newApple();
        this.running = true;
        this.timer = new Timer(this.DELAY, this); // arguments?
        this.timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g) {

        if (this.running) {
            // draws apple
            g.setColor(Color.red);
            g.fillOval(this.appleX, this.appleY, this.UNIT_SIZE,
                    this.UNIT_SIZE);

            // draws snake head and body
            for (int i = 0; i < this.bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(this.x[i], this.y[i], this.UNIT_SIZE,
                            this.UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor((new Color(this.random.nextInt(255),
                            this.random.nextInt(255),
                            this.random.nextInt(255))));
                    g.fillRect(this.x[i], this.y[i], this.UNIT_SIZE,
                            this.UNIT_SIZE);
                }
            }

            // display score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Tree", Font.BOLD, 40));
            FontMetrics metrics = this.getFontMetrics(g.getFont());

            g.drawString("Score: " + this.applesEaten,
                    (this.SCREEN_WIDTH
                            - metrics.stringWidth("Score: " + this.applesEaten))
                            / 2,
                    g.getFont().getSize());

        } else {
            this.gameOver(g);
        }
    }

    public void newApple() {
        this.appleX = this.random.nextInt(this.SCREEN_WIDTH / this.UNIT_SIZE)
                * this.UNIT_SIZE; // CAST AS INTEGER?
        this.appleY = this.random.nextInt(this.SCREEN_WIDTH / this.UNIT_SIZE)
                * this.UNIT_SIZE; // CAST AS INTEGER?

    }

    public void move() {
        for (int i = this.bodyParts; i > 0; i--) {
            this.x[i] = this.x[i - 1]; // shifting all coordinates over by one spot
            this.y[i] = this.y[i - 1]; // shifting all coordinates over by one spot
        }

        switch (this.direction) {
            case 'U':
                this.y[0] = this.y[0] - this.UNIT_SIZE; // maybe -
                break;
            case 'D':
                this.y[0] = this.y[0] + this.UNIT_SIZE; // maybe +
                break;
            case 'L':
                this.x[0] = this.x[0] - this.UNIT_SIZE;
                break;
            case 'R':
                this.x[0] = this.x[0] + this.UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {

        if (this.appleX == this.x[0] && this.appleY == this.y[0]) {
            this.bodyParts++;
            this.applesEaten++;
            this.newApple();
        }
    }

    public void checkCollision() {

        // checks if head collides with body
        for (int i = this.bodyParts; i > 0; i--) {
            if (this.x[i] == this.x[0] && this.y[i] == this.y[0]) {
                this.running = false;
            } // why is i decrementing instrad of invrementing???
        }

        // checks if head collides with left border
        if (this.x[0] < 0) {
            this.running = false;
        }

        // checks if head collides with left border
        if (this.x[0] > this.SCREEN_WIDTH) {
            this.running = false;
        }

        // checks if head collides with bottom border
        if (this.y[0] < 0) {
            this.running = false;
        }

        // checks if head collides with top border
        if (this.y[0] > this.SCREEN_HEIGHT) {
            this.running = false;
        }

        if (!this.running) {
            this.timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Tree", Font.BOLD, 75));
        FontMetrics metrics1 = this.getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (this.SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2,
                this.SCREEN_HEIGHT / 2);

        g.setFont(new Font("Ink Tree", Font.BOLD, 40));
        FontMetrics metrics2 = this.getFontMetrics(g.getFont());

        g.drawString("Score: " + this.applesEaten,
                (this.SCREEN_WIDTH
                        - metrics2.stringWidth("Score: " + this.applesEaten))
                        / 2,
                g.getFont().getSize());

    }

    @Override
    public void actionPerformed(ActionEvent e) { // inherited form ActionListener
        if (this.running) {
            this.move();
            this.checkApple();
            this.checkCollision();
        }
        this.repaint();
    }

    public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (SnakePanel.this.direction != 'R') {
                        SnakePanel.this.direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (SnakePanel.this.direction != 'L') {
                        SnakePanel.this.direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (SnakePanel.this.direction != 'D') {
                        SnakePanel.this.direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (SnakePanel.this.direction != 'U') {
                        SnakePanel.this.direction = 'D';
                    }
                    break;
            }

        }

    }

}
