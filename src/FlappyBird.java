import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private final int boardWidth = 360;
    private final int boardHeight = 640;

    private Image backgroundImg, birdImg, topPipeImg, bottomPipeImg;
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private final Random random = new Random();
    private Timer gameLoop, placePipeTimer;

    private int velocityX;
    private int velocityY = 0;
    private int gravity = 1;

    private boolean gameOver = false;
    private double score = 0;

    public FlappyBird(String difficulty) {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        loadImages();
        initGame(difficulty);
    }

    private void loadImages() {
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
    }

    private void initGame(String difficulty) {
        bird = new Bird(boardWidth / 8, boardWidth / 2, 34, 24, birdImg);
        pipes = new ArrayList<>();

        if (difficulty.equals("easy")) {
            velocityX = -6;
        } else if (difficulty.equals("hard")) {
            velocityX = -10;
        }

        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    private void placePipes() {
        int randomPipeY = (int) (0 - 512 / 4 - Math.random() * (512 / 2));
        int openingSpace = boardHeight / 4;

        pipes.add(new Pipe(boardWidth, randomPipeY, 64, 512, topPipeImg));
        pipes.add(new Pipe(boardWidth, randomPipeY + 512 + openingSpace, 64, 512, bottomPipeImg));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString(String.valueOf((int) score), 10, 35);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", boardWidth / 2 - 150, boardHeight / 2);
        }
    }

    private void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    private boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x &&
               a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        } else {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        bird.y = boardWidth / 2;
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        score = 0;
        gameLoop.start();
        placePipeTimer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}