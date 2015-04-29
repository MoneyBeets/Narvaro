package edu.csus.ecs.moneybeets.narvaro.model;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ParkMonth {

    private static final Logger LOG = Logger.getLogger(ParkMonth.class.getName());
    
    private final String parkName;
    
    private final Map<YearMonth, MonthData> monthDatas = new HashMap<YearMonth, MonthData>();
    
    public ParkMonth(final String parkName) {
        this.parkName = parkName;
    }
    
    public final String getParkName() {
        return parkName;
    }
    
    public MonthData getMonthData(final YearMonth yearMonth) {
        return monthDatas.get(yearMonth);
    }
    
    public Collection<MonthData> getAllMonthData() {
        return Collections.unmodifiableCollection(monthDatas.values());
    }
    
    /**
     * @param yearMonth The yearMonth used as a key.
     * @param monthData The data.
     * @return The previous value associated with key, or null if there was no mapping for key.
     */
    public MonthData putMonthData(final YearMonth yearMonth, final MonthData monthData) {
        return monthDatas.put(yearMonth, monthData);
    }
    
    /**
     * Inserts all of the pairs in the input map into the underlying map. Functionally
     * equivalent to iteration and insertion of each element.
     * 
     * @param m The map.
     */
    public void putAllMonthData(final Map<? extends YearMonth, ? extends MonthData> m) {
        monthDatas.putAll(m);
    }
    
}
