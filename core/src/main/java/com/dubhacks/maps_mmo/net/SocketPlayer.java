package com.dubhacks.maps_mmo.net;

import java.io.EOFException;
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
    private ObjectInputStream is; // otherwise we have to create in the constructor where it deadlocks
    private final ObjectOutputStream os;
    private final ConcurrentLinkedQueue<Packet> incomingPackets;

    public SocketPlayer(Socket socket) throws IOException {
        this.socket = socket;
        remoteAddress = socket.getRemoteSocketAddress();
        os = new ObjectOutputStream(socket.getOutputStream());
        os.flush();
        incomingPackets = new ConcurrentLinkedQueue<>();
        new Thread(new Reader()).start();
    }

    public void sendPacket(Packet packet) {
        System.out.println("Sending packet of type: " + packet.getClass().getSimpleName());
        try {
            os.writeObject(packet);
            os.flush();
        } catch (IOException e) {
            System.err.println("ERROR: Failed to write packet to " + remoteAddress + ": " + e.getMessage());
        }
    }

    public boolean hasPacket() {
        return !incomingPackets.isEmpty();
    }

    public Packet getNextPacket() {
        return incomingPackets.poll();
    }

    public Socket socket() {
        return socket;
    }

    public void disconnect() {
        try {
            System.out.println("Disconnecting client from: " + this.socket.getRemoteSocketAddress());
            socket.close();
        } catch (IOException e) {
            System.err.println("ERROR disconnecting client " + remoteAddress + ": " + e.getMessage());
        }
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            try {
                is = new ObjectInputStream(socket.getInputStream());
            } catch (EOFException e) {
                SocketPlayer.this.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (!socket.isClosed()) {
                try {
                    Object read = is.readObject();
                    if (read instanceof Packet) {
                        System.out.println("Received packet of type: " + read.getClass().getSimpleName());
                        incomingPackets.add((Packet)read);
                    } else {
                        System.err.println("ERROR: Received object from " + remoteAddress + " that is not a Packet");
                    }
                } catch (IOException e) {
                    System.err.println("ERROR: IO Exception while reading from " + remoteAddress + ": " + e.getMessage());
                    disconnect();
                } catch (ClassNotFoundException e) {
                    System.err.println("ERROR: Unknown object received from " + remoteAddress + ": " + e.getMessage());
                }
            }
        }
    }
}
