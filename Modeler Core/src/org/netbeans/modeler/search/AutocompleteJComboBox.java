/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.modeler.search;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Consumer;
import static java.util.stream.Collectors.toCollection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.ComboBoxValue;

public class AutocompleteJComboBox<T> extends JComboBox<ComboBoxValue> {

    private List<ComboBoxValue<T>> terms;
    private Consumer<T> onSelection;

    public AutocompleteJComboBox() {
        this(null, null);
    }
    
    public AutocompleteJComboBox(List<ComboBoxValue<T>> terms, Consumer<T> onSelection) {
        if(terms!=null){
            this.terms = terms;
            setModel(new DefaultComboBoxModel(new Vector(terms)));
        }
        this.onSelection = onSelection;
        setSelectedIndex(-1);
        setEditable(true);
        JTextField text = (JTextField) this.getEditor().getEditorComponent();
        text.setFocusable(true);
        text.setText("");
        text.addKeyListener(new ComboListener());
        this.addPropertyChangeListener(evt -> { //on popup item click
            if ("COMBOBOX.CP_COMBOBOX".equals(evt.getPropertyName())
                    && evt.getNewValue()!=null && "NORMAL".equals(evt.getNewValue().toString())
                    && evt.getOldValue()!=null && "PRESSED".equals(evt.getOldValue().toString())) {//COMBOBOX.CP_COMBOBOX - NORMAL - PRESSED
                if(onSelection!=null)this.onSelection.accept(getValue());
            }
        });

    }
    
    public void setValue(List<ComboBoxValue<T>> terms){
        this.terms = terms;
        setModel(new DefaultComboBoxModel(new Vector(terms)));
    }

    public T getValue() {
        Object selected = getSelectedItem();
        if(selected!=null && selected instanceof String){
            Optional<ComboBoxValue<T>> selectedTerms = terms.stream()
                    .filter(s -> s.getDisplayValue().toLowerCase().contains(((String)getSelectedItem()).toLowerCase()))
                    .findAny();
            if(selectedTerms.isPresent()){
                selected = selectedTerms.get();
            }
        }
        if(selected==null || selected instanceof String){
            return null;
        }
        return ((ComboBoxValue<T>) selected).getValue();
    }

    class ComboListener extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent key) {
            AutocompleteJComboBox<T> comboBox = AutocompleteJComboBox.this;
            JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();

            if (key.getKeyCode() == KeyEvent.VK_ENTER) { //on enter press
                if(onSelection!=null)onSelection.accept(comboBox.getValue());
            }
            if (key.getKeyCode() == KeyEvent.VK_UP
                    || key.getKeyCode() == KeyEvent.VK_DOWN
                    || key.getKeyCode() == KeyEvent.VK_LEFT
                    || key.getKeyCode() == KeyEvent.VK_RIGHT) {
                return;
            }

            String text = ((JTextField) key.getSource()).getText();
            comboBox.setModel(new DefaultComboBoxModel(terms.stream()
                    .filter(s -> s.getDisplayValue().toLowerCase().contains(text.toLowerCase()))
                    .collect(toCollection(Vector::new))));
            comboBox.setSelectedIndex(-1);
            tf.setText(text);
            try {
                comboBox.showPopup();
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

    }

}
