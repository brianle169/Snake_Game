import java.awt.*;
import java.util.Random;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener{

    //Parameters
    private JFrame frame;
    private final int unit = Constants.UNIT_SIZE;
    private final int panelWidth = Constants.PANEL_WIDTH;
    private final int panelHeight = Constants.PANEL_HEIGHT;
    private final Random random;
    private final Timer timer;
    private int delay;
    private String activateWallCollision;
    private boolean gameOver = false;

    //Key Bindings
    private Action upAction;
    private Action downAction;
    private Action leftAction;
    private Action rightAction;
    private Action restartAction;
    private Action quitAction;
    private Action changeAction;

    //Snake
    private int bodyPart;
    private final int bodySize = Constants.UNIT_SIZE;
    private ArrayList<Integer> snakeXLocation = new ArrayList<>();
    private ArrayList<Integer> snakeYLocation = new ArrayList<>();
    private int initialX;
    private int initialY;
    private char direction = 'R';
    //Food
    private int foodX;
    private int foodY;
    private final int foodSize = Constants.UNIT_SIZE;
    //Score
    private int score;


    public GamePanel(int speed, String wallCollision){
        //Setup JFrame
        frame = new JFrame();
        frame.setTitle("Snake Game v2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Set up panel:
        this.setPreferredSize(new Dimension(panelWidth,panelHeight));
        random = new Random();
        this.score = 0;
        this.activateWallCollision = wallCollision;

        //Initialize snake info:
        this.bodyPart = 3;
        this.initialX = random.nextInt(0,panelWidth/unit/2) * unit;
        this.initialY = random.nextInt(0,panelHeight/unit/2) * unit;
        for (int i = 0; i < bodyPart + 1; i++) {
            snakeXLocation.add(initialX);
            snakeYLocation.add(initialY);
        }

        //Initialize food location:
        do {
            this.foodX = random.nextInt(0,panelWidth/unit) * unit;
            this.foodY = random.nextInt(0,panelHeight/unit) * unit;}
        while (foodX == snakeXLocation.get(0) && foodY == snakeYLocation.get(0));

        //Key Bindings
        upAction = new UpAction();
        downAction = new DownAction();
        leftAction = new LeftAction();
        rightAction = new RightAction();
        restartAction = new RestartAction();
        quitAction = new QuitAction();
        changeAction = new ChangeAction();

        this.getInputMap().put(KeyStroke.getKeyStroke("UP"),"upAction");
        this.getActionMap().put("upAction",upAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"),"downAction");
        this.getActionMap().put("downAction",downAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"),"leftAction");
        this.getActionMap().put("leftAction",leftAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"),"rightAction");
        this.getActionMap().put("rightAction",rightAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("R"),"restartAction");
        this.getActionMap().put("restartAction",restartAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("Q"),"quitAction");
        this.getActionMap().put("quitAction",quitAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("C"),"changeAction");
        this.getActionMap().put("changeAction",changeAction);

        frame.add(this);
        frame.setResizable(false);
        frame.pack();

        //Listeners:
        this.delay = speed;
        timer = new Timer(delay,this);
        timer.start();

    }

    public class UpAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (direction != 'D' && direction != 'U'){
                direction = 'U';
                snakeMove(direction);
            }
        }
    }

    public class DownAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (direction != 'U' && direction != 'D'){
                direction = 'D';
                snakeMove(direction);
            }
        }
    }

    public class LeftAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (direction != 'R' && direction != 'L'){
                direction = 'L';
                snakeMove(direction);
            }
        }
    }

    public class RightAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (direction != 'L' && direction != 'R'){
                direction = 'R';
                snakeMove(direction);
            }
        }
    }

    public class RestartAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            restart();
        }
    }

    public class ChangeAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            change();
        }
    }

    public class QuitAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            quit();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snakeMove(direction);
        if (gameOver){
            timer.stop();
        }
    }

    private void quit() {
        if (gameOver){
            frame.dispose();
            System.exit(0);
        }
    }

    private void change(){
        if (gameOver) {
            frame.dispose();
            new SnakeGame();
        }
    }

    public void restart(){
        if (gameOver) {
            frame.dispose();
            new GamePanel(delay,activateWallCollision);
        }
    }

    public void paintComponent(Graphics g){
        //Draw Panel Canvas
        g.setColor(Color.BLACK);
        g.fillRect(0,0,panelWidth,panelHeight);

        //Draw food
        newFood(g);

        //Snake:
        drawSnake(g);

        //Score
        drawScore(g);

        //Game over
        if (gameOver){
            g.setColor(Color.BLACK);
            g.fillRect(0,0,panelWidth,panelHeight);
            drawScore(g);
            g.setColor(Color.RED);
            g.setFont(new Font(Font.MONOSPACED,Font.BOLD,60));
            g.drawString("GAME OVER!",125,300);
            g.setColor(Color.RED);
            g.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
            g.drawString("Press R to Restart or Q to Quit",120,350);
            g.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
            g.drawString("Or Press C to Change Configuration",105,400);
        }
    }

    public void drawScore(Graphics g){
        String scoreInfo = "Score: " + score;
        g.setColor(Color.RED);
        g.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));
        g.drawString(scoreInfo, 200,70);
    }

    public void drawSnake(Graphics g){

        for (int i = 0; i < bodyPart + 1; i++){
            if (i != 0){ //Head of the snake
                g.setColor(Color.YELLOW);
                g.fillRect(snakeXLocation.get(i),snakeYLocation.get(i),unit,unit);
            }
            else {
                g.setColor(Color.PINK);
                g.fillRect(snakeXLocation.get(i), snakeYLocation.get(i), unit, unit);
            }
        }
    }

    public void newFood(Graphics g){
        g.setColor(Color.GREEN);
        g.fillOval(foodX,foodY,foodSize,foodSize);
    }

    public void snakeMove(char dir){
        switch (dir){
            case 'U' -> snakeMoveUp();
            case 'D' -> snakeMoveDown();
            case 'L' -> snakeMoveLeft();
            case 'R' -> snakeMoveRight();
        }

        checkCollision();
        checkSelfCollision();
        checkFood();

        repaint();
    }

    private void snakeMoveUp() {
        for (int i = bodyPart; i >= 0 ; i--) {
            if (i != 0){
                snakeXLocation.set(i,snakeXLocation.get(i-1));
                snakeYLocation.set(i,snakeYLocation.get(i-1));
            }
            else {snakeYLocation.set(i,snakeYLocation.get(i) - unit);}
        }
    }

    private void snakeMoveDown() {
        for (int i = bodyPart; i >= 0 ; i--) {
            if (i != 0){
                snakeXLocation.set(i,snakeXLocation.get(i-1));
                snakeYLocation.set(i,snakeYLocation.get(i-1));
            }
            else {snakeYLocation.set(i,snakeYLocation.get(i) + unit);}
        }
    }

    private void snakeMoveLeft() {
        for (int i = bodyPart; i >= 0 ; i--) {
            if (i != 0){
                snakeXLocation.set(i,snakeXLocation.get(i-1));
                snakeYLocation.set(i,snakeYLocation.get(i-1));
            }
            else {snakeXLocation.set(i,snakeXLocation.get(i) - unit);}
        }
    }

    public void snakeMoveRight(){
        for (int i = bodyPart; i >= 0 ; i--) {
            if (i != 0){
                snakeXLocation.set(i,snakeXLocation.get(i-1));
                snakeYLocation.set(i,snakeYLocation.get(i-1));
            }
            else {snakeXLocation.set(i,snakeXLocation.get(i) + unit);}
        }
    }

    public void checkCollision(){
        int snakeHeadX = snakeXLocation.get(0);
        int snakeHeadY = snakeYLocation.get(0);

        if (snakeHeadX + bodySize > panelWidth || snakeHeadX < 0){
            if (snakeHeadX + bodySize > panelWidth && activateWallCollision.equals("Off")) {
                snakeXLocation.set(0,0);
            }
            else if (snakeHeadX < 0 && activateWallCollision.equals("Off"))
                snakeXLocation.set(0, panelWidth - unit);
            else gameOver = true;
        }
        else if (snakeHeadY + bodySize > panelHeight || snakeHeadY < 0){
            if (snakeHeadY + bodySize > panelHeight && activateWallCollision.equals("Off")){
                snakeYLocation.set(0,0);
            }
            else if (snakeHeadY < 0 && activateWallCollision.equals("Off"))
                snakeYLocation.set(0, panelHeight - unit);
            else gameOver = true;
        }
    }

    public void checkSelfCollision(){
        int snakeHeadX = snakeXLocation.get(0);
        int snakeHeadY = snakeYLocation.get(0);
        Rectangle snakeHead = new Rectangle(snakeHeadX,snakeHeadY,bodySize,bodySize);
        for (int i = 1; i < bodyPart + 1; i++) {
            if (snakeHead.intersects(new Rectangle(snakeXLocation.get(i),snakeYLocation.get(i),bodySize,bodySize))){
                gameOver = true;
            }
        }
    }

    public void checkFood() {
        Rectangle food = new Rectangle(foodX, foodY, foodSize, foodSize);
        Rectangle snakeHead = new Rectangle(snakeXLocation.get(0), snakeYLocation.get(0), bodySize, bodySize);

        if (snakeHead.intersects(food)) {
            score++;
            bodyPart++;

            int lastX = snakeXLocation.get(snakeXLocation.size()-1);
            int lastY = snakeYLocation.get(snakeYLocation.size()-1);

            snakeXLocation.add(lastX);
            snakeYLocation.add(lastY);

            foodX = random.nextInt(0, panelWidth / unit) * unit;
            foodY = random.nextInt(0, panelHeight / unit) * unit;
        }
    }
}
