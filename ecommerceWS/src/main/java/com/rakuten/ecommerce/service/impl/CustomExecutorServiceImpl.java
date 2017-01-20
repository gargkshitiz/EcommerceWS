package com.rakuten.ecommerce.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rakuten.ecommerce.service.CustomExecutorService;
import com.rakuten.ecommerce.service.util.MdcThreadPoolExecutor;
/**
 * 
 * @author kgarg
 *
 */
@Service
public class CustomExecutorServiceImpl implements CustomExecutorService {
	
	private ExecutorService backgroundExecutor;
	private static final Logger log = LoggerFactory.getLogger(CustomExecutorService.class);
	
	@Value("${threadPoolSize}")
	private int threadPoolSize;
	
    private BasicThreadFactory threadFactory;

    private Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler(){
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            log.error("Exception in thread pool worker thread:"+thread.getName()+" :"+throwable.getMessage());
        }
    };

    public CustomExecutorServiceImpl(){
        threadFactory = new BasicThreadFactory.Builder().uncaughtExceptionHandler(exceptionHandler).build();
	}
	
    @PostConstruct
    public void createExecutor(){
        backgroundExecutor = MdcThreadPoolExecutor.newWithInheritedMdc(threadPoolSize,threadFactory);
        log.info("Created thread pool for recording activities with pool size: {}", threadPoolSize);
    }
   
    @Override
	public Future<?> submit(Runnable task) {
		return backgroundExecutor.submit(task);
	}

}