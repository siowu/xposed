# xposed
这是一个xposed模块的模板<br>
需要在assets文件夹下的xposed_init文件中配置入口类<br>
需要在AndroidManifest.xml配置xposed模块相关信息<br>
release版本可以在build.gradle开启Minify配置压缩安装包大小<br>
用了Minify必须排除xposed的入口类，否则会找不到<br>
Android Studio编译的时候，需要在Build里面的Generate Signed Apk Bundle or APK打包的才能分享给别人安装。通过直接点运行，在提取的会有testOnly为true的属性
