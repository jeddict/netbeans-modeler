/**
 * Copyright 2013-2018 Gaurav Gupta
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
package org.netbeans.modeler.validation.jaxb;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class ValidateJAXB implements ValidationEventHandler {

    @Override
    public boolean handleEvent(ValidationEvent event) {
        try {
            System.out.println("\nEVENT");
            System.out.println("SEVERITY:  " + event.getSeverity());
            System.out.println("MESSAGE:  " + event.getMessage());
            System.out.println("LINKED EXCEPTION:  " + event.getLinkedException());

            if (event.getLocator() != null) {
                System.out.println("LOCATOR");
                System.out.println("    LINE NUMBER:  " + event.getLocator().getLineNumber());
                System.out.println("    COLUMN NUMBER:  " + event.getLocator().getColumnNumber());
                System.out.println("    OFFSET:  " + event.getLocator().getOffset());
                System.out.println("    OBJECT:  " + event.getLocator().getObject());
                System.out.println("    NODE:  " + event.getLocator().getNode());
                System.out.println("    URL:  " + event.getLocator().getURL());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
