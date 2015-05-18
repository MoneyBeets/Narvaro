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
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseManager;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;

public class MonthData {

    private static final Logger LOG = Logger.getLogger(MonthData.class.getName());
    
    private static final String select449FormFile = "SELECT filename, form FROM forms WHERE id = ?";

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
    private long form449 = -1;
    
    private File form449File;
    
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
            String comment, long form449, File form449File) {
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
        this.form449File = form449File;
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
    
    /**
     * Retrieves the original 449 Form from the database if the current reference is
     * null and we have a valid formId (indicating the form is already stored in the database),
     * and stores it in the NARVARO_HOME/data directory. Else, simply returns the referenced handle.
     * 
     * @return A handle to the retrieved form.
     * @throws IOException If an IO Exception occurs.
     */
    public File getForm449File() throws IOException {
        File file = null;
        if (form449File == null) {
            // let's get the file from the database
            if (form449 < 1) {
                throw new IOException("Invalid reference");
            }
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            FileOutputStream fout = null;
            try {
                con = DatabaseManager.Narvaro.getConnection();
                ps = con.prepareStatement(select449FormFile);
                ps.setLong(1, form449);
                rs = ps.executeQuery();
                // should only be a single result
                rs.next();
                String filename = rs.getString(1);
                Blob blob = rs.getBlob(2);
                byte[] bytes = new byte[(int)blob.length()];
                String pathName = getDataDir().toPath().toString() + File.separator + filename;
                fout = new FileOutputStream(pathName);
                fout.write(bytes);
                fout.flush();
                file = new File(pathName);
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                DatabaseManager.Narvaro.closeConnection(rs, ps, con);
                try {
                    if (fout != null) {
                        fout.close();
                    }
                } catch (Exception e) {
                    LOG.warn(e.getMessage(), e);
                }
                fout = null;
            }
        } else {
            // we already have a valid handle to the form
            //   this will be the case if this MonthData object
            //   was created via data entry and the form is still on
            //   the user's hard disk.
            return form449File;
        }
        this.form449File = file;
        return file;
    }
    
    /**
     * @return A handle to the NARVARO_HOME/data directory, 
     *     creating it if it does not exist.
     */
    private File getDataDir() {
        File home = new File(ConfigurationManager.NARVARO.getHomeDirectory());
        File dataDir = new File(home, "data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        return dataDir;
    }

}
