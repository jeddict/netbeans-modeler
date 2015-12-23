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
package org.netbeans.modeler.config.element;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 *
 */
public class Tester {

    public static void main(String[] args) throws JAXBException {

        ElementConfig elementConfig = new ElementConfig();

        Element e2 = new Element();
        e2.setId("e1Id");
        e2.setClassType(String.class);

        Attribute e2a1 = new Attribute();
        e2a1.setId("a1asd");
        e2a1.setName("ShgAasdd");
        e2a1.setShortDescription("sfgsajfaskfh");
        e2a1.setDisplayName("asdaksj");
        e2a1.setCondition("asfas");

        Attribute e2a2 = new Attribute();
        e2a2.setId("a1asd");
        e2a2.setName("ShgAasdd");
        e2a2.setShortDescription("sfgsajfaskfh");
        e2a2.setDisplayName("asdaksj");
        e2a2.setCondition("asfas");
        e2.addAttribute(e2a1);
        e2.addAttribute(e2a2);

        elementConfig.addElement(e2);

        Element e1 = new Element();
        e1.setId("e1Id");
        e1.setClassType(String.class);

        Attribute e1a1 = new Attribute();
        e1a1.setId("a1asd");
        e1a1.setName("ShgAasdd");
        e1a1.setShortDescription("sfgsajfaskfh");
        e1a1.setDisplayName("asdaksj");
        e1a1.setCondition("asfas");

        Attribute e1a2 = new Attribute();
        e1a2.setId("a1asd");
        e1a2.setName("ShgAasdd");
        e1a2.setShortDescription("sfgsajfaskfh");
        e1a2.setDisplayName("asdaksj");
        e1a2.setCondition("asfas");
        e1.addAttribute(e1a1);
        e1.addAttribute(e1a2);

        elementConfig.addElement(e1);

        File savedFile = new File("ElementConfig.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ElementConfig.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(elementConfig, savedFile);
        //  jaxbMarshaller.marshal(elementConfig, System.out);

    }
}
