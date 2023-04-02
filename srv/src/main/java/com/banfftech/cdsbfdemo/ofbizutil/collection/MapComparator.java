package com.banfftech.cdsbfdemo.ofbizutil.collection;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banfftech.cdsbfdemo.ofbizutil.UtilGenerics;


/**
 * MapComparator.java
 *
 */
public class MapComparator implements Comparator<Map<Object, Object>> {

    public static final String module = MapComparator.class.getName();
    public static Logger logger = LoggerFactory.getLogger(MapComparator.class);

    private List<? extends Object> keys;

    /**
     * Method MapComparator.
     * @param keys List of Map keys to sort on
     */
    public MapComparator(List<? extends Object> keys) {
        this.keys = keys;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj==null) {
            return false;
        }
        return obj.equals(this);
    }

    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Map<Object, Object> map1, Map<Object, Object> map2) {

        if (keys == null || keys.size() < 1) {
            throw new IllegalArgumentException("No sort fields defined");
        }

        for (Object key: keys) {
            // if false will be descending, ie reverse order
            boolean ascending = true;

            Object o1 = null;
            Object o2 = null;

            // if (key instanceof FlexibleMapAccessor<?>) {
            //     FlexibleMapAccessor<Object> fmaKey = UtilGenerics.cast(key);
            //     ascending = fmaKey.getIsAscending();

            //     o1 = fmaKey.get(UtilGenerics.<String, Object>checkMap(map1));
            //     o2 = fmaKey.get(UtilGenerics.<String, Object>checkMap(map2));
            // } else {
            //     if (key instanceof String) {
            //         String keyStr = (String) key;
            //         if (keyStr.charAt(0) == '-') {
            //             ascending = false;
            //             key = keyStr.substring(1);
            //         } else if (keyStr.charAt(0) == '+') {
            //             ascending = true;
            //             key = keyStr.substring(1);
            //         }
            //     }

            //     o1 = map1.get(key);
            //     o2 = map2.get(key);
            // }
            // no checking if key is instance of FlexibleMapAccessor
            if (key instanceof String) {
                String keyStr = (String) key;
                if (keyStr.charAt(0) == '-') {
                    ascending = false;
                    key = keyStr.substring(1);
                } else if (keyStr.charAt(0) == '+') {
                    ascending = true;
                    key = keyStr.substring(1);
                }
            }
            // end no checking if key is instance of FlexibleMapAccessor

            o1 = map1.get(key);
            o2 = map2.get(key);

            if (o1 == null && o2 == null) {
                continue;
            }

            int compareResult = 0;
            if (o1 != null && o2 == null) {
                compareResult = -1;
            }
            if (o1 == null && o2 != null) {
                compareResult = 1;
            }

            if (compareResult == 0) {
                try {
                    // the map values in question MUST implement the Comparable interface, if not we'll throw an exception
                    Comparable<Object> comp1 = UtilGenerics.cast(o1);
                    compareResult = comp1.compareTo(o2);
                } catch (Exception e) {
                    String errorMessage = "Error sorting list of Maps: " + e.toString();
                    logger.error(e.getMessage(), e);
                    
                    throw new RuntimeException(errorMessage);
                }
            }

            // if zero they are equal, so we carry on to the next key to try and find a difference
            if (compareResult != 0) {
                if (ascending) {
                    return compareResult;
                }
                return -compareResult;
            }
        }

        // none of them were different, so they are equal
        return 0;
    }
}
