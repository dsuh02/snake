import javax.swing.*;
import java.util.ArrayList;
import java.awt.*; 
import java.awt.event.*;
import javax.swing.event.*; 
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame 
{
    private GridView view;
    Cell[][] cells;
    final Color WHITE = new Color(255,255,255);
    ArrayList<Coordinate> snake = new ArrayList<Coordinate>();
    Coordinate apple = null;
    char dir = 'd';
    String queue ="";
    Color snekColor= Color.green;
    
    //cover
    JLabel cover = new JLabel();
    JLabel chooseColor = new JLabel();
    JButton greenB = new JButton();
    JButton purpleB = new JButton();    
    JButton brownB = new JButton();
    JButton rainbowB = new JButton();
    JLayeredPane coverLayer = new JLayeredPane();
    
    //game
    JTextField jtf1 = new JTextField();
    JLabel title = new JLabel();
    JButton reset = new JButton();
    JLabel pointCount = new JLabel("Points: 0");
    JLabel sideCover = new JLabel();
    
    //loss page
    JLabel congrats = new JLabel("Good job! You had a score of: ",SwingConstants.CENTER);
    JLabel congratsPic = new JLabel();
    JLayeredPane layer = new JLayeredPane();
    JLabel pressKey = new JLabel("Press any key to restart...", SwingConstants.CENTER);
    

    int points=0;
    boolean coverCont=false, cont=false;
    public GUI()
    {
        super("snek");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(1000,15);
        setupGrid();
        pack();
        setLayout(null);
        setBackground(Color.black);
        setResizable(false);
        setSize(800,630);
        setVisible(true);
        

        
        //textfield stuff
        jtf1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jtf1.setVisible(true);
        jtf1.addKeyListener(listener);
        this.add(jtf1);
        jtf1.setBounds(0,0,1,1);
        
        //cover stuff
        this.add(coverLayer);
        coverLayer.setBounds(0,0,800,600);
        coverLayer.setVisible(true);
        cover.setIcon(new ImageIcon("snek3.png"));
        coverLayer.add(cover,0,-1);
        chooseColor.setIcon(new ImageIcon("chooseColor.png"));
        coverLayer.add(chooseColor,0,0);
        chooseColor.setBounds(700,75,100,50);
        cover.setRequestFocusEnabled(false);
        cover.setBounds(0,0,800,600);
        cover.setVisible(true);
        coverCont=false;
        coverLayer.add(greenB,0,0);
        coverLayer.add(purpleB,0,0);
        coverLayer.add(brownB,0,0);
        coverLayer.add(rainbowB,0,0);
        //
        greenB.setBounds(725,175,50,50);
        purpleB.setBounds(725,275,50,50);
        brownB.setBounds(725,375,50,50);
        rainbowB.setBounds(725,475,50,50);
        //
        greenB.setBorderPainted(false);
        purpleB.setBorderPainted(false);
        brownB.setBorderPainted(false);
        rainbowB.setBorderPainted(false);
        //
        greenB.setBackground(Color.green);
        purpleB.setBackground(new Color(128,0,128));
        brownB.setBackground(new Color(139,69,19));
        rainbowB.setIcon(new ImageIcon("rainbow.jpg"));
        //
        ActionListener snekColorButtons = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getSource().equals(greenB)){
                    snekColor=Color.green;
                }
                else if(e.getSource().equals(brownB)){
                    snekColor=new Color(139,69,19);
                }
                else if(e.getSource().equals(purpleB)){
                    snekColor=new Color(128,0,128);
                }
                else if(e.getSource().equals(rainbowB)){
                    snekColor=Color.white;
                }
            }
        };
        //
        greenB.addActionListener(snekColorButtons);
        purpleB.addActionListener(snekColorButtons);
        brownB.addActionListener(snekColorButtons);
        rainbowB.addActionListener(snekColorButtons);
        while (coverCont==false){
            jtf1.requestFocus();
        }
        coverLayer.setVisible(false);
        
        
        //button stuff
        reset.setIcon(new ImageIcon("reset.png"));
        this.add(reset);
        reset.setBounds(700,250,100,100);
        reset.setBorderPainted(false);
        reset.setRequestFocusEnabled(false);
        reset.setToolTipText("Resets the game");
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getSource().equals(reset)){
                    resetGame();
                    points=0;
                }
            }
        });
        
        
        
        //points stuff
        this.add(pointCount);
        pointCount.setFont(new Font("Georgia", Font.PLAIN, 20));
        pointCount.setBounds(700,200,200,50);
        
        //game decor stuff
        sideCover.setIcon(new ImageIcon("sidecover.jpg"));
        this.add(sideCover);
        sideCover.setBounds(1,0,100,600);
        
        //loss stuff
        congratsPic.setIcon(new ImageIcon("loss.png"));
        congratsPic.setBounds(0,0,800,600);
        //
        congrats.setForeground(Color.black);
        congrats.setFont(new Font("Georgia", Font.PLAIN, 25));
        //
        pressKey.setForeground(Color.black);
        pressKey.setFont(new Font("Georgia", Font.PLAIN,15));
        //
        layer.setLayout(null);
        layer.add(congrats,0,0);
        layer.add(pressKey,0,0);
        layer.add(congratsPic,0,-1);
        //
        pressKey.setBounds(0,300,800,30);
        congrats.setBounds(0,250,800,30);
        layer.setBounds(0,0,800,600);
        //
        this.add(layer);
        congratsPic.setVisible(false);
        congrats.setVisible(false);
        layer.setVisible(false);
        
        //grid stuff
        view.setLocation(102,2);
        
        setSize(800,630);
        resetGame();
        
        while(1==1){
            pointCount.setText("Points: "+ points);
            jtf1.requestFocus();
            try{
                Thread.sleep(150);
                updateDir();
                step();
                showCells();
            }
            catch(InterruptedException e){}
        }
    }
    public void updateDir(){
        if(queue.length()>0){
            if((queue.charAt(0)=='w' && dir!='s')||
                (queue.charAt(0)=='a' && dir!='d')||
                (queue.charAt(0)=='s' && dir!='w')||
                (queue.charAt(0)=='d' && dir!='a'))
            {
                dir=queue.charAt(0);
            }
            queue=queue.substring(1);
        }
    }

    public void callLoss()
    {
        title.setVisible(false);
        reset.setVisible(false);
        pointCount.setVisible(false);
        view.setVisible(false);
        sideCover.setVisible(false);
        
        congratsPic.setVisible(true);
        congrats.setVisible(true);
        layer.setVisible(true);
        congrats.setText("Good job! You had a score of: "+ points+ "!");
        cont=false;
        while (cont==false){
            jtf1.requestFocus();
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){}
        }
        resetGame();
        layer.setVisible(false);
        sideCover.setVisible(true);
        title.setVisible(true);
        reset.setVisible(true);
        pointCount.setVisible(true);
        view.setVisible(true);
    }
    KeyListener listener = new KeyListener(){
        public void keyPressed(KeyEvent e){
            coverCont=true;
            cont=true;
            if(queue.length()<2)
              try{
                if(e.getKeyCode()==KeyEvent.VK_D) {
                    queue+="d";
                }
                else if(e.getKeyCode()==KeyEvent.VK_A) {
                    queue+="a";
                }
                else if(e.getKeyCode()==KeyEvent.VK_W){
                    queue+="w";
                }
                else if(e.getKeyCode()==KeyEvent.VK_S){
                    queue+="s";
                }
                else if(e.getKeyCode()==KeyEvent.VK_R){
                    resetGame();
                }
              }
              catch(Exception ex){}
        }
        public void keyReleased(KeyEvent e){
            
        }
        public void keyTyped(KeyEvent e){
            
        }
    };
    private void spawnApple(){
        Coordinate possible = getRandCoord();
        if(has(possible)){
            spawnApple();
        }
        else apple = possible;
    }
    private Coordinate getRandCoord(){
        int x= (int)(Math.random()*15);
        int y= (int)(Math.random()*15);
        return new Coordinate(x,y);
    }
    private void resetGame(){
        dir='d';
        cells=new Cell[15][15];
        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[0].length; j++){
                cells[i][j]=new Cell(new Coordinate(i,j),WHITE);
            }
        }
        queue="";
        points=0;
        snake =new ArrayList<Coordinate>();
        snake.add(new Coordinate(7,7));
        snake.add(new Coordinate(7,6));
        spawnApple();
    }
    private void setupGrid()
    {
        view = new GridView();
        this.add(view);
        view.setBounds(0,0,600,600);
    }
    public void showCells()
    {
        if(!isVisible()) {
            setVisible(true);
        }
        view.preparePaint();
        for(int row = 0; row < cells.length; row++) {
            for(int col = 0; col < cells[0].length; col++) {
                view.drawMark(col, row, cells[row][col].getColor());
            }
        }
        if(snekColor!=Color.white)
            for(int i = 0; i < snake.size(); i++) {
                view.drawMark(snake.get(i).getC(), snake.get(i).getR(), snekColor);
            }
        else
            for(int i = 0; i < snake.size(); i++) {
                if(i%7==6)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(139,0,255));
                if(i%7==5)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(75,0,130));
                if(i%7==4)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(0,0,255));
                if(i%7==3)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(0,255,0));
                if(i%7==2)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(255,255,0));
                if(i%7==1)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(255,127,0));
                if(i%7==0)view.drawMark(snake.get(i).getC(),snake.get(i).getR(),new Color(255,0,0));
            }
        if(apple!= null) view.drawMark(apple.getC(),apple.getR(),Color.red);
        view.repaint();
    }    
    public void step()
    {
        if(canStep() && wontCollide()){
            if(dir=='w'){
                snake.add(0,new Coordinate(snake.get(0).getR()-1,snake.get(0).getC()));
            }
            else if(dir=='s'){
                snake.add(0,new Coordinate(snake.get(0).getR()+1,snake.get(0).getC()));
            }
            else if(dir=='a'){
                snake.add(0,new Coordinate(snake.get(0).getR(),snake.get(0).getC()-1));
            }
            else{
                snake.add(0,new Coordinate(snake.get(0).getR(),snake.get(0).getC()+1));
            }
            if(has(apple)){
                spawnApple();
                points++;
            }
            else snake.remove(snake.size()-1);
        }
        else callLoss();
    }
    public boolean canStep()//into border
    {
        if(snake.get(0).getR()<0 || snake.get(0).getR()>14
            || snake.get(0).getC()<0 || snake.get(0).getC()>14) 
            return false;
        return true;
    }
    public boolean wontCollide()//into snake
    {
        if(dir=='w' && has(
            new Coordinate(snake.get(0).getR()-1,snake.get(0).getC())))return false;
        else if(dir=='s' && has(
            new Coordinate(snake.get(0).getR()+1,snake.get(0).getC())))return false;
        else if(dir=='a' && has(
            new Coordinate(snake.get(0).getR(),snake.get(0).getC()-1)))return false;
        else if(dir=='d' && has(
            new Coordinate(snake.get(0).getR(),snake.get(0).getC()+1)))return false;
        return true;
    }
    public boolean has(Coordinate a)
    {
        for(int i=0; i<snake.size();i++){
            if(snake.get(i).equals(a)) return true;
        }
        return false;
    }
    
    private class GridView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 1;

        private final int gridWidth, gridHeight;
        private int xScale, yScale;
        private Dimension size;
        private Graphics g;
        private Image fieldImage;

        public GridView()
        {
            gridHeight = 15;
            gridWidth = 15;
            size = new Dimension(0,0);
        }
        
        public Dimension getPreferredSize()
        {
            return new Dimension(600,600);
        }

        
        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {
                size = getSize();
                fieldImage = view.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        @Override
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
    }
