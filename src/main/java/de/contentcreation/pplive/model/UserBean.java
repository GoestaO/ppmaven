package de.contentcreation.pplive.model;

import de.contentcreation.pplive.util.QueryHelper;
import de.contentcreation.pplive.websockets.BacklogClientEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 * Diese Bean repräsentiert die Session eines eingeloggten Users und hält diesen
 * Zustand nebst gewählte Partner fest
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    private int id;
    private String nick;
    private String vorname;
    private String nachname;
    private String passwort;
    private boolean isValid;
    private User user;
    private List<Integer> partnerList;
    private BacklogClientEndpoint client;
    private String urlEnd;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getVorname() {
        return this.vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return this.nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean newValid) {
        this.isValid = newValid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Integer> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<Integer> partnerList) {
        this.partnerList = partnerList;
    }

    public void keepSessionAlive() {

    }

    public BacklogClientEndpoint getClient() {
        return client;
    }

    public void setClient(BacklogClientEndpoint client) {
        this.client = client;
    }

    public String getUrlEnd() {
        return urlEnd;
    }

    public void setUrlEnd(String urlEnd) {
        this.urlEnd = urlEnd;
    }

    public void sessionIdleListener() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('sessionExpiredConfirmation').show()");
    }

//    @PreDestroy
//    public void sessionExpiredMessage() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.execute("PF('sessionExpiredConfirmation').show()");
//    }
    
    
//    WebSocket -Verbindung aufbauen

    public void connectWebSocket(String websocketChannel, List<Integer> partnerList) throws URISyntaxException {
        String pathParameter = QueryHelper.IntegerListToStringParameter(partnerList);
        this.setUrlEnd(pathParameter);
        try {
            if (client != null) {
                client.closeConection();
            }

        } catch (IOException ex) {

            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        client = new BacklogClientEndpoint(new URI(websocketChannel));
         
        
        this.setClient(client);
    }
}
