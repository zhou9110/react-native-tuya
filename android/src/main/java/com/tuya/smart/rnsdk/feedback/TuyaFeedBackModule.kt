package com.tuya.smart.rnsdk.feedback

import com.facebook.react.bridge.*
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IThingDataCallback
import com.thingclips.smart.sdk.bean.feedback.FeedbackBean
import com.thingclips.smart.sdk.bean.feedback.FeedbackMsgBean
import com.thingclips.smart.sdk.bean.feedback.FeedbackTypeRespBean
import com.tuya.smart.rnsdk.utils.Constant
import com.tuya.smart.rnsdk.utils.Constant.HDID
import com.tuya.smart.rnsdk.utils.Constant.HDTYPE
import com.tuya.smart.rnsdk.utils.JsonUtils
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils


class TuyaFeedBackModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "TuyaFeedBackModule"
    }

    /*获取反馈列表*/
    @ReactMethod
    fun getFeedbackList(promise: Promise) {
        ThingHomeSdk.getThingFeekback().getFeedbackManager().getFeedbackList(object : IThingDataCallback<List<FeedbackBean>> {
            override fun onSuccess(var1: List<FeedbackBean>) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1!!)))
            }

            override fun onError(var1: String, var2: String) {
                promise.reject(var1, var2)
            }
        })
    }


    /*获取反馈列表*/
    @ReactMethod
    fun getFeedbackType(promise: Promise) {
        ThingHomeSdk.getThingFeekback().getFeedbackManager().getFeedbackType(object : IThingDataCallback<List<FeedbackTypeRespBean>> {
            override fun onSuccess(var1: List<FeedbackTypeRespBean>) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1!!)))
            }

            override fun onError(var1: String, var2: String) {
                promise.reject(var1, var2)
            }
        })
    }


    /*添加反馈*/
    @ReactMethod
    fun addFeedback(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.MESEAGE, Constant.CONTACT, Constant.HDID, Constant.HDTYPE), params)) {
            ThingHomeSdk.getThingFeekback().getFeedbackManager().addFeedback(params.getString(Constant.MESEAGE), params.getString(Constant.CONTACT),
                    params.getString(HDID), params.getInt(HDTYPE),
                    object : IThingDataCallback<FeedbackMsgBean> {
                        override fun onSuccess(var1: FeedbackMsgBean) {
                            promise.resolve(TuyaReactUtils.parseToWritableMap(var1!!))
                        }

                        override fun onError(var1: String, var2: String) {
                            promise.reject(var1, var2)
                        }
                    })
        }
    }


    /*获取反馈消息列表*/
    @ReactMethod
    fun getFeedbackMsg(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.HDID, Constant.HDTYPE), params)) {
            ThingHomeSdk.getThingFeekback().getFeedbackMsg(params.getString(HDID), params.getInt(HDTYPE))
                    .getMsgList(
                            object : IThingDataCallback<List<FeedbackMsgBean>> {
                                override fun onSuccess(var1: List<FeedbackMsgBean>) {
                                    promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1!!)))
                                }

                                override fun onError(var1: String, var2: String) {
                                    promise.reject(var1, var2)
                                }
                            })
        }
    }


    /*添加新反馈*/
    @ReactMethod
    fun addMsg(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.HDID, Constant.HDTYPE, Constant.MESEAGE, Constant.CONTACT), params)) {
            ThingHomeSdk.getThingFeekback().getFeedbackMsg(params.getString(HDID), params.getInt(HDTYPE))
                    .addMsg(params.getString(Constant.MESEAGE), params.getString(Constant.CONTACT),
                            object : IThingDataCallback<FeedbackMsgBean> {
                                override fun onSuccess(var1: FeedbackMsgBean) {
                                    promise.resolve(TuyaReactUtils.parseToWritableMap(var1!!))
                                }

                                override fun onError(var1: String, var2: String) {
                                    promise.reject(var1, var2)
                                }
                            })
        }
    }

}
