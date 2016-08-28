package org.fastgame.bbt.event;

import com.neovisionaries.ws.client.WebSocket;

/**
 * Events for {@link org.fastgame.bbt.connectivity.WebSocketLauncher}.
 *
 * @Author MrLi
 * @Since 08/22/2016
 */
public class SocketEvent {
    public static final int SOCKET_ON_STATE_CHANGED = 1;
    public static final int SOCKET_ON_CONNECTED = 2;
    public static final int SOCKET_ON_CONNECT_ERROR = 3;
    public static final int SOCKET_ON_DISCONNECTED = 4;
    public static final int SOCKET_ON_FRAME = 5;
    public static final int SOCKET_ON_CONTINUATION_FRAME = 6;
    public static final int SOCKET_ON_TEXT_FRAME = 7;
    public static final int SOCKET_ON_BINARY_FRAME = 8;
    public static final int SOCKET_ON_CLOSE_FRAME = 9;
    public static final int SOCKET_ON_PING_FRAME = 10;
    public static final int SOCKET_ON_PONG_FRAME = 11;
    public static final int SOCKET_ON_TEXT_MESSAGE = 12;
    public static final int SOCKET_ON_BINARY_MESSAGE = 13;
    public static final int SOCKET_ON_SENDING_FRAME = 14;
    public static final int SOCKET_ON_FRAME_SENT = 15;
    public static final int SOCKET_ON_FRAME_UNSENT = 16;
    public static final int SOCKET_ON_ERROR = 17;
    public static final int SOCKET_ON_FRAME_ERROR = 18;
    public static final int SOCKET_ON_MESSAGE_ERROR = 19;
    public static final int SOCKET_ON_MESSAGE_DECOMPRESSION_ERROR = 20;
    public static final int SOCKET_ON_TEXT_MESSAGE_ERROR = 21;
    public static final int SOCKET_ON_SEND_ERROR = 22;
    public static final int SOCKET_ON_UNEXPECTED_ERROR = 23;
    public static final int SOCKET_HANDLE_CALLBACK_ERROR = 24;
    public static final int SOCKET_ON_SENDING_HANDSHAKE = 25;

    private int type;
    private String text;
    private WebSocket webSocket;

    public SocketEvent(int type) {
        this.type = type;
    }

    public SocketEvent(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public SocketEvent(int type, WebSocket webSocket) {
        this.type = type;
        this.webSocket = webSocket;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
