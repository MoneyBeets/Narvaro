/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.ui;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import edu.csus.ecs.moneybeets.narvaro.model.DataManager;
import edu.csus.ecs.moneybeets.narvaro.model.ParkMonth;
import edu.csus.ecs.moneybeets.narvaro.util.TaskEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.apache.log4j.Logger;

public class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class.getName());

    /* Enter Data Tab Start */
    @FXML
    private Tab enterDataTab;
    @FXML
    private ComboBox<String> selectAParkDropDownMenu;
    @FXML
    private ComboBox<Integer> enterYear;
    @FXML
    private ComboBox<Month> enterMonth;
    @FXML
    private TextField conversionFactorPaidDayUseTF;
    @FXML
    private TextField paidDayUseTotalsTF;
    @FXML
    private TextField specialEventsTF;
    @FXML
    private TextField annualDayUseTF;
    @FXML
    private TextField dayUseTF;
    @FXML
    private TextField seniorTF;
    @FXML
    private TextField disabledTF;
    @FXML
    private TextField goldenBearTF;
    @FXML
    private TextField disabledVeteranTF;
    @FXML
    private TextField nonResOHVPassTF;
    @FXML
    private TextField annualPassSaleTF;
    @FXML
    private TextField campingTF;
    @FXML
    private TextField seniorCampingTF;
    @FXML
    private TextField disabledCampingTF;
    @FXML
    private TextField conversionFactorFreeDayUseTF;
    @FXML
    private TextField freeDayUseTotalsTF;
    @FXML
    private TextField totalVehiclesTF;
    @FXML
    private TextField totalPeopleTF;
    @FXML
    private TextField ratioTF;
    @FXML
    private TextArea commentsTB;
    @FXML
    private TextField mcTF;
    @FXML
    private TextField atvTF;
    @FXML
    private TextField fourByFourTF;
    @FXML
    private TextField rovTF;
    @FXML
    private TextField aqmaTF;
    @FXML
    private TextField allStarKartingTF;
    @FXML
    private TextField hangtownTF;
    @FXML
    private TextField otherTF;
    @FXML
    private Group userDataGroup;
    @FXML
    private Button clearButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button browseFileButton;
    @FXML
    private TextField browseFileTF;
    /* Enter Data Tab End */

    /* View Data Tab Start */
    @FXML
    private Tab viewDataTab;
    @FXML
    private ComboBox<?> monthSelectionOne;
    @FXML
    private ComboBox<?> yearSelectionOne;
    @FXML
    private ComboBox<?> monthSelectionTwo;
    @FXML
    private ComboBox<?> yearSelectionTwo;
    @FXML
    private ListView<?> parkView;
    @FXML
    private Button addParkButton;
    @FXML
    private Button removeParkButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane viewDataPane;
    /* View Data Tab End */

    /* Graph Data Tab Start */
    @FXML
    private Tab graphDataTab;
    @FXML
    private DatePicker selectDateX;
    @FXML
    private DatePicker selectDateY;
    @FXML
    private ToggleGroup graphType;
    @FXML
    private Button viewDataButton;
    @FXML
    private Button view449FormButton;
    @FXML
    private Button graphButton;
    @FXML
    private Button printButton;
    @FXML
    private LineChart<?, ?> graphArea;
    @FXML
    private MenuButton selectParkOne;
    @FXML
    private MenuButton selectParkTwo;
    @FXML
    private MenuButton selectCategory;
    /* Graph Data Tab End */
    
    @FXML
    public void initialize() {
        
        // get a list of all park names in the db
        List<String> parkNames = DataManager.Narvaro.getAllParkNames();
        // clear old items
        selectAParkDropDownMenu.getItems().clear();
        // add park names to window
        for (String parkName : parkNames) {
            selectAParkDropDownMenu.getItems().add(parkName);
        }

        // populate year field on enter data tab
        LocalDateTime ldt = LocalDateTime.now();
        int year = ldt.getYear();
        for (; year >= 1984; year--) {
            enterYear.getItems().add(year);
        }
        // populate month field on enter data tab
        enterMonth.getItems().addAll(Arrays.asList(Month.values()));
    }

    @FXML
    public void handleSubmitButton(final ActionEvent event) {
        ParkMonth parkMonth = new ParkMonth(getEnterPark());
        parkMonth.createAndPutMonthData(YearMonth.of(getEnterYear(), getEnterMonth()),
                getConversionFactorPaidDayUseTF(), getPaidDayUseTotalsTF(), getSpecialEventsTF(), getAnnualDayUseTF(),
                getDayUseTF(), getSeniorTF(), getDisabledTF(), getGoldenBearTF(), getDisabledVeteranTF(),
                getNonResOHVPassTF(), getAnnualPassSaleTF(), getCampingTF(), getSeniorCampingTF(),
                getDisabledCampingTF(), getConversionFactorFreeDayUseTF(), getFreeDayUseTotalsTF(),
                getTotalVehiclesTF(), getTotalPeopleTF(), getRatioTF(), getMcTF(), getAtvTF(), getAllStarKartingTF(),
                getRovTF(), getAqmaTF(), getAllStarKartingTF(), getHangtownTF(), getOtherTF(), getCommentsTB(),
                -1, getbrowseFile());
        
        // attempt to write into database
        boolean success = false;
        try {
            DataManager.Narvaro.storeParkMonth(parkMonth);
            success = true;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        if (success) {
            // clear all fields to signify OK
            handleClearButton(null);
        }
    }
    
    @FXML
    public void handleBrowseButton(final ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open 449 Form");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Excel Files", "*.xls,", "*.xlsx", "*.csv"));
        File file = fileChooser.showOpenDialog(browseFileButton.getScene().getWindow());

        if (file != null){
            String filePath = file.getPath();
            setbrowseFileTF(filePath);
        }
    }
    
    /**
     * Clears all data on-screen in text fields,
     * text-area's, and date-picker's.
     *
     * @param event The action event.
     */
    @FXML
    public void handleClearButton(final ActionEvent event) {
        Object[] o = userDataGroup.getChildren().toArray();
        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof TextField) {
                ((TextField) o[i]).clear();
            } else if (o[i] instanceof TextArea) {
                ((TextArea) o[i]).clear();
            } else if (o[i] instanceof DatePicker) {
                ((DatePicker) o[i]).setValue(null);
            }
        }
    }

    /* Getter and Setter Forest. Abandon all hope, ye who enter */
    public String getEnterPark() {
        return selectAParkDropDownMenu.getSelectionModel().getSelectedItem().toString();
    }
    
    public int getEnterYear() {
        return Integer.parseInt(enterYear.getSelectionModel().getSelectedItem().toString());
    }
    
    public int getEnterMonth() {
        return enterMonth.getSelectionModel().getSelectedItem().getValue();
    }
    
    public BigDecimal getConversionFactorPaidDayUseTF() throws NumberFormatException {
        BigDecimal temp;
        try {
            temp = new BigDecimal(conversionFactorPaidDayUseTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + conversionFactorPaidDayUseTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setConversionFactorPaidDayUseTF(final String in) {
            conversionFactorPaidDayUseTF.setText(in);
    }
    
    public long getPaidDayUseTotalsTF() throws NumberFormatException {
        long temp;
        try {
            temp = Integer.parseInt(paidDayUseTotalsTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + paidDayUseTotalsTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setPaidDayUseTotalsTF(final String in) {
        paidDayUseTotalsTF.setText(in);
    }
    
    public int getSpecialEventsTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(specialEventsTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + specialEventsTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setSpecialEventsTF(final String in) {
        specialEventsTF.setText(in);
    }
    
    public int getAnnualDayUseTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(annualDayUseTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + annualDayUseTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setAnnualDayUseTF(final String in) {
        annualDayUseTF.setText(in);
    }
    
    public int getDayUseTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(dayUseTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + dayUseTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setDayUseTF(final String in) {
        dayUseTF.setText(in);
    }
    
    public int getSeniorTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(seniorTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + seniorTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setSeniorTF(final String in) {
        seniorTF.setText(in);
    }
    
    public int getDisabledTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(disabledTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + disabledTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setDisabledTF(final String in) {
        disabledTF.setText(in);
    }
    
    public int getGoldenBearTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(goldenBearTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + goldenBearTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setGoldenBearTF(final String in) {
        goldenBearTF.setText(in);
    }
    
    public int getDisabledVeteranTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(disabledVeteranTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + disabledVeteranTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setDisabledVeteranTF(final String in) {
        disabledVeteranTF.setText(in);
    }
    
    public int getNonResOHVPassTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(nonResOHVPassTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + nonResOHVPassTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setNonResOHVPassTF(final String in) {
        nonResOHVPassTF.setText(in);
    }
    
    public int getAnnualPassSaleTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(annualPassSaleTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + annualPassSaleTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setAnnualPassSaleTF(final String in) {
        annualDayUseTF.setText(in);
    }
    
    public int getCampingTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(campingTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + campingTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setCampingTF(final String in) {
        campingTF.setText(in);
    }
    
    public int getSeniorCampingTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(seniorCampingTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + seniorCampingTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setSeniorCampingTF(final String in) {
        seniorCampingTF.setText(in);
    }
    
    public int getDisabledCampingTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(disabledCampingTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + disabledCampingTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setDisabledCampingTF(final String in) {
        disabledCampingTF.setText(in);
    }
    
    public BigDecimal getConversionFactorFreeDayUseTF() throws NumberFormatException {
        BigDecimal temp;
        try {
            temp = new BigDecimal(conversionFactorFreeDayUseTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + conversionFactorFreeDayUseTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setConversionFactorFreeDayUseTF(final String in) {
        conversionFactorFreeDayUseTF.setText(in);
    }
    
    public int getFreeDayUseTotalsTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(freeDayUseTotalsTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + freeDayUseTotalsTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setFreeDayUseTotalsTF(final String in) {
        freeDayUseTotalsTF.setText(in);
    }
    
    public int getTotalVehiclesTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(totalVehiclesTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + totalVehiclesTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setTotalVehiclesTF(final String in) {
        totalVehiclesTF.setText(in);
    }
    
    public int getTotalPeopleTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(totalPeopleTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + totalPeopleTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setTotalPeopleTF(final String in) {
        totalPeopleTF.setText(in);
    }
    
    public BigDecimal getRatioTF() throws NumberFormatException {
        BigDecimal temp;
        try {
            temp = new BigDecimal(ratioTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + ratioTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setRatioTF(final String in) {
        ratioTF.setText(in);
    }
    
    public String getCommentsTB() {
        return commentsTB.getText();
    }
    
    public void setCommentsTB(final String targetText) {
        commentsTB.setText(targetText);
    }
    
    public int getMcTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(mcTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + mcTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setMcTF(final String in) {
        mcTF.setText(in);
    }
    
    public int getAtvTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(atvTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + atvTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setAtvTF(final String in) {
        atvTF.setText(in);
    }
    
    public int getFourByFourTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(fourByFourTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + fourByFourTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setFourByFourTF(final String in ) {
        fourByFourTF.setText(in);
    }
    
    public int getRovTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(rovTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + rovTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setRovTF(final String in) {
        rovTF.setText(in);
    }
    
    public int getAqmaTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(aqmaTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + aqmaTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setAqmaTF(final String in) {
        aqmaTF.setText(in);
    }
    
    public int getAllStarKartingTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(mcTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + mcTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setAllStarKartingTF(final String in) {
        allStarKartingTF.setText(in);
    }
    
    public int getHangtownTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(mcTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + mcTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setHangtownTF(final String in) {
        hangtownTF.setText(in);
    }
    
    public int getOtherTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(otherTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + otherTF.getText());
            throw e;
        }
        return temp;
    }
    
    public void setOtherTF(final String in) {
        otherTF.setText(in);
    }
    
    public String getbrowseFileTF() {
        return browseFileTF.getText();
    }
    
    public void setbrowseFileTF(String in) {
        browseFileTF.setText(in);
    }
    
    public File getbrowseFile() {
        File file = new File(getbrowseFileTF());
        if(file.exists()) {
            return file;
        }
        else {
            /*
            Change field to red maybe?
             */
            return null;
        }
    }
    /* Enter Data Tab End */

    /* View Data Tab Start */
    public String getMonthSelectionOne() {
        return monthSelectionOne.getSelectionModel().getSelectedItem().toString();
    }
    
    public String getYearSelectionOne() {
        return yearSelectionOne.getSelectionModel().getSelectedItem().toString();
    }
    
    public String getMonthSelectionTwo() {
        return monthSelectionTwo.getSelectionModel().getSelectedItem().toString();
    }
    
    public String getYearSelectionTwo() {
        return yearSelectionTwo.getSelectionModel().getSelectedItem().toString();
    }
    /* View Data Tab End */

    /* Graph Data Tab Start */

    /* Graph Data Tab End */
}
