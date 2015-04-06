package com.longlong.wifiscanner.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.longlong.wifiscanner.dao.DAO;
import com.longlong.wifiscanner.dialog.AlertDialog;
import com.longlong.wifiscanner.model.ScanResult;
import com.longlong.wifiscanner.sound.SoundEffectEnum;
import com.longlong.wifiscanner.stage.MainStage;
import com.longlong.wifiscanner.util.Assets;

public class WifiScanner extends BaseComponent {

    // Model
    private final DAO dao;
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
    private TextField yPositionTextField;
    private Table scanResultsTable;
    private Label scanResultTitle;
    private Map<String, Label> scanResults = new HashMap<>();

    public WifiScanner(Assets assets) {
        super(assets);
        dao = assets.getDAO();
        assets.getSkin().getFont("Trebucket").setScale(0.3f);

        scanInternalTextField = new TextField(
            Integer.toString(scanIntervalSeconds),
            assets.getSkin());
        totalScanCountTextField = new TextField(Integer.toString(totalScanCount), assets.getSkin());
        xPositionTextField = new TextField("0", assets.getSkin());
        yPositionTextField = new TextField("0", assets.getSkin());

        TextButton startScanButton = new TextButton("Start", assets.getSkin(), "green");
        startScanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    scanIntervalSeconds = Integer.valueOf(scanInternalTextField.getText());
                    totalScanCount = Integer.valueOf(totalScanCountTextField.getText());
                } catch (NumberFormatException e) {
                    showAlertDialog("Error", "Scan Interval and Scan Count must be Integer.");
                    return;
                }
                startScanning = true;
            }
        });
        startScanButton.getLabel().setFontScale(0.5f);
        TextButton cancelScanButton = new TextButton("Cancel", assets.getSkin(), "green");
        cancelScanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reset();
                scanResultsTable.clearChildren();
            }
        });
        cancelScanButton.getLabel().setFontScale(0.5f);

        configTable = new Table(assets.getSkin());
        configTable.add("Scan Interval(s) : ");
        configTable.add(scanInternalTextField);
        configTable.add("X : ");
        configTable.add(xPositionTextField);
        configTable.add(startScanButton).padLeft(20);
        configTable.row();
        configTable.add("Scan Count : ");
        configTable.add(totalScanCountTextField);
        configTable.add("Y : ");
        configTable.add(yPositionTextField);
        configTable.add(cancelScanButton).padLeft(20);
        configTable.top().left();

        scanResultTitle = new Label("", assets.getSkin());

        scanResultsTable = new Table(assets.getSkin());
        scanResultsTable.top().left();
        scanResultsTable.debug();

        mainTable = addActorAndCenter(new Table());
        mainTable.pad(20);
        mainTable.columnDefaults(0).expandX();
        mainTable.add(configTable);
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
            showAlertDialog("Complete", "Wifi Scan Finished");
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
    }

    private void updateScanResultTitle(int scanCount) {
        scanResultTitle.setText("Average Wifi Levels on Count #" + scanCount + "/" + totalScanCount);
    }

    private void updateTables(float stageWidth, float stageHeight) {
        mainTable.setX(-stageWidth / 2);
        mainTable.setY(stageHeight / 2);
        mainTable.setWidth(stageWidth);
        mainTable.columnDefaults(0).width(stageWidth);
    }

    private void writeResultsToFile() {
        String x = xPositionTextField.getText();
        String y = yPositionTextField.getText();
        for (Entry<String, Double> scanResult : avgRSSIByBSSID.entrySet()) {
            String BSSID = scanResult.getKey();
            dao.put("X=" + x + ",Y=" + y + ",BSSID=" + BSSID, "SSID=" + ssidByBSSID.get(BSSID)
                    + ",RSSI=" + scanResult.getValue());
        }
    }

    private void showAlertDialog(String title, String content) {
        AlertDialog scanFinishDialog = addActorAndCenter(new AlertDialog(assets));
        scanFinishDialog.setTitle(title);
        scanFinishDialog.setContent(content);
        scanFinishDialog.show();
    }
}
