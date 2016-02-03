/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.websockets.coders;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.util.ObjectAndJSON;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author goesta
 */
public class BacklogArticleDecoder implements Decoder.Text<BacklogArticle> {

    @Override
    public BacklogArticle decode(String message) throws DecodeException {
        return ObjectAndJSON.fromJSON(message);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
