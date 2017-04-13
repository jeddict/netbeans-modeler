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

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.core.IFlowNode;
import org.netbeans.modeler.specification.model.document.core.IFlowPin;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.widget.info.WidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
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
        Map<IBaseElement,Rectangle> data = new LinkedHashMap<>();
        for (Object object : modelerScene.getSelectedObjects()) {
            if (object instanceof WidgetInfo) {
                Widget widget = modelerScene.findWidget(object);
                if (widget instanceof IBaseElementWidget&& ((IBaseElementWidget) widget).getBaseElementSpec()!=null){
                    if (widget instanceof INodeWidget) {
                        data.put(((IBaseElementWidget) widget).getBaseElementSpec(), ((INodeWidget) widget).getSceneViewBound());
                    } else {
                        data.put(((IBaseElementWidget) widget).getBaseElementSpec(), null);
                    }
                }
            }
        }
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        WidgetTransferable dataSelection = new WidgetTransferable(new WidgetData(data));
        clipboard.setContents(dataSelection, dataSelection);
    }
    
    public static void copy(PNodeWidget nodeWidget) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        WidgetTransferable dataSelection = new WidgetTransferable(
                new WidgetData(Collections.singletonMap(((IBaseElementWidget)nodeWidget).getBaseElementSpec(), nodeWidget.getSceneViewBound()))
        );
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
                    
                    Map<IBaseElement, Rectangle> data = widgetData.getData()
                            .entrySet().stream()
                            .filter(map -> (parentConatiner instanceof IModelerScene && map.getKey() instanceof IFlowNode)
                            || (parentConatiner instanceof INodeWidget && map.getKey() instanceof IFlowPin))
                            .collect(toMap(p -> p.getKey(), p -> p.getValue()));
                    List<IBaseElement> elments = new ArrayList<>(data.keySet());
                    List<IBaseElement> clonedElements = util.clone(elments);
                    Map<IBaseElement,Rectangle> clonedElementData = new LinkedHashMap<>();
        
                    //find min location point
                    int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
                    for (int i = 0; i < elments.size(); i++) {
                        IBaseElement elment = elments.get(i);
                        Rectangle location = data.get(elment);
                        minX = location.x < minX ? location.x : minX;
                        minY = location.y < minY ? location.y : minY;
                    }
                    //normalize location
                    for (int i = 0; i < elments.size(); i++) {
                        IBaseElement elment = elments.get(i);
                        Rectangle location = data.get(elment);
                        location.setLocation((int)location.getX()-minX, (int)location.getY()-minY);
                    }
                    
                    for (int i = 0; i < elments.size(); i++) {
                        IBaseElement elment = elments.get(i);
                        Rectangle location = data.get(elment);
                        clonedElementData.put(clonedElements.get(i),location);
                    }
                    
                    util.loadBaseElement(parentConatiner,clonedElementData);
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
