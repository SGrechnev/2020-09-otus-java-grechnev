package ru.otus;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.RemoteNumberServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8090;

    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteNumberServiceImpl remoteDBService = new RemoteNumberServiceImpl();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}