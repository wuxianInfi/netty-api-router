package com.infi.netty.apirouter.cfg;

import lombok.Builder;
import lombok.Data;

/** 
 * @author hongtao 
 * @version  v 0.1 , 2018年1月9日 上午10:49:18
 * @since  JDK 1.8     
 */
@Data
@Builder
public class ServerConfig {
  private int port;
  private int threadPoolSize;
  private int bufferQueueSize;
  
}
