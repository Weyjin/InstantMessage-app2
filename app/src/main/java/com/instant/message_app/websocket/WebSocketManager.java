package com.instant.message_app.websocket;

import android.content.Context;
import com.neovisionaries.ws.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebSocketManager {


    private WebSocket mWebSocket;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int FRAME_QUEUE_SIZE = 5;
    private ISocketListener socketListener;

    public interface ISocketListener {
        void success(String message);
        void error(String message);
        void textMessage(String message);
    }


    private static class SingletonHolder {
        private static final WebSocketManager INSTANCE = new WebSocketManager();
    }

    private WebSocketManager (){}
    public static final WebSocketManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void connect(String url,Context context) {

        try {
            mWebSocket = new WebSocketFactory().createSocket(url, CONNECT_TIMEOUT)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(new WsListener())//添加回调监听
                    .connectAsynchronously();//异步连接
            socketListener= (ISocketListener) context;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendText(String message){
        mWebSocket.sendText(message);

    }
    public void disconnect(){
        if(mWebSocket != null)
            mWebSocket.disconnect();
    }

    public class WsListener extends WebSocketAdapter implements WebSocketListener {


        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket,headers);
            socketListener.success("连接成功");
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
                 super.onConnectError(websocket,cause);
                 socketListener.error("连接错误");
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                super.onDisconnected(websocket,serverCloseFrame,clientCloseFrame,closedByServer);
                socketListener.error("断开连接");
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
                 super.onTextMessage(websocket,text);
                 socketListener.textMessage(text);
        }
    }

}
