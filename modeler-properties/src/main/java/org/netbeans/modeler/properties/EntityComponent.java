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
package org.netbeans.modeler.properties;

import java.awt.Component;
import java.awt.Frame;
import org.netbeans.modeler.properties.spec.Entity;
import org.netbeans.modeler.properties.window.GenericDialog;

public abstract class EntityComponent<T> extends GenericDialog {

    private Component rootComponent;//implemented for table but todo for combobox

    public EntityComponent(String title, boolean modal) {
        this((Frame) null, title, modal);
    }

    public EntityComponent() {
        this((Frame) null, "", true);
    }

    public EntityComponent(java.awt.Frame parent, boolean modal) {
        this(parent, "", modal);
    }

    public EntityComponent(java.awt.Frame parent, String title, boolean modal) {
        super(parent, title, modal);
    }


    private Entity<T> entity;

    /**
     * invoked before createEntity / updateEntity
     */
    public abstract void init();
    
    private boolean loaded = false;
    /**
     * Instance attached with the GraphNode
     * Invoked onetime after object construction but before init (on first init() invocation)
     */
    public void postConstruct(){
        
    }

    public abstract void createEntity(Class<? extends Entity> entityWrapperType);

    public abstract void updateEntity(Entity<T> entity);
    
    

    public Entity<T> getEntity() {
        return entity;
    }

    public void setEntity(Entity<T> entity) {
        this.entity = entity;
    }



    /**
     * @return the rootComponent
     */
    public Component getRootComponent() {
        return rootComponent;
    }

    /**
     * @param rootComponent the rootComponent to set
     */
    public void setRootComponent(Component rootComponent) {
        this.rootComponent = rootComponent;
    }

    /**
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(){
        this.loaded = true;
    }

}
