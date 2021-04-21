package com.demo.controller;
/**
 *
 */

import static com.google.common.base.Charsets.UTF_8;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Watch.Watcher;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * etcd 操作工具，包括启动监听和操作etcd v3 版本协议，只测试功能，未添加log
 *
 * @version 1.0
 * @author zhangyanhua
 * @date 2019年10月29日 下午4:30:57
 */
public class EtcdUtil
{
	// etcl客户端链接
	private static Client etcdClient = null;

	// 链接初始化
	public static synchronized Client getEtclClient()
	{
		if (etcdClient == null)
		{
			//String[] urls = PropertiesUtil.getValue("etcd_node_url").split(",");
			String[] urls = "http://127.0.0.1:2379".split(",");
			etcdClient = Client.builder().endpoints(urls).build();
		}
		return etcdClient;
	}

	/**
	 * 新增或者修改指定的配置
	 *
	 * @param key
	 * @param value
	 * @throws Exception
	 * @author zhangyanhua
	 * @date 2019年10月29日 下午4:41:06
	 */
	public static void putEtcdValueByKey(String key, String value) throws Exception
	{
		Client client = EtcdUtil.getEtclClient();
		client.getKVClient().put(ByteSequence.from(key, UTF_8), ByteSequence.from(value, UTF_8)).get();
		//System.out.println("put etcd key:value \"" + key + ":" + value + "\" success");
		client.close();
		etcdClient = null;
	}

	/**
	 * 查询指定的key名称对应的value
	 *
	 * @param key
	 * @return value值
	 * @throws Exception
	 * @author zhangyanhua
	 * @date 2019年10月29日 下午4:35:44
	 */
	public static String getEtcdValueByKey(String key) throws Exception
	{
		Client client = EtcdUtil.getEtclClient();
		GetResponse getResponse = client.getKVClient()
				.get(ByteSequence.from(key, UTF_8), GetOption.newBuilder().build()).get();
		client.close();
		etcdClient = null;

		// key does not exist
		if (getResponse.getKvs().isEmpty())
		{
			return null;
		}

		return getResponse.getKvs().get(0).getValue().toString(UTF_8);
	}

	/**
	 * 删除指定的配置
	 *
	 * @param key
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @author zhangyanhua
	 * @date 2019年10月29日 下午4:53:24
	 */
	public static void deleteEtcdValueByKey(String key) throws InterruptedException, ExecutionException
	{
		Client client = EtcdUtil.getEtclClient();
		client.getKVClient().delete(ByteSequence.from(key, UTF_8)).get();
		//System.out.println("delete etcd key \"" + key + "\" success");
		client.close();
		etcdClient = null;
	}

	/**
	 * 持续监控某个key变化的方法，执行后如果key有变化会被监控到，输入结果如下
	 *  watch type= "PUT", key= "zyh1", value= "zyh1-value"
	 *  watch type= "PUT", key= "zyh1", value= "zyh1-value111"
	 *  watch type= "DELETE", key= "zyh1", value= ""
	 *
	 * @param key
	 * @throws Exception
	 * @author zhangyanhua
	 * @date 2019年10月29日 下午5:26:09
	 */
	public static void watchEtcdKey(String key) throws Exception
	{
		Client client = EtcdUtil.getEtclClient();
		// 最大事件数量
		Integer maxEvents = Integer.MAX_VALUE;
		CountDownLatch latch = new CountDownLatch(maxEvents);
		Watcher watcher = null;
		try
		{
			ByteSequence watchKey = ByteSequence.from(key, UTF_8);
			WatchOption watchOpts = WatchOption.newBuilder().build();

			watcher = client.getWatchClient().watch(watchKey, watchOpts, response -> {
				for (WatchEvent event : response.getEvents())
				{
					System.out.println("watch type= \"" + event.getEventType().toString() + "\",  key= \""
							+ Optional.ofNullable(event.getKeyValue().getKey()).map(bs -> bs.toString(UTF_8)).orElse("")
							+ "\",  value= \"" + Optional.ofNullable(event.getKeyValue().getValue())
							.map(bs -> bs.toString(UTF_8)).orElse("")
							+ "\"");
				}

				latch.countDown();
			});

			latch.await();
		}
		catch (Exception e)
		{
			if (watcher != null)
			{
				watcher.close();
				client.close();
				etcdClient = null;
			}
			throw e;
		}
	}

	/**
	 * TODO
	 *
	 * @param args
	 * @throws Exception
	 * @author zhangyanhua
	 * @date 2019年10月29日 下午6:01:54
	 */
	public static void main(String[] args) throws Exception
	{
		boolean success = true;

		String key = "zyh";
		String value = "zyh-value";
		String newValue = "zyh-value-new";

		System.out.println("**** 测试方法开始 ****");
		EtcdUtil.putEtcdValueByKey(key, value);
		String retValue = EtcdUtil.getEtcdValueByKey(key);
		// System.out.println("查询key " + key + " 对应的值是 " + retValue);
		if (value.equals(retValue))
		{
			System.out.println("数据插入成功。");
			System.out.println("数据查询成功。");
		}
		else
		{
			success = false;
			System.out.println("数据插入或查询失败！");
		}

		EtcdUtil.putEtcdValueByKey(key, newValue);
		retValue = EtcdUtil.getEtcdValueByKey(key);
		// System.out.println("查询key " + key + " 对应的值是 " + retValue);
		if (newValue.equals(retValue))
		{
			System.out.println("数据更新成功。");
		}
		else
		{
			success = false;
			System.out.println("数据更新失败！");
		}

		EtcdUtil.deleteEtcdValueByKey(key);
		retValue = EtcdUtil.getEtcdValueByKey(key);
		// System.out.println("查询key " + key + " 对应的值是 " + retValue);
		if (retValue == null)
		{
			System.out.println("数据删除成功。");
		}
		else
		{
			success = false;
			System.out.println("数据删除失败！");
		}

		//EtcdUtil.watchEtcdKey(key);

		if (success)
		{
			System.out.println("**** 测试方法全部通过。 ****");
		}
		else
		{
			System.out.println("**** 测试失败！ ****");
		}
	}
}