import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * ToDo:
 * random positon start
 * paint them
 * move enemy and friends and make them shoot
 */
enum way{
    w,a,s,d
}

public class Game extends JPanel {
    ReentrantLock lock = new ReentrantLock();
    ReentrantLock lock2 = new ReentrantLock();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Friend> friends = new ArrayList<Friend>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Vector2D> positions =  new ArrayList<>();
    AirCraft player;
    final int ScreenWidth = 500;
    final int ScreenLength = 500;
    Clock watch;
    JFrame frame;
    boolean InBounds(int x, int y){
        if(x>= 0 && y>=0 && x<=ScreenWidth && y<=ScreenLength){
            return true;
        }
        return false;
    }

    public Game(){
        frame = new JFrame("Game");

        frame.setSize(ScreenWidth+14,ScreenLength+37);//these ar the sized that fit 500x500 idk why
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(this);
        
        watch = new Clock();
        watch.start();
    }
    @Override
    public void paint(Graphics g) {
        /* 
        for (Enemy enemy : enemies) {
            if(enemy.position.equals(player.position)){
                End(false);
            }
            for (Friend friend : friends) {
                if(enemy.position.equals(friend.position)){
                    enemy.dead = true;
                    friend.dead = true;
                    enemies.remove(enemy);
                    friends.remove(friend);
                    break;
                    
                }
            }
        }*/
        // TODO Auto-generated method stub
        super.paint(g);
        //g.fillRect(480,480, 20,20); this was to find correct size
        for (int i = 0;i<bullets.size();i++) {
            Bullet bullet = bullets.get(i);
            if(bullet != null){
            if(bullet.friendly){
                if(bullet.fromPlayer){
                    g.setColor(Color.ORANGE);
                    
                }
                else g.setColor(new Color(112, 52, 182));
            }
            else g.setColor(Color.BLUE);
            g.fillRect(bullet.position.x, bullet.position.y, bullet.caliber.width,bullet.caliber.height);
        }
        }
        g.setColor(Color.BLACK);
        for (int i = 0;i<enemies.size();i++) {
            Enemy enemy = enemies.get(i);
            g.fillRect(enemy.position.x,enemy.position.y,enemy.size.width,enemy.size.width);
        }
        g.setColor(Color.GREEN);
        for (int i = 0;i<friends.size();i++) {
            Friend friend = friends.get(i);
            g.fillRect(friend.position.x,friend.position.y,friend.size.width,friend.size.height);
        }
        g.setColor(Color.RED);
        g.fillRect(player.position.x,player.position.y,player.size.width,player.size.height);
            
        
    }
    public class AirCraft extends Craft implements KeyListener, MouseListener { // player

        public AirCraft() {
            int x = ScreenWidth/2;
            int y = ScreenLength/2;
            this.position = new Vector2D(x, y);
            player = this;
            addMouseListener(this);
            frame.addKeyListener(player);
            
            
        }

        

        @Override
        public void collisionControl(Object other) {
            // TODO Auto-generated method stub
            if(other instanceof Bullet){
                Bullet mag  = (Bullet) other;
                if(!mag.friendly){
                    End(false);
                    player=null;
                    
                }
            }
            else if(other instanceof Enemy){
                End(false);
                player=null;
            }
            
        }



        



        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            Bullet bullet1 = new Bullet(way.a,new Vector2D(this.position.x-this.size.width, this.position.y),true);
                bullet1.fromPlayer=true;
                bullet1.start();
                Bullet bullet2 = new Bullet(way.d,new Vector2D(this.position.x+this.size.width, this.position.y),true);
                bullet2.fromPlayer = true;
                bullet2.start();
                bullets.add(bullet1);
                bullets.add(bullet2);
            
        }



        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
        }



        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            switch(e.getKeyCode()){
                case (KeyEvent.VK_W):
                this.move(way.w);
                break;
                case (KeyEvent.VK_S):
                this.move(way.s);
                break;
                case (KeyEvent.VK_A):
                this.move(way.a);
                break;
                case (KeyEvent.VK_D):
                this.move(way.d);
                break;
                default:
                
            }

            
        }



        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
        }



        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
        }



        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
        }



        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
        }



        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
        }


    }
    
    public interface Collision {
        void collisionControl(Object other);
        boolean isInsinde(int x, int y);
        
    }
    class Vector2D{
        int x;
        int y;
        final int SPEED = 10;
        public Vector2D(int x, int y){
            this.x = x;
            this.y = y;
        }
        public void move (way dir, Object source){
            if(source instanceof Craft){
            if(x == ScreenWidth-SPEED && dir == way.d){
                dir=way.a;
            }
            else if(x == 0 && dir == way.a){
                dir = way.d;
            }
            if(y == ScreenLength-SPEED && dir == way.s ){
                dir = way.w;
            }
            else if(y == SPEED && dir == way.w){
                dir = way.s;
            }
            }   
            switch(dir){
                case w:
                    y-=SPEED;
                    break;
                case a:
                    x-=SPEED;   
                    break;
                case d:
                    x+=SPEED;
                    break;
                case s:
                    y+=SPEED;
                    break;
                default:
                    break;
            }
        }
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Vector2D){
                Vector2D vect = (Vector2D)obj;
                return (vect.x == this.x && vect.y == this.y);
            }
            return false;
        }
    }
    abstract class Craft extends Thread implements Collision {
        Vector2D position;
        Dimension size;
        
        public Craft(){
            this.size = new Dimension(10,10);
            
        }
        void shoot(){

        }
        void move(way moveWay){
            position.move(moveWay,this);
        }
        @Override
        public boolean isInsinde(int x, int y) {
            return(x<=(position.x + size.getWidth()) && x>=position.x && y>=position.y && y<=(position.y + size.getHeight()));
        }
    }
    class Bullet extends Thread{
        public Dimension caliber = new Dimension(5, 5);
        Vector2D position;
        way direction;
        public boolean friendly;
        public boolean fromPlayer = false;
        public Bullet(way direction, Vector2D position, boolean fromfriend){
            this.position = position;
            this.direction = direction;
            this.friendly =fromfriend;
            bullets.add(this);
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(true){
            position.move(direction,this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(!InBounds(position.x, position.y)){
                bullets.remove(this);
            }
            lock.lock();
            for (int i = 0; i<enemies.size(); i++) {
                Enemy enemy = enemies.get(i); 
                if(enemy.isInsinde(position.x, position.y)){
                    enemy.collisionControl(this);
                    bullets.remove(this);
                    
                }
            }
            for (int i = 0; i<friends.size(); i++) {
                Friend friend = friends.get(i);                
                if(friend.isInsinde(position.x, position.y)){
                    friend.collisionControl(this);
                    bullets.remove(this);
                }
            }
            if(player.isInsinde(position.x, position.y)){
                player.collisionControl(this);
                bullets.remove(this);
            }
            lock.unlock();
        }
        }
    }

    
    public class Friend extends Craft {
        boolean dead=false;
        public Friend(){
            Random rand = new Random();
            do{int x = (rand.nextInt(ScreenWidth/10)-1) *10;
                int y = (rand.nextInt(ScreenLength/10)-1)*10;
    
                this.position = new Vector2D(x, y);}
            while(positions.contains(this.position) || this.position == new Vector2D(ScreenWidth/2, ScreenLength/2));
            positions.add(this.position);
            friends.add(this);
        }

        @Override
        public void collisionControl(Object other) {
            // TODO Auto-generated method stub
            if(other instanceof Bullet){
                Bullet mag  = (Bullet) other;
                if(!mag.friendly){
                    friends.remove(this);
                    dead =true;
                }
            }
            else if(other instanceof Enemy){
                friends.remove(this);
                dead =true;
            }
            
        }

        
        @Override
        public void run() {
            int count =0;
            while(!dead){
            Random rand = new Random();
            int wa = rand.nextInt(4);
            this.move(way.values()[wa]);
            try {
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            count++;  
            if(count == 2){
                Bullet bullet1 = new Bullet(way.a,new Vector2D(this.position.x-this.size.width, this.position.y),true);
                bullet1.start();
                Bullet bullet2 = new Bullet(way.d,new Vector2D(this.position.x+this.size.width, this.position.y),true);
                bullet2.start();
                bullets.add(bullet1);
                bullets.add(bullet2);
                count =0;
            }
            }
        }
    }

    public class Enemy extends Craft {
        boolean dead=false;
        public Enemy(){
            
            Random rand = new Random();
            do{int x = rand.nextInt((ScreenWidth-10)/10) *10;
            int y = rand.nextInt((ScreenLength-10)/10)*10;

            this.position = new Vector2D(x, y);}
            while(positions.contains(this.position) || this.position == new Vector2D(ScreenWidth/2, ScreenLength/2));
            positions.add(this.position);
            enemies.add(this);
        }

        @Override
        public void collisionControl(Object other) {
            // TODO Auto-generated method stub
            if(other instanceof Bullet){
                Bullet mag  = (Bullet) other;
                if(mag.friendly){
                    enemies.remove(this);
                    dead=true;
                }
            }
            else if(other instanceof Friend){
                enemies.remove(this);
                dead = true;

            }
            
        }
        @Override
        public void run() {
            
            int count = 0;
            while(!dead){
                lock2.lock();
                try{
                    if(this.position.equals(player.position)){
                        player.collisionControl(this);
                    }
                    for (Friend friend : friends) {
                        if(this.position.equals(friend.position)){
                            friend.collisionControl(this);
                            this.collisionControl(friend);
                        }
                    }
                }catch(Exception e){};
                lock2.unlock();
            Random rand = new Random();
            int wa = rand.nextInt(4);
            this.move(way.values()[wa]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            count++;  
            if(count == 2){
                Bullet bullet1 = new Bullet(way.a,new Vector2D(this.position.x-this.size.width, this.position.y),false);
                bullet1.start();
                Bullet bullet2 = new Bullet(way.d,new Vector2D(this.position.x+this.size.width, this.position.y),false);
                bullet2.start();
                bullets.add(bullet1);
                bullets.add(bullet2);
                count =0;
            }
            }
        }

        
    }
    class Clock extends Thread{
        
        public Clock(){
            this.setDaemon(true);
        }
        public void run() {
            while (true){
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.getMessage());
                }
                repaint();
                if(enemies.size()==0){
                    End(true);
                    break;
                }
            }
        }
    }
    public void End(boolean good){
        EndFrame lossframe = new EndFrame("You Died");
        lossframe.setSize(300, 300);
        JLabel myLabel = new JLabel();
        if(!good)
        myLabel.setText("Oyunu kaybettiniz");
        if(good)
        myLabel.setText("Oyunu kazandınız");
        lossframe.add(myLabel);
        lossframe.setVisible(true);
    }
    public void closeWindow(){
        this.setVisible(false);
    }
    public class EndFrame extends JFrame implements WindowListener{

        public EndFrame(String name){
            super(name);
            addWindowListener(this);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        @Override
        public void windowOpened(WindowEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void windowClosing(WindowEvent e) {
            // TODO Auto-generated method stub
            closeWindow();
            
        }

        @Override
        public void windowClosed(WindowEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void windowIconified(WindowEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void windowActivated(WindowEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            // TODO Auto-generated method stub
            
        }

    }

    
}
