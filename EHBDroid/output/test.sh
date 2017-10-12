# gen a keystore：keytool -genkey -alias qian.keystore -keyalg RSA -sigalg MD5withRSA -validity 20000 -keystore qian.keystore

jarsigner -keystore qian.key -storepass 123456 -signedjar $1/$1_1.apk -digestalg SHA1 $1/$1.apk qian.key
adb install -r $1/$1_1.apk
adb push $1/$1.apk /mnt/sdcard/$1.apk
adb push $1/$1_xmlevents.txt /mnt/sdcard/$1_xmlevents.txt

