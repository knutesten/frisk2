package no.mesan.websocket.chat;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class AuthMessageDecoder implements Decoder.Text<AuthMessage> {
    private static final String MESSAGE_TYPE_AUTH = "auth";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthMessage decode(String s) throws DecodeException {
        try {
            return objectMapper.readValue(s.substring(MESSAGE_TYPE_AUTH.length()), AuthMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s.startsWith(MESSAGE_TYPE_AUTH);
    }

    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public void destroy() { }
}
