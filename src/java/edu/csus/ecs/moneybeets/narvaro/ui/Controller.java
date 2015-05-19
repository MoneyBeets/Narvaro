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
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import edu.csus.ecs.moneybeets.narvaro.model.DataManager;
import edu.csus.ecs.moneybeets.narvaro.model.MonthData;
import edu.csus.ecs.moneybeets.narvaro.model.ParkMonth;
import edu.csus.ecs.moneybeets.narvaro.model.SimpleDataProperty;
import edu.csus.ecs.moneybeets.narvaro.model.TimeSpan;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;
import edu.csus.ecs.moneybeets.narvaro.util.TaskEngine;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    private ImageView submitButtonStatusIndicator;
    @FXML
    private Button browseFileButton;
    @FXML
    private TextField browseFileTF;
    @FXML
    private Button addParkButton1;
    @FXML
    private TextField addParkTF;
    /* Enter Data Tab End */

    /* View Data Tab Start */
    @FXML
    private Tab viewDataTab;
    @FXML
    private ComboBox<Month> monthSelectionOne;
    @FXML
    private ComboBox<Integer> yearSelectionOne;
    @FXML
    private ComboBox<Month> monthSelectionTwo;
    @FXML
    private ComboBox<Integer> yearSelectionTwo;
    @FXML
    private ListView<String> parkView;
    @FXML
    private Button searchButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane viewDataPane;
    @FXML
    private TableView viewDataTable;
    @FXML
    private TableColumn<SimpleDataProperty, String> parkCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> paidConversionFactorCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> paidTotalsCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> specialEventsCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> dayUseCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> seniorCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> disabledCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> goldenBearCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> disabledVeteranCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> nonResOHVPassCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> annualPassSaleCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> campingCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> seniorCampingCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> disabledCampingCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> freeConversionFactorCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> freeTotalsCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> classVehiclesCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> classPeopleCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> mcCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> atvCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> fourByFourCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> rovCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> aqmaCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> allStarKartingCol;
    @FXML
    private TableColumn<SimpleDataProperty, String>  hangtownCol;
    @FXML
    private TableColumn<SimpleDataProperty, String> otherCol;
    /* View Data Tab End */

    /* Graph Data Tab Start */
    @FXML
    private ListView<String> selectParksGraphData;
    @FXML
    private ComboBox<Month> startingMonthGraphData;
    @FXML
    private ComboBox<Integer> startingYearGraphData;
    @FXML
    private ComboBox<Month> endingMonthGraphData;
    @FXML
    private ComboBox<Integer> endingYearGraphData;
    @FXML
    private ListView<String> selectAFieldGraphData;
    @FXML
    private Button graphButton;
    @FXML
    private HBox graphViewPane;
    /* Graph Data Tab End */
    
    private Image okImage;
    private Image errorImage;
    private Image busyImage;
    private ObservableList<Integer> row;
    
    @FXML
    public void initialize() {

        row = FXCollections.observableArrayList();
        viewDataTable.setItems(row);
        updateParkLists();
        initializeColumns();

        // populate year field on enter data tab and view data tab
        LocalDateTime ldt = LocalDateTime.now();
        int year = ldt.getYear();
        for (; year >= 1984; year--) {
            enterYear.getItems().add(year);
            yearSelectionOne.getItems().add(year);
            yearSelectionTwo.getItems().add(year);
            startingYearGraphData.getItems().add(year);
            endingYearGraphData.getItems().add(year);
        }
        // populate month field on enter data tab
        enterMonth.getItems().addAll(Arrays.asList(Month.values()));

        // populate month fields on view data tab
        monthSelectionOne.getItems().addAll(Arrays.asList(Month.values()));
        monthSelectionTwo.getItems().addAll(Arrays.asList(Month.values()));
        
        // populate month fields on graph data tab
        startingMonthGraphData.getItems().addAll(Arrays.asList(Month.values()));
        endingMonthGraphData.getItems().addAll(Arrays.asList(Month.values()));

        // permit multiple selection on park listview
        parkView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectParksGraphData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // permit only single selection for graphing a field
        selectAFieldGraphData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        
        // add change listener to all drop-downs in view data tab
        selectAParkDropDownMenu.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                displayStoredData();
            }
        });
        enterYear.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> ov,
                                Integer old_val, Integer new_val) {
                displayStoredData();
            }
        });
        enterMonth.valueProperty().addListener(new ChangeListener<Month>() {
            @Override
            public void changed(ObservableValue<? extends Month> ov,
                                Month old_val, Month new_val) {
                displayStoredData();
            }
        });
        
        // populate graph table field list
        List<String> columns = DataManager.Narvaro.getSchemaColumnNamesFromData();
        for (String column : columns) {
            
            switch (column) {
            case "id":
                break;
            case "park":
                break;
            case "month":
                break;
            case "pduConversionFactor":
                break;
            case "fduConversionFactor":
                break;
            case "fscRatio":
                break;
            case "comment":
                break;
            case "form449":
                break;
            default:
                selectAFieldGraphData.getItems().add(column);
                break;
            }
        }
    }
    
    /**
     * Displays any existing data from the database in the proper
     *   fields on the enter data tab.
     */
    private void displayStoredData() {
        try {
            String s = getEnterPark();
            if ("".equals(s) && s == null) {
                return;
            } else if (s.equals("Park")) {
                return;
            }
            int y = getEnterYear();
            if (y < 1984) {
                return;
            }
            int m = getEnterMonth();
            if (m < 1 || m > 12) {
                return;
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            return;
        }
        submitButton.setDisable(true);
        clearButton.setDisable(true);
        browseFileButton.setDisable(true);
        // show busy spinner icon
        showBusyOnSubmit();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                MonthData md = null;
                try {
                    md = DataManager.Narvaro.getMonthDataForParkAndYearMonth(YearMonth.of(getEnterYear(), getEnterMonth()), getEnterPark());

                    // set data on view data tab
                    conversionFactorPaidDayUseTF.setText(md.getPduConversionFactor().toString());
                    paidDayUseTotalsTF.setText(String.valueOf(md.getPduTotals()));
                    specialEventsTF.setText(String.valueOf(md.getPduSpecialEvents()));
                    annualDayUseTF.setText(String.valueOf(md.getPduAnnualDayUse()));
                    dayUseTF.setText(String.valueOf(md.getPduDayUse()));
                    seniorTF.setText(String.valueOf(md.getPduSenior()));
                    disabledTF.setText(String.valueOf(md.getPduDisabled()));
                    goldenBearTF.setText(String.valueOf(md.getPduGoldenBear()));
                    disabledVeteranTF.setText(String.valueOf(md.getPduDisabledVeteran()));
                    nonResOHVPassTF.setText(String.valueOf(md.getPduNonResOHVPass()));
                    annualPassSaleTF.setText(String.valueOf(md.getPduAnnualPassSale()));
                    campingTF.setText(String.valueOf(md.getPduCamping()));
                    seniorCampingTF.setText(String.valueOf(md.getPduSeniorCamping()));
                    disabledCampingTF.setText(String.valueOf(md.getPduDisabledCamping()));
                    conversionFactorFreeDayUseTF.setText(md.getFduConversionFactor().toString());
                    freeDayUseTotalsTF.setText(String.valueOf(md.getFduTotals()));
                    totalVehiclesTF.setText(String.valueOf(md.getFscTotalVehicles()));
                    totalPeopleTF.setText(String.valueOf(md.getFscTotalPeople()));
                    ratioTF.setText(md.getFscRatio().toString());
                    commentsTB.setText(md.getComment());
                    mcTF.setText(String.valueOf(md.getoMC()));
                    atvTF.setText(String.valueOf(md.getoATV()));
                    fourByFourTF.setText(String.valueOf(md.getO4X4()));
                    rovTF.setText(String.valueOf(md.getoROV()));
                    aqmaTF.setText(String.valueOf(md.getoAQMA()));
                    allStarKartingTF.setText(String.valueOf(md.getoAllStarKarting()));
                    hangtownTF.setText(String.valueOf(md.getoHangtown()));
                    otherTF.setText(String.valueOf(md.getoOther()));
                    browseFileTF.setText(md.getForm449File().toPath().toString());

                    showOKOnSubmit();
                    resetValidation();
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    Object[] o = userDataGroup.getChildren().toArray();
                    for (int i = 0; i < o.length; i++) {
                        if (o[i] instanceof TextField) {
                            ((TextField) o[i]).clear();
                        } else if (o[i] instanceof TextArea) {
                            ((TextArea) o[i]).clear();
                        }
                    }
                    resetValidation();
                }

                // enable buttons again
                clearSubmitButtonStatusIndicator();
                submitButton.setDisable(false);
                clearButton.setDisable(false);
                browseFileButton.setDisable(false);
            }
        });
    }

    @FXML
    public void handleSubmitButton(final ActionEvent event) {
        // disable buttons so user don't press again
        submitButton.setDisable(true);
        clearButton.setDisable(true);
        browseFileButton.setDisable(true);
        // show busy spinner icon
        showBusyOnSubmit();
        // Submit processing to background task so we
        //   don't block the UI thread and freeze the window
        TaskEngine.INSTANCE.submit(new StoreParkMonthTask());
    }
    
    @FXML
    public void handleAddParkButton1(final ActionEvent event) {
    	
    	String parkName = addParkTF.getText();
    	
    	try {
    	    showBusyOnSubmit(); // display busy spinner in case this takes longer than a second
    	    showValid(addParkTF); // display green area on text field
    	    DataManager.Narvaro.insertParkName(parkName);
    	    updateParkLists();
    	    showOKOnSubmit(); // display green OK checkmark
    	    addParkTF.clear(); // clear this field on success
    	    resetValid(addParkTF); // reset back to default
    	} catch (SQLException e) {
    	    LOG.error(e.getMessage(), e);
    	    showError(addParkTF); // show error on field
    	    showErrorOnSubmit(); // display error red X
    	}
    	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @FXML void handleGraphButton(final ActionEvent event) {
        try {
            // get fields
            YearMonth start = YearMonth.of(
                    startingYearGraphData.getSelectionModel().getSelectedItem(), 
                    startingMonthGraphData.getSelectionModel().getSelectedItem());
            YearMonth end = YearMonth.of(
                    endingYearGraphData.getSelectionModel().getSelectedItem(), 
                    endingMonthGraphData.getSelectionModel().getSelectedItem());
            List<String> parkNames = selectParksGraphData.getSelectionModel().getSelectedItems();
            String field = selectAFieldGraphData.getSelectionModel().getSelectedItem();
            
            // get data
            Map<String, HashMap<YearMonth, Long>> data = DataManager.Narvaro.getGraphData(field, start, end, parkNames);
            
            // blank out any existing graph
            graphViewPane.getChildren().clear();
            
            // start graph construction
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Year-Month");
            LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
            
            for (String parkName : data.keySet()) {
                Map<YearMonth, Long> m = data.get(parkName);
                
                XYChart.Series series = new XYChart.Series();
                series.setName(parkName);
                
                for (Map.Entry<YearMonth, Long> entry : m.entrySet()) {
                    series.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
                }
                
                lineChart.getData().addAll(series);
            }

            graphViewPane.getChildren().addAll(lineChart);
            
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    private boolean validateEnteredData() {
        boolean ok = true;
        try {
            getEnterPark();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectAParkDropDownMenu.setStyle("-fx-background-color:#87D37C;");
                }
            });
        } catch (Exception e) {
            ok = false;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectAParkDropDownMenu.setStyle("-fx-background-color:#EF4836;");
                }
            });
        }
        try {
            getEnterYear();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    enterYear.setStyle("-fx-background-color:#87D37C;");
                }
            });
        } catch (Exception e) {
            ok = false;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    enterYear.setStyle("-fx-background-color:#EF4836;");
                }
            });
        }
        try {
            getEnterMonth();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    enterMonth.setStyle("-fx-background-color:#87D37C;");
                }
            });
        } catch (Exception e) {
            ok = false;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    enterMonth.setStyle("-fx-background-color:#EF4836;");
                }
            });
        }
        try {
            getConversionFactorPaidDayUseTF();
            showValid(conversionFactorPaidDayUseTF);
        } catch (Exception e) {
            ok = false;
            showError(conversionFactorPaidDayUseTF);
        }
        try {
            getPaidDayUseTotalsTF();
            showValid(paidDayUseTotalsTF);
        } catch (Exception e) {
            ok = false;
            showError(paidDayUseTotalsTF);
        }
        try {
            getSpecialEventsTF();
            showValid(specialEventsTF);
        } catch (Exception e) {
            ok = false;
            showError(specialEventsTF);
        }
        try {
            getAnnualDayUseTF();
            showValid(annualDayUseTF);
        } catch (Exception e) {
            ok = false;
            showError(annualDayUseTF);
        }
        try {
            getDayUseTF();
            showValid(dayUseTF);
        } catch (Exception e) {
            ok = false;
            showError(dayUseTF);
        }
        try {
            getSeniorTF();
            showValid(seniorTF);
        } catch (Exception e) {
            ok = false;
            showError(seniorTF);
        }
        try {
            getDisabledTF();
            showValid(disabledTF);
        } catch (Exception e) {
            ok = false;
            showError(disabledTF);
        }
        try {
            getGoldenBearTF();
            showValid(goldenBearTF);
        } catch (Exception e) {
            ok = false;
            showError(goldenBearTF);
        }
        try {
            getDisabledVeteranTF();
            showValid(disabledVeteranTF);
        } catch (Exception e) {
            ok = false;
            showError(disabledVeteranTF);
        }
        try {
            getNonResOHVPassTF();
            showValid(nonResOHVPassTF);
        } catch (Exception e) {
            ok = false;
            showError(nonResOHVPassTF);
        }
        try {
            getAnnualPassSaleTF();
            showValid(annualPassSaleTF);
        } catch (Exception e) {
            ok = false;
            showError(annualPassSaleTF);
        }
        try {
            getCampingTF();
            showValid(campingTF);
        } catch (Exception e) {
            ok = false;
            showError(campingTF);
        }
        try {
            getSeniorCampingTF();
            showValid(seniorCampingTF);
        } catch (Exception e) {
            ok = false;
            showError(seniorCampingTF);
        }
        try {
            getDisabledCampingTF();
            showValid(disabledCampingTF);
        } catch (Exception e) {
            ok = false;
            showError(disabledCampingTF);
        }
        try {
            getConversionFactorFreeDayUseTF();
            showValid(conversionFactorFreeDayUseTF);
        } catch (Exception e) {
            ok = false;
            showError(conversionFactorFreeDayUseTF);
        }
        try {
            getFreeDayUseTotalsTF();
            showValid(freeDayUseTotalsTF);
        } catch (Exception e) {
            ok = false;
            showError(freeDayUseTotalsTF);
        }
        try {
            getTotalVehiclesTF();
            showValid(totalVehiclesTF);
        } catch (Exception e) {
            ok = false;
            showError(totalVehiclesTF);
        }
        try {
            getTotalPeopleTF();
            showValid(totalPeopleTF);
        } catch (Exception e) {
            ok = false;
            showError(totalPeopleTF);
        }
        try {
            getRatioTF();
            showValid(ratioTF);
        } catch (Exception e) {
            ok = false;
            showError(ratioTF);
        }
        try {
            getMcTF();
            showValid(mcTF);
        } catch (Exception e) {
            ok = false;
            showError(mcTF);
        }
        try {
            getAtvTF();
            showValid(atvTF);
        } catch (Exception e) {
            ok = false;
            showError(atvTF);
        }
        try {
            getFourByFourTF();
            showValid(fourByFourTF);
        } catch (Exception e) {
            ok = false;
            showError(fourByFourTF);
        }
        try {
            getRovTF();
            showValid(rovTF);
        } catch (Exception e) {
            ok = false;
            showError(rovTF);
        }
        try {
            getAqmaTF();
            showValid(aqmaTF);
        } catch (Exception e) {
            ok = false;
            showError(aqmaTF);
        }
        try {
            getAllStarKartingTF();
            showValid(allStarKartingTF);
        } catch (Exception e) {
            ok = false;
            showError(allStarKartingTF);
        }
        try {
            getHangtownTF();
            showValid(hangtownTF);
        } catch (Exception e) {
            ok = false;
            showError(hangtownTF);
        }
        try {
            getOtherTF();
            showValid(otherTF);
        } catch (Exception e) {
            ok = false;
            showError(otherTF);
        }
        try {
            File f = getbrowseFile();
            if (f.exists()) {
                showValid(browseFileTF);
            }
        } catch (Exception e) {
            ok = false;
            showError(browseFileTF);
        }
        if (ok == false) {
            showErrorOnSubmit();
        }
        return ok;  
    }
    
    private void resetValidation() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                selectAParkDropDownMenu.setStyle("-fx-background-color:#FFFFFF;");
                enterYear.setStyle("-fx-background-color:#FFFFFF;");
                enterMonth.setStyle("-fx-background-color:#FFFFFF;");
                resetValid(conversionFactorPaidDayUseTF);
                resetValid(paidDayUseTotalsTF);
                resetValid(specialEventsTF);
                resetValid(annualDayUseTF);
                resetValid(dayUseTF);
                resetValid(seniorTF);
                resetValid(disabledTF);
                resetValid(goldenBearTF);
                resetValid(disabledVeteranTF);
                resetValid(nonResOHVPassTF);
                resetValid(annualPassSaleTF);
                resetValid(campingTF);
                resetValid(seniorCampingTF);
                resetValid(disabledCampingTF);
                resetValid(conversionFactorFreeDayUseTF);
                resetValid(freeDayUseTotalsTF);
                resetValid(totalVehiclesTF);
                resetValid(totalPeopleTF);
                resetValid(ratioTF);
                resetValid(commentsTB);
                resetValid(mcTF);
                resetValid(atvTF);
                resetValid(fourByFourTF);
                resetValid(rovTF);
                resetValid(aqmaTF);
                resetValid(allStarKartingTF);
                resetValid(hangtownTF);
                resetValid(otherTF);
                resetValid(browseFileTF);
            }
        });
    }
    
    private void showValid(final Region r) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                r.setStyle("-fx-control-inner-background:#87D37C;");
            }
        });
    }
    
    private void showError(final Region r) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                r.setStyle("-fx-control-inner-background:#EF4836;");
            }
        });
    }
    
    private void resetValid(final Region r) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                r.setStyle("-fx-control-inner-background:#FFFFFF;");
            }
        });
    }
    
    public void showBusyOnSubmit() {
        InputStream in = null;
        if (busyImage == null) {
            try {
                in = new FileInputStream(ConfigurationManager.NARVARO.getHomeDirectory() 
                        + File.separator + "resources" + File.separator + "busy.gif");
                busyImage = new Image(in);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                submitButtonStatusIndicator.setImage(busyImage);
            }
        });
    }
    
    public void showOKOnSubmit() {
        InputStream in = null;
        if (okImage == null) {
            try {
                in = new FileInputStream(ConfigurationManager.NARVARO.getHomeDirectory() 
                        + File.separator + "resources" + File.separator + "ok.png");
                okImage = new Image(in);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                submitButtonStatusIndicator.setImage(okImage);
            }
        });
        TaskEngine.INSTANCE.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        clearSubmitButtonStatusIndicator();
                    }
                });
            }
        }, 2000);
        if (in != null) {
            try {
                in.close();
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
    }
    
    public void showErrorOnSubmit() {
        InputStream in = null;
        if (errorImage == null) {
            try {
                in = new FileInputStream(ConfigurationManager.NARVARO.getHomeDirectory() 
                        + File.separator + "resources" + File.separator + "error.png");
                errorImage = new Image(in);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                submitButtonStatusIndicator.setImage(errorImage);
            }
        });
        TaskEngine.INSTANCE.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        clearSubmitButtonStatusIndicator();
                    }
                });
            }
        }, 2000);
        if (in != null) {
            try {
                in.close();
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
    }
    
    private void clearSubmitButtonStatusIndicator() {
        try {
            submitButtonStatusIndicator.setImage(null);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    @FXML
    public void handleBrowseButton(final ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open 449 Form");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Excel Files", "*.xls", "*.xlsx", "*.csv"));
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
        selectAParkDropDownMenu.setValue(null);
        enterYear.setValue(null);
        enterMonth.setValue(null);
        resetValidation();
    }
    
    public void updateParkLists() {
        // get a list of all park names in the db
        List<String> parkNames = DataManager.Narvaro.getAllParkNames();
        // clear old items
        selectAParkDropDownMenu.getItems().clear();
        parkView.getItems().clear();
        // add park names to window
        for (String parkName : parkNames) {
            selectAParkDropDownMenu.getItems().add(parkName);
            parkView.getItems().add(parkName);
            selectParksGraphData.getItems().add(parkName);
        }
    }

    @FXML
    public void handleSearchButton(final ActionEvent event) {

        ObservableList<ObservableList<Integer>> entries = FXCollections.observableArrayList();
        viewDataTable.setItems(entries);
        entries.clear();

        int startYear = yearSelectionOne.getValue();
        Month startMonth = monthSelectionOne.getValue();
        int endYear = yearSelectionTwo.getValue();
        Month endMonth = monthSelectionTwo.getValue();
        TimeSpan ts = null;
        ParkMonth pm;

        List<String> parkNames = parkView.getSelectionModel().getSelectedItems();

//        for (String parkName : parkNames) {
//            TableColumn col = new TableColumn();
//            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
//                @Override
//                public ObservableValue call(TableColumn.CellDataFeatures param) {
//                    return new SimpleDataProperty();
//                }
//            });
//            viewDataTable.getColumns().addAll(col);
//        }

        for (String parkName : parkNames) {
            try {
                ts = DataManager.Narvaro.getTimeSpanForPark(YearMonth.of(startYear, startMonth), YearMonth.of(endYear, endMonth), parkName);
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
            pm = ts.getParkMonth(parkName);
            int[] totalsArray = new int[5];
            for (MonthData md : pm.getAllMonthData()) {
                // Accumulate all the ints here
            }
            // use totals and constants to make a SimpleDataProperty, then add to entries
            for (int i : totalsArray) {
                row.add(totalsArray[i]);
            }
            entries.add(row);
        }
    }

    private void initializeColumns() {
        parkCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("park")
        );
        paidConversionFactorCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("paidConversionFactor")
        );
        paidTotalsCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("paidTotals")
        );
        specialEventsCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("specialEvents")
        );
        dayUseCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("dayUse")
        );
        seniorCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("senior")
        );
        disabledCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("disabled")
        );
        nonResOHVPassCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("annualPassSale")
        );
        campingCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("camping")
        );
        seniorCampingCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("seniorCamping")
        );
        disabledVeteranCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("disabledVeteran")
        );
        freeConversionFactorCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("freeConversionFactor")
        );
        freeTotalsCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("freeTotals")
        );
        classVehiclesCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("classVehicles")
        );
        classPeopleCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("classPeople")
        );
        mcCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("mc")
        );
        atvCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("atv")
        );
        fourByFourCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("fourByFour")
        );
        rovCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("rov")
        );
        aqmaCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("aqma")
        );
        allStarKartingCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("allStarKarting")
        );
        hangtownCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("hangtown")
        );
        otherCol.setCellValueFactory(
                new PropertyValueFactory<SimpleDataProperty, String>("other")
        );
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
    
    /**
     * This task handles storing of data
     *
     */
    private class StoreParkMonthTask implements Runnable {
        
        ParkMonth parkMonth = null;
        boolean success = false;
        
        @Override
        public void run() {
            if(validateEnteredData()) {
                try {
                    parkMonth = new ParkMonth(getEnterPark());
                    parkMonth.createAndPutMonthData(YearMonth.of(getEnterYear(), getEnterMonth()),
                            getConversionFactorPaidDayUseTF(), getPaidDayUseTotalsTF(), getSpecialEventsTF(), getAnnualDayUseTF(),
                            getDayUseTF(), getSeniorTF(), getDisabledTF(), getGoldenBearTF(), getDisabledVeteranTF(),
                            getNonResOHVPassTF(), getAnnualPassSaleTF(), getCampingTF(), getSeniorCampingTF(),
                            getDisabledCampingTF(), getConversionFactorFreeDayUseTF(), getFreeDayUseTotalsTF(),
                            getTotalVehiclesTF(), getTotalPeopleTF(), getRatioTF(), getMcTF(), getAtvTF(), getFourByFourTF(),
                            getRovTF(), getAqmaTF(), getAllStarKartingTF(), getHangtownTF(), getOtherTF(), getCommentsTB(),
                            -1, getbrowseFile());
                    success = true;
                } catch (Exception e) {
                    // something was wrong with data
                    LOG.error(e.getMessage(), e);
                    showErrorOnSubmit();
                }
                
                if (success) {
                    // attempt to write into database
                    success = false;
                    try {
                        DataManager.Narvaro.storeParkMonth(parkMonth);
                        success = true;
                    } catch (SQLException e) {
                        LOG.error(e.getMessage(), e);
                    }
                    if (success) {
                        showOKOnSubmit();
                        resetValidation();
                    } else {
                        showErrorOnSubmit();
                    }
                }
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // enable buttons again
                    submitButton.setDisable(false);
                    clearButton.setDisable(false);
                    browseFileButton.setDisable(false);
                }
            });
        }
    }
}
