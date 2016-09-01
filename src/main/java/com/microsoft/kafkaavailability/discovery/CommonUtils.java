//*********************************************************
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
//*********************************************************

package com.microsoft.kafkaavailability.discovery;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import com.codahale.metrics.*;
import com.google.common.collect.ImmutableSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {

    private static Logger log = LoggerFactory.getLogger(CommonUtils.class);

    /**     * Get the ip address of this host
     */
    public static String getIpAddress() {
        String ipAddress = null;

        try {

            InetAddress iAddress = InetAddress.getLocalHost();
            ipAddress = iAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return (ipAddress);
    }

    /**
     * Removes any of the given metrics from the registry and returns the number of metrics removed
     *
     * @param metricRegistry  Registry to remove from
     * @param metricsToRemove Which metrics to remove
     * @return The number of metrics removed
     */
    public static int removeMetrics(MetricRegistry metricRegistry, final Metric... metricsToRemove) {
        final Set<Metric> toRemove = ImmutableSet.copyOf(metricsToRemove);
        final AtomicInteger totalRemoved = new AtomicInteger(0);
        metricRegistry.removeMatching(new MetricFilter() {
            @Override
            public boolean matches(String name, Metric metric) {
                final boolean shouldRemove = toRemove.contains(metric);
                if (shouldRemove) {
                    totalRemoved.incrementAndGet();
                }
                return shouldRemove;
            }
        });
        return totalRemoved.get();
    }
}