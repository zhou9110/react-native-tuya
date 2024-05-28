package com.tuya.smart.rnsdk.share

import com.facebook.react.bridge.*
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.ShareReceivedUserDetailBean
import com.thingclips.smart.home.sdk.bean.ShareSentUserDetailBean
import com.thingclips.smart.home.sdk.bean.SharedUserInfoBean
import com.thingclips.smart.home.sdk.callback.IThingResultCallback
import com.tuya.smart.rnsdk.utils.Constant
import com.tuya.smart.rnsdk.utils.Constant.COUNTRYCODE
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.DEVIDS
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.MEMBERID
import com.tuya.smart.rnsdk.utils.Constant.SHAREID
import com.tuya.smart.rnsdk.utils.Constant.NAME
import com.tuya.smart.rnsdk.utils.Constant.USERACCOUNT
import com.tuya.smart.rnsdk.utils.Constant.getIResultCallback
import com.tuya.smart.rnsdk.utils.JsonUtils
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils


class TuyaShareModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "TuyaShareModule"
    }


    /*添加多个设备共享（追加）*/
    @ReactMethod
    fun addShareWithHomeId(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, COUNTRYCODE, USERACCOUNT, DEVIDS), params)) {
            var list = ArrayList<String>()
            var length = (params.getArray(Constant.DEVIDS) as ReadableArray).size()-1
            var array = params.getArray(Constant.DEVIDS) as ReadableArray
            var newlist= TuyaReactUtils.parseToList(array);
            for(index in 0..length){
                list.add(newlist.get(index).toString())
            }
            ThingHomeSdk.getDeviceShareInstance().addShareWithHomeId(params.getDouble(HOMEID).toLong()
                    , params.getString(COUNTRYCODE),
                    params.getString(USERACCOUNT),
                    list,
                    getITuyaResultCallbackSingle(promise))
        }
    }

    /*批量添加设备共享*/
    @ReactMethod
    fun addShareWithMemberId(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID, DEVIDS), params)) {
            var list = ArrayList<String>()
            var length = (params.getArray(Constant.DEVIDS) as ReadableArray).size()
            for (index in 0..length) {
                list.add((params.getArray(Constant.DEVIDS) as ReadableArray).getString(index) as String)
            }
            ThingHomeSdk.getDeviceShareInstance().addShareWithMemberId(params.getDouble(MEMBERID).toLong(),
                    list,
                    getIResultCallback(promise))
        }
    }
    /*单个设备取消共享*/
    @ReactMethod
    fun disableDevShare(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID, DEVID), params)) {
            ThingHomeSdk.getDeviceShareInstance().disableDevShare(params.getString(DEVID), params.getDouble(MEMBERID).toLong(),
                    getIResultCallback(promise))
        }
    }
    /*查询主动分享的关系列表*/
    @ReactMethod
    fun queryUserShareList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            ThingHomeSdk.getDeviceShareInstance().queryUserShareList(params.getDouble(HOMEID).toLong(),
                    getITuyaResultCallback(promise))
        }
    }
    /*查询收到分享关系列表*/
    @ReactMethod
    fun queryShareReceivedUserList(promise: Promise) {
        ThingHomeSdk.getDeviceShareInstance().queryShareReceivedUserList(
                getITuyaResultCallback(promise))
    }
    /*查询指定设备的分享用户列表*/
    @ReactMethod
    fun queryDevShareUserList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            ThingHomeSdk.getDeviceShareInstance().queryDevShareUserList(params.getString(DEVID),
                    getITuyaResultCallback(promise))
        }
    }
    /* 查询指定设备是谁共享的 */
    @ReactMethod
    fun queryShareDevFromInfo(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            ThingHomeSdk.getDeviceShareInstance().queryShareDevFromInfo(params.getString(DEVID),
                    getITuyaResultCallbackSingle(promise))
        }
    }
    /* 查询分享到指定用户的共享关系 */
    @ReactMethod
    fun getUserShareInfo(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID), params)) {
            ThingHomeSdk.getDeviceShareInstance().getUserShareInfo(
                    params.getDouble(MEMBERID).toLong(),
                    getITuyaResultShareSentUserDetailBeanCallback(promise))
        }
    }


    /*查询收到指定用户共享的信息*/
    @ReactMethod
    fun getReceivedShareInfo(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID), params)) {
            ThingHomeSdk.getDeviceShareInstance().getReceivedShareInfo(
                    params.getDouble(MEMBERID).toLong(),
                    object : IThingResultCallback<ShareReceivedUserDetailBean> {
                        override fun onSuccess(bean: ShareReceivedUserDetailBean) {
                            promise.resolve(TuyaReactUtils.parseToWritableMap(bean))
                        }

                        override fun onError(errorCode: String, errorMessage: String) {
                            promise.reject(errorCode, errorMessage)
                        }
                    })
        }
    }


    /*可分享给未注册用户*/
    @ReactMethod
    fun inviteShare(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID), params)) {

        }
    }

    /* 邀请分享确认*/
    @ReactMethod
    fun confirmShareInviteShare(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID), params)) {

        }
    }


    /*删除共享关系*/
    @ReactMethod
    fun removeUserShare(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID), params)) {
            ThingHomeSdk.getDeviceShareInstance().removeUserShare(
                    params.getDouble(MEMBERID).toLong(),
                    getIResultCallback(promise))
        }
    }

    /*删除收到的共享关系*/
    @ReactMethod
    fun removeReceivedUserShare(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID), params)) {
            ThingHomeSdk.getDeviceShareInstance().removeReceivedUserShare(
                    params.getDouble(MEMBERID).toLong(),
                    getIResultCallback(promise))
        }
    }


    /*移除收到的分享设备*/
    @ReactMethod
    fun removeReceivedDevShare(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            ThingHomeSdk.getDeviceShareInstance().removeReceivedDevShare(
                    params.getString(DEVID),
                    getIResultCallback(promise))
        }
    }
    /*修改发出的分享人的备注名*/
    @ReactMethod
    fun renameShareNickname(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID, NAME), params)) {
            ThingHomeSdk.getDeviceShareInstance().renameShareNickname(
                    params.getDouble(MEMBERID).toLong(),
                    params.getString(NAME),
                    getIResultCallback(promise))
        }
    }
    /*修改接收到的分享人的备注名*/
    @ReactMethod
    fun renameReceivedShareNickname(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MEMBERID, NAME), params)) {
            ThingHomeSdk.getDeviceShareInstance().renameReceivedShareNickname(
                    params.getDouble(MEMBERID).toLong(),
                    params.getString(NAME),
                    getIResultCallback(promise))
        }
    }

    fun getITuyaResultCallback(promise: Promise):  IThingResultCallback<List<SharedUserInfoBean>> {
        return object :  IThingResultCallback<List<SharedUserInfoBean>> {
            override fun onSuccess(p0: List<SharedUserInfoBean>) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(
                        JsonUtils.toJsonArray(p0)))
            }

            override fun onError(p0: String?, p1: String?) {
                promise.reject(p0, p1)
            }
        }
    }

    fun getITuyaResultCallbackSingle(promise: Promise):  IThingResultCallback<SharedUserInfoBean> {
        return object :  IThingResultCallback<SharedUserInfoBean> {
            override fun onSuccess(bean: SharedUserInfoBean) {
                promise.resolve(TuyaReactUtils.parseToWritableMap(bean))
            }

            override fun onError(errorCode: String, errorMessage: String) {
                promise.reject(errorCode, errorMessage)
            }
        }
    }

    fun getITuyaResultShareSentUserDetailBeanCallback(promise: Promise):  IThingResultCallback<ShareSentUserDetailBean> {
        return object :  IThingResultCallback<ShareSentUserDetailBean> {
            override fun onSuccess(bean: ShareSentUserDetailBean) {
                promise.resolve(TuyaReactUtils.parseToWritableMap(bean))
            }

            override fun onError(errorCode: String, errorMessage: String) {
                promise.reject(errorCode, errorMessage)
            }
        }
    }
}
