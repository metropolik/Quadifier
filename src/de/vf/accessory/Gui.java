package de.vf.accessory;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import java.awt.FlowLayout;

public class Gui {

    private JFrame frame;
    private Gridder panel_2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Gui window = new Gui();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Gui() {
        initialize();
        
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 730, 645);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);
        
        JPanel panel_1 = new JPanel();
        sl_panel.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, panel);
        panel.add(panel_1);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                                      
        panel_2 = new Gridder();
        sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 0, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, panel_1, 0, SpringLayout.EAST, panel_2);
        sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, 0, SpringLayout.SOUTH, panel);
        
        JButton btnNewButton_3 = new JButton("Quadify");
        btnNewButton_3.addActionListener( new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {                
                panel_2.quadify();                                      
            }
        });
        panel_1.add(btnNewButton_3);
        sl_panel.putConstraint(SpringLayout.NORTH, panel_2, 0, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, panel_2, 0, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, panel_2, 0, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, panel_2, -100, SpringLayout.EAST, panel);        
        panel_2.setBackground(Color.CYAN);
        panel.add(panel_2);
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(frame);
    }
}
