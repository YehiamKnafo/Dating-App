package forrealdatingapp;

import forrealdatingapp.utilities.RouterUtils;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URI;

public class WebSocket {
    public enum websocketio{
        INSTANCE;
        public Socket socketIoInstance;

        public void connectToServer(){
            URI connectionUrl = URI.create(RouterUtils.getHost());

            try {
                IO.Options options = IO.Options.builder()
                        // ...
                        .build();
                socketIoInstance = IO.socket(connectionUrl, options);

                socketIoInstance.connect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }




}
