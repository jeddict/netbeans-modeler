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

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

/**
 *
 *
 */
public abstract class ContextPaletteButton extends JPanel {

    protected static final KeyStroke UP_KEYSTROKE
            = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    protected static final KeyStroke DOWN_KEYSTROKE
            = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);

    protected static final int END_CHILD_INDEX = Integer.MAX_VALUE;

    public ContextPaletteButton() {
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (e.isTemporary() == false) {
                    setBorder(getFocusBorder());
                    repaintPalette();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.isTemporary() == false) {
                    setBorder(getNonFocusedBorder());
                    repaintPalette();
                }

            }
        });

        InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        inputMap.put(UP_KEYSTROKE, "MoveToPrevious");
        inputMap.put(DOWN_KEYSTROKE, "MoveToNext");
        getActionMap().put("MoveToPrevious", new MoveToPreviousButtonAction());
        getActionMap().put("MoveToNext", new MoveToNextButtonAction());
    }

    protected abstract Border getFocusBorder();

    protected abstract Border getNonFocusedBorder();

    private void repaintPalette() {
        Container parent = getParent();
        while (!(parent instanceof ContextPalette)) {
            if (parent == null) {
                break;
            }

            parent = parent.getParent();
        }

        if (parent != null) {
            parent.repaint();
        }
    }

    protected void moveFocusToNextSibling(int startChildIndex, int endChildIndex) {
        Component[] components = getParent().getComponents();
        Component next = null;

        if (endChildIndex == END_CHILD_INDEX) {
            endChildIndex = components.length;
        }

        for (int index = 0; index < components.length; index++) {
            if (components[index].equals(this) == true) {
                int dx = (startChildIndex < endChildIndex ? 1 : -1);
                if ((index + dx) == endChildIndex) {
                    next = components[startChildIndex];
                } else {
                    next = components[index + dx];
                }
                break;
            }
        }

        if (next != null) {
            next.requestFocusInWindow();
        }
    }

    protected void moveFocusToPreviousSibling(int startChildIndex, int endChildIndex) {
        Component[] components = getParent().getComponents();
        Component previous = null;

        if (endChildIndex == END_CHILD_INDEX) {
            endChildIndex = components.length;
        }

        for (int index = 0; index < components.length; index++) {
            if (components[index].equals(this) == true) {
                int dx = (startChildIndex < endChildIndex ? -1 : 1);
                if (((index + dx) < startChildIndex) && (dx < 0)) {
                    previous = components[endChildIndex - 1];
                } else if (((index + dx) > startChildIndex) && (dx > 0)) {
                    previous = components[endChildIndex];
                } else {
                    previous = components[index + dx];
                }
                break;
            }
        }

        if (previous != null) {
            previous.requestFocusInWindow();
        }
    }

    public class MoveToNextButtonAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            moveFocusToNextSibling(0, END_CHILD_INDEX);
        }

    }

    public class MoveToPreviousButtonAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            moveFocusToPreviousSibling(0, END_CHILD_INDEX);
        }

    }
}
