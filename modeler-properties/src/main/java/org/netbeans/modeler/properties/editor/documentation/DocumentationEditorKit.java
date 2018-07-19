/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modeler.properties.editor.documentation;

import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.HTMLFactory;

/**
 *
 * @author Sheryl
 */
public class DocumentationEditorKit extends HTMLEditorKit {

    /**
     * Creates a new instance of DocumentationEditorKit
     */
    public DocumentationEditorKit() {
    }

    @Override
    public ViewFactory getViewFactory() {
        return new HTMLFactoryExtended();
    }

    public static class HTMLFactoryExtended extends HTMLFactory implements ViewFactory {

        public HTMLFactoryExtended() {
        }

        // override default behavior, do not display html comment
        @Override
        public View create(Element elem) {
            Object obj = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
            if (obj instanceof HTML.Tag) {
                HTML.Tag tagType = (HTML.Tag) obj;
                if (tagType == HTML.Tag.COMMENT) {
                    return new ComponentView(elem);
                }
            }
            return super.create(elem);
        }
    }

}
