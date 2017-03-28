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
package org.netbeans.modeler.properties.entity.custom.editor.combobox.client.listener;

import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.ComboBoxValue;
import org.netbeans.modeler.properties.EntityComponent;

    /**
     * ActionHandler handler provides 2 options :
     * 1) Use Entity Component to create & edit combobox value
     * 2) Use Supplier to return value on creation event , e.g connect class search panel with combobox
     * 
     */
public class ActionHandler<T> {

    private Consumer<ComboBoxValue<T>> createAction;
    private Consumer<ComboBoxValue<T>> updateAction;
    private Consumer<ComboBoxValue<T>> deleteAction;
    private IntSupplier removeMessage;
    
    private Supplier<ComboBoxValue<T>> createItem;
    private EntityComponent entityComponent;
    

    
    
    
    private ActionHandler(EntityComponent entityComponent){
                this.entityComponent = entityComponent;
    }

    public static <T> ActionHandler<T> getInstance(EntityComponent entityComponent){
        return (ActionHandler<T>)new ActionHandler<>(entityComponent);
    }
    
    private ActionHandler(Supplier<ComboBoxValue<T>> createItem){
                this.createItem = createItem;
    }

    public static <T> ActionHandler<T> getInstance(Supplier<ComboBoxValue<T>> createItem){
        return (ActionHandler<T>)new ActionHandler<>(createItem);
    }

    
    /**
     * @param createAction the createAction to set
     */
    public ActionHandler<T> afterCreation(Consumer<ComboBoxValue<T>> createAction) {
        this.createAction = createAction;
        return this;
    }

    /**
     * @param updateAction the updateAction to set
     */
    public ActionHandler<T> afterUpdation(Consumer<ComboBoxValue<T>> updateAction) {
        this.updateAction = updateAction;
        return this;
    }

    /**
     * @param deleteAction the deleteAction to set
     */
    public ActionHandler<T> afterDeletion(Consumer<ComboBoxValue<T>> deleteAction) {
        this.deleteAction = deleteAction;
        return this;
    }

    /**
     * @param removeMessage the removeMessage to set
     */
    public ActionHandler<T> beforeDeletion(IntSupplier removeMessage) {
        this.removeMessage = removeMessage;
        return this;
    }

    /**
     * @return the createAction
     */
    Consumer<ComboBoxValue<T>> getCreateAction() {
        return createAction;
    }

    /**
     * @return the updateAction
     */
    Consumer<ComboBoxValue<T>> getUpdateAction() {
        return updateAction;
    }

    /**
     * @return the deleteAction
     */
    Consumer<ComboBoxValue<T>> getDeleteAction() {
        return deleteAction;
    }

    /**
     * @return the removeMessage
     */
    IntSupplier getRemoveMessage() {
        return removeMessage;
    }

    /**
     * @return the createItem
     */
    Supplier<ComboBoxValue<T>> getItemSupplier() {
        return createItem;
    }

    /**
     * @return the entityComponent
     */
    EntityComponent getItemProducer() {
        return entityComponent;
    }

}
