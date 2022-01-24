/**
 *
 */
package com.microservice.auth.service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

/**
 * @author root
 *
 */
public interface RsaService {

	String encrypt(String msg) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

	String decrypt(String msg) throws Exception;

}
