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
package org.netbeans.modeler.svg;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.svg.SVGDocument;
/**
 *
 * @author jGauravGupta
 */
public class SVGDocumentImpl implements org.netbeans.modeler.svg.SVGDocument {
    
    private final SVGDocument document;

    public SVGDocumentImpl(SVGDocument document) {
        this.document = document;
    }

    @Override
    public org.netbeans.modeler.svg.SVGDocument cloneDocument(boolean deep) {
        return new SVGDocumentImpl((SVGDocument)document.cloneNode(deep));
    }
    
    public Element getRootElement() {
       return document.getRootElement();
    }

    @Override
    public DocumentType getDoctype() {
        return document.getDoctype();
    }

    @Override
    public DOMImplementation getImplementation() {
        return document.getImplementation();
    }

    @Override
    public Element getDocumentElement() {
        return document.getDocumentElement();
    }

    @Override
    public Element createElement(String tagName) throws DOMException {
        return document.createElement(tagName);
    }

    @Override
    public DocumentFragment createDocumentFragment() {
        return document.createDocumentFragment();
    }

    @Override
    public Text createTextNode(String data) {
        return document.createTextNode(data);
    }

    @Override
    public Comment createComment(String data) {
        return document.createComment(data);
    }

    @Override
    public CDATASection createCDATASection(String data) throws DOMException {
        return document.createCDATASection(data);
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        return document.createProcessingInstruction(target, data);
    }

    @Override
    public Attr createAttribute(String name) throws DOMException {
        return document.createAttribute(name);
    }

    @Override
    public EntityReference createEntityReference(String name) throws DOMException {
        return document.createEntityReference(name);
    }

    @Override
    public NodeList getElementsByTagName(String tagname) {
        return document.getElementsByTagName(tagname);
    }

    @Override
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        return document.importNode(importedNode, deep);
    }

    @Override
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return document.createElementNS(namespaceURI, qualifiedName);
    }

    @Override
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return document.createAttributeNS(namespaceURI, qualifiedName);
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return document.getElementsByTagNameNS(namespaceURI, localName);
    }

    @Override
    public Element getElementById(String elementId) {
        return document.getElementById(elementId);
    }

    @Override
    public String getInputEncoding() {
        return document.getInputEncoding();
    }

    @Override
    public String getXmlEncoding() {
        return document.getXmlEncoding();
    }

    @Override
    public boolean getXmlStandalone() {
        return document.getXmlStandalone();
    }

    @Override
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        document.setXmlStandalone(xmlStandalone);
    }

    @Override
    public String getXmlVersion() {
        return document.getXmlVersion();
    }

    @Override
    public void setXmlVersion(String xmlVersion) throws DOMException {
        document.setXmlVersion(xmlVersion);
    }

    @Override
    public boolean getStrictErrorChecking() {
        return document.getStrictErrorChecking();
    }

    @Override
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        document.setStrictErrorChecking(strictErrorChecking);
    }

    @Override
    public String getDocumentURI() {
        return document.getDocumentURI();
    }

    @Override
    public void setDocumentURI(String documentURI) {
        document.setDocumentURI(documentURI);
    }

    @Override
    public Node adoptNode(Node source) throws DOMException {
        return document.adoptNode(source);
    }

    @Override
    public DOMConfiguration getDomConfig() {
        return document.getDomConfig();
    }

    @Override
    public void normalizeDocument() {
        document.normalizeDocument();
    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        return document.renameNode(n, namespaceURI, qualifiedName);
    }

    @Override
    public String getNodeName() {
        return document.getNodeName();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return document.getNodeValue();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        document.setNodeValue(nodeValue);
    }

    @Override
    public short getNodeType() {
        return document.getNodeType();
    }

    @Override
    public Node getParentNode() {
        return document.getParentNode();
    }

    @Override
    public NodeList getChildNodes() {
        return document.getChildNodes();
    }

    @Override
    public Node getFirstChild() {
        return document.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return document.getLastChild();
    }

    @Override
    public Node getPreviousSibling() {
        return document.getPreviousSibling();
    }

    @Override
    public Node getNextSibling() {
        return document.getNextSibling();
    }

    @Override
    public NamedNodeMap getAttributes() {
        return document.getAttributes();
    }

    @Override
    public Document getOwnerDocument() {
        return document.getOwnerDocument();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return document.insertBefore(newChild, refChild);
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return document.replaceChild(newChild, oldChild);
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return document.removeChild(oldChild);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return document.appendChild(newChild);
    }

    @Override
    public boolean hasChildNodes() {
        return document.hasChildNodes();
    }

    @Override
    public Node cloneNode(boolean deep) {
        return document.cloneNode(deep);
    }

    @Override
    public void normalize() {
        document.normalize();
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return document.isSupported(feature, version);
    }

    @Override
    public String getNamespaceURI() {
        return document.getNamespaceURI();
    }

    @Override
    public String getPrefix() {
        return document.getPrefix();
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        document.setPrefix(prefix);
    }

    @Override
    public String getLocalName() {
        return document.getLocalName();
    }

    @Override
    public boolean hasAttributes() {
        return document.hasAttributes();
    }

    @Override
    public String getBaseURI() {
        return document.getBaseURI();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return document.compareDocumentPosition(other);
    }

    @Override
    public String getTextContent() throws DOMException {
        return document.getTextContent();
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        document.setTextContent(textContent);
    }

    @Override
    public boolean isSameNode(Node other) {
        return document.isSameNode(other);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return document.lookupPrefix(namespaceURI);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return document.isDefaultNamespace(namespaceURI);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return document.lookupNamespaceURI(prefix);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return document.isEqualNode(arg);
    }

    @Override
    public Object getFeature(String feature, String version) {
        return document.getFeature(feature, version);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return document.setUserData(key, data, handler);
    }

    @Override
    public Object getUserData(String key) {
        return document.getUserData(key);
    }

    public Event createEvent(String eventType) throws DOMException {
        return document.createEvent(eventType);
    }

    
    
}
