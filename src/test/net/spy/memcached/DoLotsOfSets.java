package net.spy.memcached;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Small test program that does a bunch of sets in a tight loop.
 */
public class DoLotsOfSets {

	public static void main(String[] args) throws Exception {
		// Create a client with a queue big enough to hold the 300,000 items
		// we're going to add.
		MemcachedClient client=new MemcachedClient(
			new DefaultConnectionFactory(350000, 8192),
			AddrUtil.getAddresses("localhost:11211"));
		long start=System.currentTimeMillis();
		byte[] toStore=new byte[26];
		Arrays.fill(toStore, (byte)'a');
		for(int i=0; i<300000; i++) {
			client.set("k" + i, 300, toStore);
		}
		long added=System.currentTimeMillis();
		System.out.printf("Finished queuing in %sms\n", added-start);
		client.waitForQueues(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		long end=System.currentTimeMillis();
		System.out.printf("Completed everything in %sms (%sms to flush)\n",
				end-start, end-added);
		client.shutdown();
	}
}