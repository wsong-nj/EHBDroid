## EHBDorid

EHBDroid is an effective and efficient Android Testing tool based on the idea of "event-handler-based" testing. Please cite the following paper if you use this tool:

[Wei Song, Xiangxing Qian, and Jeff Huang. EHBDroid: Beyond GUI Testing for Android Applications. ASE 2017.](https://github.com/wsong-nj/EHBDroid/blob/master/EHBDroid.pdf)

## How to run?

### 1. Instrument App

- Git clone this repo

- Import this project into IntelliJ IDEA

- Make sure __ANDROID_SDK__ is set

- Setup

    + Put __your-apk__ under _EHBDroid/benchmark/_
    + replace _EHBDroid/src/com/app/test/AppDir#APPNAME_ to __your-apk__ name

- run _EHBDroid/src/com/app/test/Main.java_

> Note: Currently EHBDroid only supports Java.

### 2. Output

The instrumented apk will be under _EHBDroid/output/_ 

### 3. Sign and install

- Start an emulator/device 

- cd _EHBDroid/output/_, run _./test.sh(Mac or linux)_ or _test.bat(Windows)_. It will sign and install the instrumented-apk on the emulator/device.

### 4. Explore 

Exploration script is writtern by Python. 

- cd EHBScript

- sh run.sh or python run.py

#### Some issues

1. uTest, sTest and iTest on the menu represent UI event testing, service event testing and inter-app event testing.



