package com.nylp;

import javax.swing.*;

public class Parmonizer {
    private JPanel panel1;
    private JButton button1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Parmonizer");
        frame.setContentPane(new Parmonizer().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
