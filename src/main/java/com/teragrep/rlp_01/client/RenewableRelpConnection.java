package com.teragrep.rlp_01.client;

import java.io.IOException;
import java.time.Instant;
import java.time.Period;

public class RenewableRelpConnection implements IManagedRelpConnection {

    private final IManagedRelpConnection managedRelpConnection;
    private final Period maxIdle;
    private Instant lastAccess;

    public RenewableRelpConnection(IManagedRelpConnection managedRelpConnection, Period maxIdle) {
        this.managedRelpConnection = managedRelpConnection;
        this.maxIdle = maxIdle;
        this.lastAccess = Instant.ofEpochSecond(0);
    }

    @Override
    public void reconnect() {
        managedRelpConnection.reconnect();
    }

    @Override
    public void ensureSent(byte[] bytes) {
        if (lastAccess.plus(maxIdle).isAfter(Instant.now())) {
            reconnect();
        }
        lastAccess = Instant.now();
        managedRelpConnection.ensureSent(bytes);
    }

    @Override
    public boolean isStub() {
        return managedRelpConnection.isStub();
    }

    @Override
    public void close() throws IOException {
        managedRelpConnection.close();
    }
}