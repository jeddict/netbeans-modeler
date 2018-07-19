/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;

/**
 * @author David Kaspar
 */
public final class TextFieldInplaceEditorProvider implements InplaceEditorProvider<JTextField> {

    private TextFieldInplaceEditor editor;
    private EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections;

    private KeyListener keyListener;
    private FocusListener focusListener;
    private DocumentListener documentListener;

    public TextFieldInplaceEditorProvider(TextFieldInplaceEditor editor, EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections) {
        this.editor = editor;
        this.expansionDirections = expansionDirections;
    }

    @Override
    public JTextField createEditorComponent(EditorController controller, Widget widget) {
        if (!editor.isEnabled(widget)) {
            return null;
        }
        final JTextField field = new JTextField(editor.getText(widget));
//        field.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.WHITE),new EmptyBorder(0,0,0,0)));
        field.selectAll();
        Scene scene = widget.getScene();
        double zoomFactor = scene.getZoomFactor();
        if (zoomFactor > 1.0) {
            Font font = scene.getDefaultFont();
            font = font.deriveFont((float) (font.getSize2D() * zoomFactor));
            field.setFont(font);
        }
        return field;
    }

    @Override
    public void notifyOpened(final EditorController controller, final Widget widget, JTextField editor) {
        editor.setMinimumSize(new Dimension(64, 19));
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
                    case KeyEvent.VK_ENTER:
                        e.consume();
                        controller.closeEditor(true);
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
        editor.addKeyListener(keyListener);
        editor.addFocusListener(focusListener);
        editor.getDocument().addDocumentListener(documentListener);
        editor.selectAll();
    }

    @Override
    public void notifyClosing(EditorController controller, Widget widget, JTextField editor, boolean commit) {
        editor.getDocument().removeDocumentListener(documentListener);
        editor.removeFocusListener(focusListener);
        editor.removeKeyListener(keyListener);
        if (commit) {
            this.editor.setText(widget, editor.getText());
            if (widget != null) {
                widget.getScene().validate();
            }
        }
    }

    @Override
    public Rectangle getInitialEditorComponentBounds(EditorController controller, Widget widget, JTextField editor, Rectangle viewBounds) {
        return null;
    }

    @Override
    public EnumSet<ExpansionDirection> getExpansionDirections(EditorController controller, Widget widget, JTextField editor) {
        return expansionDirections;
    }

}
