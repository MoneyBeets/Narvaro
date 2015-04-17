/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.ui;

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
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

public class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class.getName());

    /* Enter Data Tab Start */
    @FXML
    private Tab enterDataTab;
    @FXML
    private DatePicker datePicker;
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
    private MenuButton selectAParkDropDownMenu;
    /* Enter Data Tab End */

    /* View Data Tab Start */
    @FXML
    private Tab viewDataTab;
    @FXML
    private MenuButton monthSelectionOne;
    @FXML
    private MenuButton yearSelectionOne;
    @FXML
    private MenuButton monthSelectionTwo;
    @FXML
    private MenuButton yearSelectionTwo;
    @FXML
    private ListView<?> parkView;
    @FXML
    private Button addParkButton;
    @FXML
    private Button removeParkButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane dataPane;
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

    public int getConversionFactorPaidDayUseTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(conversionFactorPaidDayUseTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + conversionFactorPaidDayUseTF.getText());
            throw e;
        }
        return temp;
    }

    public int getPaidDayUseTotalsTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(paidDayUseTotalsTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + paidDayUseTotalsTF.getText());
            throw e;
        }
        return temp;
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

    public int getConversionFactorFreeDayUseTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(conversionFactorFreeDayUseTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + conversionFactorFreeDayUseTF.getText());
            throw e;
        }
        return temp;
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

    public int getRatioTF() throws NumberFormatException {
        int temp = -1;
        try {
            temp = Integer.parseInt(ratioTF.getText());
        } catch (NumberFormatException e) {
            LOG.error("Not a number: " + ratioTF.getText());
            throw e;
        }
        return temp;
    }

    public String getCommentsTB() {
        return this.commentsTB.getText();
    }

    public void setCommentsTB(final String targetText) {
        this.commentsTB.setText(targetText);
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

    public String getSelectAParkDropDownMenu() {
        return this.selectAParkDropDownMenu.getText();
    }

    public void setSelectAParkDropDownMenu(final String targetText) {
        this.selectAParkDropDownMenu.setText(targetText);
    }
    /* Enter Data Tab End */

    /* View Data Tab Start */
    public String getMonthSelectionOne() {
        return this.monthSelectionOne.getText();
    }

    public void setMonthSelectionOne(final String targetText) {
        this.monthSelectionOne.setText(targetText);
    }

    public String getYearSelectionOne() {
        return this.yearSelectionOne.getText();
    }

    public void setYearSelectionOne(final String targetText) {
        this.yearSelectionOne.setText(targetText);
    }

    public String getMonthSelectionTwo() {
        return this.monthSelectionTwo.getText();
    }

    public void setMonthSelectionTwo(final String targetText) {
        this.monthSelectionTwo.setText(targetText);
    }

    public String getYearSelectionTwo() {
        return this.yearSelectionTwo.getText();
    }

    public void setYearSelectionTwo(final String targetText) {
        this.yearSelectionTwo.setText(targetText);
    }
    /* View Data Tab End */

    /* Graph Data Tab Start */

    /* Graph Data Tab End */
}
