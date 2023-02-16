package org.mealsApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class GUI extends JFrame {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    public GUI(String title) throws HeadlessException {
        super(title);
    }


    private final int BUTTONWIDTH = 400;
    private final int BUTTONHEIGHT = 50;

    public void Simple() {
//        close app on exit
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
//        create two panels to fill the split image
        Panel panel1 = new Panel(null);
        Panel panel2 = new Panel(null);
//      fill the split panel 2 with image
        JLabel panel2bg = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/src/assets/mealsAppImg.jpg"));

        panel2bg.setSize(new Dimension(1000, 900));
        panel2bg.setVisible(true);
//      set a tbbePanel as content to the split Panel
        tabbedPane = new JTabbedPane();
//      split panels content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, tabbedPane);
        tabbedPane.add("Meals Application", panel2);
        panel2.add(panel2bg);
        add(splitPane);
        tabbedPane.setBackground(new Color(100, 100, 100,70));

        tabbedPane.setBorder(null);
        panel1.setBackground(new Color(83, 83, 83));
        splitPane.setSize(new Dimension(1400, 850));
        panel1.setSize(new Dimension(400, 900));

        splitPane.setOneTouchExpandable(false);
        splitPane.isVisible();
        splitPane.setEnabled(false);

        JButton anazitisiGeymatos = new JButton("Αναζήτηση Γευμάτων");//creating instance of JButton
        anazitisiGeymatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchMealButton jp = new SearchMealButton(tabbedPane);
                tabbedPane.add("Αναζήτηση Γευμάτων",jp);
                jp.openSearchField();
            }
        });

        JButton anazitisiKatigorias = new JButton("Αναζήτηση Κατηγοριών");//creating instance of JButton
        anazitisiKatigorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchCategoriesButton jf = new SearchCategoriesButton(tabbedPane);
                tabbedPane.add("Κατηγορίες Γευμάτων",jf);
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
        anazitisiGeymatos.setBounds(0, 20, BUTTONWIDTH, BUTTONHEIGHT);
        anazitisiGeymatos.setFont(new Font("Arial", Font.PLAIN, 14));
        anazitisiGeymatos.setBorder(BorderFactory.createLineBorder(Color.white));
        anazitisiGeymatos.setContentAreaFilled(false);
        anazitisiGeymatos.setOpaque(false);
        anazitisiGeymatos.setForeground(Color.white);

        anazitisiKatigorias.setBounds(0, 70, BUTTONWIDTH, BUTTONHEIGHT);
        anazitisiKatigorias.setFont(new Font("Arial", Font.PLAIN, 14));
        anazitisiKatigorias.setBorder(BorderFactory.createLineBorder(Color.white));
        anazitisiKatigorias.setContentAreaFilled(false);
        anazitisiKatigorias.setOpaque(false);
        anazitisiKatigorias.setForeground(Color.white);


        ektypwshStatistikwn.setBounds(0, 120, BUTTONWIDTH, BUTTONHEIGHT);
        ektypwshStatistikwn.setFont(new Font("Arial", Font.PLAIN, 14));
        ektypwshStatistikwn.setBorder(BorderFactory.createLineBorder(Color.white));
        ektypwshStatistikwn.setContentAreaFilled(false);
        ektypwshStatistikwn.setOpaque(false);
        ektypwshStatistikwn.setForeground(Color.white);

        eksodos.setBounds(0, 170, BUTTONWIDTH, BUTTONHEIGHT);
        eksodos.setFont(new Font("Arial", Font.PLAIN, 14));
        eksodos.setBorder(BorderFactory.createLineBorder(Color.white));
        eksodos.setContentAreaFilled(false);
        eksodos.setOpaque(false);
        eksodos.setForeground(Color.white);

        getContentPane().add(splitPane);
        panel1.add(anazitisiGeymatos);
        panel1.add(anazitisiKatigorias);
        panel1.add(ektypwshStatistikwn);
        panel1.add(eksodos);
        String str1 = "Make your ";
        String str2 = "meals ";
        String str3 = "come true!";

        JLabel panel1Content = new JLabel("<html><font color=white>" + str1 + "<B>" + str2 + "</B>" +str3 +"</font></html>  ", SwingConstants.CENTER);
        panel1Content.setBounds(0, 450, 400, 100);
        panel1Content.setFont(new Font("Serif", Font.PLAIN, 28));
        panel1Content.setVisible(true);
        panel1.add(panel1Content);


//        pack();
        setSize(1400, 900);
        setResizable(false);
        setLayout(null);//using no layout managers
        setLocationRelativeTo(null);
        setVisible(true);//making the frame visible
    }


}
