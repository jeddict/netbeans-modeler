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
package org.netbeans.modeler.widget.node.vmd.internal;

import java.awt.Image;
import java.util.List;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.vmd.VMDGlyphSetWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.anchors.PNodeAnchor;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.widget.design.ITextDesign;
import org.netbeans.modeler.widget.design.PinTextDesign;
import org.netbeans.modeler.widget.node.IWidgetStateHandler;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.state.WidgetStateHandler;

public abstract class AbstractPinWidget extends Widget implements IPinWidget {

    private IColorScheme colorScheme;
    private final AdvanceImageWidget imageWidget;
    private final LabelWidget nameWidget;
    private final VMDGlyphSetWidget glyphsWidget;
    private PNodeAnchor anchor;
    private final IWidgetStateHandler stateHandler;
    private ITextDesign textDesign;

    /**
     * Creates a pin widget with a specific color scheme.
     *
     * @param scene the scene
     * @param colorScheme the color scheme
     */
    public AbstractPinWidget(Scene scene, IColorScheme colorScheme, ITextDesign textDesign) {
        super(scene);
        assert colorScheme != null;
        this.colorScheme = colorScheme;
        this.textDesign = textDesign;
        setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 8));

        addChild(imageWidget = new AdvanceImageWidget(scene));
        addChild(nameWidget = new LabelWidget(scene));
        addChild(glyphsWidget = new VMDGlyphSetWidget(scene));
        
        stateHandler = new WidgetStateHandler(imageWidget);
        
        colorScheme.installUI(this);
        notifyStateChanged(ObjectState.createNormal(), ObjectState.createNormal());
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
     * Called to notify about the change of the widget state.
     *
     * @param previousState the previous state
     * @param state the new state
     */
    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        if (!this.isHighlightStatus()) {
            getColorScheme().updateUI(this, previousState, state);
        }
    }

    /**
     * Returns a pin name widget.
     *
     * @return the pin name widget
     */
    @Override
    public Widget getPinNameWidget() {
        return nameWidget;
    }

    /**
     * Sets a pin name.
     *
     * @param name the pin name
     */
    @Override
    public void setPinName(String name) {
        nameWidget.setLabel(name);
        this.getPNodeWidget().revalidate();
    }

    /**
     * Returns a pin name.
     *
     * @return the pin name
     */
    @Override
    public String getPinName() {
        return nameWidget.getLabel();
    }

    /**
     * Sets pin glyphs.
     *
     * @param glyphs the list of images
     */
    @Override
    public void setGlyphs(List<Image> glyphs) {
        glyphsWidget.setGlyphs(glyphs);
    }

    /**
     * Sets all pin properties at once.
     *
     * @param name the pin name
     * @param glyphs the pin glyphs
     */
    @Override
    public void setProperties(String name, List<Image> glyphs) {
        setPinName(name);
        glyphsWidget.setGlyphs(glyphs);
    }

    /**
     * Creates a horizontally oriented anchor similar to
     * PNodeWidget.createAnchorPin
     *
     * @return the anchor
     */
    @Override
    public Anchor createAnchor() {
        if (anchor == null) {
            anchor = new PNodeAnchor(this, false);
        }
        return anchor;
    }

    /**
     * @return the imageWidget
     */
    public AdvanceImageWidget getImageWidget() {
        return imageWidget;
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
     * @return the stateHandler
     */
    public IWidgetStateHandler getWidgetStateHandler() {
        return stateHandler;
    }
    
    
    /**
     * @return the textDesign
     */
    @Override
    public ITextDesign getTextDesign() {
        if(textDesign == null){
            textDesign = new PinTextDesign();
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
