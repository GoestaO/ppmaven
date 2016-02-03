/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.websockets;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.websockets.coders.BacklogArticleDecoder;
import de.contentcreation.pplive.websockets.coders.BacklogArticleEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author goesta
 */
@ApplicationScoped
@ServerEndpoint(value = "/primepush/backlogChannel", decoders = {BacklogArticleDecoder.class}, encoders = {BacklogArticleEncoder.class})
public class BacklogServerEndpoint {

//    private static Map<Integer, Set<Session>> sessions = Collections.synchronizedMap(new HashMap<Integer, Set<Session>>());
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

//    @OnOpen
//    public void onOpen(Session session, @PathParam("partnerArray") String partnerArray) {
//        List<Integer> partnerList = QueryHelper.StringParameterToIntegerList(partnerArray);
//        for (Integer partnerId : partnerList) {
//            sessions.get(partnerId).add(session);
//        }
//    }
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnError
    public void onError(Throwable t) {
    }

//    @OnClose
//    public void onClose(Session session, @PathParam("partnerArray") String partnerArray) {
//        List<Integer> partnerList = QueryHelper.StringParameterToIntegerList(partnerArray);
//        for (Integer partnerId : partnerList) {
//            sessions.get(partnerId).remove(session);
//        }
//    }
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(BacklogArticle article) {
        System.out.println("article = " + article);
        for (Session session : sessions) {
            session.getAsyncRemote().sendObject(article);
        }
    }

//    private Set<Session> getSessionsForPartner(Integer partnerId) {
//        return sessions.get(partnerId);
//    }

//    private Collection<Set<Session>> getAllSessions(){
//        return sessions.values().;
//    }
}
