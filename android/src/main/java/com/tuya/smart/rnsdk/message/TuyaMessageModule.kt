package com.tuya.smart.rnsdk.message

import com.facebook.react.bridge.*
import com.thingclips.smart.android.user.api.IBooleanCallback
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IThingDataCallback
import com.thingclips.smart.sdk.bean.message.MessageBean
import com.tuya.smart.rnsdk.utils.Constant
import com.tuya.smart.rnsdk.utils.Constant.IDS
import com.tuya.smart.rnsdk.utils.JsonUtils
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils
import java.util.ArrayList



class TuyaMessageModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "TuyaMessageModule"
    }
    @ReactMethod
    fun getMessageList(promise: Promise) {
       ThingHomeSdk.getMessageInstance().getMessageList(object : IThingDataCallback<List<MessageBean>> {
            override fun onSuccess(p0: List<MessageBean>?) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(p0!!)))
            }

            override fun onError(p0: String?, p1: String?) {
                promise.reject(p0,p1)
            }
        })
    }

    @ReactMethod
    fun deleteMessage(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(IDS), params)) {
            var list = ArrayList<String>()
            var length = (params.getArray(Constant.IDS) as ReadableArray).size()-1
            for (index in 0..length) {
                list.add((params.getArray(Constant.IDS) as ReadableArray).getString(index) as String)
            }
            ThingHomeSdk.getMessageInstance().deleteMessages(list,object : IBooleanCallback {
                override fun onSuccess(){
                    promise.resolve(Constant.SUCCESS)
                }

                override fun onError(var1: String, var2: String) {
                  promise.reject(var1,var2)
                }
            })
        }
    }

    @ReactMethod
    fun getMessageMaxTime(promise: Promise){
        ThingHomeSdk.getMessageInstance().getMessageMaxTime(object : IThingDataCallback<Int> {
            override fun onSuccess(p0: Int) {
                promise.resolve(p0)
            }

            override fun onError(p0: String?, p1: String?) {
                promise.reject(p0,p1)
            }
        })
    }
}
