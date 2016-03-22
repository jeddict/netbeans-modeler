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
package org.netbeans.modeler.specification.model.document.property;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.mvel2.MVEL;
import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.config.element.Element;
import org.netbeans.modeler.config.element.ElementConfig;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.config.element.Group;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.ITextElement;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.generic.ElementPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

public class ElementPropertySet {

    private ModelerFile modelerFile;
    private ElementConfig elementConfig;
    private Sheet sheet;
    private Map<String, Sheet.Set> set = new LinkedHashMap<String, Sheet.Set>();

    public ElementPropertySet(ModelerFile modelerFile, Sheet sheet) {
        this.modelerFile = modelerFile;
        this.sheet = sheet;
        this.elementConfig = modelerFile.getVendorSpecification().getElementConfigFactory().getElementConfig();;

    }

    public void createGroup(String id) {
        set.put(id, sheet.createPropertiesSet());
        set.get(id).setName(id);// Sheet.Set : name is required work as key [otherwise set is replaced]
    }

    public synchronized Node.Property<?> put(String id, Node.Property<?> p, boolean replace) {
        if (set.get(id) == null) {
            Group group = elementConfig.getGroup(id);
            if (group == null) {
                throw new RuntimeException("Group Id : " + id + " not found in element config for Element : " + p.getName());
            }
            createGroup(id);
            setGroupDisplayName(id, group.getName());
        }
        if (replace && set.get(id).get(p.getName()) != null) {
            set.get(id).remove(p.getName());
        }
        return set.get(id).put(p);
    }

    public synchronized Node.Property<?> put(String id, Node.Property<?> p) {
        return put(id, p, false);
    }

    public void setGroupDisplayName(String id, String displayName) {
        set.get(id).setDisplayName(displayName);
    }

    /**
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * @return the set
     */
    public Sheet.Set getGroup(String id) {
        return set.get(id);
    }

    public Set<String> getGroupKey() {
        return set.keySet();
    }

    public LinkedList<Sheet.Set> getGroups() {
        return new LinkedList<Sheet.Set>(set.values());
    }

    /**
     * @return the modelerFile
     */
    public ModelerFile getModelerFile() {
        return modelerFile;
    }

    /**
     * @param modelerFile the modelerFile to set
     */
    public void setModelerFile(ModelerFile modelerFile) {
        this.modelerFile = modelerFile;
    }

    public void createPropertySet(IBaseElementWidget baseElementWidget, final Object object) {
        createPropertySet(null,null,baseElementWidget, object, null, null, true, false);
    }

    public void createPropertySet(IBaseElementWidget baseElementWidget, final Object object,
            final Map<String, PropertyChangeListener> propertyChangeHandlers) {
        createPropertySet(null,null,baseElementWidget, object, propertyChangeHandlers, null, true, false);
    }
    
    /*
    * Override attributes groupId
    */
      public void createPropertySet(String groupId, IBaseElementWidget baseElementWidget, final Object object,
            final Map<String, PropertyChangeListener> propertyChangeHandlers) {
        createPropertySet(groupId,null,baseElementWidget, object, propertyChangeHandlers, null, true, false);
    }  
      
      
    /*
    * Filetr using category  
    */
      public void createPropertySet(String groupId,String category, IBaseElementWidget baseElementWidget, final Object object,
            final Map<String, PropertyChangeListener> propertyChangeHandlers) {
        createPropertySet(groupId,category,baseElementWidget, object, propertyChangeHandlers, null, true, false);
    }
    

    public void createPropertySet(IBaseElementWidget baseElementWidget, final Object object,
            final Map<String, PropertyChangeListener> propertyChangeHandlers,
            final Map<String, PropertyVisibilityHandler> propertyVisiblityHandlers) {
        createPropertySet(null,null,baseElementWidget, object, propertyChangeHandlers, propertyVisiblityHandlers, true, false);
    }

    public void createPropertySet(IBaseElementWidget baseElementWidget, final Object object,
            final Map<String, PropertyChangeListener> propertyChangeHandlers, boolean inherit) {
        createPropertySet(null,null,baseElementWidget, object, propertyChangeHandlers, null, true, false);
    }

    /**
     * Replace Property Start
     *
     * @param baseElementWidget
     * @param object
     * @param propertyChangeHandlers *
     */
    public void replacePropertySet(IBaseElementWidget baseElementWidget, final Object object,
            final Map<String, PropertyChangeListener> propertyChangeHandlers) {
        createPropertySet(null,null,baseElementWidget, object, propertyChangeHandlers, null, true, true);
    }

    private void createPropertySet(String groupId,String category, IBaseElementWidget baseElementWidget, final Object object, final Map<String, PropertyChangeListener> propertyChangeHandlers, final Map<String, PropertyVisibilityHandler> propertyVisiblityHandlers, boolean inherit, boolean replaceProperty) {
        ElementConfigFactory elementConfigFactory = modelerFile.getVendorSpecification().getElementConfigFactory();
        if (inherit) {
            for (Element element : elementConfigFactory.getElements(category,object.getClass())) {
                createPropertySetInternal(groupId,baseElementWidget, object, element, propertyChangeHandlers, propertyVisiblityHandlers, replaceProperty);
            }
        } else {
            Element element = elementConfigFactory.getElement(category,object.getClass());
            if (element != null) {
                createPropertySetInternal(groupId,baseElementWidget, object, element, propertyChangeHandlers, propertyVisiblityHandlers, replaceProperty);
            }
        }
    }

    private void createPropertySetInternal(String groupId,final IBaseElementWidget baseElementWidget, final Object object, Element element, final Map<String, PropertyChangeListener> propertyChangeHandlers, final Map<String, PropertyVisibilityHandler> propertyVisiblityHandlers, boolean replaceProperty) {
        try {
            if (element != null) {
                for (final Attribute attribute : element.getAttributes()) {
                    if (groupId == null) {
                        groupId = attribute.getGroupId();
                    }
                    if (attribute.getClassType() == ITextElement.class) {
                        final String name = attribute.getName();
                        final ITextElement expression = (ITextElement) PropertyUtils.getProperty(object, name);//return must not be null//(TExpression) PropertyUtils.getProperty(object, id) == null ? new TExpression() : (TExpression) PropertyUtils.getProperty(object, id);
//                        PropertyUtils.setProperty(object, id, expression);
                        
                        this.put(groupId, new ElementCustomPropertySupport(this.getModelerFile(), expression, String.class, "content",
                                attribute.getDisplayName(), attribute.getShortDescription(),
                                new PropertyChangeListener<String>() {
                                    @Override
                                    public void changePerformed(String value) {
                                        if (expression.getContent() == null || expression.getContent().isEmpty()) {
                                            try {
                                                PropertyUtils.setProperty(object, name, null);
                                            } catch (IllegalAccessException ex) {
                                                Exceptions.printStackTrace(ex);
                                            } catch (InvocationTargetException ex) {
                                                Exceptions.printStackTrace(ex);
                                            } catch (NoSuchMethodException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }
                                        }
                                        if (propertyChangeHandlers != null && propertyChangeHandlers.get(name) != null) {
                                            propertyChangeHandlers.get(name).changePerformed(value);
                                        }
                                        if (attribute.isRefreshOnChange()) {
                                            baseElementWidget.refreshProperties();
                                        }
                                    }
                                }, propertyVisiblityHandlers == null ? null : propertyVisiblityHandlers.get(attribute.getId())), replaceProperty);

                    } else {
                        if (attribute.isReadOnly()) {
                            String value = BeanUtils.getProperty(object, attribute.getName());
                            if (value == null) {
                                BeanUtils.setProperty(object, attribute.getName(), attribute.getValue());
                            }
                            this.put(groupId, new ElementPropertySupport(object, attribute.getClassType(), attribute.getFieldGetter(), null, attribute.getDisplayName(), attribute.getShortDescription()), replaceProperty);
                        } else {
                            PropertyVisibilityHandler propertyVisibilityHandler = propertyVisiblityHandlers == null ? null : propertyVisiblityHandlers.get(attribute.getId());

                            if (propertyVisibilityHandler == null && attribute.getVisible() != null && !attribute.getVisible().trim().isEmpty()) {
                                propertyVisibilityHandler = createPropertyVisibilityHandler(modelerFile, baseElementWidget, object, attribute.getVisibilityExpression());
                            }
                            if (propertyChangeHandlers != null && propertyChangeHandlers.get(attribute.getId()) == null && attribute.getOnChangeEvent() != null && !attribute.getOnChangeEvent().trim().isEmpty()) {
                                propertyChangeHandlers.put(attribute.getId(), createPropertyChangeHandler(modelerFile, baseElementWidget, object, attribute.getChangeListenerExpression()));
                            }
                            this.put(groupId, new ElementCustomPropertySupport(this.getModelerFile(), object, attribute.getClassType(),
                                    attribute.getName(), attribute.getDisplayName(), attribute.getShortDescription(),
                                    new PropertyChangeListener<Object>() {
                                        @Override
                                        public void changePerformed(Object value) {
                                            try {
                                                if (value != null) {
                                                    if (value instanceof String) {
                                                        if (!((String) value).isEmpty()) {
                                                            BeanUtils.setProperty(object, attribute.getName(), value);
                                                        } else {
                                                            BeanUtils.setProperty(object, attribute.getName(), null);
                                                        }
                                                    } else {
                                                        BeanUtils.setProperty(object, attribute.getName(), value);
                                                    }
                                                } else {
                                                    BeanUtils.setProperty(object, attribute.getName(), null);
                                                }
                                            } catch (IllegalAccessException ex) {
                                                Exceptions.printStackTrace(ex);
                                            } catch (InvocationTargetException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }
                                            if (propertyChangeHandlers != null && propertyChangeHandlers.get(attribute.getId()) != null) {
                                                propertyChangeHandlers.get(attribute.getId()).changePerformed(value);
                                            }
                                            if (attribute.isRefreshOnChange()) {
                                                baseElementWidget.refreshProperties();
                                            }
                                        }
                                    }, propertyVisibilityHandler), replaceProperty);

                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public static PropertyVisibilityHandler createPropertyVisibilityHandler(ModelerFile modelerFile, final IBaseElementWidget baseElementWidget, final Object object, final Serializable exp) {
        final IRootElement root = (IRootElement)modelerFile.getModelerScene().getBaseElementSpec();
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
        return (PropertyChangeListener) (Object value) -> {
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

}
