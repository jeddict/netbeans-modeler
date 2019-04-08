/**
 * Copyright 2013-2019 Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 *
 */
public class Tester extends JFrame {

    private static final long serialVersionUID = 1L;

    public Tester() {
        setTitle("Custom Component Graphics2D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void display() {
        add(new TesterPanel());
        pack();
        // enforces the minimum size of both frame and component        
        setMinimumSize(getSize());
        setVisible(true);
    }

    public static void main(String[] args) {
        Tester main = new Tester();
        main.display();

        Rectangle r = new Rectangle(50, 50, 400, 300);
        //System.out.println("contain : " + r.contains(new Point(60 , 60 )));

    }
}

class TesterPanel extends JPanel {

    TesterPanel() {
        Dimension g = new Dimension(600, 400);
        this.setPreferredSize(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.red);
        g.drawRect(50, 50, 400, 300);
        g.fillRect(60, 60, 3, 3);

    }
}
