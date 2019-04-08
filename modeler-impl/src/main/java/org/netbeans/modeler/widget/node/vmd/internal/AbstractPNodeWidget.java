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
package org.netbeans.modeler.widget.node.vmd.internal;

//import org.netbeans.modeler.widget.INodeWidget;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.model.StateModel;
import org.netbeans.api.visual.vmd.VMDMinimizeAbility;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.anchors.PNodeAnchor;
import org.netbeans.modeler.scene.vmd.AbstractPModelerScene;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.IWidgetStateHandler;
import org.netbeans.modeler.widget.pin.IPinSeperatorWidget;
import org.netbeans.modeler.widget.state.WidgetStateHandler;
import org.netbeans.modeler.widget.design.ITextDesign;
import org.netbeans.modeler.widget.design.NodeTextDesign;

public abstract class AbstractPNodeWidget extends Widget implements IPNodeWidget, StateModel.Listener, VMDMinimizeAbility {

    private final Widget header;
    private final ImageWidget minimizeWidget;
    private final AdvanceImageWidget imageWidget;
    private final LabelWidget nameWidget;
    private final LabelWidget typeWidget;
    private final SeparatorWidget pinsSeparator;
    private Map<String, IPinSeperatorWidget> pinCategoryWidgets = new HashMap<>();

    private StateModel stateModel = new StateModel(2);
    private final PNodeAnchor nodeAnchor;
    private IColorScheme colorScheme;
    private ITextDesign textDesign;
    private final IWidgetStateHandler stateHandler;
    private final WeakHashMap<Anchor, Anchor> proxyAnchorCache = new WeakHashMap<>();

    /**
     * Creates a node widget with a specific color scheme.
     *
     * @param scene the scene
     * @param colorScheme the color scheme
     * @param textDesign
     */
    public AbstractPNodeWidget(Scene scene, IColorScheme colorScheme, ITextDesign textDesign) {
        super(scene);

        this.colorScheme = colorScheme;
        this.textDesign = textDesign;
        nodeAnchor = new PNodeAnchor(this, true);

        setLayout(LayoutFactory.createVerticalFlowLayout());
        setMinimumSize(new Dimension(128, 8));

        header = new Widget(scene);
        header.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 8));
        addChild(header);

        boolean right = colorScheme.isNodeMinimizeButtonOnRight(this);

        minimizeWidget = new ImageWidget(scene, colorScheme.getMinimizeWidgetImage(this));
        minimizeWidget.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        minimizeWidget.getActions().addAction(new ToggleMinimizedAction());
        if (!right) {
            header.addChild(minimizeWidget);
        }

        imageWidget = new AdvanceImageWidget(scene);
        stateHandler = new WidgetStateHandler(imageWidget);
        header.addChild(imageWidget);

        nameWidget = new LabelWidget(scene);
        header.addChild(nameWidget);

        typeWidget = new LabelWidget(scene);
        typeWidget.setForeground(Color.BLACK);
        header.addChild(typeWidget);

        if (right) {
            Widget widget = new Widget(scene);
//            widget.setOpaque(false);
            header.addChild(widget, 1000);
            header.addChild(minimizeWidget);
        }

        pinsSeparator = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        addChild(pinsSeparator);

        Widget topLayer = new Widget(scene);
        addChild(topLayer);

        stateModel = new StateModel();
        stateModel.addListener(this);

        if(!((AbstractPModelerScene)this.getScene()).isSceneGenerating()){
          colorScheme.installUI(this);  
        }
        notifyStateChanged(ObjectState.createNormal(), ObjectState.createNormal());
    }

    /**
     * Called to check whether a particular widget is minimizable. By default it
     * returns true. The result have to be the same for whole life-time of the
     * widget. If not, then the revalidation has to be invoked manually. An
     * anchor (created by <code>VMDNodeWidget.createPinAnchor</code> is not
     * affected by this method.
     *
     * @param widget the widget
     * @return true, if the widget is minimizable; false, if the widget is not
     * minimizable
     */
    protected boolean isMinimizableWidget(Widget widget) {
        return true;
    }

    /**
     * Check the minimized state.
     *
     * @return true, if minimized
     */
    @Override
    public boolean isMinimized() {
        return stateModel.getBooleanState();
    }

    /**
     * Set the minimized state. This method will show/hide child widgets of this
     * Widget and switches anchors between node and pin widgets.
     *
     * @param minimized if true, then the widget is going to be minimized
     */
    @Override
    public void setMinimized(boolean minimized) {
        stateModel.setBooleanState(minimized);
    }

    /**
     * Toggles the minimized state. This method will show/hide child widgets of
     * this Widget and switches anchors between node and pin widgets.
     */
    @Override
    public void toggleMinimized() {
        stateModel.toggleBooleanState();
    }

    /**
     * Called when a minimized state is changed. This method will show/hide
     * child widgets of this Widget and switches anchors between node and pin
     * widgets.
     */
    @Override
    public void stateChanged() {
        boolean minimized = stateModel.getBooleanState();
        if(this.getModelerScene().isSceneGenerating()){
            return;
        }
        Rectangle rectangle = minimized ? new Rectangle(0, 0, (int) getBounds().getWidth(), 0) : null;  //getBounds().getWidth() dont need to change width
        for (Widget widget : getChildren()) {
            if (widget != header && widget != pinsSeparator) {
                getScene().getSceneAnimator().animatePreferredBounds(widget, minimized && isMinimizableWidget(widget) ? rectangle : null);
            }
        }
        minimizeWidget.setImage(colorScheme.getMinimizeWidgetImage(this));

    }

    /**
     * Called to notify about the change of the widget state.
     *
     * @param previousState the previous state
     * @param state the new state
     */
    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        if (!this.isHighlightStatus()) {
            colorScheme.updateUI(this, previousState, state);
        }
    }

    /**
     * Sets a node image.
     *
     * @param image the image
     */
    public void setImage(Image image) {
        getImageWidget().setImage(image);
    }
    
    /**
     * Returns a node name.
     *
     * @return the node name
     */
    @Override
    public String getNodeName() {
        return nameWidget.getLabel();
    }

    /**
     * Sets a node name.
     *
     * @param nodeName the node name
     */
    @Override
    public void setNodeName(String nodeName) {
        nameWidget.setLabel(nodeName);
         revalidate ();
    }

    /**
     * Sets a node type (secondary name).
     *
     * @param nodeType the node type
     */
    @Override
    public void setNodeType(String nodeType) {
        typeWidget.setLabel(nodeType != null ? "[" + nodeType + "]" : null);
    }

    /**
     * Attaches a pin widget to the node widget.
     *
     * @param widget the pin widget
     */
    @Override
    public void attachPinWidget(Widget widget) {
        widget.setCheckClipping(true);
        addChild(widget);
        if (stateModel.getBooleanState() && isMinimizableWidget(widget)) {
            widget.setPreferredBounds(new Rectangle());
        }
    }

    /**
     * Returns a node name widget.
     *
     * @return the node name widget
     */
    @Override
    public LabelWidget getNodeNameWidget() {
        return nameWidget;
    }

    /**
     * Returns a node anchor.
     *
     * @return the node anchor
     */
    @Override
    public Anchor getNodeAnchor() {
        return nodeAnchor;
    }

    /**
     * Creates an extended pin anchor with an ability of reconnecting to the
     * node anchor when the node is minimized.
     *
     * @param anchor the original pin anchor from which the extended anchor is
     * created
     * @return the extended pin anchor, the returned anchor is cached and
     * returns a single extended pin anchor instance of each original pin anchor
     */
    @Override
    public Anchor createAnchorPin(Anchor anchor) {
        Anchor proxyAnchor = proxyAnchorCache.get(anchor);
        if (proxyAnchor == null) {
            proxyAnchor = AnchorFactory.createProxyAnchor(stateModel, anchor, nodeAnchor);
            proxyAnchorCache.put(anchor, proxyAnchor);
        }
        return proxyAnchor;
    }

    /**
     * Returns a list of pin widgets attached to the node.
     *
     * @return the list of pin widgets
     */
    private List<Widget> getPinWidgets() {
        ArrayList<Widget> pins = new ArrayList<>(getChildren());
        pins.remove(header);
        pins.remove(pinsSeparator);
        return pins;
    }

    /**
     * Sorts and assigns pins into categories.
     *
     * @param pinsCategories the map of category name as key and a list of pin
     * widgets as value
     */
    @Override
    public void sortPins(Map<String, List<Widget>> pinsCategories) {

        List<Widget> previousPins = getPinWidgets();
        List<Widget> unresolvedPins = new ArrayList<>(previousPins);

        for (Iterator<Widget> iterator = unresolvedPins.iterator(); iterator.hasNext();) {
            Widget widget = iterator.next();
            if (pinCategoryWidgets.containsValue(widget)) {
                iterator.remove();
            }
        }

        ArrayList<String> unusedCategories = new ArrayList<>(pinCategoryWidgets.keySet());
        ArrayList<String> categoryNames = new ArrayList<>(pinsCategories.keySet());

        ArrayList<Widget> newWidgets = new ArrayList<>();
        for (String categoryName : categoryNames) {
            if (categoryName == null) {
                continue;
            }
            unusedCategories.remove(categoryName);
            if(!categoryName.isEmpty()){
                newWidgets.add((Widget) createPinCategoryWidget(categoryName));
            }
            List<Widget> widgets = pinsCategories.get(categoryName);
            for (Widget widget : widgets) {
                if (unresolvedPins.remove(widget)) {
                    newWidgets.add(widget);
                }
            }
        }

        if (!unresolvedPins.isEmpty()) {
            newWidgets.addAll(0, unresolvedPins);
        }

        for (String category : unusedCategories) {
            getPinCategoryWidgets().remove(category);
        }

        removeChildren(previousPins);
        addChildren(newWidgets);
        ((AbstractPModelerScene) this.getScene()).validateComponent();
    }

    private IPinSeperatorWidget createPinCategoryWidget(String categoryDisplayName) {
        IPinSeperatorWidget w = pinCategoryWidgets.get(categoryDisplayName);
        if (w != null) {
            return w;
        }
        IPinSeperatorWidget label = new PinSeperatorWidget(this.getScene(), categoryDisplayName);
        if(!((AbstractPModelerScene)this.getScene()).isSceneGenerating()){
          colorScheme.installUI(label);  
        }
        
        if (stateModel.getBooleanState()) {
            label.setPreferredBounds(new Rectangle());
        }
        pinCategoryWidgets.put(categoryDisplayName, label);
        return label;
    }

    /**
     * Collapses the widget.
     */
    @Override
    public void collapseWidget() {
        stateModel.setBooleanState(true);
    }

    /**
     * Expands the widget.
     */
    @Override
    public void expandWidget() {
        stateModel.setBooleanState(false);
    }

    /**
     * Returns a header widget.
     *
     * @return the header widget
     */
    @Override
    public Widget getHeader() {
        return header;
    }

    /**
     * Returns a minimize button widget.
     *
     * @return the miminize button widget
     */
    @Override
    public ImageWidget getMinimizeButton() {
        return minimizeWidget;
    }

    /**
     * Returns a pins separator.
     *
     * @return the pins separator
     */
    @Override
    public Widget getPinsSeparator() {
        return pinsSeparator;
    }

    /**
     * @return the imageWidget
     */
    public AdvanceImageWidget getImageWidget() {
        return imageWidget;
    }

    /**
     * @return the pinCategoryWidgets
     */
    @Override
    public Map<String, IPinSeperatorWidget> getPinCategoryWidgets() {
        return pinCategoryWidgets;
    }

    /**
     * @param pinCategoryWidgets the pinCategoryWidgets to set
     */
    @Override
    public void setPinCategoryWidgets(HashMap<String, IPinSeperatorWidget> pinCategoryWidgets) {
        this.pinCategoryWidgets = pinCategoryWidgets;
    }

    /**
     * @return the stateHandler
     */
    public IWidgetStateHandler getWidgetStateHandler() {
        return stateHandler;
    }

    private final class ToggleMinimizedAction extends WidgetAction.Adapter {

        @Override
        public WidgetAction.State mousePressed(Widget widget, WidgetAction.WidgetMouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1 || event.getButton() == MouseEvent.BUTTON2) {
                stateModel.toggleBooleanState();
                return WidgetAction.State.CONSUMED;
            }
            return WidgetAction.State.REJECTED;
        }
    }

    /**
     * @return the colorScheme
     */
    @Override
    public IColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * @param colorScheme the colorScheme to set
     */
    @Override
    public void setColorScheme(IColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    /**
     * @return the textDesign
     */
    @Override
    public ITextDesign getTextDesign() {
        if(textDesign == null){
            textDesign = new NodeTextDesign();
        }
        return textDesign;
    }

    /**
     * @param textDesign the textDesign to set
     */
    @Override
    public void setTextDesign(ITextDesign textDesign) {
        this.textDesign = textDesign;
    }
}
