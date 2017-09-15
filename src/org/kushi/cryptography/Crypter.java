package org.kushi.cryptography;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Crypter {
	
	private KeyPairGenerator keyGen;
	private KeyPair keyPair;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Cipher cipher;
	
	public Crypter(int size) {
		
		try {
			
			this.keyGen = KeyPairGenerator.getInstance("RSA");
			
			this.keyGen.initialize(size);
			this.keyPair = this.keyGen.generateKeyPair();
			this.publicKey = this.keyPair.getPublic();
			this.privateKey = this.keyPair.getPrivate();
			
			try {
				this.cipher = Cipher.getInstance("RSA");
			} catch (NoSuchPaddingException e) {
				System.out.println("No such padding!");
			}
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm!");
		}
		
	}
	
	public KeyPairGenerator getKeyGen() {
		return keyGen;
	}

	public void setKeyGen(KeyPairGenerator keyGen) {
		this.keyGen = keyGen;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public Cipher getCipher() {
		return cipher;
	}

	public void setCipher(Cipher cipher) {
		this.cipher = cipher;
	}

	public byte[] encrypt(String data) {
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
			return this.cipher.doFinal(data.getBytes());
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Key!");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Illegal Block Size!");
		} catch (BadPaddingException e) {
			System.out.println("Bad Padding!");
		}
		return null;
	}
	
	public byte[] decrypt(byte[] data) {
		
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
			return this.cipher.doFinal(data);
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Key!");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Illegal Block Size!");
		} catch (BadPaddingException e) {
			System.out.println("Bad Padding!");
		}
		return null;
	}
	
}