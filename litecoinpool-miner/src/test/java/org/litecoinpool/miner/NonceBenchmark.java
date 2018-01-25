package org.litecoinpool.miner;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@OperationsPerInvocation
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Thread)
@Threads(1)
@Fork(2)
public class NonceBenchmark {
    @GenerateMicroBenchmark
    public void reverseHex() {
    	for (Nonce nonce : Nonce.values()) {
    		Nonce.reverseHex(nonce.toString());
        }
    }

    @GenerateMicroBenchmark
    public void nonceFromString() {
    	for (Nonce nonce : Nonce.values()) {
    		Nonce.nonce(nonce.toString());
        }
    }

    @GenerateMicroBenchmark
    public void nonceFromInteger() {
    	for (Nonce nonce : Nonce.values()) {
    		Nonce.nonce(nonce.getValue());
        }
    }
    
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include( ".*" + NonceBenchmark.class.getSimpleName() + ".*" )
                .build();
        new Runner( opt ).run();
    }
}