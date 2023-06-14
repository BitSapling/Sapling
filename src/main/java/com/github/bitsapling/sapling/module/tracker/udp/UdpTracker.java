package com.github.bitsapling.sapling.module.tracker.udp;

import com.github.bitsapling.sapling.module.tracker.exception.AnnounceException;
import com.github.bitsapling.sapling.util.BencodeUtil;
import com.github.bitsapling.sapling.util.ByteUtil;
import com.github.bitsapling.sapling.util.IPUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Random;

@Service
public class UdpTracker {
    private static final Random rand = new Random();
    private static final int ACTION_CONNECTION = 0;
    private static final int ACTION_ANNOUNCE = 1;
    private static final int ACTION_SCRAPE = 2;
    private static final int ACTION_ERROR = 3;
    int port = 5357;

    private static final long PROTOCOL_ID = 0x41727101980L;

    @Bean
    public IntegrationFlow processUniCastUdpMessage() {
        return IntegrationFlow
                .from(new UnicastReceivingChannelAdapter(port))
                .handle("UdpTracker", "handleMessage")
                .get();
    }

    HashSet<Long> connections = new HashSet<>();

    public void handleMessage(Message<byte[]> message) {
        var data = message.getPayload();
        if (data.length < 16) {
            // Ignore packets < 16
            return;
        }
        var connection = ByteUtil.byte2long(data);
        if (connection != PROTOCOL_ID && !connections.contains(connection)) {
            return;
        }

        var action = ByteUtil.byte2int(data, 8);
        var socket = (SocketAddress) message.getHeaders().get(IpHeaders.PACKET_ADDRESS);
        if (!(socket instanceof InetSocketAddress)) {
            return;
        }
        var ip = IPUtil.fromSocketAddress(socket);
        if (ip == null) {
            return;
        }
        var port = ((InetSocketAddress) socket).getPort();
        var transaction = ByteUtil.byte2int(data, 12);

        UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler(ip, port);
        byte[] response;
        try {
            switch (action) {
                case 0:
                    response = connection(transaction);
                    break;
                case 1:
                    response = announce(ip, port, transaction, data);
                    break;
                case 2:
                    response = scrape(ip, port, transaction, data);
                    break;
                default:
                    return;
            }
        } catch (AnnounceException e) {
            response = error(transaction, e.getMessage());
        }
        handler.handleMessage(MessageBuilder.withPayload(response).build());
    }

    public byte[] connection(int transaction) {
        var response = new byte[16];
        ByteUtil.int2byte(ACTION_CONNECTION, response, 0);
        ByteUtil.int2byte(transaction, response, 4);
        var connection = rand.nextLong();
        connections.add(connection);
        ByteUtil.long2byte(connection, response, 8);
        return response;
    }

    public byte[] announce(String ip, int port, int transaction, byte[] data) throws AnnounceException {
        var response = new byte[16];
        return response;

    }

    public byte[] scrape(String ip, int port, int transaction, byte[] data) throws AnnounceException {
        var response = new byte[16];
        return response;

    }

    public byte[] error(int transaction, String reason) {
        var message = BencodeUtil.convertToByteArray(reason);
        var response = new byte[8 + message.length];
        ByteUtil.int2byte(ACTION_ERROR, response, 0);
        ByteUtil.int2byte(transaction, response, 4);
        System.arraycopy(message, 0, response, 8, message.length);
        return response;
    }
}
