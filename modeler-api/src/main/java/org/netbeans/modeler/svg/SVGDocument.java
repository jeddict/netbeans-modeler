/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modeler.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jGauravGupta
 */
public interface SVGDocument extends Document {

    Element getRootElement();

    SVGDocument cloneDocument(boolean deep);

}
