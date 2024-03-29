/**
 * Copyright 2013-2022 Gaurav Gupta
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
package org.netbeans.modeler.actions.export;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 *
 *
 */
public class ExportAction extends AbstractAction {

    private final IModelerScene scene;
    private final ExportPanel panel = new ExportPanel();

    public ExportAction(IModelerScene scene) {
        this.scene = scene;
        putValue(Action.SMALL_ICON, ImageUtil.getInstance().getIcon("export-as-image.png")); // NOI18N
        putValue(Action.SHORT_DESCRIPTION,
                NbBundle.getMessage(ExportAction.class, "LBL_ExportImageAction")); // NOI18N

        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift X"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String title = NbBundle.getMessage(ExportAction.class, "TITLE_ExportImage"); // NOI18N
        DialogDescriptor dialogDescriptor = new DialogDescriptor(panel, title, true,
                NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION, null);

        Dialog dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);
        dialog.getAccessibleContext().setAccessibleDescription(title);

        panel.setDialogDescriptor(dialogDescriptor);
        panel.initValue(scene);

        dialog.setVisible(true);
        if (dialogDescriptor.getValue() == DialogDescriptor.OK_OPTION) {
            panel.exportImage();
        }
    }
}
