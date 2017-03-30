/**
 * Copyright [2016] Gaurav Gupta
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
package org.netbeans.modeler.specification.model.document.property;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.mvel2.MVEL;
import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;

public class PropertySetUtil {

    public static PropertyVisibilityHandler createPropertyVisibilityHandler(ModelerFile modelerFile, final IBaseElementWidget baseElementWidget, final Object object, final Serializable exp) {
        final IRootElement root = (IRootElement) modelerFile.getModelerScene().getBaseElementSpec();
        return (PropertyVisibilityHandler) () -> {
            Map vars = new HashMap();
            vars.put("root", root);
            vars.put("widget", baseElementWidget);
            vars.put("_this", object);
            vars.put("node", baseElementWidget.getBaseElementSpec());
                return (Boolean) MVEL.executeExpression(exp, vars);
        };
    }

    public static PropertyChangeListener createPropertyChangeHandler(final ModelerFile modelerFile, final IBaseElementWidget baseElementWidget, final Object object, final Serializable exp) {
        final IRootElement root = (IRootElement)modelerFile.getModelerScene().getBaseElementSpec();
        return (oldValue, value) -> {
            Map vars = new HashMap();
            vars.put("root", root);
            vars.put("widget", baseElementWidget);
            vars.put("_this", object);
            vars.put("node", baseElementWidget.getBaseElementSpec());
            vars.put("value", value);
            vars.put("scene", modelerFile.getModelerScene());
                MVEL.executeExpression(exp, vars);
        };
    }

    public static PropertyVisibilityHandler createPropertyVisibilityHandler(ModelerFile modelerFile, final Object object, final String exp) { //this method should be removed // created cuz of MVEL BUG
        final IRootElement root = (IRootElement)modelerFile.getModelerScene().getBaseElementSpec();
        return (PropertyVisibilityHandler) () -> {
            Map vars = new HashMap();
            vars.put("root", root);
            vars.put("widget", object);
                return (Boolean) MVEL.executeExpression(MVEL.compileExpression(exp), vars);
        };
    }

    public static PropertyVisibilityHandler createPropertyVisibilityHandler(ModelerFile modelerFile, final Object object, final Serializable exp) { //this method should be removed // created cuz of MVEL BUG
        final IRootElement root = (IRootElement)modelerFile.getModelerScene().getBaseElementSpec();
        return (PropertyVisibilityHandler) () -> {
            Map vars = new HashMap();
            vars.put("root", root);
            vars.put("widget", object);
                return (Boolean) MVEL.executeExpression(exp, vars);
        };
    }

    public static void elementValueChanged(IBaseElementWidget baseElementWidget, Attribute attribute, Map<String, PropertyChangeListener> propertyChangeHandlers, Object oldValue, Object value) {
        if (propertyChangeHandlers != null && propertyChangeHandlers.get(attribute.getId()) != null) {
            propertyChangeHandlers.get(attribute.getId()).changePerformed(oldValue, value);
        }
        if (attribute.isRefreshOnChange()) {
            baseElementWidget.refreshProperties();
        }
    }

}
