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

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 下午4:41:48
 * @since JDK 1.8
 */
@Slf4j
@RequiredArgsConstructor
public class ApiRouteHttpServerHandler extends HttpServerHandler {

  private final ListeningExecutorService taskExecutor;

  @Override
  protected void handle(ChannelHandlerContext ctx, HttpRequest httpRequest, ByteBuf byteBuf)
      throws Exception {
    Callable<Integer> task = () -> {
      return 1;
    };
    boolean isKeepAlive = HttpUtil.isKeepAlive(httpRequest);
    ListenableFuture<Integer> listenableFuture = taskExecutor.submit(task);
    Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
      @Override
      public void onSuccess(Integer result) {
        FullHttpResponse httpResponse = getHttpResponse(result);
        if (isKeepAlive) {
          httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ChannelFuture future = ctx.writeAndFlush(httpResponse);
        if (!isKeepAlive) {
          try {
            future.sync();
          } catch (InterruptedException e) {
          }
          ctx.close();
        }
      }

      @Override
      public void onFailure(Throwable t) {
        log.error("should not happen", t);
      }
    });
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    log.error("Http handler exception caught", cause);
    ctx.close();
  }

  private FullHttpResponse getHttpResponse(Integer taskResult) {
    String content;
    String contentType;
    HttpResponseStatus statusCode;
    try {
      content = taskResult.toString();
      contentType = "application/json; charset=utf-8";
      statusCode = HttpResponseStatus.OK;
    } catch (Throwable t) {
      content =
          "Throwable Class(" + t.getClass().getName() + ") and Message(" + t.getMessage() + ")!";
      contentType = "application/text; charset=utf-8";
      statusCode = HttpResponseStatus.BAD_REQUEST;
    }

    FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, statusCode,
        Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));
    httpResponse.headers()
        .set(HttpHeaders.Names.CONTENT_LENGTH, httpResponse.content().readableBytes())
        .set(HttpHeaders.Names.CONTENT_TYPE, contentType);
    return httpResponse;
  }

}
