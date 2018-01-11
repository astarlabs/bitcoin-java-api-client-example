package com.astarlabs.api.blockchain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.astarlabs.api.util.Constantes;

import br.com.astarlabs.client.ApiException;
import br.com.astarlabs.client.api.SearchApi;
import br.com.astarlabs.client.api.SendApi;
import br.com.astarlabs.client.model.SingleResult;
import br.com.astarlabs.client.model.Transaction;
import br.com.astarlabs.util.DoubleSha256;
import br.com.astarlabs.util.Token;

/**
 * Esta e uma classe acessoria que encapsula toda logica de iteracao com a API
 * @author asimas
 *
 */
public class Blockchain {

	private String token;
	private Integer account;
	private String user;
	private String pass;
	private Integer id;

	/**
	 * 
	 * @param Arquivo que sera registrado na BLockchain
	 * @return Inteiro contentdo o ID da transação validada
	 * @throws IOException
	 * @throws ApiException
	 */
	public String registrarDocumento(File file) throws IOException, ApiException {
		return registrarDocumento(Files.readAllBytes(file.toPath()));

	}

	/**
	 * 
	 * @param fileString
	 * @return
	 * @throws ApiException
	 */
	public String registrarDocumento(String fileString) throws ApiException {
		return registrarDocumento(fileString.getBytes());
	}

	/**
	 * 
	 * @param bytesFile
	 * @return
	 * @throws ApiException
	 */
	public String registrarDocumento(byte[] bytesFile) throws ApiException {
		String hash = DoubleSha256.hashFile(bytesFile);
		token = getMyToken();

		SendApi api = new SendApi();
		SingleResult singleResult = api.sendHash(token, account, user, pass, hash, Constantes.COIN, Constantes.TESTNET); 

		if (singleResult.getResult() != null && singleResult.getStatus()) {
			return singleResult.getResult();
		}

		return "0";	
	}
	
	/**
	 * 
	 * @param bytesFile
	 * @return
	 * @throws ApiException
	 */
	public Transaction consultarDocumentoPorConteudo(byte[] bytesFile) throws ApiException {
		token = getMyToken();

		SearchApi api = new SearchApi();
		List<Transaction> transactions = api.searchByContent(token, account, user, pass, new String(bytesFile));
		//(token, account, user, pass, hash, Constantes.COIN, Constantes.TESTNET); 

		if (transactions != null && transactions.size() > 0) {
			return transactions.get(0);
		}

		return null;	
	}	

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ApiException
	 */
	public Transaction validarRegistroDocumento(Integer id) throws ApiException {
		token = getMyToken();

		Transaction transaction = new Transaction();
		SearchApi sa = new SearchApi();	
		transaction = sa.searchByAPIID(token, account, user, pass, id);

		return transaction;
	}


	/**
	 * you need generate a token, before any method called
	 * @return
	 */
	private String getMyToken() {
		try {
			this.token = Token.sign(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	} 


	/**
	 * Constructor
	 * @param token
	 * @param account
	 * @param user
	 * @param pass
	 */
	public Blockchain(String token, Integer account, 
			String user, String pass) {
		super();
		this.token = token;
		this.account = account;
		this.user = user;
		this.pass = pass;
	}

}


