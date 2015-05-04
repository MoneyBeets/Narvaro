package edu.csus.ecs.moneybeets.narvaro.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Calendar;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseManager;

public enum DataManager {

    Narvaro,
    
    ;
    
    private DataManager() {
        
    }
    
    private static final Logger LOG = Logger.getLogger(DataManager.class.getName());
    
    private Connection con;
    
    private static final String insertMonthData = "";
    private static final String selectMonthData = "SELECT parks.name, data.month, data.pduConversionFactor, "
            + "data.pduTotals, data.pduSpecialEvents, data.pduAnnualDayUse, data.pduDayUse, data.pduSenior, "
            + "data.pduDisabled, data.pduGoldenBear, data.pduDisabledVeteran, data.pduNonResOHVPass, "
            + "data.pduAnnualPassSale, data.pduCamping, data.pduSeniorCamping, data.pduDisabledCamping, "
            + "data.fduConversionFactor, data.fduTotals, data.fscTotalVehicles, data.fscTotalPeople, "
            + "data.fscRatio, data.oMC, data.oATV, data.o4X4, data.oROV, data.oAQMA, data.oAllStarKarting, "
            + "data.oHangtown, data.oOther, data.comment, forms.id "
            + "FROM data ((JOIN parks ON data.park = parks.id) "
            + "JOIN forms ON data.form449 = forms.id) "
            + "WHERE data.month >= ? AND data.month <= ?";
    
    private PreparedStatement psInsertMonthData = null;
    private PreparedStatement psSelectMonthData = null;
    
    public final TimeSpan getTimeSpan(final YearMonth start, final YearMonth end) {
        setupConnection();
        ResultSet rs = null;
        try {
            psSelectMonthData.setDate(1, DataManager.convertYearMonthToDate(start));
            psSelectMonthData.setDate(2, DataManager.convertYearMonthToDate(end));
            rs = psSelectMonthData.executeQuery();
            
            while (rs.next()) {
                
            }
            
        } catch (SQLException e) {
            
        }
    }
    
    //public final TimeSpan getTimeSpanForPark(
    //        final YearMonth start, final YearMonth end, final String parkName) {
    //    setupConnection();
    //    
    //}
    
    //public final TimeSpan getTimeSpanForParks(
    //        final YearMonth start, final YearMonth end, final String ... parkNames) {
    //    setupConnection();
    //    
    //}
    
    private void setupConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = DatabaseManager.Narvaro.getConnection();
            }
            if (psInsertMonthData == null) {
                psInsertMonthData = con.prepareStatement(insertMonthData);
            }
            if (psSelectMonthData == null) {
                psSelectMonthData = con.prepareStatement(selectMonthData);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }

    }
    
    /**
     * Utility method to convert a <code>YearMonth</code> into a <code>Date</code>.
     * 
     * @param ym The <code>YearMonth</code> to convert.
     * @return The converted date.
     */
    public static Date convertYearMonthToDate(final YearMonth ym) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        // Gregorian calendar has months 0 - 11, 0 being January.
        //   java.time is more sane and has months 1 - 12, 1 being January.
        //   So we must convert them here...
        cal.set(Calendar.MONTH, (ym.getMonthValue() - 1));
        // Since we cannot have a date object without a day, we must pick one
        //    or the system will use today, whatever day of the month that happens to be.
        //  So to avoid situations where it's the 30th day of the month, but the user is
        //    trying to work with February (29 days), we should pick a sane default... the 1st.
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, ym.getYear());
        return (Date) cal.getTime();
    }
    
    
}
