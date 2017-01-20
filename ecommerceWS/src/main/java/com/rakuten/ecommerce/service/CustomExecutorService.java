package com.rakuten.ecommerce.service;

import java.util.concurrent.Future;
/**
 * @author Kshitiz Garg
 */
public interface CustomExecutorService {

	Future<?> submit(Runnable task);

}
