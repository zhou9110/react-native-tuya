//
//  TuyaRNFeedBackModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNFeedBackModule.h"
#import <ThingSmartFeedbackKit/ThingSmartFeedbackKit.h>
#import "TuyaRNUtils.h"
#import "YYModel.h"

@interface TuyaRNFeedBackModule()
@property (nonatomic, strong) ThingSmartFeedback *smartFeedback;
@end


@implementation TuyaRNFeedBackModule

RCT_EXPORT_MODULE(TuyaFeedBackModule)

RCT_EXPORT_METHOD(initWithOptions:(NSDictionary *)params) {
  
}

// 获取反馈列表：
RCT_EXPORT_METHOD(getFeedbackMsg:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
    ThingSmartFeedback *feedBack = [[ThingSmartFeedback alloc] init];
  self.smartFeedback = feedBack;
  [feedBack getFeedbackList:params[@"hdId"] hdType:[params[@"hdType"] unsignedIntegerValue] success:^(NSArray<ThingSmartFeedbackModel *> *list) {
    NSMutableArray *res = [NSMutableArray array];
    for (ThingSmartFeedbackModel *item in list) {
      NSDictionary *dic = [item yy_modelToJSONObject];
      [res addObject:dic];
    }
    if (resolver) {
      resolver(res);
    }
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
  
}



// 获取反馈类型列表：
RCT_EXPORT_METHOD(getFeedbackType:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
    ThingSmartFeedback *feedBack = [[ThingSmartFeedback alloc] init];
  self.smartFeedback = feedBack;
    [feedBack getFeedbackTypeList:^(NSArray<ThingSmartFeedbackTypeListModel *> *list) {
      
      NSMutableArray *res = [NSMutableArray array];
        for (ThingSmartFeedbackTypeListModel *item in list) {
          NSDictionary *dic = [item yy_modelToJSONObject];
          [res addObject:dic];
        }
        if (resolver) {
          resolver(res);
        }
    } failure:^(NSError *error) {
        [TuyaRNUtils rejecterWithError:error handler:rejecter];
    }];
}


// 新增反馈：
RCT_EXPORT_METHOD(addMsg:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
  ThingSmartFeedback *feedBack = [[ThingSmartFeedback alloc] init];
  self.smartFeedback = feedBack;
  [feedBack addFeedback:params[@"message"] hdId:params[@"hdId"] hdType:[params[@"hdType"] integerValue] contact:params[@"contact"] success:^{
      if (resolver) {
        resolver(@"success");
      }
  } failure:^(NSError *error) {
      [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}


// 反馈消息管理：
RCT_EXPORT_METHOD(getFeedbackList:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
    ThingSmartFeedback *feedBack = [[ThingSmartFeedback alloc] init];
  self.smartFeedback = feedBack;
    [feedBack getFeedbackTalkList:^(NSArray<ThingSmartFeedbackTalkListModel *> *list) {
      NSMutableArray *res = [NSMutableArray array];
        for (ThingSmartFeedbackTalkListModel *item in list) {
          NSDictionary *dic = [item yy_modelToJSONObject];
          [res addObject:dic];
        }
        if (resolver) {
          resolver(res);
        }
    } failure:^(NSError *error) {
        [TuyaRNUtils rejecterWithError:error handler:rejecter];
    }];
}



@end
