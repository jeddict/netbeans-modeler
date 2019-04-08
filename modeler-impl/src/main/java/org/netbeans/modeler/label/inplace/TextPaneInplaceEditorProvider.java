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
package org.netbeans.modeler.label.inplace;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumSet;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;

public final class TextPaneInplaceEditorProvider implements InplaceEditorProvider<JScrollPane> {

    private TextFieldInplaceEditor editor;
    private EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections;
    private KeyListener keyListener;
    private FocusListener focusListener;
    private DocumentListener documentListener;

    public TextPaneInplaceEditorProvider(TextFieldInplaceEditor editor, EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections) {
        this.editor = editor;
        this.expansionDirections = expansionDirections;
    }
    JTextPane jTextPane1;

    @Override
    public JScrollPane createEditorComponent(EditorController controller, Widget widget) {
        if (!editor.isEnabled(widget)) {
            return null;
        }
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextPane1.setText(editor.getText(widget));
        jScrollPane2.setViewportView(jTextPane1);
//        JTextArea field = new JTextArea(editor.getText(widget));
        jTextPane1.setMinimumSize(new Dimension(150, 51));
        jTextPane1.setPreferredSize(new Dimension(150, 51));
        jScrollPane2.setMinimumSize(new Dimension(150, 51));
        jScrollPane2.setPreferredSize(new Dimension(150, 51));

//        field.getMinimumSize()
//        jTextPane1.setColumns(20);
//        jTextPane1.setRows(20);
        jTextPane1.selectAll();
        Scene scene = widget.getScene();
        double zoomFactor = scene.getZoomFactor();
        if (zoomFactor > 1.0) {
            Font font = scene.getDefaultFont();
            font = font.deriveFont((float) (font.getSize2D() * zoomFactor));
            jTextPane1.setFont(font);
        }
        return jScrollPane2;
    }

    @Override
    public void notifyOpened(final EditorController controller, Widget widget, JScrollPane editor) {
//        editor.setMinimumSize(new Dimension(64, 1d9));
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                IModelerScene modelerScene = (IModelerScene) widget.getScene();
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ESCAPE:
                        e.consume();
                        controller.closeEditor(false);
                        modelerScene.getView().requestFocus();
                        break;
                }
            }
        };
        focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                controller.closeEditor(true);
                widget.getScene().getView().requestFocusInWindow();
            }
        };
        documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged();
            }
        };
        jTextPane1.addKeyListener(keyListener);
        jTextPane1.addFocusListener(focusListener);
        jTextPane1.getDocument().addDocumentListener(documentListener);
        jTextPane1.selectAll();
    }

    @Override
    public void notifyClosing(EditorController controller, Widget widget, JScrollPane editor, boolean commit) {
        jTextPane1.getDocument().removeDocumentListener(documentListener);
        jTextPane1.removeFocusListener(focusListener);
        jTextPane1.removeKeyListener(keyListener);
        if (commit) {
            this.editor.setText(widget, jTextPane1.getText());
            if (widget != null) {
                widget.getScene().validate();
            }
        }
    }

    @Override
    public Rectangle getInitialEditorComponentBounds(EditorController controller, Widget widget, JScrollPane editor, Rectangle viewBounds) {
        return null;
    }

    @Override
    public EnumSet<ExpansionDirection> getExpansionDirections(EditorController controller, Widget widget, JScrollPane editor) {
        return expansionDirections;
    }
}
