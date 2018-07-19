/**
 * Copyright [2016] Gaurav Gupta
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
package org.netbeans.modeler.specification.version;

/**
 *
 * @author Gaurav Gupta
 */
public class SoftwareVersion implements Comparable<SoftwareVersion>
{
    private int[] parts;
    private String value;

    public static SoftwareVersion getInstance(String value){
       return new SoftwareVersion(value);
    }
    public SoftwareVersion(String value)
    {
        this.value=value;
        if(value == null)
            throw new IllegalArgumentException("Version can not be null");

        if(value.matches("^[0-9]+(\\.[0-9]+)*$") == false)
            throw new IllegalArgumentException("Invalid version format");

        String[] split = value.split("\\.");
        parts = new int[split.length];

        for (int i = 0; i < split.length; i += 1)
            parts[i] = Integer.parseInt(split[i]);
    }

    @Override
    public String toString()
    {
        String[] split = new String[parts.length];

        for (int i = 0; i < parts.length; i += 1)
            split[i] = String.valueOf(parts[i]);

        return String.join(".", split);
    }

    public static int compare(SoftwareVersion verA, SoftwareVersion verB)
    {
        if (verA == verB) return 0;
        if (verA == null) return -1;
        if (verB == null) return 1;

        int max = Math.max(verA.parts.length, verB.parts.length);

        for (int i = 0; i < max; i += 1)
        {
            int partA = i < verA.parts.length ? verA.parts[i] : 0;
            int partB = i < verB.parts.length ? verB.parts[i] : 0;
            if (partA < partB) return -1;
            if (partA > partB) return 1;
        }

        return 0;
    }

    
    public int compareTo(String that)
    {
        return compare(this, new SoftwareVersion(that));
    }
    
    @Override
    public int compareTo(SoftwareVersion that)
    {
        return compare(this, that);
    }

    public static boolean equals(SoftwareVersion verA, SoftwareVersion verB)
    {
        if (verA == verB) return true;
        if (verA == null) return false;
        if (verB == null) return false;
        return compare(verA, verB) == 0;
    }

    @Override
    public boolean equals(Object that)
    {
        return that instanceof SoftwareVersion && equals(this, (SoftwareVersion)that);
    }
    
//    public static class Migration {
//        public static boolean isValid(String previousVersion, String currentVersion){
//            
//        }
//    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
