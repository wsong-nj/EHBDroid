## How to run EHBDroid?

### 1. Run EHBDroid/Instrument App

- Git clone this repo/project

- Import this project into IntelliJ IDEA

- Make sure __ANDROID_SDK__ is set

- Setup

    + Put __your-apk__ under _EHBDroid/benchmark/_
    + replace _EHBDroid/src/com/app/test/AppDir#APPNAME_ to __your-apk__ name

- run _EHBDroid/src/com/app/test/Main.java_

### 2. Output

The instrumented apk will be under _EHBDroid/output/_

### 3. Sign and install

- Start an emulator/device

- cd _EHBDroid/output/_, run _./test.sh(Unix)_ or _test.bat(Window)_. It will sign and install the instrumented-apk on the emulator/device.

### 4. Explore

There are two ways to play with EHBDroid.

__Way 1 -- Manually__

- start app

- click menu

- click uTest

__Way 2 -- EHBScript__

- cd EHBScript

- sh run.sh or python run.py

#### Some issues

1. uTest, sTest and iTest on the menu represent UI event testing, service event testing and inter-app event testing.


