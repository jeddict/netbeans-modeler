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
package org.netbeans.modeler.widget.pin;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import static org.netbeans.modeler.core.engine.ModelerDiagramEngine.cleanActions;
import org.netbeans.modeler.label.LabelInplaceEditor;
import org.netbeans.modeler.label.inplace.InplaceEditorAction;
import org.netbeans.modeler.label.inplace.TextFieldInplaceEditorProvider;
import org.netbeans.modeler.properties.view.manager.BasePropertyViewManager;
import org.netbeans.modeler.properties.view.manager.IPropertyManager;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IPModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.vmd.internal.AbstractPinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

public abstract class PinWidget<S extends IPModelerScene> extends AbstractPinWidget {

    private IPNodeWidget nodeWidget;
    private PinWidgetInfo pinWidgetInfo;
    private boolean activeStatus = true;
    private boolean highlightStatus = false;

    public PinWidget(S scene, IPNodeWidget nodeWidget, PinWidgetInfo pinWidgetInfo) {
        super((Scene) scene, scene.getColorScheme());
        this.setModelerScene(scene);
        this.pinWidgetInfo = pinWidgetInfo;
        this.nodeWidget = nodeWidget;
        WidgetAction editAction = new InplaceEditorAction<>(new TextFieldInplaceEditorProvider(new LabelInplaceEditor((Widget) this), null));
        getPinNameWidget().getActions().addAction(editAction);
        this.setProperties(pinWidgetInfo.getName(), null);
    }

    @Override
    public void setLabel(String label) {
        this.setPinName(label);
    }

    @Override
    public String getLabel() {
        return this.getPinName();
    }
    private S scene;

    /**
     * @return the scene
     */
    @Override
    public S getModelerScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setModelerScene(S scene) {
        this.scene = scene;
    }

    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuItemList = new LinkedList<>();
        menuItemList.add(getPropertyMenu());
        return menuItemList;
    }

    protected JMenuItem getPropertyMenu() {
        JMenuItem baseProperty = new JMenuItem("Properties");
        baseProperty.setIcon(ImageUtil.getInstance().getIcon("properties.gif"));
        baseProperty.addActionListener((ActionEvent e) -> {
            PinWidget.this.showProperties();
            PinWidget.this.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
        });
        return baseProperty;
    }

    @Override
    public PopupMenuProvider getPopupMenuProvider() {
        final PopupMenuProvider popupMenuProvider;
        final JPopupMenu popupMenu; //PopupMenu used to give some funcionality to the widget

        popupMenu = new JPopupMenu();

        List<JMenuItem> menuItemList = getPopupMenuItemList();
        for (JMenuItem menuItem : menuItemList) {
            if (menuItem == null) {
                popupMenu.addSeparator();
            } else {
                popupMenu.add(menuItem);
            }
        }
        popupMenuProvider = (final Widget widget, final Point location1) -> popupMenu;

        return popupMenuProvider;
    }

    private BasePropertyViewManager node;

    @Override
    public void exploreProperties() {
        if (node == null) {
            node = new BasePropertyViewManager((IBaseElementWidget) this);
        }
        org.netbeans.modeler.properties.util.PropertyUtil.exploreProperties(node, this.getPinName(), propertyVisibilityHandlers);
    }
    
    public IPropertyManager getPropertyManager(){
        return node;
    }

    @Override
    public void refreshProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.refreshProperties(node, this.getPinName(), propertyVisibilityHandlers);
    }

    @Override
    public void showProperties() {
        org.netbeans.modeler.properties.util.PropertyUtil.showProperties(node, this.getPinName(), propertyVisibilityHandlers);
    }

    @Override
    public boolean isActiveStatus() {
        return activeStatus;
    }

    /**
     * @param activeStatus the activeStatus to set
     */
    @Override
    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    /**
     * @return the pinWidgetInfo
     */
    @Override
    public PinWidgetInfo getPinWidgetInfo() {
        return pinWidgetInfo;
    }

    private final Map<String, PropertyChangeListener> propertyChangeHandlers = new HashMap<String, PropertyChangeListener>();

    @Override
    public void addPropertyChangeListener(String id, PropertyChangeListener propertyChangeListener) {
        this.propertyChangeHandlers.put(id, propertyChangeListener);
    }

    @Override
    public void removePropertyChangeListener(String id) {
        propertyChangeHandlers.remove(id);
    }

    @Override
    public Map<String, PropertyChangeListener> getPropertyChangeListeners() {
        return propertyChangeHandlers;
    }
    private final Map<String, PropertyVisibilityHandler> propertyVisibilityHandlers = new HashMap<String, PropertyVisibilityHandler>();

    @Override
    public void addPropertyVisibilityHandler(String id, PropertyVisibilityHandler propertyVisibilityHandler) {
        this.propertyVisibilityHandlers.put(id, propertyVisibilityHandler);
    }

    @Override
    public void removePropertyVisibilityHandler(String id) {
        propertyVisibilityHandlers.remove(id);
    }

    @Override
    public Map<String, PropertyVisibilityHandler> getPropertyVisibilityHandlers() {
        return propertyVisibilityHandlers;
    }

    /**
     * @return the nodeWidget
     */
    @Override
    public IPNodeWidget getPNodeWidget() {
        return nodeWidget;
    }

    /**
     * @param nodeWidget the nodeWidget to set
     */
    @Override
    public void setPNodeWidget(IPNodeWidget nodeWidget) {
        this.nodeWidget = nodeWidget;
    }

    @Override
    public boolean remove() {
        return remove(false);
    }

    @Override
    public boolean remove(boolean notification) {
        if (notification) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(String.format("are you sure you want to delete %s ?", this.getLabel()), String.format("Delete ", this.getLabel()), NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
                removePin();
                return true;
            }
        } else {
            removePin();
            return true;
        }
        return false;
    }

    private void removePin() {
        if (!locked) {
            this.setLabel("");
//            ((IBaseElementWidget) this).destroy();
//        ((IPFlowNodeWidget) nodeWidget).deleteFlowPinWidget((IFlowPinWidget) this);
            nodeWidget.deletePinWidget(this);
            scene.getModelerPanelTopComponent().changePersistenceState(false);
            cleanReference();
        }
    }
    private boolean locked = false;

    /**
     * @return the exist
     */
    @Override
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * @return the highlightStatus
     */
    @Override
    public boolean isHighlightStatus() {
        return highlightStatus;
    }

    /**
     * @param highlightStatus the highlightStatus to set
     */
    @Override
    public void setHighlightStatus(boolean highlightStatus) {
        this.highlightStatus = highlightStatus;
    }

    private int anchorGap;

    @Override
    public int getAnchorGap() {
        return anchorGap;
    }

    @Override
    public void setAnchorGap(int anchorGap) {
        this.anchorGap = anchorGap;
    }
    
    public void cleanReference(){
        if (this.getPropertyManager() != null) {
                this.getPropertyManager().getElementPropertySet().clearGroup();//clear ElementSupportGroup
            }
        this.getModelerScene().getModelerFile().getModelerDiagramEngine().clearPinWidgetAction(this);
        cleanActions(getPinNameWidget().getActions());
    }
}
