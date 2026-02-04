package forrealdatingapp;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.apache.hc.core5.util.Args;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import static java.util.Collections.singletonMap;

public class WebSocket {
    public enum websocketio{
        INSTANCE;
        private URI connectionUrl = URI.create("http://localhost:3000");
        public Socket socketIoInstance;

        public void connectToServer(){

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
