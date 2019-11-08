import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameWindow extends JFrame {

    private static GameWindow gameWindow;

    private static Image background_1;
    private static Image background_2;
    private static Image the_cat;
    private static Image cookie;
    private static Image rainbow;

    private static float ground_left = 0;
    private static float mouse_x = 0;
    private static float mouse_y = 0;
    private static int cookie_x = 0;
    private static int cookie_y = 0;
    private static int score;
    private static int groundSpeed = 1; //Скорость фона
    private static ArrayList <movableObject> groundTrack = new ArrayList<>(); //Список, который содержит в качество объектов фоны

    private static int rainbowSpeed = 5; //Скорость радуги
    private static ArrayList <movableObject> rainbowTrack = new ArrayList<>(); //Список, который содержит в качестве объектов каждый элемент радуги

    private static int cookieSpeed = 1; //Скорость печенек
    private static ArrayList <movableObject> cookiesSet = new ArrayList<>();

    private static int scaleOfCat = 65; //Масштаб кота.  1 / Доля площади окна, которую занимает кот
    private static int the_cat_width;
    private static int the_cat_height;
    private static int rainbow_width;
    private static int rainbow_height;
    private static int the_cookie_width;
    private static int the_cookie_height;
    private static int scaleOfCookie = 155;

    public static void main(String[] args) throws IOException {

        background_1 = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        background_2 = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        the_cat = ImageIO.read(GameWindow.class.getResourceAsStream("the_cat.png"));
        cookie = ImageIO.read(GameWindow.class.getResourceAsStream("cookie.png"));
        rainbow = ImageIO.read(GameWindow.class.getResourceAsStream("rainbow.png"));

        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(300, 200);
        gameWindow.setSize(800,539);
//        gameWindow.setResizable(false);

        rainbowTrack.add(0,new movableObject(0,0, rainbowSpeed));
        groundTrack.add(0, new movableObject(0,0, groundSpeed));

        cookie_y = the_cookie_height + (int) (Math.random() * gameWindow.getHeight() - the_cookie_height * 2);
        cookie_x = gameWindow.getWidth();
        cookiesSet.add(new movableObject(cookie_x, cookie_y, cookieSpeed));

                GameField gameField = new GameField();
        gameField.addMouseMotionListener(new MouseAdapter() {
                                             @Override
                                             public void mouseMoved(MouseEvent e) {
                                                 super.mouseMoved(e);
                                                 mouse_x = e.getX();
                                                 mouse_y = e.getY();

                                                 rainbowTrack.add(new movableObject((int) mouse_x - rainbow_width - the_cat_width / 2,(int) mouse_y - rainbow_height + the_cat_height / 2 , rainbowSpeed));

                                                 int cookie_y_center = (int) cookie_y + the_cookie_height/2;
                                                 int cookie_x_center = (int) cookie_x + the_cookie_width/2;
                                                 if (mouse_x > cookie_x_center - 20 && mouse_x < cookie_x_center + 20 && mouse_y > cookie_y_center- 20 && mouse_y < cookie_y_center + 20){
                                                     score++;
                                                     cookiesSet.remove(0);
                                                     gameWindow.setTitle("Score: " + score);
                                                     cookie_y = the_cookie_height + (int) (Math.random() * (gameWindow.getHeight() - the_cookie_height * 2));
                                                     cookie_x = gameWindow.getWidth();
                                                     cookiesSet.add(new movableObject(cookie_x,cookie_y,cookieSpeed));
                                                 }

                                             }
                                         });
        gameWindow.add(gameField);
        gameWindow.setVisible(true);
    }

    private static void onRepaint(Graphics g){

        //Рисуем фон. !!! Исправить проблему округления в районе нуля в (int) в механике работы класса movableObject
        //из-за чего картинка фона замедляется при приближении к левому краю (0 по X)
        for (int i = 0; i <= groundTrack.size() - 1; i++) {
            g.drawImage(
                    background_1,
                    groundTrack.get(i).getObjectX(),
                    groundTrack.get(i).getObjectY(),
                    gameWindow.getWidth(),
                    gameWindow.getHeight(),
                    null
            );
            groundTrack.get(i).moveLeft();

            if (groundTrack.get(groundTrack.size()-1).getObjectX() + gameWindow.getWidth() <= gameWindow.getWidth()){
                groundTrack.add(new movableObject(gameWindow.getWidth(), 0, groundSpeed));
            }

            System.out.println("delta " + groundTrack.get(i).delta_time);
            System.out.println("x " + groundTrack.get(i).x);
            System.out.println("objectX " + groundTrack.get(i).getObjectX());

        }

//        g.drawImage (background_1, (int) ground_left,0, gameWindow.getWidth(), gameWindow.getHeight(), null);
//        g.drawImage (background_2, (int) ground_left + gameWindow.getWidth(),0,  gameWindow.getWidth(), gameWindow.getHeight(),null);
//        if (ground_left - groundSpeed > - background_1.getWidth(null)) {
//            ground_left = ground_left - groundSpeed;
//        } else { ground_left = ground_left + background_2.getWidth(null); }

        //Находим размеры кота. Размер зависит от scaleOfCat, указанного в пропорции от площади окна. Определяем место где его рисовать.
        the_cat_height = (int) Math.sqrt(gameWindow.getHeight() * gameWindow.getWidth() * the_cat.getHeight(null) / the_cat.getWidth(null) / scaleOfCat);
        the_cat_width = (int) Math.sqrt(gameWindow.getHeight() * gameWindow.getWidth() * the_cat.getWidth(null) / the_cat.getHeight(null) / scaleOfCat);
        int cat_x = (int) mouse_x - the_cat_width;
        int cat_y = (int) mouse_y - the_cat_height /2;

        //Определяем размер печенек
        the_cookie_height = (int) Math.sqrt(gameWindow.getHeight() * gameWindow.getWidth() * cookie.getHeight(null) / cookie.getWidth(null) / scaleOfCookie);
        the_cookie_width = (int) Math.sqrt(gameWindow.getHeight() * gameWindow.getWidth() * cookie.getWidth(null) / cookie.getHeight(null) / scaleOfCookie);

        //Находим размер радуги. Высота привязана к высоте кота, вирина исчисляется относительно высоты.
        rainbow_height = the_cat_height;
        rainbow_width = rainbow.getWidth(null) * rainbow_height / rainbow.getWidth(null);

        //В цикле рисуем радугу от конца кота до левого края экрана с шагом в ширину одной картинки радуги
//        for  (int i = cat_x; i > - rainbow.getWidth(null) ; i = i - rainbow.getWidth(null) ){
//            g.drawImage(rainbow, i, cat_y, null);
//        }

        for (int i = 0; i <= rainbowTrack.size() - 1; i++){
            g.drawImage (
                    rainbow,
                    rainbowTrack.get(i).getObjectX(),
                    rainbowTrack.get(i).getObjectY(),
                    rainbow_width,
                    rainbow_height,
                    null
            );

            rainbowTrack.get(i).moveLeft();
            if (rainbowTrack.get(i).getIterationOfMove() > 100){
                rainbowTrack.remove(i);
            }

        }

        //Рисуем кота, печенье и счет
        g.drawImage(the_cat,
                cat_x,
                cat_y,
                the_cat_width,
                the_cat_height,
                null); //Рисуем кота
//        g.drawImage(cookie, (int) cookie_x, (int) cookie_y, the_cookie_width, the_cookie_height, null);
//        Рисуем печенье

        for (int i = 0; i <= cookiesSet.size() - 1; i++) {
            g.drawImage(
                    cookie,
                    (int) cookiesSet.get(i).getObjectX(),
                    cookiesSet.get(i).getObjectY(),
                    the_cookie_width,
                    the_cookie_height,
                    null
            );
            cookiesSet.get(i).moveLeft();

        }
        g.drawString("Счет: " + score, 20,20); //Рисуем счет

    }

    private static class GameField extends JPanel{
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }



}
