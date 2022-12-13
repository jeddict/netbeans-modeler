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
package org.netbeans.modeler.properties.spec;

public class RowValue extends Entity {

    private Object[] row;
    
    public RowValue(Object[] row) {
        this("", row);
    }

    public RowValue(String id, Object[] row) {
        super(id);
        this.row = row;
    }

    /**
     * @return the row
     */
    public Object[] getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(Object[] row) {
        this.row = row;
    }

//    /**
//     * @return the tableModel
//     */
//    public AbstractTableModel getTableModel() {
//        return tableModel;
//    }
//
//    /**
//     * @param tableModel the tableModel to set
//     */
//    public void setTableModel(AbstractTableModel tableModel) {
//        this.tableModel = tableModel;
//    }
}
