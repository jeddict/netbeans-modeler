/**
 * Copyright 2013-2022 Gaurav Gupta
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
package org.netbeans.modeler.properties.combobox;

import java.util.List;
import org.netbeans.modeler.properties.spec.ComboBoxValue;

public interface ComboBoxListener<T> {

    void setItem(ComboBoxValue<T> value);

    String getDefaultText();

    ComboBoxValue<T> getItem();

    List<ComboBoxValue<T>> getItemList();

    default public ActionHandler getActionHandler(){
        return null;
    }

}
