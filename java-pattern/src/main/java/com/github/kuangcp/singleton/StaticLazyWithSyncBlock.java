package com.github.kuangcp.singleton;

/**
 * TODO 验证
 * @author https://github.com/kuangcp
 * @date 2019-05-08 14:26
 */
public class StaticLazyWithSyncBlock {

  private static StaticLazyWithSyncBlock singleton;

  private StaticLazyWithSyncBlock() {
  }

  public static StaticLazyWithSyncBlock getInstance() {
    synchronized (StaticLazyWithSyncBlock.class) {
      if (singleton == null) {
        singleton = new StaticLazyWithSyncBlock();
      }
    }
    return singleton;
  }
}
