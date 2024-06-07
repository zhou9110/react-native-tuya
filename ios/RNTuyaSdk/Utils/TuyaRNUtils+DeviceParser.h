//
//  TuyaRNUtils+DeviceParser.h
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/3/4.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNUtils.h"
#import <ThingSmartDeviceKit/ThingSmartShareDeviceModel.h>
#import <ThingSmartDeviceKit/ThingSmartGroup+DpCode.h>
#import <YYModel/YYModel.h>

static inline NSArray *getValidDataForDeviceModel(NSArray <ThingSmartDeviceModel *> *deviceModelList) {
  if(!deviceModelList || [deviceModelList count] == 0) {
    return @[];
  }
  NSMutableArray *list = [NSMutableArray array];
  for (ThingSmartDeviceModel *tempModel in deviceModelList) {

    NSDictionary *dic = [tempModel yy_modelToJSONObject];
    [list addObject:dic];
  }
  return list;
}

static inline NSArray *getValidDataForGroupModel(NSArray <ThingSmartGroupModel *> *groupModelList) {
  if(!groupModelList || [groupModelList count] == 0) {
    return @[];
  }

  NSMutableArray *list = [NSMutableArray array];
  for (ThingSmartGroupModel *tempModel in groupModelList) {
    NSDictionary *dic = [tempModel yy_modelToJSONObject];
    [list addObject:dic];
  }
  return list;
}


NS_ASSUME_NONNULL_BEGIN

@interface TuyaRNUtils (DeviceParser)

@end

NS_ASSUME_NONNULL_END
