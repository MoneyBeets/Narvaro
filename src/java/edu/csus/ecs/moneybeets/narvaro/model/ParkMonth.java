/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 * 
 */

package edu.csus.ecs.moneybeets.narvaro.model;

import java.io.File;
import java.math.BigDecimal;
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
    
    /**
     * Convenience method to create a <code>MonthData</code> and register it
     *   with this <code>ParkMonth</code>.
     *   
     * @param month
     * @param pduConversionFactor
     * @param pduTotals
     * @param pduSpecialEvents
     * @param pduAnnualDayUse
     * @param pduDayUse
     * @param pduSenior
     * @param pduDisabled
     * @param pduGoldenBear
     * @param pduDisabledVeteran
     * @param pduNonResOHVPass
     * @param pduAnnualPassSale
     * @param pduCamping
     * @param pduSeniorCamping
     * @param pduDisabledCamping
     * @param fduConversionFactor
     * @param fduTotals
     * @param fscTotalVehicles
     * @param fscTotalPeople
     * @param fscRatio
     * @param oMC
     * @param oATV
     * @param o4x4
     * @param oROV
     * @param oAQMA
     * @param oAllStarKarting
     * @param oHangtown
     * @param oOther
     * @param comment
     * @param form449
     */
    public void createAndPutMonthData(YearMonth month, BigDecimal pduConversionFactor,
            long pduTotals, long pduSpecialEvents, long pduAnnualDayUse,
            long pduDayUse, long pduSenior, long pduDisabled,
            long pduGoldenBear, long pduDisabledVeteran, long pduNonResOHVPass,
            long pduAnnualPassSale, long pduCamping, long pduSeniorCamping,
            long pduDisabledCamping, BigDecimal fduConversionFactor,
            long fduTotals, long fscTotalVehicles, long fscTotalPeople,
            BigDecimal fscRatio, long oMC, long oATV, long o4x4, long oROV,
            long oAQMA, long oAllStarKarting, long oHangtown, long oOther,
            String comment, long form449, File form449File) {
        
        putMonthData(month, new MonthData(
                                month, pduConversionFactor, pduTotals, pduSpecialEvents, 
                                pduAnnualDayUse, pduDayUse, pduSenior, pduDisabled, pduGoldenBear, 
                                pduDisabledVeteran, pduNonResOHVPass, pduAnnualPassSale, pduCamping, 
                                pduSeniorCamping, pduDisabledCamping, fduConversionFactor, fduTotals, 
                                fscTotalVehicles, fscTotalPeople, fscRatio, oMC, oATV, o4x4, oROV, 
                                oAQMA, oAllStarKarting, oHangtown, oOther, comment, form449, form449File));
    }
    
}
