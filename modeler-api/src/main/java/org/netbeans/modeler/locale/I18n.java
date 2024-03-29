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
package org.netbeans.modeler.locale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.openide.util.NbBundle;

/**
 * I18n is used to internationalize all the keys in the project.
 *
 *
 */
public class I18n {

    private static List<ResourceBundle> bundleLocations = new ArrayList<ResourceBundle>();

    public static void addBundleLocation(ResourceBundle bundle) {
        if (!bundleLocations.contains(bundle)) {
            bundleLocations.add(bundle);
        }
    }

    public static Map<String, ResourceBundle> cachedBundles = new HashMap<String, ResourceBundle>();

    //This is the default Resource Boundle for all the keys of the project.
    static final String DEFAULT_PACKAGE = "";

    /**
     * Finds a localized and/or branded string in the iReport resource bundle.
     * This call never throws an exception, but a warning message is printed
     * when a requested resource is not found.
     *
     * @param key name of the resource to look for
     * @return the localizaed string or the key if no resource is found
     */
    //The method given a key return its Value saved in the Resource Boundle.
    public static String getString(String key) {
        return getString(I18n.class, key);
    }

    /**
     * Finds a localized and/or branded string in the iReport resource bundle
     * and formats the message by passing requested parameters. This call never
     * throws an exception, but a warning message is printed when a requested
     * resource is not found.
     *
     * @param key name of the resource to look for
     * @param params
     * @return
     */
    public static String getString(String key, Object[] params) {
        return getString(I18n.class, key, params);
    }

    /**
     * Finds a localized and/or branded string in the iReport resource bundle
     * and formats the message by passing requested parameters.
     *
     * This call never throws an exception, but a warning message is printed
     * when a requested resource is not found.
     *
     * @param key name of the resource to look for
     * @param param1 the argument to use when formatting the message
     * @return
     */
    public static String getString(String key, Object param) {
        return getString(I18n.class, key, new Object[]{param});
    }

    /**
     * Finds a localized and/or branded string in the iReport resource bundle
     * and formats the message by passing requested parameters. This call never
     * throws an exception, but a warning message is printed when a requested
     * resource is not found.
     *
     * @param key name of the resource to look for
     * @param param1 the argument to use when formatting the message
     * @param param2 the second argument to use for formatting
     * @return
     */
    public static String getString(String key, Object param1, Object param2) {
        return getString(I18n.class, key, new Object[]{param1, param2});
    }

    public static String getString(Class clazz, String key, Object param1) {
        return getString(clazz, key, new Object[]{param1});
    }

    /**
     * Finds a localized and/or branded string in the iReport resource bundle
     * and formats the message by passing requested parameters. This call never
     * throws an exception, but a warning message is printed when a requested
     * resource is not found.
     *
     * @param key name of the resource to look for
     * @param param1 the argument to use when formatting the message
     * @param param2 the second argument to use for formatting
     * @param param3 the third argument to use for formatting
     * @return
     */
    public static String getString(String key, Object param1, Object param2, Object param3) {
        //return java.util.ResourceBundle.getBundle(DEFAULT_PACKAGE).getString(key);

        return getString(I18n.class, key, new Object[]{param1, param2, param3});
    }

    /**
     * Finds a localized and/or branded string in the iReport resource bundle.
     * This call never throws an exception, but a warning message is printed
     * when a requested resource is not found.
     *
     * @param clazz - the class to use to locate the bundle
     * @param key name of the resource to look for
     * @return the localizaed string or the key if no resource is found
     */
    //The method given a key return its Value saved in the Resource Boundle.
    public static String getString(Class clazz, String key) {
        //return java.util.ResourceBundle.getBundle(DEFAULT_PACKAGE).getString(key);

        String bundleName = findName(clazz);
        if (!cachedBundles.containsKey(bundleName)) {
            try {
                cachedBundles.put(bundleName, NbBundle.getBundle(clazz));
            } catch (MissingResourceException ex) {
                //System.out.println("Bundle: " + bundleName + " not found...");
            }
        }

        ResourceBundle rb = cachedBundles.get(bundleName);
        if (rb != null) {
            try {
                return rb.getString(key);
            } catch (Exception ex) {
                // Missing resource...
            }
        }

        // Locate the message somewhere else...
        for (ResourceBundle bundle : bundleLocations) {
            try {
                return bundle.getString(key);
            } catch (Exception ex) {
                // Missing resource...
            }
        }

        return key;
    }

    /**
     * Finds a localized and/or branded string in the iReport resource bundle
     * and formats the message by passing requested parameters. This call never
     * throws an exception, but a warning message is printed when a requested
     * resource is not found.
     *
     * @param clazz - the class to use to locate the bundle
     * @param key name of the resource to look for
     * @param params
     * @return
     */
    public static String getString(Class clazz, String key, Object[] params) {
        //return java.util.ResourceBundle.getBundle(DEFAULT_PACKAGE).getString(key);
        try {
            String s = getString(clazz, key);
            return java.text.MessageFormat.format(s, params);
        } catch (Exception ex) {
        }
        return key;
    }

    private static String findName(Class clazz) {
        String pref = clazz.getName();
        int last = pref.lastIndexOf('.');

        if (last >= 0) {
            pref = pref.substring(0, last + 1);

            return pref + "Bundle"; // NOI18N
        } else {
            // base package, search for bundle
            return "Bundle"; // NOI18N
        }
    }
}
