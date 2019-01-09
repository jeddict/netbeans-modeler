/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modeler.svg;

import java.awt.Dimension;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 *
 * @author jGauravGupta
 */
public interface SvgNodeWidgetFactory {
    
    SvgNodeWidget create(IModelerScene scene, INodeWidget nodeWidget, SVGDocument doc, Dimension dimension);
}
