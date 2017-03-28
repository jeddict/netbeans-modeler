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
package org.netbeans.modeler.properties.nentity;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.modeler.properties.EntityComponent;

public class NAttributeEntity {

    private Boolean enableActionPanel = true;
    private String name;
    private String displayName;
    private String shortDescription;
    private String[] countDisplay = new String[3];

    private Table table = new Table(430, 350);
    private List<Column> columns = new ArrayList<>();
    private EntityComponent customDialog;
    private INEntityDataListener tableDataListener;

    public NAttributeEntity(String name, String displayName, String shortDescription) {
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
        countDisplay[0] = "No attributes set";
        countDisplay[1] = "One attribute set";
        countDisplay[2] = "attributes set";
    }

    public NAttributeEntity() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * @return the data
     */
//    public Object[][] getData() {
//        return data;
//    }
//
//    /**
//     * @param data the data to set
//     */
//    public void setData(Object[][] data) {
//        this.data = data;
//    }
    /**
     * @return the columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    public List<String> getColumnsName() {
        List<String> names = new ArrayList<String>();
        for (Column column : columns) {
            names.add(column.getName());
        }
        return names;
    }

    public List<Boolean> getColumnsEditable() {
        List<Boolean> editableList = new ArrayList<Boolean>();
        for (Column column : columns) {
            editableList.add(column.isEditable());
        }
        return editableList;
    }

    public List<Class> getColumnsType() {
        List<Class> types = new ArrayList<>();
        for (Column column : columns) {
            types.add(column.getClassType());
        }
        return types;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<Column> columns) {
        this.columns = new ArrayList<>();
        this.columns.add(new Column("OBJECT", false, true, Object.class));
        this.columns.addAll(columns);
    }

    /**
     * @return the customDialog
     */
    public EntityComponent getCustomDialog() {
        return customDialog;
    }

    /**
     * @param customDialog the customDialog to set
     */
    public void setCustomDialog(EntityComponent customDialog) {
        this.customDialog = customDialog;
    }

    /**
     * @return the tableDataListener
     */
    public INEntityDataListener getTableDataListener() {
        return tableDataListener;
    }

    /**
     * @param tableDataListener the tableDataListener to set
     */
    public void setTableDataListener(INEntityDataListener tableDataListener) {
        this.tableDataListener = tableDataListener;
    }

    /**
     * @return the countDisplay
     */
    public String[] getCountDisplay() {
        return countDisplay;
    }

    /**
     * @param countDisplay the countDisplay to set
     */
    public void setCountDisplay(String[] countDisplay) {
        this.countDisplay = countDisplay;
    }

//    /**
//     * @param countDisplay the countDisplay to set
//     */
//    public void setCountDisplay(String countDisplay) {
//        this.countDisplay = new String[]{String.format("No %s exist", English.plural(countDisplay)),
//            String.format("One %s exist", countDisplay), String.format("%s exist", English.plural(countDisplay))};
//    }
    
    /**
     * @return the table
     */
    public Table getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return the enableActionPanel
     */
    public Boolean isEnableActionPanel() {
        return enableActionPanel;
    }

    /**
     * @param enableActionPanel the enableActionPanel to set
     */
    public void setEnableActionPanel(Boolean enableActionPanel) {
        this.enableActionPanel = enableActionPanel;
    }
}
