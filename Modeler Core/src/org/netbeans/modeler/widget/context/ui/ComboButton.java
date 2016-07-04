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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.netbeans.modeler.widget.context.ContextPaletteButtonModel;
import org.netbeans.modeler.widget.context.PaletteDirection;
import org.netbeans.modeler.widget.node.IWidget;

/**
 *
 *
 */
public class ComboButton extends ContextPaletteButton {

    private static final Border RIGHT_POPOUT_BORDER = BorderFactory.createEmptyBorder(1, 4, 1, 1);
    private static final Border RIGHT_POPOUT_FOCUSBORDER
            = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIManager.getColor("List.selectionBackground"), 11),
                    BorderFactory.createEmptyBorder(0, 2, 0, 0));
    private static final Border LEFT_POPOUT_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 4);
    private static final Border LEFT_POPOUT_FOCUSBORDER
            = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIManager.getColor("List.selectionBackground"), 11),
                    BorderFactory.createEmptyBorder(0, 0, 0, 3));
    private IWidget associatedWidget = null;
    private ContextPaletteButtonModel model = null;
    private ArrayList< ComboButtonListener> listeners
            = new ArrayList< ComboButtonListener>();
    private boolean expanded = false;
    private PaletteDirection direction = PaletteDirection.RIGHT;
    private ArrayList< ContextButtonListener> buttonListeners
            = new ArrayList< ContextButtonListener>();
    private ButtonListener myButtonListener = new ButtonListener();

    public ComboButton(IWidget context, ContextPaletteButtonModel desc) {
        setContext(context);
        setModel(desc);

        setLayout(new BorderLayout());

        setBackground(ContextPalette.BACKGROUND);
        setExpanded(false);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
//                setExpanded(false);
            }
        });

        InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LeftAction");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RightAction");
        getActionMap().put("LeftAction", new LeftMoveButtonAction());
        getActionMap().put("RightAction", new RightMoveButtonAction());
    }

    @Override
    protected Border getFocusBorder() {
        Border retVal = BorderFactory.createLineBorder(Color.PINK, 2);
//        if (isExpanded() == true) {
//            retVal = BorderFactory.createEmptyBorder(1, 1, 1, 1);
//        }

        return retVal;
    }

    @Override
    protected Border getNonFocusedBorder() {
        return BorderFactory.createLineBorder(Color.GREEN, 2);//1, 1, 1, 1);
    }

    /**
     * Initializes the control to contain all of the buttons need to support the
     * expanded state. If the expand state is true the hidden actions are also
     * shown. If the add parameter is true, the initialized component will be
     * added to the button.
     *
     * @param expand build the component as if the button is expanded.
     * @param add adds the components to the button.
     */
    private JComponent initializeComponents(boolean expand, boolean add) {
        Box container = Box.createHorizontalBox();

        if (getDirection() == PaletteDirection.RIGHT) {
            createMainButton(container);
//            container.add(Box.createHorizontalStrut(5));
            createPopout(container, expand);
        } else {
            createPopout(container, expand);
//            container.add(Box.createHorizontalStrut(5));
            createMainButton(container);
        }

        if (add == true) {
            removeAll();

            JPanel filler = new JPanel();
            filler.setOpaque(false);
            filler.setPreferredSize(new Dimension(0, 0));

            setLayout(new BorderLayout());

            if (getDirection() == PaletteDirection.RIGHT) {
                add(container, BorderLayout.WEST);
            } else {
                add(container, BorderLayout.EAST);
            }

//            add(filler, BorderLayout.CENTER);
            setSize(getPreferredSize());
        }

//        JComponent lastComp = null;
//        for (int i = 1; i < container.getComponents().length; i++) { // 1: skip mainbutton
//            Component component = container.getComponents()[i];
//            if (component instanceof JComponent) {
//                ((JComponent) component).setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, ContextPalette.BORDER_COLOR));
//                lastComp = (JComponent) component;
//            }
//        }
//        if (lastComp != null) {
//            lastComp.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, ContextPalette.BORDER_COLOR));
//        }
        return container;
    }

    protected void createPopout(Box container, boolean expand) {
        if (expand == true) {
            ArrayList<ContextPaletteButtonModel> popupContents = model.getChildren();
            if (getDirection() == PaletteDirection.RIGHT) {
                for (ContextPaletteButtonModel curDesc : popupContents) {
                    PaletteButton curBtn = new PaletteButton(associatedWidget,
                            curDesc,
                            getDirection(),
                            false) {
                                @Override
                                protected Border getFocusBorder() {
                                    return RIGHT_POPOUT_FOCUSBORDER;
                                }

                                @Override
                                protected Border getNonFocusedBorder() {
                                    return RIGHT_POPOUT_BORDER;
                                }
                            };

                    curBtn.addContextButtonListener(myButtonListener);
//                    curBtn.setBorder(RIGHT_POPOUT_BORDER);

                    InputMap inputMap = curBtn.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
                    inputMap.remove(UP_KEYSTROKE);
                    inputMap.remove(DOWN_KEYSTROKE);

//                    curBtn.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.CYAN));
                    container.add(curBtn);
                    Box.Filler strut = (Box.Filler) Box.createHorizontalStrut(4);
                    strut.setOpaque(true);
                    strut.setBackground(ContextPalette.BACKGROUND);
                    container.add(strut);
                    curBtn.setOpaque(true);
                    curBtn.setBackground(ContextPalette.BACKGROUND);
                }

            } else {
                for (int index = popupContents.size() - 1; index >= 0; index--) {
                    ContextPaletteButtonModel curDesc = popupContents.get(index);
                    PaletteButton curBtn = new PaletteButton(associatedWidget,
                            curDesc,
                            getDirection(),
                            false) {
                                @Override
                                protected Border getFocusBorder() {
                                    return LEFT_POPOUT_FOCUSBORDER;
                                }

                                @Override
                                protected Border getNonFocusedBorder() {
                                    return LEFT_POPOUT_BORDER;
                                }
                            };

                    curBtn.addContextButtonListener(myButtonListener);
//                    curBtn.setBorder(LEFT_POPOUT_BORDER);
                    curBtn.getInputMap().remove(UP_KEYSTROKE);
                    curBtn.getInputMap().remove(DOWN_KEYSTROKE);

                    container.add(curBtn);
                    Box.Filler strut = (Box.Filler) Box.createHorizontalStrut(4);
                    strut.setOpaque(true);
                    strut.setBackground(ContextPalette.BACKGROUND);
                    container.add(strut);
                    curBtn.setOpaque(true);
                    curBtn.setBackground(ContextPalette.BACKGROUND);
                }
            }
        }
    }

    protected void createMainButton(Box container) {
        ArrowButton popupBtn = new ArrowButton();
        ContextPaletteButtonModel mainDesc = model;//.getChildren().get(0);
        PaletteButton btn = new PaletteButton(associatedWidget,
                mainDesc,
                getDirection(),
                false);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                setExpanded(!isExpanded());
            }

//            public void mouseEntered(MouseEvent me) {
//                setExpanded(true);
//            }
//
//            public void mouseExited(MouseEvent me) {
//                setExpanded(false);
//            }
        });

        Box mainBtnPanel = Box.createHorizontalBox();
        if (getDirection() == PaletteDirection.RIGHT) {
            mainBtnPanel.add(btn);
            mainBtnPanel.add(popupBtn);
        } else {
            mainBtnPanel.add(popupBtn);
            mainBtnPanel.add(btn);
        }

//        mainBtnPanel.getPreferredSize();
//        mainBtnPanel.setBorder(getNonFocusedBorder());
        container.add(mainBtnPanel);
    }

    protected void fireComboExpandChanged() {
        for (ComboButtonListener listener : listeners) {
            listener.expandStateChanged(this, expanded);
        }
    }

    public void addComboButtonListener(ComboButtonListener listener) {
        listeners.add(listener);
    }

    public void removeComboBUttonListener(ComboButtonListener listener) {
        listeners.remove(listener);
    }

    ///////////////////////////////////////////////////////////////
    // Button listener methods.
    public void addContextButtonListener(ContextButtonListener listener) {
        buttonListeners.add(listener);
    }

    public void removeContextButtonListener(ContextButtonListener listener) {
        buttonListeners.remove(listener);
    }

    /////////////////////////////////////////////////////////
    // Getter/Setters
    public IWidget getContext() {
        return associatedWidget;
    }

    public void setContext(IWidget context) {
        this.associatedWidget = context;
    }

    public ContextPaletteButtonModel getModel() {
        return model;
    }

    public void setModel(ContextPaletteButtonModel description) {
        this.model = description;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        initializeComponents(expanded, true);
        revalidate();

        setOpaque(false);

//        setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));//BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fireComboExpandChanged();
    }

    int getExpandedWidth() {
        int retVal = getPreferredSize().width;

        if (isExpanded() == false) {
            JComponent item = initializeComponents(true, false);
            retVal = item.getPreferredSize().width;
        }

        return retVal;
    }

    public void setDirection(PaletteDirection direction) {
        this.direction = direction;

        initializeComponents(isExpanded(), true);
        revalidate();
    }

    public PaletteDirection getDirection() {
        return direction;
    }

    /////////////////////////////////////////////////////////
    // Inner Classes
    public class ArrowButton extends JPanel {

        private static final int ARROW_HEIGHT = 6;
        private static final int ARROW_WIDTH = ARROW_HEIGHT / 2;

        public ArrowButton() {
            setBorder(null);
            setPreferredSize(new Dimension(ARROW_WIDTH + 4, ARROW_HEIGHT));
            setMaximumSize(new Dimension(ARROW_WIDTH + 4, 64));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    setExpanded(!isExpanded());
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            int height = getBounds().height;

            int halfHeight = ARROW_HEIGHT / 2;
            int startX = 2;
            int startY = height / 2 - halfHeight;

            int[] xPoints = {startX, startX + ARROW_WIDTH, startX};
            int[] yPoints = {startY, startY + halfHeight, startY + ARROW_HEIGHT};

            if (getDirection() == PaletteDirection.LEFT) {
                startX = getWidth() - 2;
                xPoints = new int[]{startX, startX - ARROW_WIDTH, startX};
            }

//            Color origColor = g.getColor();
//            g.setColor(Color.BLACK);
            g.setColor(ContextPalette.BORDER_COLOR);
            g.fillPolygon(xPoints, yPoints, 3);
//            g.setColor(origColor);
        }
    }

    private void setFocusToFirstChild() {
        if (getComponent(0) instanceof Container) {
            Container container = (Container) getComponent(0);

            if (isExpanded() == true) {
                if (getDirection() == PaletteDirection.RIGHT) {
                    container.getComponent(2).requestFocusInWindow();
                } else {
                    int firstChild = container.getComponentCount() - 4;
                    container.getComponent(firstChild).requestFocusInWindow();
                }
            } else {
                requestFocusInWindow();
            }
        }
    }

    @Override
    protected void moveFocusToNextSibling(int startChildIndex, int endChildIndex) {
        setExpanded(false);
        super.moveFocusToNextSibling(startChildIndex, endChildIndex);
    }

    @Override
    protected void moveFocusToPreviousSibling(int startChildIndex, int endChildIndex) {
        setExpanded(false);
        super.moveFocusToPreviousSibling(startChildIndex, endChildIndex);
    }

    private PaletteButton getFocusedChild() {
        PaletteButton retVal = null;

        if (getComponent(0) instanceof Container) {
            Container container = (Container) getComponent(0);
            for (Component child : container.getComponents()) {
                if (child instanceof PaletteButton) {
                    PaletteButton button = (PaletteButton) child;
                    if (button.isFocusOwner() == true) {
                        retVal = button;
                        break;
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * LeftMoveButtonAction handles the left arrow keystroke. When the combo
     * button is collapsed the button will first be expanded, then the first
     * child will be selected.
     */
    public class LeftMoveButtonAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isExpanded() == false) {
                if (getDirection() == PaletteDirection.LEFT) {
                    setExpanded(true);
                    setFocusToFirstChild();
                } else {
                    setExpanded(false);
                    setFocusToFirstChild();
                }
            } else {
                PaletteButton curFocus = getFocusedChild();
                if (curFocus != null) {
                    if (getDirection() == PaletteDirection.LEFT) {
                        int endChildIndex = getModel().getChildren().size() - 1;
                        curFocus.moveFocusToNextSibling(endChildIndex, -1);
                    } else {
                        curFocus.moveFocusToPreviousSibling(2, END_CHILD_INDEX);
                    }
                }
            }
        }
    }

    public class RightMoveButtonAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isExpanded() == false) {
                if (getDirection() == PaletteDirection.LEFT) {
                    setExpanded(false);
                    setFocusToFirstChild();
                } else {
                    setExpanded(true);
                    setFocusToFirstChild();
                }
            } else {
                PaletteButton curFocus = getFocusedChild();
                if (curFocus != null) {
                    if (getDirection() == PaletteDirection.RIGHT) {
                        curFocus.moveFocusToNextSibling(2, END_CHILD_INDEX);
                    } else {
                        int endChildIndex = getModel().getChildren().size() - 1;
                        curFocus.moveFocusToPreviousSibling(endChildIndex, 0);
                    }
                }
            }
        }
    }

    public class ButtonListener implements ContextButtonListener {

        @Override
        public void actionPerformed(PaletteButton source, boolean locked) {
            //System.out.println("Combo ButtonListener actionPerformed");
            for (ContextButtonListener listener : buttonListeners) {
                listener.actionPerformed(source, locked);
            }
        }
    }
}
