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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.config.element.Element;
import org.netbeans.modeler.config.element.ElementConfig;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.config.element.Group;
import org.netbeans.modeler.config.element.ModelerSheetProperty;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.properties.enumtype.EnumComboBoxResolver;
import org.netbeans.modeler.properties.type.Enumy;
import org.netbeans.modeler.specification.model.document.ITextElement;
import static org.netbeans.modeler.specification.model.document.property.PropertySetUtil.createPropertyChangeHandler;
import static org.netbeans.modeler.specification.model.document.property.PropertySetUtil.createPropertyVisibilityHandler;
import static org.netbeans.modeler.specification.model.document.property.PropertySetUtil.elementValueChanged;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.generic.ElementPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class ElementPropertySet {

    private ModelerFile modelerFile;
    private final ElementConfig elementConfig;
    private Sheet sheet;
    private Map<Group, Sheet.Set> set = new HashMap<>();

    public ElementPropertySet(ModelerFile modelerFile, Sheet sheet) {
        this.modelerFile = modelerFile;
        this.sheet = sheet;
        this.elementConfig = modelerFile.getModelerDiagramModel().getElementConfigFactory().getElementConfig();;
    }

    public void createGroup(Group group) {
        set.put(group, sheet.createPropertiesSet());
        set.get(group).setName(group.getId());// Sheet.Set : name is required work as key [otherwise set is replaced]
    }
    
    public void deleteGroup(String id) {
        Group group = getElementConfigGroup(id);
        set.remove(group);
    }
    
    public void clearGroup(String id) {
        Group group = getElementConfigGroup(id);
        if(set.get(group)!=null){
            for(Node.Property<?> property : set.get(group).getProperties()){
                set.get(group).remove(property.getName());
            }
        }
    }
    
    public void clearGroups() {
        for(Sheet.Set setTmp : set.values()){
            Arrays.fill( setTmp.getProperties(), null );
        }
        set.clear();
    }

    private Map<String, LinkedList<SheetProperty>> preOrderedPropeties = new LinkedHashMap<>();
    
   
    public synchronized void put(String id, Node.Property<?> p, boolean replace) {
        LinkedList<SheetProperty> properties = preOrderedPropeties.get(id);
        if(properties==null){
            preOrderedPropeties.put(id, properties = new LinkedList<>());
        }
        properties.add(new SheetProperty(p,replace));
    }
    
    
    public void executeProperties(){
        for(Entry<String, LinkedList<SheetProperty>> entry : preOrderedPropeties.entrySet()){
            LinkedList<SheetProperty> propertyList = entry.getValue();
            //Prepare hashmap container
            Map<String, PropertyLinkedList<SheetProperty>> filterContainerMap= new LinkedHashMap<>();
            propertyList.stream().forEach(p -> {
                if (filterContainerMap.get(p.getProperty().getName()) == null) {
                    PropertyLinkedList<SheetProperty> propertyLinkedList = new PropertyLinkedList<>();
                    propertyLinkedList.addFirst(p);
                    filterContainerMap.put(p.getProperty().getName(), propertyLinkedList);
                } else {
                    filterContainerMap.get(p.getProperty().getName()).addLast(p);
                }
            });
            
            propertyList.stream().forEach(p -> {
                if(p.getProperty() instanceof ModelerSheetProperty){
                    ModelerSheetProperty msp = (ModelerSheetProperty)p.getProperty();
                    if(msp.getAfter()!=null){
                        PropertyLinkedList<SheetProperty>  linklistA = filterContainerMap.get(msp.getAfter());
                        PropertyLinkedList<SheetProperty>  linklistB = filterContainerMap.get(p.getProperty().getName());
                        if(linklistA==null){
                           System.out.println("Invalid after type : " + msp.getAfter());
                       }
                        if(linklistA!=null && linklistA!=linklistB){
                            linklistA.addLast(linklistB.getHead());
                        }
                    }
                    if(msp.getBefore()!=null){
                       PropertyLinkedList<SheetProperty>  linklistB = filterContainerMap.get(p.getProperty().getName());
                       PropertyLinkedList<SheetProperty>  linklistA = filterContainerMap.get(msp.getBefore());
                       if(linklistA==null){
                           System.out.println("Invalid before type : " + msp.getBefore());
                       }
                        if(linklistA!=null && linklistA!=linklistB){
                            linklistB.addLast(linklistA.getHead());
                        } 
                    }
                }
            });
            
            for (Entry<String, PropertyLinkedList<SheetProperty>> filterEntry : filterContainerMap.entrySet()) {
                PropertyLinkedList<SheetProperty> propertyLinkedList = filterEntry.getValue();
                if (propertyLinkedList.getHead().prev == null) {
                    PropertyLinkedList<SheetProperty>.Node node = propertyLinkedList.getHead();
                    while (node != null) {
                        putProperty(entry.getKey(), node.element.getProperty(), node.element.isReplace());
                        node = node.next;
                    }
                }
            }
            
        }
        preOrderedPropeties.clear();
    }
    
    public synchronized Node.Property<?> putProperty(String id, Node.Property<?> p, boolean replace) {
        Group group = getElementConfigGroup(id);
        if (set.get(group) == null) {
            createGroup(group);
            setGroupDisplayName(id, group.getName());
        }
        if (replace && set.get(group).get(p.getName()) != null) {
            set.get(group).remove(p.getName());
        }
        return set.get(group).put(p);
    }
    
    private Group getElementConfigGroup(String id){
        Group group = elementConfig.getGroup(id);
            if (group == null) {
                throw new RuntimeException("Group Id : <" + id + "> not found in element config");
            }
            return group;
    }

    public synchronized void put(String id, Node.Property<?> p) {
        put(id, p, false);
    }

    public void setGroupDisplayName(String id, String displayName) {
        Group group = getElementConfigGroup(id);
        set.get(group).setDisplayName(displayName);
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
        Group group = getElementConfigGroup(id);
        return set.get(group);
    }

    public Set<String> getGroupKey() {
        return set.keySet().stream().map(Group::getName).collect(toSet());
    }

    public List<Sheet.Set> getGroups() {
        return set.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getValue()).collect(toList());
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
    * Filter using category
      category used when object have more than one child of same type
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

        ElementConfigFactory elementConfigFactory = modelerFile.getModelerDiagramModel().getElementConfigFactory();
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
                    String attributeGroupId = groupId;
                    if (attributeGroupId == null) {
                        attributeGroupId = attribute.getGroupId();
                    }
                    if (attribute.getClassType() == ITextElement.class) {
                        final String name = attribute.getName();
                        final ITextElement expression = (ITextElement) PropertyUtils.getProperty(object, name);//return must not be null//(TExpression) PropertyUtils.getProperty(object, id) == null ? new TExpression() : (TExpression) PropertyUtils.getProperty(object, id);
                        this.put(attributeGroupId, new ElementCustomPropertySupport(this.getModelerFile(), expression, String.class, attribute.getId(),"content",
                                attribute.getDisplayName(), attribute.getShortDescription(), (PropertyChangeListener<String>) (oldValue, value) -> {
                                    if (expression.getContent() == null || expression.getContent().isEmpty()) {
                                        try {
                                            PropertyUtils.setProperty(object, name, null);
                                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                                            Exceptions.printStackTrace(ex);
                                        }
                                    }
                                    if (propertyChangeHandlers != null && propertyChangeHandlers.get(name) != null) {
                                        propertyChangeHandlers.get(name).changePerformed(oldValue, value);
                                    }
                                    if (attribute.isRefreshOnChange()) {
                                        baseElementWidget.refreshProperties();
                                    }
                        }, propertyVisiblityHandlers == null ? null : propertyVisiblityHandlers.get(attribute.getId())), replaceProperty);

                    } else if (Enumy.class.isAssignableFrom(attribute.getClassType())) {
                        EnumComboBoxResolver resolver = Lookup.getDefault().lookup(EnumComboBoxResolver.class);
                        this.put(attributeGroupId,resolver.getPropertySupport(modelerFile, attribute, baseElementWidget, object, propertyChangeHandlers));
                    }else {
                        if (attribute.isReadOnly()) {
                            String value = BeanUtils.getProperty(object, attribute.getName());
                            if (value == null) {
                                BeanUtils.setProperty(object, attribute.getName(), attribute.getValue());
                            }
                            this.put(attributeGroupId, new ElementPropertySupport(object, attribute.getFieldGetter(), null, attribute), replaceProperty);
                        } else {
                            PropertyVisibilityHandler propertyVisibilityHandler = propertyVisiblityHandlers == null ? null : propertyVisiblityHandlers.get(attribute.getId());

                            Serializable visibilityExpression = attribute.getVisibilityExpression();
                            if (propertyVisibilityHandler == null && visibilityExpression != null) {
                                propertyVisibilityHandler = createPropertyVisibilityHandler(modelerFile, baseElementWidget, object, visibilityExpression);
                            }
                            if (propertyChangeHandlers != null && propertyChangeHandlers.get(attribute.getId()) == null && attribute.getOnChangeEvent() != null && !attribute.getOnChangeEvent().trim().isEmpty()) {
                                propertyChangeHandlers.put(attribute.getId(), createPropertyChangeHandler(modelerFile, baseElementWidget, object, attribute.getChangeListenerExpression()));
                            }
                            this.put(attributeGroupId, new ElementCustomPropertySupport(this.getModelerFile(), object, attribute, (PropertyChangeListener<Object>) (oldValue, value) -> {
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
                                        } catch (IllegalAccessException | InvocationTargetException ex) {
                                            Exceptions.printStackTrace(ex);
                                        }
                                      elementValueChanged(baseElementWidget, attribute, propertyChangeHandlers, oldValue, value );  
                            }, propertyVisibilityHandler), replaceProperty);

                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
     
}
 class SheetProperty {
  private Node.Property<?> property; private boolean replace;

    public SheetProperty(Node.Property<?> property) {
        this.property = property;
    }

    public SheetProperty(Node.Property<?> property, boolean replace) {
        this.property = property;
        this.replace = replace;
    }

    /**
     * @return the property
     */
    public Node.Property<?> getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(Node.Property<?> property) {
        this.property = property;
    }

    /**
     * @return the replace
     */
    public boolean isReplace() {
        return replace;
    }

    /**
     * @param replace the replace to set
     */
    public void setReplace(boolean replace) {
        this.replace = replace;
    }
}
    