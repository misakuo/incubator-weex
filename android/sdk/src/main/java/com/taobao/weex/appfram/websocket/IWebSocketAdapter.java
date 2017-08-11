/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.taobao.weex.appfram.websocket;

import android.support.annotation.Nullable;

/**
 * Created by moxun on 16/12/27.
 */

public interface IWebSocketAdapter {

    String HEADER_SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    int PAYLOAD_TYPE_TEXT = 1;
    int PAYLOAD_TYPE_BINARY = 2;

    void connect(String url, @Nullable String protocol, EventListener listener);

    /**
     * using {@link #send(int, Object)} instead.
     */
    @Deprecated
    void send(String data);

    void send(int type, Object data);

    void close(int code, String reason);

    void destroy();

    interface EventListener {
        void onOpen();

        /**
         * using {@link #onMessage(int type, Object data)} instead.
         */
        @Deprecated
        void onMessage(String data);

        /**
         * @param type should be {@link #PAYLOAD_TYPE_TEXT} or {@link #PAYLOAD_TYPE_BINARY}
         * @param data String or byte[]
         */
        void onMessage(int type, Object data);

        void onClose(int code, String reason, boolean wasClean);

        void onError(String msg);
    }
}
