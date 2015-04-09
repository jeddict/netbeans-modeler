/** Copyright [2014] Gaurav Gupta
   *
   *Licensed under the Apache License, Version 2.0 (the "License");
   *you may not use this file except in compliance with the License.
   *You may obtain a copy of the License at
   *
   *    http://www.apache.org/licenses/LICENSE-2.0
   *
   *Unless required by applicable law or agreed to in writing, software
   *distributed under the License is distributed on an "AS IS" BASIS,
   *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   *See the License for the specific language governing permissions and
   *limitations under the License.
   */
 package org.netbeans.modeler.widget.context;

import org.netbeans.modeler.widget.node.INodeWidget;


/**
 *
 * 
 */
public interface RelationshipFactory
{
    INodeWidget create(INodeWidget source, INodeWidget target);
    
    /**
     * Reconnects a new source to the relationship.  Since some relationships 
     * are not true relationships, but instead represent properties on one of 
     * the nodes, the old source and the target are also passed into the method.
     * 
     * To make it easy for true relationships, which are the majority of 
     * relationships, this implementation calls the abstract reconnectSource
     * that only takes the relationship and the source element.
     * 
     * @param relationship The model element associated with the edge.
     * @param oldSource The old source end of the edge.
     * @param source The new source end of the edge.
     * @param target The target end of the edge.
     * @param see #reconnectSource(IElement, IElement)
     */
    public void reconnectSource(INodeWidget relationship, 
                                INodeWidget oldSource, 
                                INodeWidget source,
                                INodeWidget target);
    
    /**
     * Reconnects a new target to the relationship.  Since some relationships 
     * are not true relationships, but instead represent properties on one of 
     * the nodes, the old target and the source are also passed into the method.
     * 
     * To make it easy for true relationships, which are the majority of 
     * relationships, this implementation calls the abstract reconnectSource
     * that only takes the relationship and the target element.
     * 
     * @param relationship The model element associated with the edge.
     * @param oldTarget The old target end of the edge.
     * @param target The new target end of the edge.
     * @param source The source end of the edge.
     * @param see #reconnectTarget(IElement, IElement)
     */
    public void reconnectTarget(INodeWidget relationship, 
                                INodeWidget oldTarget, 
                                INodeWidget target,
                                INodeWidget source);
    
    /**
     * The name of the relationship.
     * 
     * @return the name.
     */
    public String getElementType();
    
    /**
     * Deletes a relationship.  If the fromModel parameter is true then the 
     * model element will be deleted as well as the model elements presentation
     * information.  If the fromModel is false, only the presentation 
     * information is deleted.
     * 
     * Since soem relationships are not true relationships, but instead 
     * represents propeties on one of the ends, the source and element elements
     * are also supplied.
     * 
     * @param fromModel if true remove from the model as well.
     * @param element The model element.
     * @param source The source end of the relationship.
     * @param target The target end of the relationship.
     */
    public void delete(boolean fromModel, INodeWidget element, 
                       INodeWidget source, INodeWidget target);
     
}
