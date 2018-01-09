/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.infi.netty.apirouter.server;

import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.infi.netty.apirouter.cfg.GroupConfig;
import com.infi.netty.apirouter.cfg.ServerConfig;
import com.infi.netty.apirouter.transport.ChInitializer;
import com.infi.netty.apirouter.transport.server.HttpServerInitializer;
import com.infi.netty.apirouter.util.thread.TaskThreadPoolFactory;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 上午10:54:35
 * @since JDK 1.8
 */
@Slf4j
public class NettyHttpServer implements Server {

  private final NettyServer nettyServer;
  private final ServerConfig serverConf;

  public NettyHttpServer(ServerConfig serverConf) {
    Preconditions.checkArgument(serverConf.getPort() > 1 && serverConf.getPort() < 0xFFFF,
        "Server port is out of range");
    this.serverConf = serverConf;
    ListeningExecutorService taskExecutor =
        MoreExecutors.listeningDecorator((ThreadPoolExecutor) TaskThreadPoolFactory
            .createExecutor(serverConf.getThreadPoolSize(), serverConf.getBufferQueueSize()));
    GroupConfig bossConfig = new GroupConfig().setOption(ChannelOption.SO_BACKLOG, 128);
    GroupConfig workerConfig = new GroupConfig()
        .setChannelHandler(new ChInitializer(new HttpServerInitializer(taskExecutor)))
        .setOption(ChannelOption.SO_KEEPALIVE, true) //
        .setOption(ChannelOption.TCP_NODELAY, true);
    this.nettyServer = new NettyServer(bossConfig, workerConfig);
  }

  @Override
  public void start() {
    try {
      nettyServer.start(serverConf.getPort());
    } catch (InterruptedException e) {
      log.error("Fail to start server", e);
      throw new RuntimeException("Fail to start server", e);
    }
  }

}
