# react-native-android-installed-apps-categories

This has been built off of "react-native-android-installed-apps-unblocking".
This version also provides methods that return the App category from the play store. Additionally it provides methods to return apps specifically visible to the app drawer.

## Getting started

`$ npm install react-native-android-installed-apps-categories`

### Mostly automatic installation

`$ npx react-native link react-native-android-installed-apps-categories`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import com.androidinstalledapps.RNAndroidInstalledAppsPackage;` to the imports at the top of the file
- Add `new RNAndroidInstalledAppsPackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:
   ```
   include ':react-native-android-installed-apps-categories'
   project(':react-native-android-installed-apps-categories').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-android-installed-apps/android')
   ```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
   ```
     compile project(':react-native-android-installed-apps-categories')
   ```

## Methods

#### 1 - getApps()

#### 2 - getNonSystemApps()

#### 3 - getSystemApps()

#### 4 - getNonsystemAppsCats()

#### 5 - getAppDrawerApps()

#### 6 - getAppDrawerAppsCats()

#### 7 - handleCategoryIndividual()

## Return Result

# 1-6

- packageName
- versionName
- versionCode
- firstInstallTime
- lastUpdateTime
- appName
- icon // Base64
- apkDir
- size // Bytes
- category (only available in method #4 & #6 )

## 7

- category

## Usage

```javascript
import RNAndroidInstalledApps from 'react-native-android-installed-apps-categories'

RNAndroidInstalledApps.getApps()
	.then((apps) => {
		this.setState({ apps })
	})
	.catch((error) => {
		alert(error)
	})

RNAndroidInstalledApps.handleCategoryIndividual('packageName')
	.then((apps) => {
		this.setState({ apps })
	})
	.catch((error) => {
		alert(error)
	})
```
