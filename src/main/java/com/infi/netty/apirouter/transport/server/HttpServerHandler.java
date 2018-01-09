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
package com.infi.netty.apirouter.transport.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 下午4:32:50
 * @since JDK 1.8
 */
@Sharable
public abstract class HttpServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Object[] msgs = (Object[]) msg;
    handle(ctx, (HttpRequest) msgs[0], (ByteBuf) msgs[1]);
  }

  protected abstract void handle(ChannelHandlerContext ctx, HttpRequest httpRequest,
      ByteBuf byteBuf) throws Exception;
}
