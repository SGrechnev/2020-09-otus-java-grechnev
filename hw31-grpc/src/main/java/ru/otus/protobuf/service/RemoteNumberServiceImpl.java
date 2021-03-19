package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteSeqServiceGrpc;
import ru.otus.protobuf.generated.RequestMessage;
import ru.otus.protobuf.generated.ResponseMessage;

public class RemoteNumberServiceImpl extends RemoteSeqServiceGrpc.RemoteSeqServiceImplBase {

    final static long TIME_TO_SLEEP = 2000; // ms

    @Override
    public void getSequence(RequestMessage request, StreamObserver<ResponseMessage> responseObserver) {
        long firstValue = request.getFirstValue();
        long lastValue = request.getLastValue();

        if (firstValue >= lastValue) {
            throw new IllegalArgumentException("firstValue (" + firstValue + ") is greater than or equal lastValue (" + lastValue + ')');
        }

        for (long value = firstValue + 1; value < lastValue + 1; value++) {
            System.out.printf("send %d%n", value);
            responseObserver.onNext(long2ResponseMessage(value));
            sleep(TIME_TO_SLEEP);
        }
        responseObserver.onCompleted();
    }

    private ResponseMessage long2ResponseMessage(long value) {
        return ResponseMessage.newBuilder()
                .setNumber(value)
                .build();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}