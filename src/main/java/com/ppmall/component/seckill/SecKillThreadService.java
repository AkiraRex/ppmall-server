package com.ppmall.component.seckill;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import com.ppmall.rabbitmq.message.SecKillMessage;

@Component
public class SecKillThreadService implements BeanFactoryAware {

	private static Logger logger = LoggerFactory.getLogger(SecKillThreadService.class);
	private BeanFactory factory;// 用于从IOC里取对象
	// 线程池维护线程的最少数量
	private final static int CORE_POOL_SIZE = 2;
	// 线程池维护线程的最大数量
	private final static int MAX_POOL_SIZE = 10;
	// 线程池维护线程所允许的空闲时间
	private final static int KEEP_ALIVE_TIME = 0;
	// 线程池所使用的缓冲队列大小
	private final static int WORK_QUEUE_SIZE = 50;
	// 消息缓冲队列
	Queue<SecKillMessage> msgQueue = new LinkedList<SecKillMessage>();

	// 用于储存在队列中的订单,防止重复提交
	Map<String, Object> cacheMap = new ConcurrentHashMap<>();

	// 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
	final RejectedExecutionHandler handler = new RejectedExecutionHandler() {
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			logger.info("线程池到达MAX_POOL_SIZE,放入消息缓冲队列等待线程池空余时优先处理");
			// 当线程池中线程数量已经到达 MAX_POOL_SIZE ，将进入此方法
			msgQueue.offer(((SecKillDataBaseThread) r).getMsg());// 放入自定义的缓冲队列
		}
	};

	// 订单线程池 ArrayBlockingQueue有界阻塞队列，
	final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
			TimeUnit.SECONDS, new ArrayBlockingQueue(WORK_QUEUE_SIZE), this.handler);

	// 调度线程池。此线程池支持定时以及周期性执行任务的需求。
	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

	// 访问消息缓存的调度线程,每秒执行一次
	// 查看是否有待定请求，如果有，则创建一个新的AccessDBThread，并添加到线程池中
	final ScheduledFuture taskHandler = scheduler.scheduleAtFixedRate(new Runnable() {
		@Override
		public void run() {
			if (!msgQueue.isEmpty()) { // 先判断缓冲队列是否存在缓冲消息
				if (threadPool.getQueue().size() < WORK_QUEUE_SIZE) { // 判断线程池中的队列是否有空余
					logger.info("调度：");
					SecKillMessage message = (SecKillMessage) msgQueue.poll();// 取出缓冲队列中的消息进行处理
					SecKillDataBaseThread accessDBThread = (SecKillDataBaseThread) factory.getBean("dBThread");
					accessDBThread.setMsg(message);
					threadPool.execute(accessDBThread);
				}else{
					
				}
			}
		}
	}, 0, 1, TimeUnit.SECONDS);

	// 终止订单线程池+调度线程池
	public void shutdown() {
		// true表示如果定时任务在执行，立即中止，false则等待任务结束后再停止
		System.out.println(taskHandler.cancel(false));
		scheduler.shutdown();
		threadPool.shutdown();
	}

	public Queue<SecKillMessage> getMsgQueue() {
		return msgQueue;
	}

	// 将任务加入订单线程池
	public void processOrders(SecKillMessage message) {
//		if (cacheMap.get(orderId) == null) {
//			cacheMap.put(orderId, new Object());
			SecKillDataBaseThread accessDBThread = (SecKillDataBaseThread) factory.getBean("dBThread");
			accessDBThread.setMsg(message);
			threadPool.execute(accessDBThread);
//		}
	}

	// BeanFactoryAware
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		factory = beanFactory;
	}
}
