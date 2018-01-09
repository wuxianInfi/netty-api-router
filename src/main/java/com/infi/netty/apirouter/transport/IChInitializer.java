package com.infi.netty.apirouter.transport;

import io.netty.channel.Channel;

/** 
 * @author hongtao 
 * @version  v 0.1 , 2018年1月9日 上午10:54:35
 * @since  JDK 1.8     
 */
public interface IChInitializer {

  void initChannel(Channel channel) throws Exception;
}
