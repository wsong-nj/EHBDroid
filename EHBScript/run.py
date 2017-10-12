import os
os.popen('adb push EHBScript.jar /data/local/tmp/')
os.popen('adb shell uiautomator runtest EHBScript.jar -c TraverseActivity')
