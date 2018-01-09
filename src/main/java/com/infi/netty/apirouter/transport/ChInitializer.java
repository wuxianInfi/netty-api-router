package com.infi.netty.apirouter.transport;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.RequiredArgsConstructor;

/** 
 * @author hongtao 
 * @version  v 0.1 , 2018年1月9日 上午10:54:35
 * @since  JDK 1.8     
 */
@RequiredArgsConstructor
public class ChInitializer extends ChannelInitializer<Channel> {
  private final IChInitializer initializer;

  @Override
  protected void initChannel(Channel ch) throws Exception {
    initializer.initChannel(ch);
  }
}
