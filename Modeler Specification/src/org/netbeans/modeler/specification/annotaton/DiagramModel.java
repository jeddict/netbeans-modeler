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
package org.netbeans.modeler.specification.annotaton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.specification.export.IExportManager;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface DiagramModel {

    public String id();

    public String name();

    public Class<? extends IModelerPanel> modelerPanel() default IModelerPanel.class;//for widget container component/window

    public Class<? extends IModelerUtil> modelerUtil();

    public Class<? extends IModelerScene> modelerScene();

    public Class<? extends IExportManager> exportManager();

    public Class<? extends IModelerDiagramEngine> modelerDiagramEngine() default IModelerDiagramEngine.class; //for widget event handler

    public Class<? extends IRelationValidator> relationValidator();

}
