/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modeler.svg;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 *
 * @author jGauravGupta
 */
public abstract class SvgNodeWidget extends Widget {

    public SvgNodeWidget(Scene scene) {
        super(scene);
    }
 
    public abstract SVGDocument getSvgDocument();
    
    public abstract void setSVGDocument(SVGDocument document);
    
    public abstract INodeWidget getParentNodeWidget();
    
    public abstract AffineTransform getTransform();
    
    public abstract Shape getOutlineShape();
    
    public abstract ResizeType getResizeType();
    
}
