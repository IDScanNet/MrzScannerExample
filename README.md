# ScanMrz Library

## Setup

1. Add **idscan-public** maven repository to the project **build.gradle** file.
```
allprojects {
    repositories {
        ...
        maven {
            url 'https://www.myget.org/F/idscan-public/maven/'
        }
        ...
    }
}```


2. Add the following to the module **build.gradle** file:
```
dependencies {
    ...
    implementation 'net.idscan.components.android:scanmrz:1.0.0'
    ...
}
```

## Using

For scanning you need to call ```MrzScanActivity```:

```
Intent i = new Intent(MainActivity.this, MrzScanActivity.class);
i.putExtra(MrzScanActivity.EXTRA_LICENSE_KEY, ** LICENSE KEY **);
startActivityForResult(i, SCAN_ACTIVITY_CODE);
```
**Note** need to replace ```** LICENSE KEY **``` by your **License Key**.

To process the result you need to override ```onActivityResult()``` of your Activity.

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if (requestCode == SCAN_ACTIVITY_CODE) {
    switch (resultCode) {
      case MrzScanActivity.RESULT_OK:
        if (data != null) {
          MrzScanActivity.MRZData result =
                  data.getParcelableExtra(MrzScanActivity.DOCUMENT_DATA);
          // TODO: Handle the data.
        }
        break;

      case MrzScanActivity.ERROR_RECOGNITION:
        if (data != null) {
          String desc =
                   data.getStringExtra(MrzScanActivity.ERROR_DESCRIPTION);
          // TODO: Handle the error.
        }
        break;

      case MrzScanActivity.ERROR_INVALID_CAMERA_NUMBER:
      case MrzScanActivity.ERROR_CAMERA_NOT_AVAILABLE:
      case MrzScanActivity.ERROR_INVALID_CAMERA_ACCESS:
      case MrzScanActivity.RESULT_CANCELED:
        // TODO: Handle the error.
        break;
    }
  }
}```

#### Error codes:

* ```ERROR_RECOGNITION``` internal error.

* ```ERROR_CAMERA_NOT_AVAILABLE``` device has no camera.

* ```ERROR_INVALID_CAMERA_NUMBER``` invalid camera number is selected.

* ```ERROR_INVALID_CAMERA_ACCESS``` application cannot access the camera. For example, camera can be captured by the other application or application has no permission to use the camera.

* ```ERROR_INVALID_LICENSE_KEY``` **License Key** is invalid.

## Customization

For customization **scanning activity** you need to extend MrzScanActivity and override some methods.

#### Custom Viewfinder

The **scanning activity** has the following structure:
![Structure of Scanning View](/images/scan_view_structure.png)

By default, **Viewfinder** layer is a simple view with a frame. You can replace it with a custom view. For that you need to override ```getViewFinder(LayoutInflater inflater)``` method. Also, you can add any views to **Viewfinder** layer.
```
@Override
protected View getViewFinder(LayoutInflater inflater) {
  View v = inflater.inflate(R.layout.custom_viewfinder, null);

  // TODO: setup view.

  return v;
}
```

**Note** **Viewfinder** layer is drawn as an overlay above the **camera preview** layer, so it should has a transparent background color.

#### Select camera

You have two ways to select active camera in the **scanning activity**:

1. You can override ```selectCamera(int numberOfCameras)``` method and return the number of desired camera.
```
@Override
protected int selectCamera(int numberOfCameras) {
    // TODO: Return number of camera in range [0, numberOfCameras).
}
```
2. You can call ```setCamera(int id)``` method to change the current active camera.


#### Handle scanned data

By default, when MRZ is recognized it returns via ```onActivityResult``` method. But you can change this behavior by overriding ```onData(PDF4MRZData17Result result)``` method. That is default implementation of this method:
```
protected void onData(@NonNull MRZData result) {
  finish(result);
}
```
But you can process scanned data in a different way. For example, you can display MRZ on **Viewfiender** layer. Also you don't have to return the result immediately. Instead of, you can return scanned data at any time in future by calling ```void finish(MRZData result)``` method.

#### Flashlight

You have two ways to control flashlight:

1. You can call **scanning activity** with ```EXTRA_FLASH_STATE``` parameter with ```true``` value and if flashlight is available it will be turned on.
```
Intent i = new Intent(MainActivity.this, MrzScanActivity.class);
i.putExtra(MrzScanActivity.EXTRA_LICENSE_KEY, LIC_KEY);
i.putExtra(MrzScanActivity.EXTRA_FLASH_STATE, true);
startActivityForResult(i, SCAN_ACTIVITY_CODE);
```
2. You can change state of the flashlight by calling ```setFlashState(booelan state)```.
```
public class CustomScanActivity extends MrzScanActivity {
    ...
    void switchFlashlight() {
        setFlashState(!getFlashState());
    }
    ...
}
```
