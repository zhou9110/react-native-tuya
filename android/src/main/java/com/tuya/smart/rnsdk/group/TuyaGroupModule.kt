package com.tuya.smart.rnsdk.group

import com.facebook.react.bridge.*
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHome
import com.thingclips.smart.home.sdk.callback.IThingResultCallback
import com.thingclips.smart.sdk.api.IGroupListener
import com.thingclips.smart.sdk.api.IThingGroup
import com.thingclips.smart.sdk.bean.GroupDeviceBean
import com.tuya.smart.rnsdk.utils.*
import com.tuya.smart.rnsdk.utils.Constant.COMMAND
import com.tuya.smart.rnsdk.utils.Constant.DEVIDS
import com.tuya.smart.rnsdk.utils.Constant.GROUPID
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.NAME
import com.tuya.smart.rnsdk.utils.Constant.PRODUCTID
import com.tuya.smart.rnsdk.utils.Constant.getIResultCallback




class TuyaGroupModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "TuyaGroupModule"
    }

    /*创建群组*/
    @ReactMethod
    fun createGroup(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, PRODUCTID, NAME, DEVIDS), params)) {
            getITuyaHome(params.getDouble(HOMEID).toLong()).createGroup(
                    params.getString((PRODUCTID)),
                            params.getString(NAME),
                            JsonUtils.parserArraybyMap(params.getArray(DEVIDS) as ReadableArray,
                                    String::class.java) as MutableList<String>?,
                            object : IThingResultCallback<Long> {
                                override fun onSuccess(p0: Long) {
                                    promise.resolve(p0)
                                }

                                override fun onError(p0: String?, p1: String?) {
                                    promise.reject(p0,p1)
                                }
                            }
                    )
        }
    }

    /**onNetworkStatusChanged
     * 此接口主要是从云端拉取最新群组列表 根据产品ID
     */
    @ReactMethod
    fun queryDeviceListToAddGroup(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID,PRODUCTID), params)) {
            getITuyaHome(params.getDouble(HOMEID).toLong()).queryDeviceListToAddGroup(params.getDouble(HOMEID).toLong(),params.getString(PRODUCTID),
                    object : IThingResultCallback<List<GroupDeviceBean>>{
                override fun onSuccess(bizResult: List<GroupDeviceBean>) {
                    promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(bizResult)))
                }

                override fun onError(errorCode: String, errorMsg: String) {
                    promise.reject(errorCode,errorMsg)
                }
            })
        }
    }

    @ReactMethod
    fun dismissGroup(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID), params)) {
            getITuyaGroup(params.getDouble(GROUPID).toLong())
                    ?.dismissGroup(getIResultCallback(promise)
                    )
        }
    }

    @ReactMethod
    fun updateGroupName(params: ReadableMap, promise: Promise){
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID, NAME), params)) {
            getITuyaGroup(params.getDouble(GROUPID).toLong())
                    ?.renameGroup( params.getString(NAME)
                            ,getIResultCallback(promise)
                    )
        }
    }

    @ReactMethod
    fun registerGroupListener(params: ReadableMap) {
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID), params)) {
            getITuyaGroup(params.getDouble(GROUPID).toLong())
                    ?.registerGroupListener(object : IGroupListener {
                        override fun onDpUpdate(var1: Long, var3: String){
                            var map=Arguments.createMap();
                            map.putDouble("id",var1.toDouble())
                            map.putString("dps",var3)
                            map.putString("type","onDpUpdate")
                            BridgeUtils.groupListener(reactApplicationContext,map,params.getDouble(GROUPID).toLong())
                        }

                        override fun onDpCodeUpdate(groupId: Long, dpCodeMap: MutableMap<String, Any>?) {
                            //
                        }

                        override fun onGroupInfoUpdate(var1: Long){
                            var map=Arguments.createMap();
                            map.putDouble("id",var1.toDouble())
                            map.putString("type","onGroupInfoUpdate")
                            BridgeUtils.groupListener(reactApplicationContext,map,params.getDouble(GROUPID).toLong())
                        }

                        override fun onGroupRemoved(var1: Long){
                            var map=Arguments.createMap();
                            map.putDouble("id",var1.toDouble())
                            map.putString("type","onGroupRemoved")
                            BridgeUtils.groupListener(reactApplicationContext,map,params.getDouble(GROUPID).toLong())
                        }
                    })
        }
    }

    @ReactMethod
    fun unregisterGroupListener(params: ReadableMap) {
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID), params)) {
            getITuyaGroup(params.getDouble(GROUPID).toLong())
                    ?.unRegisterGroupListener()
        }
    }
    @ReactMethod
    fun publishDps(params: ReadableMap,promise: Promise){
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID,COMMAND), params)) {
            getITuyaGroup(params.getDouble(GROUPID).toLong())
                    ?.publishDps(JsonUtils.toString(TuyaReactUtils.parseToMap(params.getMap(COMMAND) as ReadableMap)), getIResultCallback(promise))
        }
    }

    @ReactMethod
    fun onDestroy(params: ReadableMap){
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID,COMMAND), params)) {
            getITuyaGroup(params.getDouble(GROUPID).toLong())
                    ?.onDestroy()
        }
    }

    fun getITuyaHome(homeId: Long): IThingHome {
        return ThingHomeSdk.newHomeInstance(homeId)
    }

    fun getITuyaGroup(groupId: Long): IThingGroup {
        return ThingHomeSdk.newGroupInstance(groupId)
    }

}
