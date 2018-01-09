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
package com.infi.netty.apirouter;

import com.infi.netty.apirouter.cfg.ServerConfig;
import com.infi.netty.apirouter.server.NettyHttpServer;
import com.infi.netty.apirouter.server.Server;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 下午5:19:41
 * @since JDK 1.8
 */
public class TestHttpServer {

  public static void main(String[] args) {
    Server server = new NettyHttpServer(
        ServerConfig.builder().threadPoolSize(16).bufferQueueSize(10000).port(8889).build());
    server.start();

  }

}
