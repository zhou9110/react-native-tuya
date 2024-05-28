//
//  TuyaRNHomeListener.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/3/6.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNHomeListener.h"
#import <ThingSmartDeviceKit/ThingSmartHome.h>
#import <ThingSmartDeviceKit/ThingSmartHomeManager.h>
#import "TuyaRNEventEmitter.h"
#import <ThingSmartDeviceKit/ThingSmartRoomModel.h>
#import <ThingSmartDeviceKit/ThingSmartShareDeviceModel.h>
#import <ThingSmartDeviceKit/ThingSmartGroup+DpCode.h>

@interface TuyaRNHomeListener()<ThingSmartHomeDelegate>

@property (strong, nonatomic) ThingSmartHome *homeChangeSmartHome;
@property (strong, nonatomic) ThingSmartHome *homeStatusSmartHome;

@end

@implementation TuyaRNHomeListener

+ (instancetype)shareInstance {
  static TuyaRNHomeListener *_instance = nil;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    _instance = [[TuyaRNHomeListener alloc] init];
  });
  return _instance;
}

- (void)registerHomeChangeWithSmartHome:(ThingSmartHome *)smartHome {

  if (!smartHome) {
    return;
  }
  [TuyaRNHomeListener shareInstance].homeChangeSmartHome = smartHome;
  [TuyaRNHomeListener shareInstance].homeChangeSmartHome.delegate = self;
}

- (void)removeHomeChangeSmartHome {

  [TuyaRNHomeListener shareInstance].homeChangeSmartHome = nil;
  [TuyaRNHomeListener shareInstance].homeChangeSmartHome.delegate = nil;

}

- (void)registerHomeStatusWithSmartHome:(ThingSmartHome *)smartHome {

  if (!smartHome) {
    return;
  }
  [TuyaRNHomeListener shareInstance].homeStatusSmartHome = smartHome;
  [TuyaRNHomeListener shareInstance].homeStatusSmartHome.delegate = self;
}

- (void)removeHomeStatusSmartHome {

  [TuyaRNHomeListener shareInstance].homeStatusSmartHome = nil;
  [TuyaRNHomeListener shareInstance].homeStatusSmartHome.delegate = nil;

}


#pragma mark - ThingSmartHomeDelegate

// 家庭的信息更新，例如name
- (void)homeDidUpdateInfo:(ThingSmartHome *)home {

  if (!self.homeChangeSmartHome) {
    return;
  }

  if (home.homeModel.homeId <= 0) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"type": @"onHomeInfoChanged"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeChangeEvent stringByAppendingString:@"//"] withBody:dic];
}

// 我收到的共享设备列表变化
- (void)homeDidUpdateSharedInfo:(ThingSmartHome *)home {

  if (!self.homeChangeSmartHome) {
    return;
  }

  if (home.homeModel.homeId <= 0) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"type": @"onSharedDeviceList"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeChangeEvent stringByAppendingString:@"//"] withBody:dic];
}

// 房间信息变更，例如name
- (void)home:(ThingSmartHome *)home roomInfoUpdate:(ThingSmartRoomModel *)room {

  if (!self.homeChangeSmartHome) {
    return;
  }

  //房间的名字的变更
  if (home.homeModel.homeId <= 0) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"roomId": [NSNumber numberWithLongLong:room.roomId],
                        @"type": @"onHomeRoomInfo"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeChangeEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];
}

// 房间与设备，群组的关系变化
- (void)home:(ThingSmartHome *)home roomRelationUpdate:(ThingSmartRoomModel *)room {

}

// 添加设备
- (void)home:(ThingSmartHome *)home didAddDeivice:(ThingSmartDeviceModel *)device {

  if (!self.homeStatusSmartHome) {
    return;
  }

  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"devId": device.devId,
                        @"type": @"onDeviceAdded"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeStatusEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];

}

// 删除设备
- (void)home:(ThingSmartHome *)home didRemoveDeivice:(NSString *)devId {

  if (!self.homeStatusSmartHome) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"devId": devId,
                        @"type": @"onDeviceRemoved"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeStatusEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];

}

// 设备信息更新，例如name
- (void)home:(ThingSmartHome *)home deviceInfoUpdate:(ThingSmartDeviceModel *)device {

}

// 设备dp数据更新
- (void)home:(ThingSmartHome *)home device:(ThingSmartDeviceModel *)device dpsUpdate:(NSDictionary *)dps {

}

// 添加群组
- (void)home:(ThingSmartHome *)home didAddGroup:(ThingSmartGroupModel *)group {
  if (!self.homeStatusSmartHome) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"devId": group.groupId,
                        @"type": @"onGroupAdded"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeStatusEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];
}

// 群组dp数据更新
- (void)home:(ThingSmartHome *)home group:(ThingSmartGroupModel *)group dpsUpdate:(NSDictionary *)dps {
  if (!self.homeStatusSmartHome || !dps ||  dps.count == 0) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"groupId": group,
                        @"dps":dps,
                        @"type": @"onGroupDpsUpdate"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeStatusEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];
}

// 删除群组
- (void)home:(ThingSmartHome *)home didRemoveGroup:(NSString *)groupId {
  if (!self.homeStatusSmartHome) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"groupId": groupId,
                        @"type": @"onGroupRemoved"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeStatusEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];
}

// 群组信息更新，例如name
- (void)home:(ThingSmartHome *)home groupInfoUpdate:(ThingSmartGroupModel *)group {
  if (!self.homeStatusSmartHome) {
    return;
  }
  NSDictionary *dic = @{
                        @"homeId": [NSNumber numberWithLongLong:home.homeModel.homeId],
                        @"groupId": group.groupId,
                        @"type": @"onGroupInfoUpdate"
                        };
  [TuyaRNEventEmitter ty_sendEvent:[kTYEventEmitterHomeStatusEvent stringByAppendingFormat:@"//%lld",home.homeModel.homeId] withBody:dic];
}

@end
