package com.geigeoffer.honeycombcoupon.merchant.admin.dao.sharding;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import org.apache.shardingsphere.infra.util.exception.ShardingSpherePreconditions;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.apache.shardingsphere.sharding.exception.algorithm.sharding.ShardingAlgorithmInitializationException;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DBHashModShardingAlgorithm implements StandardShardingAlgorithm<Long> {
    @Getter
    private Properties pros;
    private int shardingCount;
    private static final String SHARDING_COUNT_KEY = "sharding-count";



    private int getShardingCount(final Properties pros) {
        ShardingSpherePreconditions.checkState(pros.containsKey(SHARDING_COUNT_KEY),()-> new ShardingAlgorithmInitializationException(getType(), "Sharding count cannot be null."));
        return Integer.parseInt(pros.getProperty(SHARDING_COUNT_KEY));
    }
    private long hashShardingValue(final Comparable<?> shardingValue) {
        return Math.abs((long) shardingValue.hashCode());
    }

    @Override
    public String doSharding(Collection<String> availableTargetName, PreciseShardingValue<Long> preciseShardingValue) {
        long id = preciseShardingValue.getValue();
        int dbSize = availableTargetName.size();
        int mod = (int)hashShardingValue(id) % shardingCount / (shardingCount /dbSize);
        int index = 0;
        for(String targetName : availableTargetName) {
            if(index == mod) {
                return targetName;
            }
            index++;
        }
        throw new IllegalArgumentException("No target found for value: " + id);
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return List.of();
    }

    @Override
    public void init(Properties props) {
        this.pros = props;
        this.shardingCount = getShardingCount(pros);
    }
}
