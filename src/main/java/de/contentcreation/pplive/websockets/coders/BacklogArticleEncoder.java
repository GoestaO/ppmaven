/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.websockets.coders;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.util.ObjectAndJSON;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author goesta
 */
public class BacklogArticleEncoder implements Encoder.Text<BacklogArticle> {

    @Override
    public String encode(BacklogArticle article) throws EncodeException {
        return ObjectAndJSON.toJSON(article);
    }

    @Override
    public void init(EndpointConfig config) {
       
    }

    @Override
    public void destroy() {
    }

}
