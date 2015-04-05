package com.longlong.wifiscanner.component;

import java.util.HashMap;
import java.util.Map;

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
import com.longlong.wifiscanner.stage.MainStage;
import com.longlong.wifiscanner.util.Assets;

public class WifiScanner extends BaseComponent {

    private static final int SCAN_INTERVAL_SECONDS = 1;
    private static final int TOTAL_SCAN_COUNT = 10;

    // Model
    private final DAO dao;
    private boolean startScanning = false;
    private float elapseTime = 0;
    private int scanCount = 0;
    private Map<String, Double> avgRSSIByBSSID = new HashMap<>();

    // View
    private Table mainTable;
    private Table positionTable;
    private TextField xPositionTextField;
    private TextField yPositionTextField;
    private TextButton startScanButton;
    private Table scanResultsTable;
    private Label scanResultTitle;
    private Map<String, Label> scanResults = new HashMap<>();

    public WifiScanner(Assets assets) {
        super(assets);
        dao = assets.getDAO();
        assets.getSkin().getFont("Trebucket").setScale(0.3f);

        xPositionTextField = new TextField("", assets.getSkin());
        yPositionTextField = new TextField("", assets.getSkin());

        startScanButton = new TextButton("Start", assets.getSkin(), "green");
        startScanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startScanning = true;
                Gdx.app.log("WifiScanner", "startScanButton clicked");
            }
        });
        startScanButton.getLabel().setFontScale(0.5f);

        positionTable = new Table(assets.getSkin());
        positionTable.add("X : ");
        positionTable.add(xPositionTextField);
        positionTable.add("Y : ");
        positionTable.add(yPositionTextField);
        positionTable.add(startScanButton).padLeft(20);
        positionTable.top().left();

        scanResultTitle = new Label("", assets.getSkin());

        scanResultsTable = new Table(assets.getSkin());
        scanResultsTable.top().left();

        mainTable = addActorAndCenter(new Table());
        mainTable.pad(20);
        mainTable.columnDefaults(0).expandX();
        mainTable.add(positionTable);
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
        elapseTime += delta;
        if (elapseTime > SCAN_INTERVAL_SECONDS) {
            Gdx.app.log("WifiScanner", "Start Scanning WIFI!");
            elapseTime = 0;
            scanCount++;
            if (scanCount > TOTAL_SCAN_COUNT) {
                startScanning = false;
                scanCount = 0;
                avgRSSIByBSSID.clear();
                scanResults.clear();
                // write to DAO
                AlertDialog scanFinishDialog = addActorAndCenter(new AlertDialog(assets));
                scanFinishDialog.setTitle("Complete");
                scanFinishDialog.setContent("Wifi Scan Finished");
                scanFinishDialog.show();
                return;
            }
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

    private void updateScanResultTitle(int scanCount) {
        scanResultTitle.setText("Average Wifi Levels on Count #" + scanCount + "/"
                + TOTAL_SCAN_COUNT);
    }

    private void updateTables(float stageWidth, float stageHeight) {
        mainTable.setX(-stageWidth / 2);
        mainTable.setY(stageHeight / 2);
        mainTable.setWidth(stageWidth);
        mainTable.columnDefaults(0).width(stageWidth);
    }
}
