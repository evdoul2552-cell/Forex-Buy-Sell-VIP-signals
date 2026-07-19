package com.aurum.trader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aurum.trader.pref.SPManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkModeSwitch;
    private Switch autoTradingSwitch;
    private EditText lotSizeInput;
    private EditText riskInput;
    private Spinner languageSpinner;
    private EditText brokerInput;
    private EditText accountIdInput;
    private EditText apiKeyInput;
    private EditText apiSecretInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SPManager.INSTANCE.init(this);

        darkModeSwitch = findViewById(R.id.switch_dark_mode);
        autoTradingSwitch = findViewById(R.id.switch_auto_trading);
        lotSizeInput = findViewById(R.id.input_lot_size);
        riskInput = findViewById(R.id.input_risk_pct);
        languageSpinner = findViewById(R.id.spinner_language);
        brokerInput = findViewById(R.id.input_broker);
        accountIdInput = findViewById(R.id.input_account_id);
        apiKeyInput = findViewById(R.id.input_api_key);
        apiSecretInput = findViewById(R.id.input_api_secret);

        darkModeSwitch.setChecked(SPManager.INSTANCE.getSpBoolean("dark_mode", false));
        autoTradingSwitch.setChecked(SPManager.INSTANCE.getSpBoolean("auto_trading", false));
        lotSizeInput.setText(String.valueOf(SPManager.INSTANCE.getFloatValueFromPref("lot_size", 0.01f)));
        riskInput.setText(String.valueOf(SPManager.INSTANCE.getFloatValueFromPref("risk_pct", 1f)));
        brokerInput.setText(SPManager.INSTANCE.getStringEmptyValueFromPref("broker_name"));
        accountIdInput.setText(SPManager.INSTANCE.getStringEmptyValueFromPref("account_id"));
        apiKeyInput.setText(SPManager.INSTANCE.getStringEmptyValueFromPref("api_key"));
        apiSecretInput.setText(SPManager.INSTANCE.getStringEmptyValueFromPref("api_secret"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        Button saveButton = findViewById(R.id.btn_save_settings);
        saveButton.setOnClickListener(v -> saveSettings());

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void saveSettings() {
        SPManager.INSTANCE.saveSPBoolean("dark_mode", darkModeSwitch.isChecked());
        SPManager.INSTANCE.saveSPBoolean("auto_trading", autoTradingSwitch.isChecked());
        SPManager.INSTANCE.saveSPFloat("lot_size", Float.parseFloat(lotSizeInput.getText().toString().isEmpty() ? "0.01" : lotSizeInput.getText().toString()));
        SPManager.INSTANCE.saveSPFloat("risk_pct", Float.parseFloat(riskInput.getText().toString().isEmpty() ? "1" : riskInput.getText().toString()));
        SPManager.INSTANCE.saveSPString("broker_name", brokerInput.getText().toString());
        SPManager.INSTANCE.saveSPString("account_id", accountIdInput.getText().toString());
        SPManager.INSTANCE.saveSPString("api_key", apiKeyInput.getText().toString());
        SPManager.INSTANCE.saveSPString("api_secret", apiSecretInput.getText().toString());

        AppCompatDelegate.setDefaultNightMode(darkModeSwitch.isChecked() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
