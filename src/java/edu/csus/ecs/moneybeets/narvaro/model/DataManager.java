/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 * 
 */

package edu.csus.ecs.moneybeets.narvaro.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseManager;

/**
 * This class manages creation and storage of Narvaro data objects.
 *
 */
public enum DataManager {

    Narvaro,
    
    ;
    
    private DataManager() {
    }
    
    private static final Logger LOG = Logger.getLogger(DataManager.class.getName());
    
    private Connection con;
    private ResultSet rs = null;
    
    /*******************************************************************************************
     * Selection queries
     */
    
    /**
     * Gets database id of a park by name.
     * 
     * Input:
     *     1) parkName
     * 
     * Output:
     *     1) id
     */
    private static final String selectParkIdByName = "SELECT id FROM parks WHERE parks.name = ?";
    
    /**
     * Gets the parkName of a park by id.
     * 
     * Input:
     *     1) id
     * 
     * Output:
     *     1) parkName
     */
    private static final String selectParkNameById = "SELECT name FROM parks WHERE parks.id = ?";
    
    /**
     * Gets all park names.
     * 
     * Input:
     *     none
     * 
     * Output:
     *     1) parkName
     */
    private static final String selectAllParkNames = "SELECT name FROM parks";
    
    /**
     * Gets all park id's and names.
     * 
     * Input:
     *     none
     * 
     * Output:
     *     1) id
     *     2) parkName
     */
    private static final String selectAllParkNamesAndIds = "SELECT id, name FROM parks";
    
    /**
     * Gets all park id's and names that have data within the specified date range.
     * 
     * Input:
     *     1) starting date
     *     2) ending date
     * 
     * Output:
     *     1) id
     *     2) parkName
     */
    private static final String selectAllParkNamesAndIdsExistInRange = "SELECT DISTINCT parks.id, parks.name FROM parks "
            + "JOIN data ON parks.id = data.park WHERE data.month >= ? AND data.month <= ?";
    
    /**
     * Gets all park data for the specified date range.
     * 
     * Input:
     *     1) starting date
     *     2) ending date
     * 
     * Output:
     *     1) parkName
     *     2) month
     *     3) pduConversionFactor
     *     4) pduTotals
     *     5) pduSpecialEvents
     *     6) pduAnnualDayUse
     *     7) pduDayUse
     *     8) pduSenior
     *     9) pduDisabled
     *     10) pduGoldenBear
     *     11) pduDisabledVeteran
     *     12) pduNonResOHVPass
     *     13) pduAnnualPassSale
     *     14) pduCamping
     *     15) pduSeniorCamping
     *     16) pduDisabledCamping
     *     17) fduConversionFactor
     *     18) fduTotals
     *     19) fscTotalVehicles
     *     20) fscTotalPeople
     *     21) fscRatio
     *     22) oMC
     *     23) oATV
     *     24) o4X4
     *     25) oROV
     *     26) oAQMA
     *     27) oAllStarKarting
     *     28) oHangTown
     *     29) oOther
     *     30) comment
     *     31) formId
     */
    private static final String selectAllMonthDataByRange = "SELECT parks.name, data.month, data.pduConversionFactor, "
            + "data.pduTotals, data.pduSpecialEvents, data.pduAnnualDayUse, data.pduDayUse, data.pduSenior, "
            + "data.pduDisabled, data.pduGoldenBear, data.pduDisabledVeteran, data.pduNonResOHVPass, "
            + "data.pduAnnualPassSale, data.pduCamping, data.pduSeniorCamping, data.pduDisabledCamping, "
            + "data.fduConversionFactor, data.fduTotals, data.fscTotalVehicles, data.fscTotalPeople, "
            + "data.fscRatio, data.oMC, data.oATV, data.o4X4, data.oROV, data.oAQMA, data.oAllStarKarting, "
            + "data.oHangtown, data.oOther, data.comment, forms.id "
            + "FROM data JOIN parks ON data.park = parks.id "
            + "JOIN forms ON data.form449 = forms.id "
            + "WHERE data.month >= ? AND data.month <= ?";
    
    /**
     * Gets all park data for the specified park by name.
     * 
     * Input:
     *     1) parkName
     * 
     * Output:
     *     1) parkName
     *     2) month
     *     3) pduConversionFactor
     *     4) pduTotals
     *     5) pduSpecialEvents
     *     6) pduAnnualDayUse
     *     7) pduDayUse
     *     8) pduSenior
     *     9) pduDisabled
     *     10) pduGoldenBear
     *     11) pduDisabledVeteran
     *     12) pduNonResOHVPass
     *     13) pduAnnualPassSale
     *     14) pduCamping
     *     15) pduSeniorCamping
     *     16) pduDisabledCamping
     *     17) fduConversionFactor
     *     18) fduTotals
     *     19) fscTotalVehicles
     *     20) fscTotalPeople
     *     21) fscRatio
     *     22) oMC
     *     23) oATV
     *     24) o4X4
     *     25) oROV
     *     26) oAQMA
     *     27) oAllStarKarting
     *     28) oHangTown
     *     29) oOther
     *     30) comment
     *     31) formId
     */
    private static final String selectMonthDataByPark = "SELECT parks.name, data.month, data.pduConversionFactor, "
            + "data.pduTotals, data.pduSpecialEvents, data.pduAnnualDayUse, data.pduDayUse, data.pduSenior, "
            + "data.pduDisabled, data.pduGoldenBear, data.pduDisabledVeteran, data.pduNonResOHVPass, "
            + "data.pduAnnualPassSale, data.pduCamping, data.pduSeniorCamping, data.pduDisabledCamping, "
            + "data.fduConversionFactor, data.fduTotals, data.fscTotalVehicles, data.fscTotalPeople, "
            + "data.fscRatio, data.oMC, data.oATV, data.o4X4, data.oROV, data.oAQMA, data.oAllStarKarting, "
            + "data.oHangtown, data.oOther, data.comment, forms.id "
            + "FROM data JOIN parks ON data.park = parks.id "
            + "JOIN forms ON data.form449 = forms.id "
            + "WHERE parks.name = ?";
    
    /*******************************************************************************************
     * Insertion queries
     */
    
    /**
     * Inserts a new park by name into the database.
     * 
     * Input:
     *     1) parkName
     * 
     * Output:
     *     none
     */
    private static final String insertPark = "INSERT INTO parks (name) VALUES(?)";
    
    /**
     * Inserts a new 449 Form into the database.
     * 
     * Input:
     *     1) filename
     *     2) InputStream of file
     * 
     * Output:
     *     none
     */
    private static final String insertForm = "INSERT INTO forms (filename, form) VALUES(?, ?)";
    
    /**
     * Inserts park month data into database.
     * (Must be run after inserting 449 Form to have valid formId)
     * 
     * Input:
     *     1) parkId
     *     2) month
     *     3) pduConversionFactor
     *     4) pduTotals
     *     5) pduSpecialEvents
     *     6) pduAnnualDayUse
     *     7) pduDayUse
     *     8) pduSenior
     *     9) pduDisabled
     *     10) pduGoldenBear
     *     11) pduDisabledVeteran
     *     12) pduNonResOHVPass
     *     13) pduAnnualPassSale
     *     14) pduCamping
     *     15) pduSeniorCamping
     *     16) pduDisabledCamping
     *     17) fduConversionFactor
     *     18) fduTotals
     *     19) fscTotalVehicles
     *     20) fscTotalPeople
     *     21) fscRatio
     *     22) oMC
     *     23) oATV
     *     24) o4X4
     *     25) oROV
     *     26) oAQMA
     *     27) oAllStarKarting
     *     28) oHangTown
     *     29) oOther
     *     30) comment
     *     31) formId (automagically gotten from last insert)
     * 
     * Output:
     *     none
     */
    private static final String insertMonthData = "INSERT INTO data (park, month, pduConversionFactor, pduTotals, "
            + "pduSpecialEvents, pduAnnualDayUse, pduDayUse, pduSenior, pduDisabled, pduGoldenBear, pduDisabledVeteran, "
            + "pduNonResOHVPass, pduAnnualPassSale, pduCamping, pduSeniorCamping, pduDisabledCamping, fduConversionFactor, "
            + "fduTotals, fscTotalVehicles, fscTotalPeople, fscRatio, oMC, oATV, o4X4, oROV, oAQMA, oAllStarKarting, oHangtown, "
            + "oOther, comment, form449) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, LAST_INSERT_ID())";
    
    /**
     * Setup all prepared statements for later use
     */
    
    // select statements
    private PreparedStatement psSelectParkIdByName = null;
    private PreparedStatement psSelectParkNameById = null;
    private PreparedStatement psSelectAllParkNames = null;
    private PreparedStatement psSelectAllParkNamesAndIds = null;
    private PreparedStatement psSelectAllParkNamesAndIdsExistInRange = null;
    private PreparedStatement psSelectAllMonthDataByRange = null;
    private PreparedStatement psSelectMonthDataByPark = null;
    // insert statements
    private PreparedStatement psInsertPark = null;
    private PreparedStatement psInsertForm = null;
    private PreparedStatement psInsertMonthData = null;
    
    /**
     * Starts the Data Manager by first obtaining a database connection
     *   and then registering all prepared statements.
     */
    public void start() {
        try {
            // get a database connection
            con = DatabaseManager.Narvaro.getConnection();
            // register all prepared statements
            psSelectParkIdByName = con.prepareStatement(selectParkIdByName);
            psSelectParkNameById = con.prepareStatement(selectParkNameById);
            psSelectAllParkNames = con.prepareStatement(selectAllParkNames);
            psSelectAllParkNamesAndIds = con.prepareStatement(selectAllParkNamesAndIds);
            psSelectAllParkNamesAndIdsExistInRange = con.prepareStatement(selectAllParkNamesAndIdsExistInRange);
            psSelectAllMonthDataByRange = con.prepareStatement(selectAllMonthDataByRange);
            psSelectMonthDataByPark = con.prepareStatement(selectMonthDataByPark);
            psInsertPark = con.prepareStatement(insertPark);
            psInsertForm = con.prepareStatement(insertForm);
            psInsertMonthData = con.prepareStatement(insertMonthData);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    /**
     * Shuts down the Data manager by closing all prepared statements,
     *   result sets, and connections.
     */
    public void shutdown() {
        DatabaseManager.Narvaro.closeStatement(psSelectParkIdByName);
        DatabaseManager.Narvaro.closeStatement(psSelectParkNameById);
        DatabaseManager.Narvaro.closeStatement(psSelectAllParkNames);
        DatabaseManager.Narvaro.closeStatement(psSelectAllParkNamesAndIds);
        DatabaseManager.Narvaro.closeStatement(psSelectAllParkNamesAndIdsExistInRange);
        DatabaseManager.Narvaro.closeStatement(psSelectAllMonthDataByRange);
        DatabaseManager.Narvaro.closeStatement(psSelectMonthDataByPark);
        DatabaseManager.Narvaro.closeStatement(psInsertPark);
        DatabaseManager.Narvaro.closeStatement(psInsertForm);
        DatabaseManager.Narvaro.closeStatement(psInsertMonthData);
        DatabaseManager.Narvaro.closeResultSet(rs);
        DatabaseManager.Narvaro.closeConnection(con);
    }
    
    public final TimeSpan getTimeSpan(final YearMonth start, final YearMonth end) throws SQLException {
        
        // create base timespan
        TimeSpan timeSpan = new TimeSpan(start, end);
        
        // discover valid park names that have data in the range
        //   and create base ParkMonths
        try {
            psSelectAllParkNamesAndIdsExistInRange.setDate(1, DataManager.yearMonthToDate(start));
            psSelectAllParkNamesAndIdsExistInRange.setDate(2, DataManager.yearMonthToDate(end));
            
            rs = psSelectAllParkNamesAndIdsExistInRange.executeQuery();
            
            if (rs != null) {
                while (rs.next()) {
                    
                    // parkname is the second column in this result set
                    String parkName = rs.getString(2);
                    timeSpan.putParkMonth(parkName, new ParkMonth(parkName));
                    
                }
            }
        } catch (SQLException e) {
            // log and then throw exception
            LOG.error(e.getMessage(), e);
            throw new SQLException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            }
            rs = null;
        }
        
        // fetch data from range and populate MonthDatas
        try {
            psSelectAllMonthDataByRange.setDate(1, DataManager.yearMonthToDate(start));
            psSelectAllMonthDataByRange.setDate(2, DataManager.yearMonthToDate(end));
            
            rs = psSelectAllMonthDataByRange.executeQuery();
            
            if (rs != null) {
                while (rs.next()) {
                    
                    // create a base month data
                    MonthData md = new MonthData(dateToYearMonth(rs.getDate(2)), rs.getBigDecimal(3), 
                            rs.getLong(4), rs.getLong(5), rs.getLong(6), rs.getLong(7), rs.getLong(8), 
                            rs.getLong(9), rs.getLong(10), rs.getLong(11), rs.getLong(12), rs.getLong(13), 
                            rs.getLong(14), rs.getLong(15), rs.getLong(16), rs.getBigDecimal(17), rs.getLong(18), 
                            rs.getLong(19), rs.getLong(20), rs.getBigDecimal(21), rs.getLong(22), rs.getLong(23), 
                            rs.getLong(24), rs.getLong(25), rs.getLong(26), rs.getLong(27), rs.getLong(28), 
                            rs.getLong(29), rs.getString(30), rs.getLong(31), null);
                    
                    // store it in the timepsan for the proper park
                    timeSpan.getParkMonth(rs.getString(1)).putMonthData(dateToYearMonth(rs.getDate(2)), md);
                }
            }
            
        } catch (SQLException e) {
            // log and then throw exception
            LOG.error(e.getMessage(), e);
            throw new SQLException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            }
            rs = null;
        }
        return timeSpan;
    }
    
    // TODO implement these convenience methods
    //public final TimeSpan getTimeSpanForPark(
    //        final YearMonth start, final YearMonth end, final String parkName) {
    //    
    //}
    
    //public final TimeSpan getTimeSpanForParks(
    //        final YearMonth start, final YearMonth end, final String ... parkNames) {
    //    
    //}
    
    /**
     * Stores a <code>ParkMonth</code> into the database.
     * First writes a MonthData's 449 Form then writes the MonthData.
     * 
     * @param pm The ParkMonth
     * @throws SQLException If an SQL Exception occurs.
     */
    public void storeParkMonth(final ParkMonth pm) throws SQLException {
        int parkId = -1;
        if (pm != null) {
            try {
                // get the parkId from the database
                psSelectParkIdByName.setString(1, pm.getParkName());
                rs = psSelectParkIdByName.executeQuery();
                // should only have a single result
                rs.next();
                parkId = rs.getInt(1);
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
                throw new SQLException(e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    LOG.warn(e.getMessage(), e);
                }
                rs = null;
            }
            if (parkId > 0) {
                for (MonthData md : pm.getAllMonthData()) {
                    // insert 449 Form into database
                    InputStream in = null;
                    try {
                        in = new FileInputStream(md.getForm449File().toPath().toFile());
                        psInsertForm.setString(1, md.getForm449File().getName());
                        psInsertForm.setBlob(2, in);
                        psInsertForm.execute();
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (Exception ex) {
                            LOG.warn(ex.getMessage(), ex);
                        }
                        in = null;
                    }
                    // insert monthdata
                    psInsertMonthData.setLong(1, parkId);
                    psInsertMonthData.setDate(2, yearMonthToDate(md.getMonth()));
                    psInsertMonthData.setBigDecimal(3, md.getPduConversionFactor());
                    psInsertMonthData.setLong(4, md.getPduTotals());
                    psInsertMonthData.setLong(5, md.getPduSpecialEvents());
                    psInsertMonthData.setLong(6, md.getPduAnnualDayUse());
                    psInsertMonthData.setLong(7, md.getPduDayUse());
                    psInsertMonthData.setLong(8, md.getPduSenior());
                    psInsertMonthData.setLong(9, md.getPduDisabled());
                    psInsertMonthData.setLong(10, md.getPduGoldenBear());
                    psInsertMonthData.setLong(11, md.getPduDisabledVeteran());
                    psInsertMonthData.setLong(12, md.getPduNonResOHVPass());
                    psInsertMonthData.setLong(13, md.getPduAnnualPassSale());
                    psInsertMonthData.setLong(14, md.getPduCamping());
                    psInsertMonthData.setLong(15, md.getPduSeniorCamping());
                    psInsertMonthData.setLong(16, md.getPduDisabledCamping());
                    psInsertMonthData.setBigDecimal(17, md.getFduConversionFactor());
                    psInsertMonthData.setLong(18, md.getFduTotals());
                    psInsertMonthData.setLong(19, md.getFscTotalVehicles());
                    psInsertMonthData.setLong(20, md.getFscTotalPeople());
                    psInsertMonthData.setBigDecimal(21, md.getFscRatio());
                    psInsertMonthData.setLong(22, md.getoMC());
                    psInsertMonthData.setLong(23, md.getoATV());
                    psInsertMonthData.setLong(24, md.getO4X4());
                    psInsertMonthData.setLong(25, md.getoROV());
                    psInsertMonthData.setLong(26, md.getoAQMA());
                    psInsertMonthData.setLong(27, md.getoAllStarKarting());
                    psInsertMonthData.setLong(28, md.getoHangtown());
                    psInsertMonthData.setLong(29, md.getoOther());
                    psInsertMonthData.setString(30, md.getComment());
                    
                    psInsertMonthData.execute();
                }
            } else {
                throw new SQLException("Park Id not found for park: " + pm.getParkName());
            }
        }
    }
    
    public void storeAllParkMonth(final ParkMonth ... pms) throws SQLException {
        // cheating for now, can probably make this less hacky
        for (ParkMonth pm : pms) {
            storeParkMonth(pm);
        }
    }
    
    public void storeAllParkMonth(final Collection<ParkMonth> pms) throws SQLException {
        // cheating for now, can probably make this less hacky
        for (ParkMonth pm : pms) {
            storeParkMonth(pm);
        }
    }
    
    /**
     * @return A <code>List</code> of park name strings.
     */
    public List<String> getAllParkNames() {
        List<String> parkNames = new ArrayList<String>();
        try {
            rs = psSelectAllParkNames.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    parkNames.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                LOG.warn(e.getMessage(), e);
            }
            rs = null;
        }
        return parkNames;
    }
    
    /**
     * Utility method to convert a <code>YearMonth</code> into a <code>java.sql.Date</code>.
     * 
     * @param ym The <code>YearMonth</code> to convert.
     * @return The converted date.
     */
    public static Date yearMonthToDate(final YearMonth ym) {
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
    
    /**
     * Converts a <code>java.sql.Date</code> into a <code>YearMonth</code>.
     * 
     * @param date the <code>java.sql.Date</code> to convert.
     * @return The converted <code>YearMonth</code>.
     */
    public static YearMonth dateToYearMonth(final Date date) {
        return YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
    
    
}
