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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 下午4:30:08
 * @since JDK 1.8
 */
public class HttpServerAggregator extends ChannelInboundHandlerAdapter {

  private HttpRequest httpRequest;
  private ByteBuf byteBuf;

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof HttpRequest) {
      httpRequest = (HttpRequest) msg;
      byteBuf = Unpooled.buffer();
    }

    if (msg instanceof HttpContent) {
      HttpContent httpContent = (HttpContent) msg;
      ByteBuf buf = httpContent.content();
      byteBuf.writeBytes(buf);
      buf.release();

      if (httpContent instanceof LastHttpContent) {
        try {
          ctx.fireChannelRead(new Object[] {httpRequest, byteBuf});
        } finally {
          httpRequest = null;
          byteBuf = null;
        }
      }
    }
  }

}
