
package com.androidinstalledapps;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;


import javax.annotation.Nullable;

import com.helper.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class RNAndroidInstalledAppsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNAndroidInstalledAppsModule(final ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNAndroidInstalledApps";
  }

  @ReactMethod
  public void getApps(final Promise promise) {
    class OneShotTask implements Runnable {
      private final ReactApplicationContext reactContext;

      OneShotTask(final ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
      }
      public void run() {
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          final List<PackageInfo> pList = pm.getInstalledPackages(0);
          final WritableArray list = Arguments.createArray();
          for (int i = 0; i < pList.size(); i++) {
            final PackageInfo packageInfo = pList.get(i);
            final WritableMap appInfo = Arguments.createMap();
            appInfo.putString("packageName", packageInfo.packageName);
            appInfo.putString("versionName", packageInfo.versionName);
            appInfo.putDouble("versionCode", packageInfo.versionCode);
            appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
            appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
            appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());
            appInfo.putDouble("systemApp", (ApplicationInfo.FLAG_SYSTEM));
            appInfo.putDouble("systemApp2", (packageInfo.applicationInfo.flags));

            final Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
            appInfo.putString("icon", Utility.convert(icon));

            final String apkDir = packageInfo.applicationInfo.publicSourceDir;
            appInfo.putString("apkDir", apkDir);

            final File file = new File(apkDir);
            final double size = file.length();
            appInfo.putDouble("size", size);

            list.pushMap(appInfo);
          }
          promise.resolve(list);
        } catch (final Exception ex) {
          promise.reject(ex);
        }
      }
    }
    Thread t = new Thread(new OneShotTask(this.reactContext));
    t.start();
  }

  @ReactMethod
  public void getNonSystemApps(final Promise promise) {
    class OneShotTask implements Runnable {
      private final ReactApplicationContext reactContext;

      OneShotTask(final ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
      }

      public void run() {
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          final List<PackageInfo> pList = pm.getInstalledPackages(0);
          final WritableArray list = Arguments.createArray();
          for (int i = 0; i < pList.size(); i++) {
            final PackageInfo packageInfo = pList.get(i);
            final WritableMap appInfo = Arguments.createMap();
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
              appInfo.putString("packageName", packageInfo.packageName);
              appInfo.putString("versionName", packageInfo.versionName);
              appInfo.putDouble("versionCode", packageInfo.versionCode);
              appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
              appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
              appInfo.putDouble("systemApp", (ApplicationInfo.FLAG_SYSTEM));

              appInfo.putDouble("systemApp2", (packageInfo.applicationInfo.flags));
              appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

              final Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
              appInfo.putString("icon", Utility.convert(icon));

              final String apkDir = packageInfo.applicationInfo.publicSourceDir;
              appInfo.putString("apkDir", apkDir);

              final File file = new File(apkDir);
              final double size = file.length();
              appInfo.putDouble("size", size);

              list.pushMap(appInfo);
            }
          }
          promise.resolve(list);
        } catch (final Exception ex) {
          promise.reject(ex);
        }

      }
    }
    Thread t = new Thread(new OneShotTask(this.reactContext));
    t.start();

  }

    @ReactMethod
  public void getAppDrawerApps(final Promise promise) {
    class OneShotTask implements Runnable {
      private final ReactApplicationContext reactContext;

      OneShotTask(final ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
      }
      public void run() {
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          final Intent intent = new Intent(Intent.ACTION_MAIN, null);
          intent.addCategory(Intent.CATEGORY_LAUNCHER);
          List<ResolveInfo> pList = pm.queryIntentActivities(intent, 0);
          final WritableArray list = Arguments.createArray();
          for (int i = 0; i < pList.size(); i++) {
            final ResolveInfo resolveInfo = pList.get(i);
            final PackageInfo packageInfo = pm.getPackageInfo(resolveInfo.activityInfo.packageName, pm.GET_ACTIVITIES);
            final WritableMap appInfo = Arguments.createMap();
              appInfo.putString("packageName", resolveInfo.activityInfo.packageName);
              appInfo.putString("versionName", packageInfo.versionName);
              appInfo.putDouble("versionCode", packageInfo.versionCode);
              appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
              appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
  

              appInfo.putString("appName", ((String) resolveInfo.loadLabel(pm)).trim());

              final Drawable icon = resolveInfo.loadIcon(pm);
              appInfo.putString("icon", Utility.convert(icon));

              final String apkDir = packageInfo.applicationInfo.publicSourceDir;
              appInfo.putString("apkDir", apkDir);

              final File file = new File(apkDir);
              final double size = file.length();
              appInfo.putDouble("size", size);

              list.pushMap(appInfo);
          }
          promise.resolve(list);
        } catch (final Exception ex) {
          promise.reject(ex);
        }

      }
    }
    Thread t = new Thread(new OneShotTask(this.reactContext));
    t.start();
  }

      @ReactMethod
  public void getAppDrawerAppsCats(final Promise promise) {
    class OneShotTask implements Runnable {
      private final ReactApplicationContext reactContext;

      OneShotTask(final ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
      }
      public void run() {
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          final Intent intent = new Intent(Intent.ACTION_MAIN, null);
          intent.addCategory(Intent.CATEGORY_LAUNCHER);
          List<ResolveInfo> pList = pm.queryIntentActivities(intent, 0);
          final WritableArray list = Arguments.createArray();
          for (int i = 0; i < pList.size(); i++) {
            final ResolveInfo resolveInfo = pList.get(i);
            final PackageInfo packageInfo = pm.getPackageInfo(resolveInfo.activityInfo.packageName, pm.GET_ACTIVITIES);
            final WritableMap appInfo = Arguments.createMap();
              String packageName = resolveInfo.activityInfo.packageName;
              String category = handleCategory((String) packageName);
              appInfo.putString("category", category);
              appInfo.putString("packageName", resolveInfo.activityInfo.packageName);
              appInfo.putString("versionName", packageInfo.versionName);
              appInfo.putDouble("versionCode", packageInfo.versionCode);
              appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
              appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
  

              appInfo.putString("appName", ((String) resolveInfo.loadLabel(pm)).trim());

              final Drawable icon = resolveInfo.loadIcon(pm);
              appInfo.putString("icon", Utility.convert(icon));

              final String apkDir = packageInfo.applicationInfo.publicSourceDir;
              appInfo.putString("apkDir", apkDir);

              final File file = new File(apkDir);
              final double size = file.length();
              appInfo.putDouble("size", size);

              list.pushMap(appInfo);
          }
          promise.resolve(list);
        } catch (final Exception ex) {
          promise.reject(ex);
        }

      }
    }
    Thread t = new Thread(new OneShotTask(this.reactContext));
    t.start();
  }

  @ReactMethod
  public void getNonsystemAppsCats(final Promise promise) {
    class OneShotTask implements Runnable {
      private final ReactApplicationContext reactContext;

      OneShotTask(final ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
      }

      public void run() {
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          final List<PackageInfo> pList = pm.getInstalledPackages(0);
          final WritableArray list = Arguments.createArray();
          for (int i = 0; i < pList.size(); i++) {
            final PackageInfo packageInfo = pList.get(i);
            final WritableMap appInfo = Arguments.createMap();
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
              String packageName = packageInfo.packageName;
              String category = handleCategory((String) packageName);
              appInfo.putString("category", category);
              appInfo.putString("packageName", packageInfo.packageName);
              appInfo.putString("versionName", packageInfo.versionName);
              appInfo.putDouble("versionCode", packageInfo.versionCode);
              appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
              appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
              appInfo.putDouble("systemApp", (ApplicationInfo.FLAG_SYSTEM));

              appInfo.putDouble("systemApp2", (packageInfo.applicationInfo.flags));
              appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

              final Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
              appInfo.putString("icon", Utility.convert(icon));

              final String apkDir = packageInfo.applicationInfo.publicSourceDir;
              appInfo.putString("apkDir", apkDir);

              final File file = new File(apkDir);
              final double size = file.length();
              appInfo.putDouble("size", size);

              list.pushMap(appInfo);
            }
          }
          promise.resolve(list);
        } catch (final Exception ex) {
          promise.reject(ex);
        }

      }
    }
    Thread t = new Thread(new OneShotTask(this.reactContext));
    t.start();

  }

  @ReactMethod
  public void getSystemApps(final Promise promise) {
    class OneShotTask implements Runnable {
      private final ReactApplicationContext reactContext;

      OneShotTask(final ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
      }
      public void run(){
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          final List<PackageInfo> pList = pm.getInstalledPackages(0);
          final WritableArray list = Arguments.createArray();
          for (int i = 0; i < pList.size(); i++) {
            final PackageInfo packageInfo = pList.get(i);
            final WritableMap appInfo = Arguments.createMap();
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
              appInfo.putString("packageName", packageInfo.packageName);
              appInfo.putString("versionName", packageInfo.versionName);
              appInfo.putDouble("versionCode", packageInfo.versionCode);
              appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
              appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
              appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

              final Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
              appInfo.putString("icon", Utility.convert(icon));

              final String apkDir = packageInfo.applicationInfo.publicSourceDir;
              appInfo.putString("apkDir", apkDir);

              final File file = new File(apkDir);
              final double size = file.length();
              appInfo.putDouble("size", size);

              list.pushMap(appInfo);
            }
          }
          promise.resolve(list);
        } catch (final Exception ex) {
          promise.reject(ex);
        }
      }
    }
  }


  @ReactMethod
  public void handleCategoryIndividual(String appName, final Promise promise) {
        try {
          final PackageManager pm = this.reactContext.getPackageManager();
          String GOOGLE_URL = "https://play.google.com/store/apps/details?id=";
          String query_url = GOOGLE_URL + appName;
          String category = getCategory(query_url);
          promise.resolve(category);
        } catch (final Exception ex) {
          promise.reject(ex);
        }

  }


  @ReactMethod
  public String handleCategory(String appName) {
      try {
          final PackageManager pm = this.reactContext.getPackageManager();
          String GOOGLE_URL = "https://play.google.com/store/apps/details?id=";
          String query_url = GOOGLE_URL + appName;
          String category = getCategory(query_url);
          return category;
        } catch (Exception e) {
            return "Unable to find category";
        }
    }

  @ReactMethod
  private String getCategory(String query_url) {
    try {
        Document doc = Jsoup.connect(query_url).get();
        Element link = doc.select("a[class=\"hrTbp R8zArc\"]").last();
        return link.text();
    } catch (Exception e) {
        return "N/A";
    }
  } 

}    
