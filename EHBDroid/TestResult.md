# EHBDroid插装结果

## 成功

1. 开卷有益
2. QQ音乐
3. 东方头条
4. 腾讯动漫
5. 网易云漫画
6. 网易云阅
7. 腾讯视频

## 失败

1. 今日头条 v2签名

```
Failed to install 今日头条/今日头条_1.apk: Failure [INSTALL_PARSE_FAILED_NO_CERTIFICATES: Failed to collect certificates from /data/app/vmdl134972144.tmp/base.apk: /data/134972144.tmp/base.apk: File classes4.dex in manifest does not exist]
```

2. 爱回收 v2签名

```
Failed to install 爱回收/爱回收_1.apk: Failure [INSTALL_PARSE_FAILED_NO_CERTIFICATES: Failed to collect certificates from /data/app/vmdl1960437559.tmp/base.apk: /data/app960437559.tmp/base.apk: File assets/rocoo.dex in manifest does not exist]
```

3. QQ安全中心 签名错误

```
12-24 11:35:12.558 19377 19377 E AndroidRuntime: java.lang.NoClassDefFoundError: oicq.wlogin_sdk.request.WtloginHelper
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.t.<init>(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.t.a(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.ui.WtLoginAccountInput.onCreate(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.Activity.performCreate(Activity.java:6751)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1119)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2660)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2768)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.ActivityThread.-wrap12(ActivityThread.java)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1515)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.os.Handler.dispatchMessage(Handler.java:102)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.os.Looper.loop(Looper.java:160)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at android.app.ActivityThread.main(ActivityThread.java:6252)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at java.lang.reflect.Method.invoke(Native Method)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:895)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:785)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: Caused by: java.lang.VerifyError: Verifier rejected class oicq.wlogin_sdk.request.WtloginHelper: byte[] oicq.wlogin_sdk.request.WtloginHelper.GetTicketSigKey(oicq.wlogin_sdk.request.WUserSigInfo, int) failed to verify: byte[] oicq.wlogin_sdk.request.WtloginHelper.GetTicketSigKey(oicq.wlogin_sdk.request.WUserSigInfo, int): [0x1F] rejecting non-direct call to constructor void java.lang.NullPointerException.<init>(java.lang.String) (declaration of 'oicq.wlogin_sdk.request.WtloginHelper' appears in /data/app/com.tencent.token-1/base.apk)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.t.<init>(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.t.a(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.core.protocolcenter.protocol.ProtoDoSessionInfo.a(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.core.protocolcenter.e.c(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.core.protocolcenter.protocol.ProtoDoExchangeKey.a(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.core.protocolcenter.e.c(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.core.protocolcenter.r.a(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at com.tencent.token.core.protocolcenter.r.call(SourceFile)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at java.util.concurrent.FutureTask.run(FutureTask.java:237)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1133)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:607)
12-24 11:35:12.558 19377 19377 E AndroidRuntime: 	at java.lang.Thread.run(Thread.java:761)
12-24 11:35:13.063  1597  1630 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.miui.home/com.miui.home.launcher.Launcher/10000
```

4. 网易新闻 启动错误

```
12-24 12:05:52.650  1597  4153 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.netease.newsreader.activity/com.netease.nr.biz.ad.AdActivity/766
12-24 12:05:53.712  1597  3287 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.netease.newsreader.activity/com.netease.nr.biz.ad.AdActivity/766
12-24 12:05:54.229  1597  4153 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.netease.newsreader.activity/com.netease.nr.biz.ad.AdActivity/766
12-24 12:05:54.725  1597  2159 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.netease.newsreader.activity/com.netease.nr.biz.ad.AdActivity/766
12-24 12:05:55.252  1597  1908 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.netease.newsreader.activity/com.netease.nr.biz.ad.AdActivity/766
^[[1;2D12-24 12:05:55.749  1597  1908 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.netease.newsreader.activity/com.netease.nr.biz.ad.AdActivity/766
```

5. 腾讯新闻 签名错误

```
12-24 12:12:45.687  1597  3241 E ActivityTrigger: activityStartTrigger: not whiteListedcom.tencent.news/com.tencent.news.activity.SplashActivity/5500
12-24 12:12:45.688  1597  3241 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.tencent.news/com.tencent.news.activity.SplashActivity/5500
12-24 12:12:45.691  1597  3205 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.tencent.news/com.tencent.news.activity.SplashActivity/5500
12-24 12:12:45.837 16121 16121 E debug_tag: debug flag = false
12-24 12:12:46.088  1597  3285 E ActivityTrigger: activityStartTrigger: not whiteListedcom.tencent.news/com.tencent.news.ui.GuideActivity/5500
12-24 12:12:46.090  1597  3285 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.tencent.news/com.tencent.news.ui.GuideActivity/5500
12-24 12:12:46.116  1597  3241 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.tencent.news/com.tencent.news.ui.GuideActivity/5500
12-24 12:12:46.127  2628  2655 E octvm_power: unsupported flags[0] or no event trigger found
12-24 12:12:46.359 25374 16141 E Market-ConnectionRSA: get key exception : com.android.org.bouncycastle.util.encoders.DecoderException: unable to decode base64 string: invalid characters encountered in base64 data
12-24 12:12:47.119 16143 16143 E debug_tag: debug flag = false
12-24 12:12:47.412 16143 16413 E System  : Ignoring attempt to set property "java.net.preferIPv6Addresses" to value "false".
12-24 12:12:47.658 16121 16186 E AndroidRuntime: FATAL EXCEPTION: [RunnablePool]NoName
12-24 12:12:47.658 16121 16186 E AndroidRuntime: Process: com.tencent.news, PID: 16121
12-24 12:12:47.658 16121 16186 E AndroidRuntime: java.lang.VerifyError: Verifier rejected class oicq.wlogin_sdk.request.WtloginHelper: byte[] oicq.wlogin_sdk.request.WtloginHelper.GetTicketSigKey(oicq.wlogin_sdk.request.WUserSigInfo, int) failed to verify: byte[] oicq.wlogin_sdk.request.WtloginHelper.GetTicketSigKey(oicq.wlogin_sdk.request.WUserSigInfo, int): [0x1F] rejecting non-direct call to constructor void java.lang.NullPointerException.<init>(java.lang.String) (declaration of 'oicq.wlogin_sdk.request.WtloginHelper' appears in /data/app/com.tencent.news-1/base.apk:classes2.dex)
12-24 12:12:47.658 16121 16186 E AndroidRuntime:    at com.tencent.news.oauth.qq.h.run(WtloginManager.java)
12-24 12:12:47.658 16121 16186 E AndroidRuntime:    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1133)
12-24 12:12:47.658 16121 16186 E AndroidRuntime:    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:607)
12-24 12:12:47.658 16121 16186 E AndroidRuntime:    at com.tencent.news.task.z.run(ThreadPoolComponentProvider.java)
12-24 12:12:47.658 16121 16186 E AndroidRuntime:    at java.lang.Thread.run(Thread.java:761)
12-24 12:12:47.746  1597  4153 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.miui.home/com.miui.home.launcher.Launcher/10000
12-24 12:12:47.749  1597  2162 E ActivityTrigger: activityResumeTrigger: not whiteListedcom.miui.home/com.miui.home.launcher.Launcher/10000
12-24 12:12:47.755  1597  2162 E ActivityManager:   Invalid thumbnail dimensions: 0x0
12-24 12:12:47.884  2628  2655 E octvm_power: unsupported flags[0] or no event trigger found
```


