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

public class Controller {

    /* Enter Data Tab Start */
    @FXML private Tab enterDataTab;
    @FXML private DatePicker datePicker;
    @FXML private TextField conversionFactorPaidDayUseTF;
    @FXML private TextField paidDayUseTotalsTF;
    @FXML private TextField specialEventsTF;
    @FXML private TextField annualDayUseTF;
    @FXML private TextField dayUseTF;
    @FXML private TextField seniorTF;
    @FXML private TextField disabledTF;
    @FXML private TextField goldenBearTF;
    @FXML private TextField disabledVeteranTF;
    @FXML private TextField nonResOHVPassTF;
    @FXML private TextField annualPassSaleTF;
    @FXML private TextField campingTF;
    @FXML private TextField seniorCampingTF;
    @FXML private TextField disabledCampingTF;
    @FXML private TextField conversionFactorFreeDayUseTF;
    @FXML private TextField freeDayUseTotalsTF;
    @FXML private TextField totalVehiclesTF;
    @FXML private TextField totalPeopleTF;
    @FXML private TextField ratioTF;
    @FXML private TextArea commentsTB;
    @FXML private TextField mcTF;
    @FXML private TextField atvTF;
    @FXML private TextField fourByFourTF;
    @FXML private TextField rovTF;
    @FXML private TextField aqmaTF;
    @FXML private TextField allStarKartingTF;
    @FXML private TextField hangtownTF;
    @FXML private TextField otherTF;
    @FXML private Group userDataGroup;
    @FXML private Button clearButton;
    @FXML private Button submitButton;
    @FXML private MenuButton selectAParkDropDownMenu;
    /* Enter Data Tab End */
    
    /* View Data Tab Start */
    @FXML private Tab viewDataTab;
    @FXML private MenuButton monthSelectionOne;
    @FXML private MenuButton yearSelectionOne;
    @FXML private MenuButton monthSelectionTwo;
    @FXML private MenuButton yearSelectionTwo;
    @FXML private ListView<?> parkView;
    @FXML private Button addParkButton;
    @FXML private Button removeParkButton;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane dataPane;
    /* View Data Tab End */
    
    /* Graph Data Tab Start */
    @FXML private Tab graphDataTab;
    @FXML private DatePicker selectDateX;
    @FXML private DatePicker selectDateY;
    @FXML private ToggleGroup graphType;
    @FXML private Button viewDataButton;
    @FXML private Button view449FormButton;
    @FXML private Button graphButton;
    @FXML private Button printButton;
    @FXML private LineChart<?,?> graphArea;
    @FXML private MenuButton selectParkOne;
    @FXML private MenuButton selectParkTwo;
    @FXML private MenuButton selectCategory;
    /* Graph Data Tab End */
    
    /**
     * Clears all data on-screen in text fields,
     *   text-area's, and date-picker's.
     * 
     * @param event The action event.
     */
    @FXML
    public void handleClearButton(final ActionEvent event) {
        Object[] o = userDataGroup.getChildren().toArray();
        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof TextField) {
                ((TextField)o[i]).clear();
            }
            if (o[i] instanceof TextArea) {
                ((TextArea)o[i]).clear();
            }
            if (o[i] instanceof DatePicker) {
                ((DatePicker)o[i]).setValue(null);
            }
        }
    }
    
}
