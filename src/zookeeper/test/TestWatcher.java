package zookeeper.test;

import zookeeper.util.BaseWatcher;

public class TestWatcher extends BaseWatcher {
	
	public static final String TEST_ROOT = "/test";

	public TestWatcher(String host) {
		super(host, TEST_ROOT);
	}

}
