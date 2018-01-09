package com.infi.netty.apirouter.cfg;

import java.util.Map;

import com.google.common.collect.Maps;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import lombok.Getter;

/** 
 * @author hongtao 
 * @version  v 0.1 , 2018年1月9日 上午10:49:18
 * @since  JDK 1.8     
 */
@Getter
public class GroupConfig {
  private int threads;
  private ChannelHandler channelHandler;
  private Map<ChannelOption<?>, Object> options = Maps.newHashMap();

  public GroupConfig setTheads(int threads) {
    if (threads < 0) {
      throw new IllegalArgumentException("Threads cannot be negative:" + threads + "!");
    }
    this.threads = threads;
    return this;
  }

  public GroupConfig setChannelHandler(ChannelHandler channelHandler) {
    this.channelHandler = channelHandler;
    return this;
  }

  public <T> GroupConfig setOption(ChannelOption<T> option, T value) {
    options.put(option, value);
    return this;
  }
}

