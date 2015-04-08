package com.longlong.wifiscanner.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.longlong.wifiscanner.dialog.AlertDialog;
import com.longlong.wifiscanner.model.ScanResult;
import com.longlong.wifiscanner.sound.SoundEffectEnum;
import com.longlong.wifiscanner.stage.MainStage;
import com.longlong.wifiscanner.util.Assets;

public class WifiScanner extends BaseComponent {

    private static final Vector2 BUTTON_SIZE = new Vector2(200, 120);

    // Model
    private final FileHandle fileHandle;
    private boolean startScanning = false;
    private float elapseTime = 0;
    private int scanCount = 0;
    private Map<String, Double> avgRSSIByBSSID = new HashMap<>();
    private Map<String, String> ssidByBSSID = new HashMap<>();

    // View
    private Table mainTable;

    private Table configTable;
    private TextField scanInternalTextField;
    private int scanIntervalSeconds = 1;
    private TextField totalScanCountTextField;
    private int totalScanCount = 60;
    private TextField xPositionTextField;
    private String xPosition = "0";
    private TextField yPositionTextField;
    private String yPosition = "0";

    private Table buttonsTable;
    private TextButton startScanButton;
    private TextButton cancelScanButton;

    private Table scanResultsTable;
    private Label scanResultTitle;
    private Map<String, Label> scanResults = new HashMap<>();

    public WifiScanner(Assets assets) {
        super(assets);
        if (Gdx.files.isExternalStorageAvailable()) {
            Gdx.app.log("WifiScanner", "external:" + Gdx.files.getExternalStoragePath());
            fileHandle = Gdx.files.external("WiFiScanner");
        } else if (Gdx.files.isLocalStorageAvailable()) {
            Gdx.app.log("WifiScanner", "internal:" + Gdx.files.getLocalStoragePath());
            fileHandle = Gdx.files.local("WiFiScanner");
        } else {
            Gdx.app.log("WifiScanner", "Storage not available.");
            fileHandle = null;
        }
        assets.getSkin().getFont("Trebucket").setScale(0.3f);

        scanInternalTextField = new TextField(
            Integer.toString(scanIntervalSeconds),
            assets.getSkin());
        totalScanCountTextField = new TextField(Integer.toString(totalScanCount), assets.getSkin());
        xPositionTextField = new TextField(xPosition, assets.getSkin());
        yPositionTextField = new TextField(yPosition, assets.getSkin());

        startScanButton = new TextButton("Start", assets.getSkin(), "green");
        startScanButton.getLabel().setFontScale(0.5f);
        cancelScanButton = new TextButton("Cancel", assets.getSkin(), "green");
        cancelScanButton.getLabel().setFontScale(0.5f);

        startScanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    scanIntervalSeconds = Integer.valueOf(scanInternalTextField.getText());
                    totalScanCount = Integer.valueOf(totalScanCountTextField.getText());
                    xPosition = xPositionTextField.getText();
                    yPosition = yPositionTextField.getText();
                } catch (NumberFormatException e) {
                    showAlertDialog("Error", "Scan Interval and Scan Count must be Integer.");
                    return;
                }
                startScanning = true;
                buttonsTable.clearChildren();
                buttonsTable.add(cancelScanButton).size(BUTTON_SIZE.x, BUTTON_SIZE.y);
            }
        });
        cancelScanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reset();
                scanResultsTable.clearChildren();
            }
        });

        configTable = new Table(assets.getSkin());
        configTable.columnDefaults(1).width(75);
        configTable.columnDefaults(2).padLeft(30);
        configTable.columnDefaults(3).width(75);
        configTable.add("Scan Interval(s) : ");
        configTable.add(scanInternalTextField);
        configTable.add("X : ");
        configTable.add(xPositionTextField);
        configTable.row();
        configTable.add("Scan Count : ");
        configTable.add(totalScanCountTextField);
        configTable.add("Y : ");
        configTable.add(yPositionTextField);
        configTable.top().left();

        buttonsTable = addActorAndCenter(new Table());
        buttonsTable.pad(20);
        buttonsTable.add(startScanButton).size(BUTTON_SIZE.x, BUTTON_SIZE.y);
        buttonsTable.top().right();

        scanResultTitle = new Label("", assets.getSkin());

        scanResultsTable = new Table(assets.getSkin());
        scanResultsTable.columnDefaults(1).padLeft(20).padRight(20).align(Align.left);
        scanResultsTable.columnDefaults(2).align(Align.right);
        scanResultsTable.top().left();
        scanResultsTable.debug();

        mainTable = addActorAndCenter(new Table());
        mainTable.pad(20);
        mainTable.columnDefaults(0).expandX();
        mainTable.add(configTable).align(Align.left);
        mainTable.row();
        mainTable.add(scanResultsTable).padTop(20);
        mainTable.top().left();
        updateTables(MainStage.STAGE_WIDTH, MainStage.STAGE_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!startScanning) {
            return;
        }
        if (scanCount >= totalScanCount) {
            writeResultsToFile();
            reset();
            assets.getSoundManager().playSound(SoundEffectEnum.COMPLETE);
            showAlertDialog(
                "Complete",
                "Wifi Scan Finished.\n\nAll results have been written to preference file \"WifiScanner\" with key:\nX="
                        + xPosition + ",Y=" + yPosition + ",BSSID=$(BSSID)");
            return;
        }
        elapseTime += delta;
        if (elapseTime > scanIntervalSeconds) {
            elapseTime = 0;
            scanCount++;
            updateScanResultTitle(scanCount);
            for (ScanResult scanResult : assets.getWifiScannerAdapter().getScanResults()) {
                Gdx.app.log("WifiScanner", scanResult.toString());
                if (scanResults.isEmpty()) {
                    scanResultsTable.clearChildren();
                    scanResultsTable.add(scanResultTitle).colspan(3);
                }
                if (!avgRSSIByBSSID.containsKey(scanResult.BSSID)) {
                    avgRSSIByBSSID.put(scanResult.BSSID, (double) scanResult.RSSI);
                    Label wifiLevel = new Label(scanResult.RSSI + " dBm", assets.getSkin());
                    scanResults.put(scanResult.BSSID, wifiLevel);
                    scanResultsTable.row();
                    scanResultsTable.add(scanResult.BSSID);
                    scanResultsTable.add(scanResult.SSID);
                    scanResultsTable.add(wifiLevel).expandX();
                } else {
                    double avgRSSI = (avgRSSIByBSSID.get(scanResult.BSSID) * (scanCount - 1) + scanResult.RSSI)
                            / scanCount;
                    avgRSSIByBSSID.put(scanResult.BSSID, avgRSSI);
                    scanResults.get(scanResult.BSSID).setText(avgRSSI + " dBm");
                }
                if (!ssidByBSSID.containsKey(scanResult.BSSID)
                        || !ssidByBSSID.get(scanResult.BSSID).equals(scanResult.SSID)) {
                    ssidByBSSID.put(scanResult.BSSID, scanResult.SSID);
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float stageWidth = getStage().getViewport().getWorldWidth();
        float stageHeight = getStage().getViewport().getWorldHeight();
        updateTables(stageWidth, stageHeight);
    }

    private void reset() {
        startScanning = false;
        scanCount = 0;
        elapseTime = 0;
        avgRSSIByBSSID.clear();
        scanResults.clear();
        buttonsTable.clearChildren();
        buttonsTable.add(startScanButton).size(BUTTON_SIZE.x, BUTTON_SIZE.y);
    }

    private void updateScanResultTitle(int scanCount) {
        scanResultTitle.setText("Average Wifi Levels on Count #" + scanCount + "/" + totalScanCount);
    }

    private void updateTables(float stageWidth, float stageHeight) {
        mainTable.setX(-stageWidth / 2);
        mainTable.setY(stageHeight / 2);
        mainTable.setWidth(stageWidth);
        mainTable.columnDefaults(0).width(stageWidth);
        buttonsTable.setX(-stageWidth / 2);
        buttonsTable.setY(stageHeight / 2);
        buttonsTable.setWidth(stageWidth);
    }

    private void writeResultsToFile() {
        for (Entry<String, Double> scanResult : avgRSSIByBSSID.entrySet()) {
            String BSSID = scanResult.getKey();
            fileHandle.writeString(
                "X=" + xPosition + ",Y=" + yPosition + ",BSSID=" + BSSID + ",SSID="
                        + ssidByBSSID.get(BSSID) + ",RSSI=" + scanResult.getValue() + "\n",
                true);
        }
    }

    private void showAlertDialog(String title, String content) {
        // TODO: Add table border and remove scanResultsTable debug mode
        scanResultsTable.setDebug(false);
        AlertDialog scanFinishDialog = addActorAndCenter(new AlertDialog(assets, new Runnable() {
            @Override
            public void run() {
                scanResultsTable.debug();
            }
        }));
        scanFinishDialog.setTitle(title);
        scanFinishDialog.setContent(content);
        scanFinishDialog.show();
    }
}
