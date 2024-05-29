# @owowagency/react-native-tuya

This is a fork of [TuyaInc/tuyasmart-home-sdk-react-native](https://github.com/TuyaInc/tuyasmart-home-sdk-react-native), fixing a lot of issues we came across and a better install guide. It also uses TypeScript. We use it currently in multiple projects for clients and it is stable.

Some features are not implemented, feel free to send a PR for those missing features. Unfortunately there is no demo yet

## Feature Overview

Tuya Smart APP SDK provides the interface package for the communication with hardware and Tuya Cloud to accelerate the application development process, including the following features:

Hardware functions (network configuration, control, status reporting, regular tasks, groups, firmware upgrades, sharing)
Account system (phone number, email registration, login, password reset and other general account functions)
Tuya Cloud HTTP API interface package

## Getting started

```
npm install @owowagency/react-native-tuya
```

This library contains native code which is automatically linked in React Native >= 0.59. For iOS, run `cd ios && pod install`.

## Installation

In the Tuya development environment create a new app and make sure you have an "App key", "App secret" and "Secure image". [Read how to do this](https://tuyainc.github.io/tuyasmart_home_ios_sdk_doc/en/resource/Preparation.html).

### iOS

Put the secure image into the root path of your project as [explained here](https://tuyainc.github.io/tuyasmart_home_ios_sdk_doc/en/resource/Preparation.html).

In `ios/AppDelegate.m`, add the following import;

```obj-c
#import <ThingSmartHomeKit/ThingSmartKit.h>
```

Then, under the `roootView.backgroundColor` line in the same file, add this:

```obj-c
  #ifdef DEBUG
    [[ThingSmartSDK sharedInstance] setDebugMode:YES];
  #endif

  [[ThingSmartSDK sharedInstance] startWithAppKey:@"xxx" secretKey:@"xxx"];
```

Now replace the `xxx` with your app key and secret key.

### Android

Assuming you already have created an app in the Tuya development environment (otherwise follow the iOS steps before this), follow [these steps](https://tuyainc.github.io/tuyasmart_home_android_sdk_doc/en/resource/Integrated.html#3-integrated-security-image). You should now have an app key, app secret and security image for Android. Make sure the security image is put in `android/src/main/assets/t_s.bmp`.

Open your `AndroidManifest.xml` and put the following **in the `<application>` tag**:

```xml
<meta-data
  android:name="THING_SMART_APPKEY"
  android:value="xxx" />
<meta-data
  android:name="THING_SMART_SECRET"
  android:value="xxx" />
```

Replace the `xxx` with your app key and secret key.

Now open `MainApplication.java` and add the following import to the top:

```java
import com.tuya.smart.rnsdk.core.TuyaCoreModule;
```

Change the `onCreate` function to look like this:

```java
@Override
public void onCreate() {
  super.onCreate();
  SoLoader.init(this, /* native exopackage */ false);
  initializeFlipper(this); // Remove this line if you don't want Flipper enabled
  TuyaCoreModule.Companion.initTuyaSDKWithoutOptions(this);
}
```

Now you can try to build, but you'll probably run into an error saying that it can't choose between `libc++_shared` or something. One fix for this (don't know if it's the best fix) is to open `android/app/build.gradle` and add this;

```
android {
    ...
    packagingOptions {
        pickFirst '**/armeabi-v7a/libc++_shared.so'
        pickFirst '**/x86/libc++_shared.so'
        pickFirst '**/arm64-v8a/libc++_shared.so'
        pickFirst '**/x86_64/libc++_shared.so'
        pickFirst '**/x86/libjsc.so'
        pickFirst '**/armeabi-v7a/libjsc.so'
    }
}
```

## Usage

Now you can actually use the methods in this package. Unfortunately I don't have time to document them all, so it is advised to read the source code, but here's a start.

To login with an existing account:

```js
import { loginWithEmail } from '@owowagency/react-native-tuya';

await loginWithEmail({
  countryCode: '+1',
  email: 'you@example.com',
  password: 'testtest'
});
```

To register a new account you first need to validate the email address. And then actually register using the code in the email.

```js
import { getRegisterEmailValidateCode, registerAccountWithEmail } from '@owowagency/react-native-tuya';

await getRegisterEmailValidateCode({
  countryCode: '+1',
  email: 'you@example.com'
});

...

await registerAccountWithEmail({
  countryCode: '+1',
  email: 'you@example.com',
  password: 'testtest',
  validateCode: 'xxxxxx'
})
```

To get the currently logged in user:

```js
import { getCurrentUser } from '@owowagency/react-native-tuya';

const user = await getCurrentUser();
```

## Local Development

### `yarn start`

Runs the project in development/watch mode. Your project will be rebuilt upon changes. TSDX has a special logger for you convenience. Error messages are pretty printed and formatted for compatibility VS Code's Problems tab.

<img src="https://user-images.githubusercontent.com/4060187/52168303-574d3a00-26f6-11e9-9f3b-71dbec9ebfcb.gif" width="600" />

Your library will be rebuilt if you make edits.

### `yarn build`

Bundles the package to the `dist` folder.
The package is optimized and bundled with Rollup into multiple formats (CommonJS, UMD, and ES Module).

<img src="https://user-images.githubusercontent.com/4060187/52168322-a98e5b00-26f6-11e9-8cf6-222d716b75ef.gif" width="600" />
