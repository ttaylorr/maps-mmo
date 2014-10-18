package com.dubhacks.map_mmo.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dubhacks.maps_mmo.packets.Packet;

public class SocketPlayer {
    private final Socket socket;
    private final SocketAddress remoteAddress;
    private final ObjectInputStream is;
    private final ObjectOutputStream os;
    private final ConcurrentLinkedQueue<Packet> incomingPackets;

    public SocketPlayer(Socket socket) throws IOException {
        this.socket = socket;
        this.remoteAddress = socket.getRemoteSocketAddress();
        this.is = new ObjectInputStream(socket.getInputStream());
        this.os = new ObjectOutputStream(socket.getOutputStream());
        this.incomingPackets = new ConcurrentLinkedQueue<>();
        new Thread(new Reader()).start();
    }

    public void sendPacket(Packet packet) {
        try {
            this.os.writeObject(packet);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to write packet to " + this.remoteAddress + ": " + e.getMessage());
        }
    }

    public boolean hasPacket() {
        return !this.incomingPackets.isEmpty();
    }

    public Packet getNextPacket() {
        return this.incomingPackets.poll();
    }

    public Socket socket() {
        return this.socket;
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            while (!SocketPlayer.this.socket.isInputShutdown()) {
                try {
                    Object read = SocketPlayer.this.is.readObject();
                    if (read instanceof Packet) {
                        SocketPlayer.this.incomingPackets.add((Packet)read);
                    } else {
                        System.err.println("ERROR: Received object from " + SocketPlayer.this.remoteAddress + " that is not a Packet");
                    }
                } catch (IOException e) {
                    System.err.println("ERROR: IO Exception while reading from " + SocketPlayer.this.remoteAddress + ": " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.err.println("ERROR: Unknown object received from " + SocketPlayer.this.remoteAddress + ": " + e.getMessage());
                }
            }
        }
    }
}
