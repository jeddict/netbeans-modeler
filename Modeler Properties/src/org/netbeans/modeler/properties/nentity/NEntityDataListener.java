/**
 * Copyright [2017] Gaurav Gupta
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NEntityDataListener<T> implements INEntityDataListener {

    private List<Object[]> data;  //object[] represent row
    private int count;
    private final Collection<T> inputData; //T represent row
    private final Function<T,List> displayDataFunction;
    private BiConsumer<T, Object[]> onSaveRowCallBack;
    private Runnable onSaveTableCallBack;

    public NEntityDataListener(Collection<T> inputData, Function<T,List> displayDataFunction) {
        this.inputData = inputData;
        this.displayDataFunction = displayDataFunction;
    }

    public NEntityDataListener(Collection<T> inputData, Function<T, List> displayDataFunction, BiConsumer<T, Object[]> onSaveRowCallBack) {
        this.inputData = inputData;
        this.displayDataFunction = displayDataFunction;
        this.onSaveRowCallBack = onSaveRowCallBack;
    }

    public NEntityDataListener(Collection<T> inputData, Function<T, List> displayDataFunction, BiConsumer<T, Object[]> onSaveRowCallBack, Runnable onSaveTableCallBack) {
        this.inputData = inputData;
        this.displayDataFunction = displayDataFunction;
        this.onSaveRowCallBack = onSaveRowCallBack;
        this.onSaveTableCallBack = onSaveTableCallBack;
    }
    
    
    
    @Override
    public void initCount() {
        count = inputData.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void initData() {
        List<Object[]> data_local = new LinkedList<>();
        Iterator<? extends T> itr = inputData.iterator();
        while (itr.hasNext()) {
            T t = itr.next();
            List displayData = displayDataFunction.apply(t);
            Object[] row = new Object[displayData.size() + 1];
            row[0] = t;//reserved hidden column for object row-0
            for (int i = 1; i < displayData.size()+1; i++) { //row-1 to row-n
                row[i] = displayData.get(i - 1);
            }
            data_local.add(row);
        }
        this.data = data_local;
    }

    @Override
    public List<Object[]> getData() {
        return data;
    }

    @Override
    public void setData(List<Object[]> data) {
        inputData.clear();
        data.stream().forEach((row) -> {
            T t = (T) row[0];
            if(onSaveRowCallBack!=null){
                onSaveRowCallBack.accept(t, row);
            }
            inputData.add(t);
        });
        if(onSaveTableCallBack!=null){
            onSaveTableCallBack.run();
        }
        this.data = data;
    }
    
       
    

}
