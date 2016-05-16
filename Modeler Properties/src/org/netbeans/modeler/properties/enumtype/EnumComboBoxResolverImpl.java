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
import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.ComboBoxValue;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.listener.ActionHandler;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.listener.ComboBoxListener;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.support.ComboBoxPropertySupport;
import org.netbeans.modeler.properties.type.Enumy;
import org.openide.nodes.PropertySupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jGauravGupta <gaurav.gupta.jc@gmail.com>
 */
//@ServiceProvider(service = EnumComboBoxResolver.class)
public class EnumComboBoxResolverImpl implements EnumComboBoxResolver {

     public PropertySupport getPropertySupport(ModelerFile modelerFile, Attribute attribute, Object object) {
        ComboBoxListener<Enumy> comboBoxListener = new ComboBoxListener<Enumy>() {
            @Override
            public void setItem(ComboBoxValue<Enumy> value) {
                Enumy enumy = value.getValue();
                try {
                org.apache.commons.beanutils.PropertyUtils.setProperty(object, attribute.getName(), enumy);
                }catch(IllegalAccessException | NoSuchMethodException | InvocationTargetException ex){
                    modelerFile.handleException(ex);
                }
            }

            @Override
            public ComboBoxValue<Enumy> getItem() {
                Enumy enumy = null;
                try {
                    enumy = (Enumy) org.apache.commons.beanutils.PropertyUtils.getProperty(object, attribute.getName());
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    modelerFile.handleException(ex);
                }
                if (enumy == null) {
                    Object[] enumConstants = attribute.getClassType().getEnumConstants();
                    enumy = ((Enumy) enumConstants[0]).getDefault();
                }
                return new ComboBoxValue(enumy, enumy.getDisplay());
            }

            @Override
            public List<ComboBoxValue<Enumy>> getItemList() {
                List<ComboBoxValue<Enumy>> values = new ArrayList<>();
                Object[] enumConstants = attribute.getClassType().getEnumConstants();
                for(Object enumConstant : enumConstants){
                    Enumy enumy = (Enumy)enumConstant;
                    values.add(new ComboBoxValue(enumy,enumy.getDisplay()));
                }
                return values;
            }

            @Override
            public String getDefaultText() {
                Object[] enumConstants = attribute.getClassType().getEnumConstants();
                 Enumy enumy = (Enumy)enumConstants[0];
                return enumy.getDefault().getDisplay();
            }

            @Override
            public ActionHandler getActionHandler() {
                return null;
            }
        };
        return new ComboBoxPropertySupport(modelerFile, attribute.getId(), attribute.getDisplayName(), attribute.getShortDescription(), comboBoxListener);
    }

}
