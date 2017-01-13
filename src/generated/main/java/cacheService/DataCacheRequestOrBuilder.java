// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CacheService.proto

package cacheService;

public interface DataCacheRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cacheService.DataCacheRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional float M = 1;</code>
   */
  float getM();

  /**
   * <code>repeated float W = 2;</code>
   */
  java.util.List<java.lang.Float> getWList();
  /**
   * <code>repeated float W = 2;</code>
   */
  int getWCount();
  /**
   * <code>repeated float W = 2;</code>
   */
  float getW(int index);

  /**
   * <code>repeated float m_min = 3;</code>
   */
  java.util.List<java.lang.Float> getMMinList();
  /**
   * <code>repeated float m_min = 3;</code>
   */
  int getMMinCount();
  /**
   * <code>repeated float m_min = 3;</code>
   */
  float getMMin(int index);

  /**
   * <code>repeated .cacheService.UtilityFunction U = 4;</code>
   */
  java.util.List<cacheService.UtilityFunction> 
      getUList();
  /**
   * <code>repeated .cacheService.UtilityFunction U = 4;</code>
   */
  cacheService.UtilityFunction getU(int index);
  /**
   * <code>repeated .cacheService.UtilityFunction U = 4;</code>
   */
  int getUCount();
  /**
   * <code>repeated .cacheService.UtilityFunction U = 4;</code>
   */
  java.util.List<? extends cacheService.UtilityFunctionOrBuilder> 
      getUOrBuilderList();
  /**
   * <code>repeated .cacheService.UtilityFunction U = 4;</code>
   */
  cacheService.UtilityFunctionOrBuilder getUOrBuilder(
      int index);
}
