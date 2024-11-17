package BrickBreaker;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // creating the top level container
        JFrame obj = new JFrame();

        // initializing Game Play
        GamePlay gamePlay = new GamePlay();

        // setting the properties of the container
        obj.setBounds(10, 10, 714, 600); // (10,10,700,600)
        obj.setTitle("BrickBreaker Game");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //adding the gameplay object to the container
        obj.add(gamePlay);
    }
}
