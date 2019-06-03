/*
 * Copyright (c) 2017 IDScan.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Support: support@idscan.net
 */

package net.idscan.example.android.mrzscannerexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import net.idscan.components.android.scanmrz.MrzScanActivity;
import net.idscan.components.android.scanmrz.Version;

public class MainActivity extends AppCompatActivity {
    private final static int SCAN_ACTIVITY_CODE = 0x001;
    private final static int REQUEST_CAMERA_PERMISSIONS_DEFAULT = 0x100;
    private final static int REQUEST_CAMERA_PERMISSIONS_CUSTOM = 0x101;

    // Valid key.
    private final static String LIC_KEY = "HnbRlGg5dNGMR8R0q35waR9GBLERp0u0I2nqElr+roc5W3+72TagPH6udPA0zC/h3H73r4gNOfWh3JNzdOYQWl3a9aO1n1lUZssy2vQbaRsW85QcUEHIjDT/zj1ube0XNEmeTv91HVHKhtDaCtqUenxUrGfWz/ucKu7XbafRIuA=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.tv_result)).setMovementMethod(new ScrollingMovementMethod());

        ((TextView) findViewById(R.id.tv_version)).setText("Version: " + Version.getVersion());

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDefaultScanView();
            }
        });

        findViewById(R.id.btn_scan_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomScanView();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSIONS_DEFAULT:
                if (checkCameraPermissions()) {
                    showDefaultScanView();
                }
                break;

            case REQUEST_CAMERA_PERMISSIONS_CUSTOM:
                if (checkCameraPermissions()) {
                    showCustomScanView();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_ACTIVITY_CODE) {
            TextView tv_result = findViewById(R.id.tv_result);

            switch (resultCode) {
                case MrzScanActivity.RESULT_OK:
                    if (data != null) {
                        MrzScanActivity.MRZData result = data.getParcelableExtra(MrzScanActivity.DOCUMENT_DATA);
                        if (result != null) {
                            StringBuilder text = new StringBuilder();
                            for (MrzScanActivity.MRZField f : result.fields.values()) {
                                switch (f.type) {
                                    case DocumentType:
                                        text.append("DocumentType: ").append(f.value).append("\n");
                                        break;
                                    case FullName:
                                        text.append("FullName: ").append(f.value).append("\n");
                                        break;
                                    case LastName:
                                        text.append("LastName: ").append(f.value).append("\n");
                                        break;
                                    case FirstName:
                                        text.append("FirstName: ").append(f.value).append("\n");
                                        break;
                                    case Dob:
                                        text.append("Dob: ").append(f.value).append("\n");
                                        break;
                                    case Exp:
                                        text.append("Exp: ").append(f.value).append("\n");
                                        break;
                                    case DocumentNumber:
                                        text.append("DocumentNumber: ").append(f.value).append("\n");
                                        break;
                                    case Gender:
                                        text.append("Gender: ").append(f.value).append("\n");
                                        break;
                                    case IssuingState:
                                        text.append("IssuingState: ").append(f.value).append("\n");
                                        break;
                                    case Nationality:
                                        text.append("Nationality: ").append(f.value).append("\n");
                                        break;
                                    case Line1:
                                        text.append("Line1: ").append(f.value).append("\n");
                                        break;
                                    case Line2:
                                        text.append("Line2: ").append(f.value).append("\n");
                                        break;
                                    case Line3:
                                        text.append("Line3: ").append(f.value).append("\n");
                                        break;
                                }
                            }

                            tv_result.setText(text.toString());
                        }
                    }
                    break;

                case MrzScanActivity.ERROR_RECOGNITION:
                    tv_result.setText(data.getStringExtra(MrzScanActivity.ERROR_DESCRIPTION));
                    break;

                case MrzScanActivity.ERROR_INVALID_CAMERA_NUMBER:
                    tv_result.setText("Invalid camera number.");
                    break;

                case MrzScanActivity.ERROR_CAMERA_NOT_AVAILABLE:
                    tv_result.setText("Camera not available.");
                    break;

                case MrzScanActivity.ERROR_INVALID_CAMERA_ACCESS:
                    tv_result.setText("Invalid camera access.");
                    break;

                case MrzScanActivity.RESULT_CANCELED:
                    break;

                default:
                    tv_result.setText("Undefined error.");
                    break;
            }
        }
    }

    private void showDefaultScanView() {
        if (checkCameraPermissions()) {
            Intent i = new Intent(MainActivity.this, MrzScanActivity.class);
            i.putExtra(MrzScanActivity.EXTRA_LICENSE_KEY, LIC_KEY);
            startActivityForResult(i, SCAN_ACTIVITY_CODE);
        } else {
            requestCameraPermissions(REQUEST_CAMERA_PERMISSIONS_DEFAULT);
        }
    }

    private void showCustomScanView() {
        if (checkCameraPermissions()) {
            Intent i = new Intent(MainActivity.this, CustomScanActivity.class);
            i.putExtra(MrzScanActivity.EXTRA_LICENSE_KEY, LIC_KEY);
            startActivityForResult(i, SCAN_ACTIVITY_CODE);
        } else {
            requestCameraPermissions(REQUEST_CAMERA_PERMISSIONS_CUSTOM);
        }
    }

    private boolean checkCameraPermissions() {
        int status = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return (status == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermissions(int requestCode) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                requestCode);
    }
}
