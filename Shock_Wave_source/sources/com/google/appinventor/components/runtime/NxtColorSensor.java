package com.google.appinventor.components.runtime;

import android.os.Handler;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.LegoMindstormsNxtSensor;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import java.util.HashMap;
import java.util.Map;

@SimpleObject
@DesignerComponent(category = ComponentCategory.LEGOMINDSTORMS, description = "A component that provides a high-level interface to a color sensor on a LEGO MINDSTORMS NXT robot.", iconName = "images/legoMindstormsNxt.png", nonVisible = true, version = 1)
public class NxtColorSensor extends LegoMindstormsNxtSensor implements Deleteable {
    private static final int DEFAULT_BOTTOM_OF_RANGE = 256;
    private static final String DEFAULT_SENSOR_PORT = "3";
    private static final int DEFAULT_TOP_OF_RANGE = 767;
    static final int SENSOR_TYPE_COLOR_BLUE = 16;
    static final int SENSOR_TYPE_COLOR_FULL = 13;
    static final int SENSOR_TYPE_COLOR_GREEN = 15;
    static final int SENSOR_TYPE_COLOR_NONE = 17;
    static final int SENSOR_TYPE_COLOR_RED = 14;
    private static final Map<Integer, Integer> mapColorToSensorType = new HashMap();
    private static final Map<Integer, Integer> mapSensorValueToColor = new HashMap();
    /* access modifiers changed from: private */
    public boolean aboveRangeEventEnabled;
    /* access modifiers changed from: private */
    public boolean belowRangeEventEnabled;
    /* access modifiers changed from: private */
    public int bottomOfRange;
    private boolean colorChangedEventEnabled;
    /* access modifiers changed from: private */
    public boolean detectColor;
    private int generateColor;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    /* access modifiers changed from: private */
    public int previousColor = 16777215;
    /* access modifiers changed from: private */
    public State previousState = State.UNKNOWN;
    /* access modifiers changed from: private */
    public final Runnable sensorReader = new Runnable() {
        public void run() {
            State currentState;
            if (NxtColorSensor.this.bluetooth != null && NxtColorSensor.this.bluetooth.IsConnected()) {
                if (NxtColorSensor.this.detectColor) {
                    LegoMindstormsNxtSensor.SensorValue<Integer> sensorValue = NxtColorSensor.this.getColorValue("");
                    if (sensorValue.valid) {
                        int currentColor = ((Integer) sensorValue.value).intValue();
                        if (currentColor != NxtColorSensor.this.previousColor) {
                            NxtColorSensor.this.ColorChanged(currentColor);
                        }
                        int unused = NxtColorSensor.this.previousColor = currentColor;
                    }
                } else {
                    LegoMindstormsNxtSensor.SensorValue<Integer> sensorValue2 = NxtColorSensor.this.getLightValue("");
                    if (sensorValue2.valid) {
                        if (((Integer) sensorValue2.value).intValue() < NxtColorSensor.this.bottomOfRange) {
                            currentState = State.BELOW_RANGE;
                        } else if (((Integer) sensorValue2.value).intValue() > NxtColorSensor.this.topOfRange) {
                            currentState = State.ABOVE_RANGE;
                        } else {
                            currentState = State.WITHIN_RANGE;
                        }
                        if (currentState != NxtColorSensor.this.previousState) {
                            if (currentState == State.BELOW_RANGE && NxtColorSensor.this.belowRangeEventEnabled) {
                                NxtColorSensor.this.BelowRange();
                            }
                            if (currentState == State.WITHIN_RANGE && NxtColorSensor.this.withinRangeEventEnabled) {
                                NxtColorSensor.this.WithinRange();
                            }
                            if (currentState == State.ABOVE_RANGE && NxtColorSensor.this.aboveRangeEventEnabled) {
                                NxtColorSensor.this.AboveRange();
                            }
                        }
                        State unused2 = NxtColorSensor.this.previousState = currentState;
                    }
                }
            }
            if (NxtColorSensor.this.isHandlerNeeded()) {
                NxtColorSensor.this.handler.post(NxtColorSensor.this.sensorReader);
            }
        }
    };
    /* access modifiers changed from: private */
    public int topOfRange;
    /* access modifiers changed from: private */
    public boolean withinRangeEventEnabled;

    private enum State {
        UNKNOWN,
        BELOW_RANGE,
        WITHIN_RANGE,
        ABOVE_RANGE
    }

    static {
        mapColorToSensorType.put(-65536, 14);
        mapColorToSensorType.put(Integer.valueOf(Component.COLOR_GREEN), 15);
        mapColorToSensorType.put(Integer.valueOf(Component.COLOR_BLUE), 16);
        mapColorToSensorType.put(16777215, 17);
        mapSensorValueToColor.put(1, -16777216);
        mapSensorValueToColor.put(2, Integer.valueOf(Component.COLOR_BLUE));
        mapSensorValueToColor.put(3, Integer.valueOf(Component.COLOR_GREEN));
        mapSensorValueToColor.put(4, -256);
        mapSensorValueToColor.put(5, -65536);
        mapSensorValueToColor.put(6, -1);
    }

    public NxtColorSensor(ComponentContainer container) {
        super(container, "NxtColorSensor");
        SensorPort(DEFAULT_SENSOR_PORT);
        DetectColor(true);
        ColorChangedEventEnabled(false);
        BottomOfRange(256);
        TopOfRange(DEFAULT_TOP_OF_RANGE);
        BelowRangeEventEnabled(false);
        WithinRangeEventEnabled(false);
        AboveRangeEventEnabled(false);
        GenerateColor(16777215);
    }

    /* access modifiers changed from: protected */
    public void initializeSensor(String functionName) {
        setInputMode(functionName, this.port, this.detectColor ? 13 : mapColorToSensorType.get(Integer.valueOf(this.generateColor)).intValue(), 0);
        resetInputScaledValue(functionName, this.port);
    }

    @DesignerProperty(defaultValue = "3", editorType = "lego_nxt_sensor_port")
    @SimpleProperty(userVisible = false)
    public void SensorPort(String sensorPortLetter) {
        setSensorPort(sensorPortLetter);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the sensor should detect color or light. True indicates that the sensor should detect color; False indicates that the sensor should detect light. If the DetectColor property is set to True, the BelowRange, WithinRange, and AboveRange events will not occur and the sensor will not generate color. If the DetectColor property is set to False, the ColorChanged event will not occur.")
    public boolean DetectColor() {
        return this.detectColor;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void DetectColor(boolean detectColor2) {
        boolean handlerWasNeeded = isHandlerNeeded();
        this.detectColor = detectColor2;
        if (this.bluetooth != null && this.bluetooth.IsConnected()) {
            initializeSensor("DetectColor");
        }
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            this.handler.removeCallbacks(this.sensorReader);
        }
        this.previousColor = 16777215;
        this.previousState = State.UNKNOWN;
        if (!handlerWasNeeded && handlerIsNeeded) {
            this.handler.post(this.sensorReader);
        }
    }

    @SimpleFunction(description = "Returns the current detected color, or the color None if the color can not be read or if the DetectColor property is set to False.")
    public int GetColor() {
        if (!checkBluetooth("GetColor")) {
            return 16777215;
        }
        if (!this.detectColor) {
            this.form.dispatchErrorOccurredEvent(this, "GetColor", 417, new Object[0]);
            return 16777215;
        }
        LegoMindstormsNxtSensor.SensorValue<Integer> sensorValue = getColorValue("GetColor");
        if (sensorValue.valid) {
            return ((Integer) sensorValue.value).intValue();
        }
        return 16777215;
    }

    /* access modifiers changed from: private */
    public LegoMindstormsNxtSensor.SensorValue<Integer> getColorValue(String functionName) {
        byte[] returnPackage = getInputValues(functionName, this.port);
        if (returnPackage != null && getBooleanValueFromBytes(returnPackage, 4)) {
            int scaledValue = getSWORDValueFromBytes(returnPackage, 12);
            if (mapSensorValueToColor.containsKey(Integer.valueOf(scaledValue))) {
                return new LegoMindstormsNxtSensor.SensorValue<>(true, Integer.valueOf(mapSensorValueToColor.get(Integer.valueOf(scaledValue)).intValue()));
            }
        }
        return new LegoMindstormsNxtSensor.SensorValue<>(false, null);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the ColorChanged event should fire when the DetectColor property is set to True and the detected color changes.")
    public boolean ColorChangedEventEnabled() {
        return this.colorChangedEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void ColorChangedEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        this.colorChangedEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            this.handler.removeCallbacks(this.sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            this.previousColor = 16777215;
            this.handler.post(this.sensorReader);
        }
    }

    @SimpleEvent(description = "Detected color has changed. The ColorChanged event will not occur if the DetectColor property is set to False or if the ColorChangedEventEnabled property is set to False.")
    public void ColorChanged(int color) {
        EventDispatcher.dispatchEvent(this, "ColorChanged", Integer.valueOf(color));
    }

    @SimpleFunction(description = "Returns the current light level as a value between 0 and 1023, or -1 if the light level can not be read or if the DetectColor property is set to True.")
    public int GetLightLevel() {
        if (!checkBluetooth("GetLightLevel")) {
            return -1;
        }
        if (this.detectColor) {
            this.form.dispatchErrorOccurredEvent(this, "GetLightLevel", ErrorMessages.ERROR_NXT_CANNOT_DETECT_LIGHT, new Object[0]);
            return -1;
        }
        LegoMindstormsNxtSensor.SensorValue<Integer> sensorValue = getLightValue("GetLightLevel");
        if (sensorValue.valid) {
            return ((Integer) sensorValue.value).intValue();
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public LegoMindstormsNxtSensor.SensorValue<Integer> getLightValue(String functionName) {
        byte[] returnPackage = getInputValues(functionName, this.port);
        if (returnPackage == null || !getBooleanValueFromBytes(returnPackage, 4)) {
            return new LegoMindstormsNxtSensor.SensorValue<>(false, null);
        }
        return new LegoMindstormsNxtSensor.SensorValue<>(true, Integer.valueOf(getUWORDValueFromBytes(returnPackage, 10)));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The bottom of the range used for the BelowRange, WithinRange, and AboveRange events.")
    public int BottomOfRange() {
        return this.bottomOfRange;
    }

    @DesignerProperty(defaultValue = "256", editorType = "non_negative_integer")
    @SimpleProperty
    public void BottomOfRange(int bottomOfRange2) {
        this.bottomOfRange = bottomOfRange2;
        this.previousState = State.UNKNOWN;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The top of the range used for the BelowRange, WithinRange, and AboveRange events.")
    public int TopOfRange() {
        return this.topOfRange;
    }

    @DesignerProperty(defaultValue = "767", editorType = "non_negative_integer")
    @SimpleProperty
    public void TopOfRange(int topOfRange2) {
        this.topOfRange = topOfRange2;
        this.previousState = State.UNKNOWN;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the BelowRange event should fire when the DetectColor property is set to False and the light level goes below the BottomOfRange.")
    public boolean BelowRangeEventEnabled() {
        return this.belowRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void BelowRangeEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        this.belowRangeEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            this.handler.removeCallbacks(this.sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            this.previousState = State.UNKNOWN;
            this.handler.post(this.sensorReader);
        }
    }

    @SimpleEvent(description = "Light level has gone below the range. The BelowRange event will not occur if the DetectColor property is set to True or if the BelowRangeEventEnabled property is set to False.")
    public void BelowRange() {
        EventDispatcher.dispatchEvent(this, "BelowRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the WithinRange event should fire when the DetectColor property is set to False and the light level goes between the BottomOfRange and the TopOfRange.")
    public boolean WithinRangeEventEnabled() {
        return this.withinRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void WithinRangeEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        this.withinRangeEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            this.handler.removeCallbacks(this.sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            this.previousState = State.UNKNOWN;
            this.handler.post(this.sensorReader);
        }
    }

    @SimpleEvent(description = "Light level has gone within the range. The WithinRange event will not occur if the DetectColor property is set to True or if the WithinRangeEventEnabled property is set to False.")
    public void WithinRange() {
        EventDispatcher.dispatchEvent(this, "WithinRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the AboveRange event should fire when the DetectColor property is set to False and the light level goes above the TopOfRange.")
    public boolean AboveRangeEventEnabled() {
        return this.aboveRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void AboveRangeEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        this.aboveRangeEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            this.handler.removeCallbacks(this.sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            this.previousState = State.UNKNOWN;
            this.handler.post(this.sensorReader);
        }
    }

    @SimpleEvent(description = "Light level has gone above the range. The AboveRange event will not occur if the DetectColor property is set to True or if the AboveRangeEventEnabled property is set to False.")
    public void AboveRange() {
        EventDispatcher.dispatchEvent(this, "AboveRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The color that should generated by the sensor. Only None, Red, Green, or Blue are valid values. The sensor will not generate color when the DetectColor property is set to True.")
    public int GenerateColor() {
        return this.generateColor;
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "lego_nxt_generated_color")
    @SimpleProperty
    public void GenerateColor(int generateColor2) {
        if (mapColorToSensorType.containsKey(Integer.valueOf(generateColor2))) {
            this.generateColor = generateColor2;
            if (this.bluetooth != null && this.bluetooth.IsConnected()) {
                initializeSensor("GenerateColor");
                return;
            }
            return;
        }
        this.form.dispatchErrorOccurredEvent(this, "GenerateColor", 419, new Object[0]);
    }

    /* access modifiers changed from: private */
    public boolean isHandlerNeeded() {
        if (this.detectColor) {
            return this.colorChangedEventEnabled;
        }
        return this.belowRangeEventEnabled || this.withinRangeEventEnabled || this.aboveRangeEventEnabled;
    }

    public void onDelete() {
        this.handler.removeCallbacks(this.sensorReader);
        super.onDelete();
    }
}
