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

public class ModelerException extends Exception {

    /**
     * Creates a new instance of <code>ModelerException</code> without detail
     * message.
     */
    public ModelerException() {
    }

    /**
     * Constructs an instance of <code>ModelerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelerException(String msg) {
        super(msg);
    }
}
