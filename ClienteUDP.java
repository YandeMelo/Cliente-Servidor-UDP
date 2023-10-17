import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {

    public static void main(String args[]) throws Exception {

        //criando as entradas para a troca de informação
        try (DatagramSocket entrada1 = new DatagramSocket();
             DatagramSocket entrada2 = new DatagramSocket();
             DatagramSocket entrada3 = new DatagramSocket()) {

            //estabelecer uma conexão com o host
            InetAddress ip = InetAddress.getLocalHost();

            // Código para obter as mensagens via teclado
            // Recebendo a mensagem/operação em uma variável
            System.out.println("Se deseja encerrar a conexão, digite encerrar");
            System.out.print("Primeira operacao (exemplo: 1 + 2): ");
            Scanner teclado = new Scanner(System.in);
            String mensagem1 = teclado.nextLine();

            // Enviar a mensagem/operação para o servidor
            byte[] processarMensagem1 = mensagem1.getBytes();
            DatagramPacket compactarMensagem1 = new DatagramPacket(processarMensagem1, processarMensagem1.length, ip, 5001);
            entrada1.send(compactarMensagem1);

            // Finalizar a conexão caso a mensagem seja "encerrar"
            if (mensagem1.equalsIgnoreCase("encerrar")) {
                entrada1.close();
                entrada2.close();
                entrada3.close();
                System.exit(0);
            }

            // Recebendo a segunda mensagem/operação
            System.out.print("Segunda operacao (exemplo: 1 * 2): ");
            String mensagem2 = teclado.nextLine();

            // Enviando novamente para o servidor
            byte[] processarMensagem2 = mensagem2.getBytes();
            DatagramPacket compactarMensagem2 = new DatagramPacket(processarMensagem2, processarMensagem2.length, ip, 5002);
            entrada2.send(compactarMensagem2);

            // Caso seja "encerrar", finalizar a conexão
            if (mensagem2.equalsIgnoreCase("encerrar")) {
                entrada1.close();
                entrada2.close();
                entrada3.close();
                System.exit(0);
            }

            // Por fim, a terceira mensagem/operação
            System.out.print("Terceira operacao (exemplo: 1 / 2): ");
            String mensagem3 = teclado.nextLine();

            // Enviando para o servidor
            byte[] processarMensagem3 = mensagem3.getBytes();
            DatagramPacket compactarMensagem3 = new DatagramPacket(processarMensagem3, processarMensagem3.length, ip, 5003);
            entrada3.send(compactarMensagem3);

            // Finalizar a conexão caso a mensagem seja "encerrar"
            if (mensagem3.equalsIgnoreCase("encerrar")) {
                entrada1.close();
                entrada2.close();
                entrada3.close();
                System.exit(0);
            }

            // Receber as respostas do servidor
            // Retornar a resposta das mensagens/operações ao cliente
            DatagramSocket[] entradasParaOServidor = {entrada1, entrada2, entrada3};

            for (DatagramSocket entradaServidor : entradasParaOServidor) {
                byte[] cartaAReceber = new byte[100];
                DatagramPacket envelopeAReceber = new DatagramPacket(cartaAReceber, cartaAReceber.length);
                entradaServidor.receive(envelopeAReceber);
                String mensagemRecebida = new String(envelopeAReceber.getData());
                System.out.println(mensagemRecebida);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}