package ru.otus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteSeqServiceGrpc;
import ru.otus.protobuf.generated.RequestMessage;
import ru.otus.protobuf.generated.ResponseMessage;

import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8090;
    private static final long TIME_TO_SLEEP = 1000; //ms

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        final Long[] currentDiff = {0L};

        CountDownLatch latch = new CountDownLatch(1);
        RemoteSeqServiceGrpc.RemoteSeqServiceStub newStub = RemoteSeqServiceGrpc.newStub(channel);
        newStub.getSequence(
                RequestMessage.newBuilder().setFirstValue(0).setLastValue(30).build(),
                new StreamObserver<ResponseMessage>() {
                    @Override
                    public void onNext(ResponseMessage um) {
                        latch.countDown();
                        synchronized (currentDiff[0]) {
                            currentDiff[0] = um.getNumber();
                            System.out.printf("{server number: %d}%n", currentDiff[0]);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("\tSequence ended!");
                    }
                }
        );

        Long currentValue = 0L;

        for (int i = 0; i < 50; i++) {
            synchronized (currentDiff[0]) {
                currentValue = currentValue + currentDiff[0] + 1;
                System.out.printf("currentValue: %d, currentDiff: %d%n", currentValue, currentDiff[0]);
                currentDiff[0] = 0L;
            }
            latch.await();
            sleep(TIME_TO_SLEEP);
        }

        channel.shutdown();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
