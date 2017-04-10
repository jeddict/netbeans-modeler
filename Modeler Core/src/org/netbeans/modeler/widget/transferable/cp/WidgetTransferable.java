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
package org.netbeans.modeler.widget.transferable.cp;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.widget.info.WidgetInfo;
import org.netbeans.modeler.widget.node.vmd.PNodeWidget;
import org.openide.util.Exceptions;

/**
 *
 * @author jGauravGupta
 */
public class WidgetTransferable implements Transferable, ClipboardOwner {

    private static final DataFlavor DATA_FLAVOR = new DataFlavor(WidgetData.class, WidgetTransferable.class.getName());
    private final WidgetData selection;

    public WidgetTransferable(WidgetData selection) {
        this.selection = selection;
    }

    public static void copy(IModelerScene modelerScene) {
        List<IBaseElement> data = modelerScene.getSelectedObjects()
                .stream()
                .filter(wi -> wi instanceof WidgetInfo)
                .map(wi -> ((WidgetInfo) wi).getBaseElementSpec())
                .collect(toList());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        WidgetTransferable dataSelection = new WidgetTransferable(new WidgetData(data));
        clipboard.setContents(dataSelection, dataSelection);
    }
    
    public static void copy(PNodeWidget nodeWidget) {
        List<IBaseElement> data = Collections.singletonList(nodeWidget.getNodeWidgetInfo().getBaseElementSpec());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        WidgetTransferable dataSelection = new WidgetTransferable(new WidgetData(data));
        clipboard.setContents(dataSelection, dataSelection);
    }

    public static void paste(IBaseElementWidget parentConatiner) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipboardContent = clipboard.getContents(parentConatiner.getModelerScene());

        DataFlavor[] flavors = clipboardContent.getTransferDataFlavors();
        for (DataFlavor flavor : flavors) {
            if (flavor.getRepresentationClass() == WidgetData.class) {
                try {
                    WidgetData widgetData = (WidgetData) clipboardContent.getTransferData(flavor);
                    IModelerUtil util = parentConatiner.getModelerScene().getModelerFile().getModelerUtil();
                    util.loadBaseElement(parentConatiner,
                            widgetData.getData()
                                    .stream()
                                    .filter(data -> data!=null)
                                    .map(data -> util.clone(data))
                                    .collect(toList()));
                } catch (UnsupportedFlavorException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] ret = {DATA_FLAVOR};
        return ret;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DATA_FLAVOR.equals(flavor);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (isDataFlavorSupported(flavor)) {
            return this.selection;
        } else {
            throw new UnsupportedFlavorException(DATA_FLAVOR);
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

}
