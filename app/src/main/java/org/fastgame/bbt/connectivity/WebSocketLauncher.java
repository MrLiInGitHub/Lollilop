package org.fastgame.bbt.connectivity;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import org.fastgame.bbt.constant.ServerConstant;
import org.fastgame.bbt.event.SocketEvent;
import org.fastgame.bbt.utility.LogUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * {@link WebSocket} related business.
 *
 * @Author MrLi
 * @Since 08/22/2016
 */
public class WebSocketLauncher {

    private static WebSocketLauncher mInstance;
    private static WebSocketFactory mWebSocketFactory;
    /** What is the most suitable {@link java.util.Collections} for this to store the {@link com.neovisionaries.ws.client.WebSocket} */
    private static WeakHashMap<String, WebSocket> mWebSockets;
    private static BlockingQueue<String> mMsgWatingQueue;
    private static WebSocket mWebSocket;
    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
            LogUtils.debug("TAG", "onStateChanged");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_STATE_CHANGED));
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            LogUtils.debug("TAG", "onConnected");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_CONNECTED, websocket));
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
            LogUtils.debug("TAG", "onConnectError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_CONNECT_ERROR));
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_DISCONNECTED));
            LogUtils.debug("TAG", "onDisconnected");
        }

        @Override
        public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_FRAME));
        }

        @Override
        public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onContinuationFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_CONTINUATION_FRAME));
        }

        @Override
        public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onTextFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_TEXT_FRAME));
        }

        @Override
        public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onBinaryFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_BINARY_FRAME));
        }

        @Override
        public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onCloseFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_CLOSE_FRAME));
        }

        @Override
        public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onPingFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_PING_FRAME));
        }

        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onPongFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_PONG_FRAME));
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            LogUtils.debug("TAG", "onTextMessage");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_TEXT_MESSAGE, text));
        }

        @Override
        public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
            LogUtils.debug("TAG", "onBinaryMessage");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_BINARY_MESSAGE));
        }

        @Override
        public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onSendingFrame");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_SENDING_FRAME));
        }

        @Override
        public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onFrameSent");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_FRAME_SENT));
        }

        @Override
        public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onFrameUnsent");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_FRAME_UNSENT));
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
            LogUtils.debug("TAG", "onError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_ERROR));
        }

        @Override
        public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onFrameError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_FRAME_ERROR));
        }

        @Override
        public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
            LogUtils.debug("TAG", "onMessageError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_MESSAGE_ERROR));
        }

        @Override
        public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
            LogUtils.debug("TAG", "onMessageDecompressionError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_MESSAGE_DECOMPRESSION_ERROR));
        }

        @Override
        public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
            LogUtils.debug("TAG", "onTextMessageError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_TEXT_MESSAGE_ERROR));
        }

        @Override
        public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
            LogUtils.debug("TAG", "onSendError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_SEND_ERROR));
        }

        @Override
        public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
            LogUtils.debug("TAG", "onUnexpectedError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_UNEXPECTED_ERROR));
        }

        @Override
        public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
            LogUtils.debug("TAG", "handleCallbackError");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_HANDLE_CALLBACK_ERROR));
        }

        @Override
        public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {
            LogUtils.debug("TAG", "onSendingHandshake");
            EventBus.getDefault().post(new SocketEvent(SocketEvent.SOCKET_ON_SENDING_HANDSHAKE));
        }
    };

    private WebSocketLauncher() {}

    private static void checkAndNewWebSocketLauncher() {
        if (mInstance == null) {
            synchronized (WebSocketLauncher.class) {
                if (mInstance == null) {
                    mInstance = new WebSocketLauncher();
                }
            }
        }
    }

    private static void checkAndNewWebSocketFactory() {
        if (mWebSocketFactory == null) {
            synchronized (WebSocketFactory.class) {
                if (mWebSocketFactory == null) {
                    mWebSocketFactory = new WebSocketFactory().setConnectionTimeout(5000);
                }
            }
        }
    }

    private static void checkAndNewWebSocketHashMap() {
        if (mWebSockets == null) {
            synchronized (WeakHashMap.class) {
                if (mWebSockets == null) {
                    mWebSockets = new WeakHashMap<>();
                }
            }
        }
    }

    private static void checkAndNewMsgWaitingQueue() {
        if (mMsgWatingQueue == null) {
            synchronized (BlockingQueue.class) {
                if (mMsgWatingQueue == null) {
                    mMsgWatingQueue = new LinkedBlockingQueue<>();
                }
            }
        }
    }

    public synchronized static WebSocketLauncher getInstance() {

        checkAndNewWebSocketLauncher();
        checkAndNewWebSocketFactory();
        checkAndNewWebSocketHashMap();
        checkAndNewMsgWaitingQueue();

        return mInstance;
    }

    /**
     * Connect to the server.
     *
     * @param serverAddress  The address of the server to connect.
     * @param webSocketListener  The {@link WebSocketListener} for the connectiion.
     * @return  If the connection is already existing, return it or return null.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WebSocket connectServer(String serverAddress, WebSocketListener webSocketListener) {
        if (mWebSockets.containsKey(serverAddress) || mWebSocketFactory == null) {
            WebSocket webSocket = mWebSockets.get(serverAddress);
            webSocket.addListener(webSocketListener);
            return webSocket;
        }

        try {
            final WebSocket webSocket = mWebSocketFactory.createSocket(serverAddress);
            mWebSockets.put(serverAddress, webSocket);
            webSocket.addListener(webSocketListener);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        webSocket.connect();
                    } catch (WebSocketException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public WebSocket connectServer(String serverAddress) {
         return connectServer(serverAddress, mWebSocketListener);
    }

    public void sendMsgToServer(final String msg) {

        String serverAddress = ServerConstant.SERVER_ADDRESS + ":" + ServerConstant.SERVER_PORT;

        if (mWebSocket == null) {
            Log.d("TAG", "mWebSocket is null");
            mWebSocket = connectServer(serverAddress);
            addMsgToWaitingQueue(msg);
        } else {
            Log.d("TAG", "mWebSocket is NOT null");
            mWebSocket.sendText(msg);
        }
    }

    public void sendAllWaitingMessage(WebSocket webSocket) {
        while (!mMsgWatingQueue.isEmpty()) {
            webSocket.sendText(mMsgWatingQueue.poll());
        }
    }

    public void addMsgToWaitingQueue(String msg) {
        checkAndNewMsgWaitingQueue();

        mMsgWatingQueue.add(msg);
    }

}
