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
package org.netbeans.modeler.widget.context;

import org.netbeans.modeler.widget.node.NodeWidget;

/**
 *
 *
 */
public abstract class AbstractRelationshipFactory implements RelationshipFactory {

    public void delete(boolean fromModel,
            NodeWidget element, //IPresentationElement
            NodeWidget source,
            NodeWidget target) {
//        if(fromModel == true)
//        {
//            NodeWidget modelElement = element.getFirstSubject(); 
//            if(modelElement != null)
//            {
//                modelElement.delete();
//            }
//        }
//            
//        element.delete();
    }

    /**
     * Reconnects a new source to the relationship. Since some relationships are
     * not true relationships, but instead represent properties on one of the
     * nodes, the old source and the target are also passed into the method.
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
    public void reconnectSource(NodeWidget relationship,
            NodeWidget oldSource,
            NodeWidget source,
            NodeWidget target) {
        reconnectSource(relationship, source);
    }

    /**
     * Reconnects a new target to the relationship. Since some relationships are
     * not true relationships, but instead represent properties on one of the
     * nodes, the old target and the source are also passed into the method.
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
    public void reconnectTarget(NodeWidget relationship,
            NodeWidget oldTarget,
            NodeWidget target,
            NodeWidget source) {
        reconnectTarget(relationship, target);
    }

    /**
     * Since most relationship factories are true relationships, this method is
     * provided to make it easier to reconnect the source of standard
     * relationships.
     *
     * @param relationship The relationship.
     * @param source The relationships new source.
     */
    protected abstract void reconnectSource(NodeWidget relationship, NodeWidget source);

    /**
     * Since most relationship factories are true relationships, this method is
     * provided to make it easier to reconnect the target of standard
     * relationships.
     *
     * @param relationship The relationship.
     * @param source The relationships new target.
     */
    protected abstract void reconnectTarget(NodeWidget relationship, NodeWidget target);
}
