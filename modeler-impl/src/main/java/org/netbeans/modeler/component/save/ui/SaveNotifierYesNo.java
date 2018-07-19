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
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

/**
 *
 * @author Craig Conover, craig.conover@sun.com
 */
public class SaveNotifierYesNo {

    private static SaveNotifierYesNo instance = null;
    public final static Object SAVE_ALWAYS_OPTION = 9999;
    public final static int SAVE_DISCARD_CANCEL = 0;
    public final static int SAVE_CANCEL = 1;

    public static SaveNotifierYesNo getDefault() {
        if (instance == null) {
            instance = new SaveNotifierYesNo();
        }

        return instance;
    }

    /**
     * Creates a new instance of SaveNotifierYesNo
     */
    private SaveNotifierYesNo() {
    }

    public Object displayNotifier(
            String dialogTitle, String message, int buttonType) {
        DialogManager dmgr = new DialogManager(dialogTitle, message, buttonType);
        dmgr.prompt();

        return dmgr.getResult();
    }

    public Object displayNotifier(
            String dialogTitle, String saveType, String saveName) {
        // create a default save-discard-cancel dialog with a default message
        DialogManager dmgr = new DialogManager(dialogTitle, saveType, saveName);
        dmgr.prompt();

        return dmgr.getResult();
    }

    private static class DialogManager implements ActionListener {

        private DialogDescriptor dialogDesc = null;
        private Dialog dialog = null;
        private Object result = DialogDescriptor.CANCEL_OPTION;

        private final Object[] closeOptions
                = {
                    DialogDescriptor.DEFAULT_OPTION,
                    DialogDescriptor.NO_OPTION,
                    DialogDescriptor.DEFAULT_OPTION,
                    DialogDescriptor.YES_OPTION
                };

        public DialogManager(String dialogTitle, String saveType, String saveName) {

            this(dialogTitle,
                    NbBundle.getMessage(SaveNotifierYesNo.class, "LBL_SaveNotifierYesNoDialog_Question", saveName),
                    SaveNotifierYesNo.SAVE_DISCARD_CANCEL);
        }

        public DialogManager(
                String dialogTitle, String message, int buttonType) {
            JButton saveButton = new JButton(NbBundle.getMessage(
                    SaveNotifierYesNo.class, "LBL_SaveButton")); // NOI18N

            saveButton.setActionCommand(NbBundle.getMessage(
                    SaveNotifierYesNo.class, "LBL_SaveButton")); // NOI18N

            Object buttonOptions[] = {
                saveButton,
                DialogDescriptor.CANCEL_OPTION};

            if (buttonType == SaveNotifierYesNo.SAVE_DISCARD_CANCEL) {
                JButton discardButton = new JButton(NbBundle.getMessage(
                        SaveNotifierYesNo.class, "LBL_DiscardButton")); // NOI18N

                discardButton.setActionCommand(NbBundle.getMessage(
                        SaveNotifierYesNo.class, "LBL_DiscardButton")); // NOI18N

                Mnemonics.setLocalizedText(
                        discardButton, NbBundle.getMessage(
                                SaveNotifierYesNo.class, "LBL_DiscardButton")); // NOI18N

                Object buttons[] = {
                    saveButton,
                    discardButton,
                    DialogDescriptor.CANCEL_OPTION};

                buttonOptions = buttons;
            }

            dialogDesc = new DialogDescriptor(
                    message, // message to display
                    dialogTitle, // title
                    true, // modal?
                    buttonOptions,
                    saveButton, // default option
                    DialogDescriptor.DEFAULT_ALIGN,
                    null, // help context
                    this, // button action listener
                    false); // leaf?

            dialogDesc.setMessageType(DialogDescriptor.QUESTION_MESSAGE);
            dialogDesc.setClosingOptions(closeOptions);
            dialog = DialogDisplayer.getDefault().createDialog(dialogDesc);
        }

        private void prompt() {
            dialog.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getActionCommand().equalsIgnoreCase(
                    NbBundle.getMessage(SaveNotifierYesNo.class, "LBL_SaveButton"))) // NOI18N
            {
                result = DialogDescriptor.YES_OPTION;
            } else if (actionEvent.getActionCommand().equalsIgnoreCase(
                    NbBundle.getMessage(SaveNotifierYesNo.class, "LBL_DiscardButton"))) // NOI18N))
            {
                result = DialogDescriptor.NO_OPTION;
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
