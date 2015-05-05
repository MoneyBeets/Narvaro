/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 * 
 */

package edu.csus.ecs.moneybeets.narvaro.model;

import java.math.BigDecimal;
import java.time.YearMonth;

import org.apache.log4j.Logger;

public class MonthData {

    private static final Logger LOG = Logger.getLogger(MonthData.class.getName());

    private final YearMonth month;
    private final BigDecimal pduConversionFactor;
    private final long pduTotals;
    private final long pduSpecialEvents;
    private final long pduAnnualDayUse;
    private final long pduDayUse;
    private final long pduSenior;
    private final long pduDisabled;
    private final long pduGoldenBear;
    private final long pduDisabledVeteran;
    private final long pduNonResOHVPass;
    private final long pduAnnualPassSale;
    private final long pduCamping;
    private final long pduSeniorCamping;
    private final long pduDisabledCamping;
    private final BigDecimal fduConversionFactor;
    private final long fduTotals;
    private final long fscTotalVehicles;
    private final long fscTotalPeople;
    private final BigDecimal fscRatio;
    private final long oMC;
    private final long oATV;
    private final long o4X4;
    private final long oROV;
    private final long oAQMA;
    private final long oAllStarKarting;
    private final long oHangtown;
    private final long oOther;
    private final String comment;
    private final long form449;
    
    /**
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
    public MonthData(YearMonth month, BigDecimal pduConversionFactor,
            long pduTotals, long pduSpecialEvents, long pduAnnualDayUse,
            long pduDayUse, long pduSenior, long pduDisabled,
            long pduGoldenBear, long pduDisabledVeteran, long pduNonResOHVPass,
            long pduAnnualPassSale, long pduCamping, long pduSeniorCamping,
            long pduDisabledCamping, BigDecimal fduConversionFactor,
            long fduTotals, long fscTotalVehicles, long fscTotalPeople,
            BigDecimal fscRatio, long oMC, long oATV, long o4x4, long oROV,
            long oAQMA, long oAllStarKarting, long oHangtown, long oOther,
            String comment, long form449) {
        this.month = month;
        this.pduConversionFactor = pduConversionFactor;
        this.pduTotals = pduTotals;
        this.pduSpecialEvents = pduSpecialEvents;
        this.pduAnnualDayUse = pduAnnualDayUse;
        this.pduDayUse = pduDayUse;
        this.pduSenior = pduSenior;
        this.pduDisabled = pduDisabled;
        this.pduGoldenBear = pduGoldenBear;
        this.pduDisabledVeteran = pduDisabledVeteran;
        this.pduNonResOHVPass = pduNonResOHVPass;
        this.pduAnnualPassSale = pduAnnualPassSale;
        this.pduCamping = pduCamping;
        this.pduSeniorCamping = pduSeniorCamping;
        this.pduDisabledCamping = pduDisabledCamping;
        this.fduConversionFactor = fduConversionFactor;
        this.fduTotals = fduTotals;
        this.fscTotalVehicles = fscTotalVehicles;
        this.fscTotalPeople = fscTotalPeople;
        this.fscRatio = fscRatio;
        this.oMC = oMC;
        this.oATV = oATV;
        this.o4X4 = o4x4;
        this.oROV = oROV;
        this.oAQMA = oAQMA;
        this.oAllStarKarting = oAllStarKarting;
        this.oHangtown = oHangtown;
        this.oOther = oOther;
        this.comment = comment;
        this.form449 = form449;
    }

    /**
     * @return the month
     */
    public YearMonth getMonth() {
        return month;
    }

    /**
     * @return the pduConversionFactor
     */
    public BigDecimal getPduConversionFactor() {
        return pduConversionFactor;
    }

    /**
     * @return the pduTotals
     */
    public long getPduTotals() {
        return pduTotals;
    }

    /**
     * @return the pduSpecialEvents
     */
    public long getPduSpecialEvents() {
        return pduSpecialEvents;
    }

    /**
     * @return the pduAnnualDayUse
     */
    public long getPduAnnualDayUse() {
        return pduAnnualDayUse;
    }

    /**
     * @return the pduDayUse
     */
    public long getPduDayUse() {
        return pduDayUse;
    }

    /**
     * @return the pduSenior
     */
    public long getPduSenior() {
        return pduSenior;
    }

    /**
     * @return the pduDisabled
     */
    public long getPduDisabled() {
        return pduDisabled;
    }

    /**
     * @return the pduGoldenBear
     */
    public long getPduGoldenBear() {
        return pduGoldenBear;
    }

    /**
     * @return the pduDisabledVeteran
     */
    public long getPduDisabledVeteran() {
        return pduDisabledVeteran;
    }

    /**
     * @return the pduNonResOHVPass
     */
    public long getPduNonResOHVPass() {
        return pduNonResOHVPass;
    }

    /**
     * @return the pduAnnualPassSale
     */
    public long getPduAnnualPassSale() {
        return pduAnnualPassSale;
    }

    /**
     * @return the pduCamping
     */
    public long getPduCamping() {
        return pduCamping;
    }

    /**
     * @return the pduSeniorCamping
     */
    public long getPduSeniorCamping() {
        return pduSeniorCamping;
    }

    /**
     * @return the pduDisabledCamping
     */
    public long getPduDisabledCamping() {
        return pduDisabledCamping;
    }

    /**
     * @return the fduConversionFactor
     */
    public BigDecimal getFduConversionFactor() {
        return fduConversionFactor;
    }

    /**
     * @return the fduTotals
     */
    public long getFduTotals() {
        return fduTotals;
    }

    /**
     * @return the fscTotalVehicles
     */
    public long getFscTotalVehicles() {
        return fscTotalVehicles;
    }

    /**
     * @return the fscTotalPeople
     */
    public long getFscTotalPeople() {
        return fscTotalPeople;
    }

    /**
     * @return the fscRatio
     */
    public BigDecimal getFscRatio() {
        return fscRatio;
    }

    /**
     * @return the oMC
     */
    public long getoMC() {
        return oMC;
    }

    /**
     * @return the oATV
     */
    public long getoATV() {
        return oATV;
    }

    /**
     * @return the o4X4
     */
    public long getO4X4() {
        return o4X4;
    }

    /**
     * @return the oROV
     */
    public long getoROV() {
        return oROV;
    }

    /**
     * @return the oAQMA
     */
    public long getoAQMA() {
        return oAQMA;
    }

    /**
     * @return the oAllStarKarting
     */
    public long getoAllStarKarting() {
        return oAllStarKarting;
    }

    /**
     * @return the oHangtown
     */
    public long getoHangtown() {
        return oHangtown;
    }

    /**
     * @return the oOther
     */
    public long getoOther() {
        return oOther;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return the form449
     */
    public long getForm449() {
        return form449;
    }

}
