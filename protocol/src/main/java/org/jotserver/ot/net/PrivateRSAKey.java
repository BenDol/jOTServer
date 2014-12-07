package org.jotserver.ot.net;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;

public class PrivateRSAKey extends RSAPrivateKeySpec implements RSAPrivateKey {
	private static final long serialVersionUID = 1L;

	// 
	private final static BigInteger p = new BigInteger("14299623962416399520070177382898895550795403345466153217470516082934737582776038882967213386204600674145392845853859217990626450972452084065728686565928113");
	
	//
	private final static BigInteger q = new BigInteger("7630979195970404721891201847792002125535401292779123937207447574596692788513647179235335529307251350570728407373705564708871762033017096809910315212884101");
	
	// Private key exponent
	private final static BigInteger d = new BigInteger("46730330223584118622160180015036832148732986808519344675210555262940258739805766860224610646919605860206328024326703361630109888417839241959507572247284807035235569619173792292786907845791904955103601652822519121908367187885509270025388641700821735345222087940578381210879116823013776808975766851829020659073");
	
	
	private final static BigInteger modulus = p.multiply(q);
	
	private static PrivateRSAKey _instance = null;
	public static PrivateRSAKey getInstance() {
		if(_instance == null) {
			_instance = new PrivateRSAKey(modulus, d);
		}
		return _instance;
	}
	
	public PrivateRSAKey(BigInteger modulus, BigInteger privateExponent) {
		super(modulus, privateExponent);
	}

	
	public String getAlgorithm() {
		return "RSA";
	}

	
	public byte[] getEncoded() {
		return null;
	}

	
	public String getFormat() {
		return null;
	}
	
}
