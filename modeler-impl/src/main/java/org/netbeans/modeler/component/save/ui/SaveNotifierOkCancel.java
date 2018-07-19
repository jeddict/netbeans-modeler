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
package org.netbeans.modeler.component.save.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

/**
 *
 * @author Craig Conover, craig.conover@sun.com
 */
public class SaveNotifierOkCancel {

    private static SaveNotifierOkCancel instance = null;

    public static SaveNotifierOkCancel getDefault() {
        if (instance == null) {
            instance = new SaveNotifierOkCancel();
        }

        return instance;
    }

    /**
     * Creates a new instance of SaveNotifier
     */
    private SaveNotifierOkCancel() {
    }

    public Object displayNotifier(
            String title, String message) {
        DialogManager dmgr = new DialogManager(title, message);
        dmgr.prompt();

        return dmgr.getResult();
    }

    private static class DialogManager implements ActionListener {

        private DialogDescriptor dialogDesc = null;
        private Dialog dialog = null;
        private Object result = DialogDescriptor.CANCEL_OPTION;

        private final Object[] closeOptions
                = {
                    DialogDescriptor.OK_OPTION,
                    DialogDescriptor.CANCEL_OPTION
                };

        public DialogManager(String title, String message) {
            JButton saveButton = new JButton(NbBundle.getMessage(
                    SaveNotifier.class, "LBL_SaveButton")); // NOI18N
            saveButton.setActionCommand(NbBundle.getMessage(
                    SaveNotifier.class, "LBL_SaveButton")); // NOI18N
            saveButton.getAccessibleContext().setAccessibleDescription(
                    NbBundle.getMessage(
                            SaveNotifier.class, "ACSD_SaveButton")); // NOI18N
            Mnemonics.setLocalizedText(
                    saveButton, NbBundle.getMessage(
                            SaveNotifier.class, "LBL_SaveButton")); // NOI18N

            Object[] buttonOptions
                    = {
                        saveButton,
                        DialogDescriptor.CANCEL_OPTION
                    };

            dialogDesc = new DialogDescriptor(
                    message, // message
                    title, // title
                    true, // modal?
                    buttonOptions,
                    DialogDescriptor.OK_OPTION, // default option
                    DialogDescriptor.DEFAULT_ALIGN,
                    null, // help context
                    this, // button action listener
                    false); // leaf?

            dialogDesc.setMessageType(NotifyDescriptor.QUESTION_MESSAGE);
            dialogDesc.setClosingOptions(closeOptions);
            dialog = DialogDisplayer.getDefault().createDialog(dialogDesc);
        }

        private void prompt() {
            dialog.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getActionCommand().equalsIgnoreCase(
                    NbBundle.getMessage(SaveNotifier.class, "LBL_SaveButton"))) // NOI18N
            {
                result = DialogDescriptor.OK_OPTION;
            } else // Cancel or 'x' box close
            {
                result = DialogDescriptor.CANCEL_OPTION;
            }

            dialog.setVisible(false);
            dialog.dispose();
        }

        public Object getResult() {
            return result;
        }
    }

}
