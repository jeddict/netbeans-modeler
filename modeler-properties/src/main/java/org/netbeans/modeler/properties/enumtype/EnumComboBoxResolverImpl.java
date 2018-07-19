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

package org.netbeans.modeler.properties.enumtype;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.properties.spec.ComboBoxValue;
import org.netbeans.modeler.properties.combobox.ActionHandler;
import org.netbeans.modeler.properties.combobox.ComboBoxListener;
import org.netbeans.modeler.properties.combobox.ComboBoxPropertySupport;
import org.netbeans.modeler.properties.type.Enumy;
import static org.netbeans.modeler.specification.model.document.property.PropertySetUtil.elementValueChanged;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.nodes.PropertySupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jGauravGupta <gaurav.gupta.jc@gmail.com>
 */
@ServiceProvider(service = EnumComboBoxResolver.class)
public class EnumComboBoxResolverImpl implements EnumComboBoxResolver {

     @Override
     public PropertySupport getPropertySupport(ModelerFile modelerFile, Attribute attribute, IBaseElementWidget baseElementWidget, Object object, Map<String, PropertyChangeListener> propertyChangeHandlers) {
         final Object[] ENUMS = attribute.getClassType().getEnumConstants();
         Enumy DEFAULT = ((Enumy)ENUMS[0]).getDefault();
        ComboBoxListener<Enumy> comboBoxListener = new ComboBoxListener<Enumy>() {
            @Override
            public void setItem(ComboBoxValue<Enumy> value) {
                ComboBoxValue<Enumy> oldValue = getItem();
                Enumy enumy = value.getValue();
                try {
                org.apache.commons.beanutils.PropertyUtils.setProperty(object, attribute.getName(), enumy);
                }catch(IllegalAccessException | NoSuchMethodException | InvocationTargetException ex){
                    modelerFile.handleException(ex);
                }
                if (attribute.isRefreshOnChange()) {
                    baseElementWidget.refreshProperties();
                }
                elementValueChanged(baseElementWidget, attribute, propertyChangeHandlers, oldValue, value);  
            }

            @Override
            public ComboBoxValue<Enumy> getItem() {
                Enumy enumy = null;
                try {
                    enumy = (Enumy) org.apache.commons.beanutils.PropertyUtils.getProperty(object, attribute.getName());
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    modelerFile.handleException(ex);
                }
                if (enumy == null && DEFAULT!=null) {
                    enumy = DEFAULT;
                }
                if (enumy == null){
                return new ComboBoxValue(null, StringUtils.EMPTY);
                } else {
                return new ComboBoxValue(enumy, enumy.getDisplay());    
                }
            }

            @Override
            public List<ComboBoxValue<Enumy>> getItemList() {
                List<ComboBoxValue<Enumy>> values = new ArrayList<>();
                 if (DEFAULT == null) {
                     values.add(new ComboBoxValue(null, StringUtils.EMPTY));// null, ""
                }
                for(Object enumConstant : ENUMS){
                    Enumy enumy = (Enumy)enumConstant;
                    values.add(new ComboBoxValue(enumy,enumy.getDisplay()));
                }
                return values;
            }

            @Override
            public String getDefaultText() {
                 if (DEFAULT != null) {
                    return DEFAULT.getDisplay();
                } else {
                    return StringUtils.EMPTY;
                }
            }

            @Override
            public ActionHandler getActionHandler() {
                return null;
            }
        };
        return new ComboBoxPropertySupport(modelerFile, attribute, comboBoxListener);
    }

}
