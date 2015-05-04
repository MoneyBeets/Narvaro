package edu.csus.ecs.moneybeets.narvaro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.YearMonth;

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
        psSelectMonthData.setDate(1, start);
        psSelectMonthData.setDate(2, end.);
    }
    
    public final TimeSpan getTimeSpanForPark(
            final YearMonth start, final YearMonth end, final String parkName) {
        setupConnection();
        
    }
    
    public final TimeSpan getTimeSpanForParks(
            final YearMonth start, final YearMonth end, final String ... parkNames) {
        setupConnection();
        
    }
    
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
    
    
}
