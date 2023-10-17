import java.io.IOException;
import java.net.*;

public class ServidorUDP {

    public static void main(String[] args) throws Exception {
 
        System.out.println("Servidor em execução!");

        // Criando as entradas e estabelecendo as portas onde as mensagens serão passadas (aleatoriamente)
        try (DatagramSocket tomadaServidora1 = new DatagramSocket();
             DatagramSocket tomadaServidora2 = new DatagramSocket();
             DatagramSocket tomadaServidora3 = new DatagramSocket()) {

            int portaServidora1 = tomadaServidora1.getLocalPort();
            int portaServidora2 = tomadaServidora2.getLocalPort();
            int portaServidora3 = tomadaServidora3.getLocalPort();

            // Mostrar as portas criadas na tela (apenas por organização)
            System.out.println("Porta do servidor 1: " + portaServidora1);
            System.out.println("Porta do servidor 2: " + portaServidora2);
            System.out.println("Porta do servidor 3: " + portaServidora3);

            // Receber as mensagens/operações enviadas pelo cliente
            DatagramSocket[] tomadasServidoras = {tomadaServidora1, tomadaServidora2, tomadaServidora3};

            while (true) {
                for (DatagramSocket tomadaServidora : tomadasServidoras) {
                    byte[] cartaAReceber = new byte[100];
                    DatagramPacket envelopeAReceber = new DatagramPacket(cartaAReceber, cartaAReceber.length);

                    tomadaServidora.receive(envelopeAReceber);

                    /* //
                    // Verificar se a mensagem recebida é "encerrar",
                    // para que a conexão possa ser finalizada
                    // tanto no Cliente quanto no Servidor. 
                    */ //

                    String textoRecebido = new String(envelopeAReceber.getData()).trim();
                    if (textoRecebido.equalsIgnoreCase("encerrar")) {
                        System.out.println("Servidor encerrado.");
                        tomadaServidora1.close();
                        tomadaServidora2.close();
                        tomadaServidora3.close();
                        System.exit(0); // Encerrar o programa
                    }

                    // Processar a mensagem, separando os valores como String e transformando para Float
                    String[] mudartexto = textoRecebido.split(" ");
                    float operador1 = Float.parseFloat(mudartexto[0]);
                    float operador2 = Float.parseFloat(mudartexto[2]);

                    // O array [1], será o que reconhecerá a operação (adicionada a operação de "-")
                    float resultado = 0;
                    switch (mudartexto[1]) {
                        case "+":
                            resultado = operador1 + operador2;
                            break;
                        case "-":
                            resultado = operador1 - operador2;
                            break;
                        case "*":
                            resultado = operador1 * operador2;
                            break;
                        case "/":
                            resultado = operador1 / operador2;
                            break;
                    }

                    // Enviar resposta para o cliente
                    byte[] cartaAEnviar = ("Resultado: " + resultado).getBytes();
                    InetAddress ipCliente = envelopeAReceber.getAddress();
                    int portaCliente = envelopeAReceber.getPort();
                    DatagramPacket envelopeAEnviar = new DatagramPacket(cartaAEnviar, cartaAEnviar.length, ipCliente, portaCliente);
                    tomadaServidora.send(envelopeAEnviar);

                    // Limpar o buffer para a próxima mensagem
                    cartaAReceber = new byte[100];
                }
                
                // Definindo o tempo para o envio de cada mensagem e para finalizar a conexão
                Thread.sleep(2000);
                tomadaServidora1.close();
                tomadaServidora2.close();
                tomadaServidora3.close();
                System.exit(0);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}