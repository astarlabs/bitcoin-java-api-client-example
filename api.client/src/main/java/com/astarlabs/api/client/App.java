package com.astarlabs.api.client;

import static com.astarlabs.api.util.Constantes.API_PASS;
import static com.astarlabs.api.util.Constantes.API_USER;
import static com.astarlabs.api.util.Constantes.MY_API_ACCOUNT;
import static com.astarlabs.api.util.Constantes.PRIVATE_KEY;
import static com.astarlabs.api.util.Constantes.ZERO;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.astarlabs.api.blockchain.Blockchain;

import br.com.astarlabs.client.model.Transaction;

/**
 * Esta é uma classe de exemplo contendo todos os metodos da API.
 * @author asimas
 * @since
 * @version 1.0
 */
public class App {
	
	
	private String numeroProtocolo;
	
	public static void main(String[] args) {
		System.out.println("Registrar um documento existente");
		
		App application = new App();
		
//		application.registrarNovoDocumento();
		
		application.registrarDocumentoExistente();
		
//		application.consultarPorProtocolo(1832);
	}
	
	/**
	 * Utilize este método para registrar um documento sempre novo.<br/>
	 * Este método só pode ser utilizado para teste, uma vez que gera um arquivo contendo os milisegundos<br/>
	 */
	private void registrarNovoDocumento() {
		File arquivoNaoRegistrado = null;
		
		try{
			
			arquivoNaoRegistrado =  File.createTempFile("teste", "txt");
			arquivoNaoRegistrado.createNewFile();
			
			FileUtils.writeStringToFile(arquivoNaoRegistrado, "Hello File " + System.currentTimeMillis(),  Charset.defaultCharset(), false);
		}catch(Exception e){
			System.out.println("O arquivo não pode ser criado");
			return;
		}
		
		try{
			byte[] bytes = Files.readAllBytes(arquivoNaoRegistrado.toPath());
			Blockchain blockchain = new Blockchain(PRIVATE_KEY, MY_API_ACCOUNT, API_USER, API_PASS);
			numeroProtocolo = blockchain.registrarDocumento(bytes);

			if(numeroProtocolo != null && !"".equals(numeroProtocolo)){
				System.out.println("Este é o seu número para consulta posterior: ");
				System.out.println(numeroProtocolo);
				System.out.println("A API retornou um protocolo, em seguida chame o método consultarPorProtolco("+numeroProtocolo+")");
			}else{
				System.out.println("Não houve retorno da API, contacte o administrador, ou tente novamente mais tarde..");
			}
			
		}catch(Exception e){
			System.out.println("O arquivo não pode ser carregado");
			return;
		}
		
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * Este metodo faz uma tentativa de registro de um arquivo que já foi registrado.
	 */
	private void registrarDocumentoExistente(){
		
		try{
			ClassLoader classLoader = getClass().getClassLoader();
			File arquivoJaRegistrado = new File(classLoader.getResource("already_registered.csv").getFile());
			
			byte[] bytes = Files.readAllBytes(arquivoJaRegistrado.toPath());
			
			Blockchain blockchain = new Blockchain(PRIVATE_KEY, MY_API_ACCOUNT, API_USER, API_PASS);
			numeroProtocolo = blockchain.registrarDocumento(bytes);
			
			/**
			 * Como a API eh assincrona, voce sempre deve verificar se houve retorno
			 */
			if(numeroProtocolo != null && !"".equals(numeroProtocolo)){
				System.out.println("Este é o seu número para consulta posterior: ");
				System.out.println(numeroProtocolo);
				
				/**
				 * Se o protocolo retornar zerado, eh pq estes bytes já foram registrados
				 */
				if(ZERO.equals(numeroProtocolo)){
					System.out.println("Nenhum protocolo foi retornado, parece que este arquivo já foi registrado, vamos fazer uma consulta por conteudo...");
					consultarPorConteudo(bytes);
				}else{
					System.out.println("A API retornou um protocolo, em seguida chame o método consultarPorProtolco("+numeroProtocolo+")");
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Este método faz uma consulta de registro por conteúdo
	 * @param bytes
	 */
	private void consultarPorConteudo(byte[] bytes) {
		System.out.println("Tudo pronto para consultar o registro, passando os bytes do arquivo...");
		 Blockchain blockchain = new Blockchain(PRIVATE_KEY, MY_API_ACCOUNT, API_USER, API_PASS);	
		 
		 /**
		  * Este método retorna as informações do registro.
		  * É possivel que ele não retorne o Hash da transação, caso o arquivo ainda não tenha sido registrada na rede Blockchain
		  */
		 Transaction transacao = null;
		 
		 try{
			 transacao = blockchain.consultarDocumentoPorConteudo(bytes);
		 }catch(Exception e ){
			 System.out.println("Ocorreu um erro na chanada da API. API está fora do ar.");
			 e.printStackTrace();
			 return;
		 }
		 
		 /**
		  * Verifico se ainda não tem o id da blockchain
		  */
		 System.out.println("Este é o protocolo da transacao: " + new Long(getId(transacao)));
		 
		 /**
		  * A transacao foi retornada
		  */
		 if(transacao != null && transacao.getTxid() != null ){
			 try{
				 
				 System.out.println("Este é o hash da transacao: " + transacao.getTxid());
				 System.out.println("Este é o timestamp da transacao: " + transacao.getBlockchaincreationdate());
				 
			 }catch(Exception e){}
		 }
		
	}
	
	/**
	 * Utilize este metodo para fazer a consulta após receber um número de protocolo
	 * @param idProtocolo
	 */
	private void consultarPorProtocolo(Integer idProtocolo){
		
		/**
		 * Caso você se esqueça de passar um número de protocolo, utilizamos um número existente
		 */
		if(idProtocolo == null){
			idProtocolo = 1831;
		}
		
		try {

			Blockchain blockchain = new Blockchain(PRIVATE_KEY, MY_API_ACCOUNT,	API_USER, API_PASS);
			Transaction transacao = blockchain.validarRegistroDocumento(idProtocolo);

			if(transacao == null){
				System.out.println("Não houve retorno da API, efetuar a chamada novamente em alguns instantes");
				return;
			}
			
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			

			System.out.println("_________.__                 __          .__           .__      ");
			System.out.println("\\______  \\  |   ____   ____ |  | __ ____ |  |__ _____  |__| ____");  
			System.out.println("|    |  _/  |  /  _ \\_/ ___\\|  |/ // ___\\|  |  \\\\__  \\ |  |/    \\"); 
			System.out.println("|    |   \\  |_(  <_> )  \\___|    <\\  \\___|   Y  \\/ __ \\|  |   |  \\");
			System.out.println("|______  /____/\\____/ \\___  >__|_ \\___  >___|  (____  /__|___|  /");
			System.out.println("\\/                 \\/     \\/    \\/     \\/     \\/        \\/"); 

			
			System.out.println("");
			System.out.println("Hash da rede Blockchain: " + transacao.getTxid());
			System.out.println("Número do protocolo: " + new Long(getId(transacao)));
			System.out.println("Data de criacao na Blockchain: " + transacao.getBlockchaincreationdate());
			System.out.println("Numero de Confirmacoes na rede Blockchain (ideal acima de 3): " + transacao.getConfirmations());
			System.out.println("");
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um ero na consulta por Protocolo");
		}			
		
	}
	
	/**
	 * 
	 * @param transacao
	 * @return
	 */
	public Integer getId(Transaction transacao) {
		try {
			Integer o = (Integer) FieldUtils.readField(transacao, "id", true);
			return o;
		}catch(Exception e) {}

		return 0;
	}
	
}
