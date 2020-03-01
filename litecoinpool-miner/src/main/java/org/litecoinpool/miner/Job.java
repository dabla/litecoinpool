package org.litecoinpool.miner;

import java.security.DigestException;

import static org.litecoinpool.miner.Hasher.NO_HASHER;
import static org.litecoinpool.miner.TargetMatcher.DEFAULT_MATCHER;

public class Job {
    static final Job NO_JOB = new Job(null, DEFAULT_MATCHER, NO_HASHER);
    private final String jobId;
    private final TargetMatcher target;
    private Hasher hasher;

    private Job(String jobId, TargetMatcher target, Hasher hasher) {
        this.jobId = jobId;
        this.target = target;
        this.hasher = hasher;
    }

    static Job job(String jobId, TargetMatcher target, Hasher hasher) {
        return new Job(jobId, target, hasher);
    }

    public String getJobId() {
        return jobId;
    }

    public byte[] hash(Nonce nonce) throws DigestException {
        return hasher.hash(nonce);
    }

    public boolean matches(byte[] hash) {
        return target.matches(hash);
    }

    public String getExtranonce2() {
        return hasher.getExtranonce2();
    }

    public String getNtime() {
        return hasher.getNtime();
    }

    public Job abort() {
        hasher = NO_HASHER;
        return this;
    }

    public boolean exists() {
        return jobId != null;
    }
}
