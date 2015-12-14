/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.contentcreation.pplive.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *diese klasse codeirt und Strings in MD5-Hashwerte
 * @author daniel
 */
public class MD5Secure {
    
    
    /**
     * codiert einen String in ein md5Hash wert
     * @param encodeString
     * @return 
     */
    public String encode(String encodeString)
    {
        String md5Hex = DigestUtils.md5Hex (encodeString);
        return md5Hex;
    }
    
    /**
     * decodiert einen md5-Hashwert /// nicht implementert
     * @param decodeString
     * @return 
     */
    public String decode(String decodeString)
    {
        return "";
    }
    
    
}
