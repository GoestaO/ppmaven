/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.websockets;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.websockets.coders.BacklogArticleDecoder;
import de.contentcreation.pplive.websockets.coders.BacklogArticleEncoder;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author goesta
 */
@ClientEndpoint(decoders = BacklogArticleDecoder.class, encoders = BacklogArticleEncoder.class)
public class BacklogClientEndpoint {

    private Session userSession = null;

    private BacklogArticleMessageHandler backlogArticleMessageHandler;

    public void closeConection() throws IOException {
       if(userSession !=null){
           userSession.close();
       }
    }

    public static interface BacklogArticleMessageHandler {

        public void handleMessage(BacklogArticle article);
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) throws IOException {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(final BacklogArticle article) {
        System.out.println("client = " + article);
        if (article != null) {
            backlogArticleMessageHandler.handleMessage(article);
        }
    }

    public void sendMessage(BacklogArticle article) {
        userSession.getAsyncRemote().sendObject(article);
    }

    public BacklogClientEndpoint(final URI endpointURI) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, endpointURI);
        } catch (DeploymentException ex) {
            Logger.getLogger(BacklogClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BacklogClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBacklogArticleMessageHandler(BacklogArticleMessageHandler messageHandler) {
        this.backlogArticleMessageHandler = messageHandler;
    }
    
    

}
