package no.mesan.websocket.chat;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class ChatMessageDecoder implements Decoder.Text<ChatMessage> {
    private static final String MESSAGE_TYPE_CHAT = "auth";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatMessage decode(String s) throws DecodeException {
        try {
            return objectMapper.readValue(s.substring(MESSAGE_TYPE_CHAT.length()), ChatMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s.startsWith(MESSAGE_TYPE_CHAT);
    }

    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public void destroy() { }
}
