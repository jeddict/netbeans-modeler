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
package org.netbeans.modeler.core.exception;

public class SVGAttributeNotFoundException extends SVGDocumentException {

    private String element;
    private String attribute;

    /**
     * Creates a new instance of <code>SVGAttributeNotFoundException</code>
     * without detail message.
     */
    public SVGAttributeNotFoundException() {
    }

    public SVGAttributeNotFoundException(String element, String attribute) {
        super("Element : " + element + " , Attribute : " + attribute);
        this.element = element;
        this.attribute = attribute;
    }

    /**
     * Constructs an instance of <code>SVGAttributeNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public SVGAttributeNotFoundException(String msg) {
        super(msg);
    }

    /**
     * @return the element
     */
    public String getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(String element) {
        this.element = element;
    }

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
