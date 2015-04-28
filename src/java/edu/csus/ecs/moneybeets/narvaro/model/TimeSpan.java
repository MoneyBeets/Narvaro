package edu.csus.ecs.moneybeets.narvaro.model;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class TimeSpan {
    
    private static final Logger LOG = Logger.getLogger(TimeSpan.class.getName());
    
    private final YearMonth start;
    private final YearMonth end;
    
    private final Map<String, ParkMonth> parkMonths = new HashMap<String, ParkMonth>();

    public TimeSpan(final YearMonth start, final YearMonth end) {
        this.start = start;
        this.end = end;
    }
    
    public YearMonth getStart() {
        return start;
    }
    
    public YearMonth getEnd() {
        return end;
    }
    
    /**
     * @param key The park name to return a <code>ParkMonth</code> from.
     * @return A <code>ParkMonth</code> represented by this key.
     */
    public final ParkMonth getParkMonth(final String key) {
        return parkMonths.get(key);
    }
    
    /**
     * @return A collection of <code>ParkMonth</code> representing this
     *     time span.
     */
    public Collection<ParkMonth> getAllParkMonths() {
        return Collections.unmodifiableCollection(parkMonths.values());
    }
    
    /**
     * @return A collection of all park names registered with the database.
     */
    public Collection<String> getAllParkNames() {
        return Collections.unmodifiableCollection(parkMonths.keySet());
    }
    
}
