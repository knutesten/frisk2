package no.mesan.websocket.chat;

import javax.websocket.Session;

interface Message {
    void accept(Session session, MessageVisitor messageVisitor);
}
