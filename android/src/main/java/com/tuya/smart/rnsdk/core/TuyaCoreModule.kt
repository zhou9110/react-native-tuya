package com.tuya.smart.rnsdk.core

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.facebook.react.bridge.*
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.INeedLoginListener
import com.thingclips.smart.sdk.api.IThingDataCallback
import com.tuya.smart.rnsdk.utils.Constant.API_REQUEST_ERROR
import com.tuya.smart.rnsdk.utils.Constant.NEEDLOGIN
import com.tuya.smart.rnsdk.utils.TYRCTCommonUtil
import com.tuya.smart.rnsdk.utils.TuyaReactUtils
import java.util.HashMap


class TuyaCoreModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        fun initTuyaSDk(appKey:String,appSecret:String,application: Application){
            ThingHomeSdk.init(application, appKey, appSecret)
        }

        fun initTuyaSDKWithoutOptions(application: Application){
            ThingHomeSdk.init(application)
        }

        fun setSDKDebug(open:Boolean){
            ThingHomeSdk.setDebugMode(open)
        }
    }

    override fun getName(): String {
        return "TuyaCoreModule"
    }

    /**
     *  不带参数的初始化，appKey和appSecret要写在AndroidManifest中
     */
    @ReactMethod
    @Deprecated("Android can't initSDK in react-native,it should be used in application")
    fun initWithoutOptions() {
        ThingHomeSdk.init(reactApplicationContext.applicationContext as Application?);
        ThingHomeSdk.setOnNeedLoginListener {
            TuyaReactUtils.sendEvent(reactApplicationContext, NEEDLOGIN, null)
        }
    }

    @ReactMethod
    @Deprecated("Android can't initSDK in react-native,it should be used in application")
    fun initWithOptions(params: ReadableMap) {
        val appKey = params.getString("appKey")
        val appSecret = params.getString("appSecret")
        ThingHomeSdk.init(reactApplicationContext.applicationContext as Application?, appKey, appSecret)
        ThingHomeSdk.setOnNeedLoginListener(INeedLoginListener() {
            fun onNeedLogin(context: Context?) {
                TuyaReactUtils.sendEvent(reactApplicationContext, NEEDLOGIN, null)
            }
        })
    }

    @ReactMethod
    fun setOnNeedLoginListener(){
        ThingHomeSdk.setOnNeedLoginListener(INeedLoginListener() {
            fun onNeedLogin(context: Context?) {
                TuyaReactUtils.sendEvent(reactApplicationContext, NEEDLOGIN, null)
            }
        })
    }



    @ReactMethod
    fun exitApp() {
        ThingHomeSdk.onDestroy();
    }

    @ReactMethod
    fun apiRequest(params: ReadableMap, promise: Promise) {
        val callback = object : IThingDataCallback<Any> {
            override fun onSuccess(data: Any?) {
                if (data is Boolean) {
                    Log.e("apiRequest", data.toString())
                    promise.resolve("success")
                    return
                }

                if(data is JSONArray){
                    val writableArray = TYRCTCommonUtil.parseToWritableArray(data as com.alibaba.fastjson.JSONArray)
                    promise.resolve(writableArray)
                    return
                }
                val writableMap = TYRCTCommonUtil.parseToWritableMap(data)
                promise.resolve(writableMap)

            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                promise.reject(errorCode, errorMsg)
            }

        }

//        val withoutSession = params.getBoolean("withoutSession")
        val withoutSession = false
        val apiName = params.getString("apiName")
        val apiVersion = params.getString("version")
        val postData = TYRCTCommonUtil.parseToMap(params.getMap("postData"))
        if (TextUtils.isEmpty(apiName)) {
            promise.reject(API_REQUEST_ERROR, "ApiName is empty")
            return
        }

        if (withoutSession) {
            ThingHomeSdk.getRequestInstance().requestWithApiNameWithoutSession(apiName, apiVersion, postData, Any::class.java, callback)
        } else {
            ThingHomeSdk.getRequestInstance().requestWithApiName(apiName, apiVersion, postData, Any::class.java,callback)
        }

    }

}

