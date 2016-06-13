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

import org.netbeans.modeler.config.element.Attribute;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author jGauravGupta <gaurav.gupta.jc@gmail.com>
 */
public interface EnumComboBoxResolver {
    PropertySupport getPropertySupport(ModelerFile modelerFile, Attribute attribute, IBaseElementWidget baseElementWidget, Object object);
}
