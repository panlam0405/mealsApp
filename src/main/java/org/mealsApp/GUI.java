package org.mealsApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JFrame frame;

    public GUI(String title) throws HeadlessException {
        super(title);
    }

    private final int BUTTONWIDTH = 180;
    private final int BUTTONHEIGHT = 50;

    public void Simple() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton anazitisiGeymatos = new JButton("Αναζήτηση Γευμάτων");//creating instance of JButton
        anazitisiGeymatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchMealButton jf = new SearchMealButton("Αναζήτηση Γεύματος");
                jf.openSearchField();
            }
        });

        JButton anazitisiKatigorias = new JButton("Αναζήτηση Κατηγοριών");//creating instance of JButton
        anazitisiKatigorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchCategoriesButton jf = new SearchCategoriesButton("Κατηγορίες Γευμάτων");
                jf.openCategoriesFrame();
            }
        });

        JButton ektypwshStatistikwn = new JButton("Αναζήτηση Στατιστικών");//creating instance of JButton
        JButton eksodos = new JButton("Εξοδος");//creating instance of JButton
        eksodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        anazitisiGeymatos.setBounds(30, 100, BUTTONWIDTH, BUTTONHEIGHT);
        anazitisiKatigorias.setBounds(240, 100, BUTTONWIDTH, BUTTONHEIGHT);
        ektypwshStatistikwn.setBounds(30,200,BUTTONWIDTH,BUTTONHEIGHT);
        eksodos.setBounds(240,200,BUTTONWIDTH,BUTTONHEIGHT);

        add(anazitisiGeymatos);
        add(anazitisiKatigorias);
        add(ektypwshStatistikwn);
        add(eksodos);

        setSize(500, 400);//400 width and 500 height
        setLayout(null);//using no layout managers
        setVisible(true);//making the frame visible
    }


}
