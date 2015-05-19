package edu.csus.ecs.moneybeets.narvaro.model;

import java.math.BigDecimal;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Ryan on 5/18/2015.
 */
public class SimpleDataProperty {
    private SimpleStringProperty park;
    private SimpleStringProperty paidConversionFactor;
    private SimpleStringProperty paidTotals;
    private SimpleStringProperty specialEvents;
    private SimpleStringProperty dayUse;
    private SimpleStringProperty senior;
    private SimpleStringProperty disabled;
    private SimpleStringProperty goldenBear;
    private SimpleStringProperty disabledVeteran;
    private SimpleStringProperty nonResOHVPass;
    private SimpleStringProperty annualPassSale;
    private SimpleStringProperty camping;
    private SimpleStringProperty seniorCamping;
    private SimpleStringProperty disabledCamping;
    private SimpleStringProperty freeConversionFactor;
    private SimpleStringProperty freeTotals;
    private SimpleStringProperty classVehicles;
    private SimpleStringProperty classPeople;
    private SimpleStringProperty mc;
    private SimpleStringProperty atv;
    private SimpleStringProperty fourByFour;
    private SimpleStringProperty rov;
    private SimpleStringProperty aqma;
    private SimpleStringProperty allStarKarting;
    private SimpleStringProperty hangtown;
    private SimpleStringProperty other;

    public SimpleDataProperty(final String park,
            final BigDecimal paidConversionFactor,
            final long paidTotals,
            final long specialEvents,
            final long dayUse,
            final long senior,
            final long disabled,
            final long goldenBear,
            final long disabledVeteran,
            final long nonResOHVPass,
            final long annualPassSale,
            final long camping,
            final long seniorCamping,
            final long disabledCamping,
            final BigDecimal freeConversionFactor,
            final long freeTotals,
            final long classVehicles,
            final long classPeople,
            final long mc,
            final long atv,
            final long fourByFour,
            final long rov,
            final long aqma,
            final long allStarKarting,
            final long hangtown,
            final long other) {
        this.park = new SimpleStringProperty(park);
        this.paidConversionFactor = new SimpleStringProperty(paidConversionFactor.toString());
        this.paidTotals = new SimpleStringProperty(String.valueOf(paidTotals));
        this.specialEvents = new SimpleStringProperty(String.valueOf(specialEvents));
        this.dayUse = new SimpleStringProperty(String.valueOf(dayUse));
        this.senior = new SimpleStringProperty(String.valueOf(senior));
        this.disabled = new SimpleStringProperty(String.valueOf(disabled));
        this.goldenBear = new SimpleStringProperty(String.valueOf(goldenBear));
        this.disabledVeteran = new SimpleStringProperty(String.valueOf(disabledVeteran));
        this.nonResOHVPass = new SimpleStringProperty(String.valueOf(nonResOHVPass));
        this.annualPassSale = new SimpleStringProperty(String.valueOf(annualPassSale));
        this.camping = new SimpleStringProperty(String.valueOf(camping));
        this.seniorCamping = new SimpleStringProperty(String.valueOf(seniorCamping));
        this.disabled = new SimpleStringProperty(String.valueOf(disabled));
        this.disabledCamping = new SimpleStringProperty(String.valueOf(disabledCamping));
        this.freeConversionFactor = new SimpleStringProperty(freeConversionFactor.toString());
        this.freeTotals = new SimpleStringProperty(String.valueOf(freeTotals));
        this.classVehicles = new SimpleStringProperty(String.valueOf(classVehicles));
        this.classPeople = new SimpleStringProperty(String.valueOf(classPeople));
        this.mc = new SimpleStringProperty(String.valueOf(mc));
        this.atv = new SimpleStringProperty(String.valueOf(atv));
        this.fourByFour = new SimpleStringProperty(String.valueOf(fourByFour));
        this.rov = new SimpleStringProperty(String.valueOf(rov));
        this.aqma = new SimpleStringProperty(String.valueOf(aqma));
        this.allStarKarting = new SimpleStringProperty(String.valueOf(allStarKarting));
        this.hangtown = new SimpleStringProperty(String.valueOf(hangtown));
        this.other = new SimpleStringProperty(String.valueOf(other));
    }

    public String getPark() {
        return park.get();
    }

    public SimpleStringProperty parkProperty() {
        return park;
    }

    public String getPaidConversionFactor() {
        return paidConversionFactor.get();
    }

    public SimpleStringProperty paidConversionFactorProperty() {
        return paidConversionFactor;
    }

    public String getPaidTotals() {
        return paidTotals.get();
    }

    public SimpleStringProperty paidTotalsProperty() {
        return paidTotals;
    }

    public String getSpecialEvents() {
        return specialEvents.get();
    }

    public SimpleStringProperty specialEventsProperty() {
        return specialEvents;
    }

    public String getDayUse() {
        return dayUse.get();
    }

    public SimpleStringProperty dayUseProperty() {
        return dayUse;
    }

    public String getSenior() {
        return senior.get();
    }

    public SimpleStringProperty seniorProperty() {
        return senior;
    }

    public String getDisabled() {
        return disabled.get();
    }

    public SimpleStringProperty disabledProperty() {
        return disabled;
    }

    public String getGoldenBear() {
        return goldenBear.get();
    }

    public SimpleStringProperty goldenBearProperty() {
        return goldenBear;
    }

    public String getDisabledVeteran() {
        return disabledVeteran.get();
    }

    public SimpleStringProperty disabledVeteranProperty() {
        return disabledVeteran;
    }

    public String getNonResOHVPass() {
        return nonResOHVPass.get();
    }

    public SimpleStringProperty nonResOHVPassProperty() {
        return nonResOHVPass;
    }

    public String getAnnualPassSale() {
        return annualPassSale.get();
    }

    public SimpleStringProperty annualPassSaleProperty() {
        return annualPassSale;
    }

    public String getCamping() {
        return camping.get();
    }

    public SimpleStringProperty campingProperty() {
        return camping;
    }

    public String getSeniorCamping() {
        return seniorCamping.get();
    }

    public SimpleStringProperty seniorCampingProperty() {
        return seniorCamping;
    }

    public String getDisabledCamping() {
        return disabledCamping.get();
    }

    public SimpleStringProperty disabledCampingProperty() {
        return disabledCamping;
    }

    public String getFreeConversionFactor() {
        return freeConversionFactor.get();
    }

    public SimpleStringProperty freeConversionFactorProperty() {
        return freeConversionFactor;
    }

    public String getFreeTotals() {
        return freeTotals.get();
    }

    public SimpleStringProperty freeTotalsProperty() {
        return freeTotals;
    }

    public String getClassVehicles() {
        return classVehicles.get();
    }

    public SimpleStringProperty classVehiclesProperty() {
        return classVehicles;
    }

    public String getClassPeople() {
        return classPeople.get();
    }

    public SimpleStringProperty classPeopleProperty() {
        return classPeople;
    }

    public String getMc() {
        return mc.get();
    }

    public SimpleStringProperty mcProperty() {
        return mc;
    }

    public String getAtv() {
        return atv.get();
    }

    public SimpleStringProperty atvProperty() {
        return atv;
    }

    public String getFourByFour() {
        return fourByFour.get();
    }

    public SimpleStringProperty fourByFourProperty() {
        return fourByFour;
    }

    public String getRov() {
        return rov.get();
    }

    public SimpleStringProperty rovProperty() {
        return rov;
    }

    public String getAqma() {
        return aqma.get();
    }

    public SimpleStringProperty aqmaProperty() {
        return aqma;
    }

    public String getAllStarKarting() {
        return allStarKarting.get();
    }

    public SimpleStringProperty allStarKartingProperty() {
        return allStarKarting;
    }

    public String getHangtown() {
        return hangtown.get();
    }

    public SimpleStringProperty hangtownProperty() {
        return hangtown;
    }

    public String getOther() {
        return other.get();
    }

    public SimpleStringProperty otherProperty() {
        return other;
    }
}
