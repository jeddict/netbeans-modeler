/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modeler.properties.view.manager;

import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;

/**
 *
 * @author SRV001
 */
public interface IPropertyManager {

    /**
     * @return the baseElementWidget
     */
    IBaseElementWidget getBaseElementWidget();

    /**
     * @return the modelerScene
     */
    IModelerScene getModelerScene();
    
}
