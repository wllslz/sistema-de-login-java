package br.com.senac.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;        
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordService{

 private static  final String ALGORITHM ="PBKDF2WithHmacSHA256";
 
 private static final int ITERATIONS= 210000;
 //quantas vezes o bichao vai repetir o calculo
 private static final int SALT_LENGHT= 16;

 ///tamanho do salt em bytes ou bits? checar depois...//

 private static final int KEY_LENGHT=256 ;
 //tamando do hash no final 
 public record HashedPassword(String hash, String salt) {}
//nao esquecer de botar isso na documentação 
//gerar o salt: salt é tipo um aleatorizador de array de bites, cada um pra cada cadastro
//ate onde entendi: salt+hash=sistema de senhas
private byte [] generateSalt() {
    
    SecureRandom gerador = new SecureRandom();

    byte[] token = new byte[16];

    gerador.nextBytes(token);
//o byte devolve o hash char recebe a senha e o byte salt recebe o salt, tipo bater whey com ovo
  return token; }

private byte[] derive(char[] password, byte[] salt) {
  //se esse pbekeyspec der certo ele vai ser o responsavel pela ficha das senhas, tamanho do hash e tudo mais   
  PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGHT);
    try {
        SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ALGORITHM);
        return fabrica.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        throw new IllegalStateException("erro de senha", e);
    } finally { 
  spec.clearPassword(); 
}
//usar finally pra apagar a copia da senha na memoria
}

public HashedPassword hashPassword(char[] password) {
    byte[] salt = generateSalt();
    byte[] hash = derive(password, salt);

    String hashTexto = Base64.getEncoder().encodeToString(hash);
    String saltTexto = Base64.getEncoder().encodeToString(salt);

    return new HashedPassword(hashTexto, saltTexto);
}


// NAO MECHER NESSA BAGAÇA ^

//o mais difficl ja foi, no papel agr o sistema pega o salt, converte p bite
//calcula o hash usando o mesmo salt, compara com q ja ta la e ai comprova

public boolean verifyPassword(char[] password, String storedHash, String storedSalt) {
    byte[] salt = Base64.getDecoder().decode(storedSalt );
    byte[] expected = Base64.getDecoder().decode(storedHash);
    byte[] actual = derive(password, salt) ;
    return MessageDigest.isEqual(expected ,actual );
//  o decoder converte essa bagaça de volta pra byte, o actual é o hesh refeito
// e ai tem o expected e o actual, autoexplicativo
}
}