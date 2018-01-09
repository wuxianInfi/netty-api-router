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

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.infi.netty.apirouter.cfg.GroupConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 
 * @author hongtao 
 * @version  v 0.1 , 2018年1月9日 上午10:49:43
 * @since  JDK 1.8     
 */
@Slf4j
@RequiredArgsConstructor
public class NettyServer {

  private final GroupConfig bossConfig;
  private final GroupConfig workerConfig;

  public void start(int port) throws InterruptedException {
    this.start("", port);
  }

  public void start(String host, int port) throws InterruptedException {
    ThreadFactory threadRpcFactory = new CustomizableThreadFactory("netty-server");
    int parallel = Runtime.getRuntime().availableProcessors() * 2;
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup =
        new NioEventLoopGroup(parallel, threadRpcFactory, SelectorProvider.provider());
    try {
      ServerBootstrap bootstrap = new ServerBootstrap() //
          .group(bossGroup, workerGroup) //
          .channel(NioServerSocketChannel.class);
      bootBoss(bootstrap);
      bootWorker(bootstrap);
      ChannelFuture future =
          !StringUtils.isEmpty(host) ? bootstrap.bind(host, port) : bootstrap.bind(port);
      future.sync();
      log.info("Server started on port {}", port);
      future.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  @FunctionalInterface
  private interface OptionOp {
    <T> void execute(ChannelOption<T> option, T value);
  }

  @SuppressWarnings("unchecked")
  private <T> void bootOption(ChannelOption<T> option, Object value, OptionOp op) {
    op.execute(option, (T) value);
  }

  private void bootBoss(ServerBootstrap bootstrap) {
    if (bossConfig.getChannelHandler() != null) {
      bootstrap.handler(bossConfig.getChannelHandler());
    }
    bossConfig.getOptions().forEach((option, value) -> {
      bootOption(option, value, bootstrap::option);
    });
  }

  private void bootWorker(ServerBootstrap bootstrap) {
    if (workerConfig.getChannelHandler() != null) {
      bootstrap.childHandler(workerConfig.getChannelHandler());
    }
    workerConfig.getOptions().forEach((option, value) -> {
      bootOption(option, value, bootstrap::childOption);
    });
  }
}
