/**
 * Copyright [2014] Gaurav Gupta
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
package org.netbeans.modeler.widget.context.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.context.PaletteDirection;
import org.netbeans.modeler.widget.context.SwingPaletteManager;
import org.netbeans.modeler.widget.node.IWidget;

/**
 *
 *
 */
public class ContextPalette extends JPanel {

    private static final int PULLOUT_MARGIN = 3;

    private ContextPaletteModel model = null;
    private Dimension collapsedSize = null;
    private ComboButton activePullout = null;
    private PaletteDirection direction = PaletteDirection.RIGHT;
    private Border RIGHT_BORDER = new EmptyBorder(5, 2, 5, 2);//this border prints overall combo container that needs to hide so empty border
    private Border LEFT_BORDER = new EmptyBorder(5, 2, 5, 2);//this border prints overall combo container that needs to hide so empty border

    static final Color BACKGROUND = new Color(242, 249, 253);
    static final Color BORDER_COLOR = new Color(181, 198, 209);//new Color(181, 198 , 209);

    public ContextPalette() {
        super();
    }

    public ContextPalette(ContextPaletteModel model) {
        setModel(model);
    }

    protected void initalizePalette() {
        removeAll();
        JComponent[] buttonList = buildChildrenList();

        JPanel container = new JPanel();

        container.setLayout(new GridLayout(buttonList.length, 1, 2, 2));
        for (JComponent btn : buttonList) {
            container.add(btn);
        }

        setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);

        setOpaque(false);
//        // The green background is for debuging.  Usally the container is not
//        // opaque, so it does not really matter.
//        container.setBackground(Color.GREEN);
        container.setOpaque(false);

        container.setBorder(RIGHT_BORDER);

        collapsedSize = getPreferredSize();

    }

    public int getExpandedWidth() {
        int retVal = getPreferredSize().width;

        JPanel container = (JPanel) getComponent(0);
        for (Component child : container.getComponents()) {
            if (child instanceof ComboButton) {
                ComboButton btn = (ComboButton) child;
                int fullWidth = btn.getExpandedWidth();
                retVal = Math.max(retVal, fullWidth);
            }

        }

        return retVal;
    }

    public void setDirection(PaletteDirection dir) {
        direction = dir;

        JPanel container = (JPanel) getComponent(0);
        for (Component child : container.getComponents()) {
            if (child instanceof ComboButton) {
                ComboButton btn = (ComboButton) child;
                btn.setDirection(direction);
            } else if (child instanceof PaletteButton) {
                PaletteButton btn = (PaletteButton) child;
                btn.setDirection(direction);
            }
        }

        Border border = RIGHT_BORDER;
        if (dir == PaletteDirection.LEFT) {
            border = LEFT_BORDER;
        }
        container.setBorder(border);
    }

    public PaletteDirection getDirection() {
        return direction;
    }

    private JComponent[] buildChildrenList() {
        //  //System.out.println(" ContextPalette buildChildrenList....");
        ArrayList<JComponent> widgetList = new ArrayList< JComponent>();
//        System.out.println("buildChildrenList __________________________________________________________________");
        ArrayList< ContextPaletteButtonModel> children = model.getChildren();

        ButtonListener buttonListener = new ButtonListener();
        for (ContextPaletteButtonModel componentModel : children) {
            System.out.println("componentModel : " + componentModel.getTooltip());
            if(componentModel.isVisible()){
            if (componentModel.isGroup() == true) {
                ComboButton btn = new ComboButton(model.getContext(), componentModel);
                btn.setDirection(getDirection());
                btn.addComboButtonListener(new ComboListener());
                btn.addContextButtonListener(buttonListener);
                widgetList.add(btn);
            } else {
                PaletteButton btn = new PaletteButton(model.getContext(),
                        componentModel,
                        getDirection(), true);
                widgetList.add(btn);
                btn.addContextButtonListener(buttonListener);
            }
            }
        }

        JComponent[] retVal = new JComponent[widgetList.size()];
        widgetList.toArray(retVal);
        return retVal;
    }

    @Override
    public boolean requestFocusInWindow() {
        boolean retVal = super.requestFocusInWindow();

        if (retVal == true) {
            JPanel panel = (JPanel) getComponent(0);
            if (panel != null) {
                // get the first button
                retVal = panel.getComponent(0).requestFocusInWindow();
            }
        }

        return retVal;
    }

    ///////////////////////////////////////////////////////////////
    // Data Access Methods
    public ContextPaletteModel getModel() {
        return model;
    }

    public void setModel(ContextPaletteModel model) {
        this.model = model;
        initalizePalette();
    }

    @Override
    public void paint(Graphics g) {
        Color origColor = g.getColor();

        g.setColor(BACKGROUND);

        int x = 0;
        if (getDirection() == PaletteDirection.LEFT) {
            x = getWidth() - collapsedSize.width;
        }

        g.fillRect(x, 0, collapsedSize.width, collapsedSize.height);
        g.setColor(origColor);
        super.paint(g);

        if (getDirection() == PaletteDirection.RIGHT) {
            paintFancyRightBorder(g);
        } else {
            paintFancyLeftBorder(g);
        }
    }

    private void paintFancyRightBorder(Graphics g) {

        int barWidth = collapsedSize.width;
        int barHeight = collapsedSize.height;

        int popoutStartY = -1;
        int popoutEndY = -1;
        int popoutWidth = -1;

        if (activePullout != null) {
            Insets insets = getInsets();

            popoutStartY = activePullout.getY() - PULLOUT_MARGIN;
            popoutEndY = popoutStartY + activePullout.getHeight() + (PULLOUT_MARGIN * 2);
            popoutWidth = activePullout.getWidth() + insets.right;
        }

        Color origColor = g.getColor();

        int left = 0;
        int top = 0;

        if (popoutStartY > -1) {
            g.setColor(BORDER_COLOR);
            g.drawRoundRect(0, 0, barWidth - 1, barHeight - 1, 5, 5);

            // Popout
            int popoutStartX = left + barWidth;
            int popoutEndX = left + popoutWidth + 1;

            // First Paint the pullout margin
            g.setColor(BACKGROUND);
            int width = popoutEndX - popoutStartX - 1;
            g.fillRect(popoutStartX, popoutStartY, width, PULLOUT_MARGIN);
            g.fillRect(popoutStartX, popoutEndY - PULLOUT_MARGIN, width, PULLOUT_MARGIN + 1);

            // Now paint the border.
            g.setColor(BORDER_COLOR);

            g.drawArc(popoutStartX, popoutStartY - 5, 5, 5, 180, 90);
//            g.setColor(g.getColor().brighter());
            g.drawLine(popoutStartX + PULLOUT_MARGIN, popoutStartY, popoutEndX - PULLOUT_MARGIN, popoutStartY);

//            g.setColor(g.getColor().brighter());
            g.drawArc(popoutEndX - 5, popoutStartY, 5, 5, 0, 90);
//            g.setColor(g.getColor().brighter());
            g.drawLine(popoutEndX, popoutStartY + 2, popoutEndX, popoutEndY - PULLOUT_MARGIN);
//            g.setColor(g.getColor().brighter());
            g.drawLine(popoutStartX + PULLOUT_MARGIN, popoutEndY, popoutEndX - PULLOUT_MARGIN, popoutEndY);
//            g.setColor(g.getColor().brighter());
            g.drawArc(popoutEndX - 5, popoutEndY - 5, 5, 5, 270, 90);
//            g.setColor(g.getColor().brighter());
            g.drawArc(popoutStartX, popoutEndY, 5, 5, 90, 90);
        } else {
            g.setColor(BORDER_COLOR);
            g.drawRoundRect(0, 0, barWidth - 1, barHeight - 1, 5, 5);
        }

        g.setColor(origColor);
    }

    private void paintFancyLeftBorder(Graphics g) {

        int barWidth = collapsedSize.width;
        int height = collapsedSize.height;
        int popoutStartY = -1;
        int popoutEndY = -1;
        int popoutWidth = -1;

        if (activePullout != null) {
            Insets insets = getInsets();
            popoutStartY = activePullout.getY() - PULLOUT_MARGIN;
            popoutEndY = popoutStartY + activePullout.getHeight() + (PULLOUT_MARGIN * 2);
            popoutWidth = activePullout.getWidth() + insets.left;
        }
        Color origColor = g.getColor();
        int barLeft = getWidth() - barWidth;
        // Adjust the popoutWidth to not account for the bar.
        popoutWidth -= barWidth;
        if (popoutStartY > -1) {
            g.setColor(BORDER_COLOR);
            g.drawRoundRect(barLeft, 0, barWidth - 1, getHeight() - 1, 5, 5);
            int popoutLeft = barLeft - popoutWidth;
            // First Paint the pullout margin
            g.setColor(BACKGROUND);
            int width = barLeft - popoutLeft - 1;
            g.fillRect(popoutLeft + 1, popoutStartY, width, PULLOUT_MARGIN);
            g.fillRect(popoutLeft + 1, popoutEndY - PULLOUT_MARGIN, width, PULLOUT_MARGIN + 1);
            // Popout
            g.setColor(BORDER_COLOR);
            g.drawArc(barLeft - 6, popoutStartY - 6, 5, 5, 270, 90);
            g.drawLine(barLeft - PULLOUT_MARGIN, popoutStartY, popoutLeft + PULLOUT_MARGIN, popoutStartY);
            g.drawArc(popoutLeft, popoutStartY, 5, 5, 90, 90);
            g.drawLine(popoutLeft, popoutStartY + 2, popoutLeft, popoutEndY - PULLOUT_MARGIN);
            g.drawLine(popoutLeft + PULLOUT_MARGIN, popoutEndY, barLeft - PULLOUT_MARGIN, popoutEndY);
            g.drawArc(popoutLeft, popoutEndY - 5, 5, 5, 180, 90);
            g.drawArc(barLeft - 6, popoutEndY, 5, 5, 0, 90);
        } else {
            g.setColor(BORDER_COLOR);
            g.drawRoundRect(0, 0, barWidth - 1, height - 1, 5, 5);
        }

        g.setColor(origColor);
    }

    ///////////////////////////////////////////////////////////////
    // Helper Methods
    protected void updateSize(boolean expanded) {
        int width = -1;

        for (Component child : ((JPanel) getComponent(0)).getComponents()) {
            Dimension dim = child.getPreferredSize();
            int childWidth = dim.width;

            if (childWidth > width) {
                width = childWidth;
            }

            child.setSize(dim);
        }

        if (expanded == true) {
            setSize(new Dimension(width + 8, getHeight()));
            updatePosition(expanded, width + 8);
        } else {
            setSize(collapsedSize);
            updatePosition(expanded, collapsedSize.width);
        }
    }

    protected void updatePosition(boolean expanded, int width) {
        if (getDirection() == PaletteDirection.LEFT) {
            IWidget widget = model.getContext();
            IModelerScene scene = (IModelerScene) widget.getScene();
            Point location = widget.getPreferredLocation();
            location = widget.getParentWidget().convertLocalToScene(location);
            Point viewLocaton = scene.convertSceneToView(location);
            int xPos = viewLocaton.x - SwingPaletteManager.SPACE_FROM_WIDGET - width;
            setLocation(xPos, getY());
        }
    }

    ///////////////////////////////////////////////////////////////
    // Helper classes
    private class ComboListener implements ComboButtonListener {

        private boolean responding = false;

        public ComboListener() {
        }

        @Override
        public void expandStateChanged(ComboButton btn, boolean expanded) {

            if (expanded == true) {
                responding = true;

                try {
                    if (activePullout != null) {
                        activePullout.setExpanded(false);
                    }
                    activePullout = btn;

                } finally {
                    responding = false;
                }
            } else if (responding == false) {
                activePullout = null;
            }

            updateSize(expanded);
        }
    }

    private class ButtonListener implements ContextButtonListener {

        @Override
        public void actionPerformed(PaletteButton source, boolean locked) {
            ////System.out.println("ContextPalette ButtonListener  actionPerformed...");
            if (locked == true) {
                setVisible(false);
            } else if (isVisible() == false) {
                setVisible(true);
            }
        }

    }
}
