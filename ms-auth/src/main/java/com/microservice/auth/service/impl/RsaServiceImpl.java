package com.microservice.auth.service.impl;

import com.microservice.auth.service.RsaService;
import com.microservice.auth.utils.RsaHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

/**
 * @author
 *
 */
@Slf4j
@Service()
public class RsaServiceImpl implements RsaService {

	RsaHelper rsaHelper;

	@PostConstruct
	public void init() {
//		RsaHelper rsaHelper = new RsaHelper();
//		System.out.println(rsaHelper.toString());

		String PemPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDymMZuLibuQRFTltjv/IcHuJuq5UplAeABx+yANGZubPRJ77Ne1a+JW5x++JXmVsntOlOlwZlCsGZTldV8HUITwBAl5PUZhV6QZVOCqcqoVKJ3afxJZB2+U00tC7Mo3h5mLkprGLfpW03uw975yMOpLaHzAyVKVKurIzCnBI9BfQIDAQAB";
		String PemPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPKYxm4uJu5BEVOW2O/8hwe4m6rlSmUB4AHH7IA0Zm5s9Envs17Vr4lbnH74leZWye06U6XBmUKwZlOV1XwdQhPAECXk9RmFXpBlU4KpyqhUondp/ElkHb5TTS0LsyjeHmYuSmsYt+lbTe7D3vnIw6ktofMDJUpUq6sjMKcEj0F9AgMBAAECgYAOVL8YICrVlUtwPC+JkcnqR1oO0OfBqsgwYr6SXkYUzOPrgDkA56+sdhh7GbVFO6hHFtR9LMAgg+ovkuDfKZ1PDuwQjnWLJQIFi/s6+t3YSRwioXT8e4jefK82Q98KVGLS2yfPvUPvO2QRwPwaxaDN/Nu/ttwx2O6VPyyJ2Vjo0wJBAPMGotjECE2S+FJwtiLonh02KW38knYop+ZolGnOq+nT3ghjcazrGJfhpEoQhkbE8uJskPa85kY/M9utrikKELcCQQD/jEYlR3n9CGy6qGnt4hAaw3DCdos4UuqdUNhN9e5+lHFWKW+lanSyaL5jVWTQFSdSiF021R5R6HhaeGx7uONrAkEAq2PztuHRZTwQKrvYh5f74mgPIcUtWW1ks8bvXAtoDFjhzpKrWaLXTk3Qunca64/8JxkfxxMDZ2ktXnEHUWQK0wJASCENw4PzOKpiFiVnEM/X/9XeQ+U10oYE1rZ09zxNPskjXFCBxMzRd3H9GwiFVf5ChRccSFfLIQ9euRp4dEtxVQJBAIstSPODIJVwCm1AjYBudi7ZJbRJnKupTTY0+j7IJvDdG94dvqfVW+h2bLufivs70Qs2aQ9n6QE0lN77RwOVK0M=";

		String publicExponent = "10001";
		String modulus = "f298c66e2e26ee41115396d8effc8707b89baae54a6501e001c7ec8034666e6cf449efb35ed5af895b9c7ef895e656c9ed3a53a5c19942b0665395d57c1d4213c01025e4f519855e90655382a9caa854a27769fc49641dbe534d2d0bb328de1e662e4a6b18b7e95b4deec3def9c8c3a92da1f303254a54abab2330a7048f417d";
		String privateExponent = "e54bf18202ad5954b703c2f8991c9ea475a0ed0e7c1aac83062be925e4614cce3eb803900e7afac76187b19b5453ba84716d47d2cc02083ea2f92e0df299d4f0eec108e758b2502058bfb3afaddd8491c22a174fc7b88de7caf3643df0a5462d2db27cfbd43ef3b6411c0fc1ac5a0cdfcdbbfb6dc31d8ee953f2c89d958e8d3";
		rsaHelper = new RsaHelper(modulus, publicExponent, privateExponent);
	}

	@Override
	public String encrypt(String msg) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return rsaHelper.encrypt(msg);
	}

	@Override
	public String decrypt(String msg) throws Exception {
		try {
			return rsaHelper.decrypt(msg);
		} catch (Exception e) {
			log.error("RSA解密失败, Exception:" + e.getMessage());
			throw e;
		}
	}

}
