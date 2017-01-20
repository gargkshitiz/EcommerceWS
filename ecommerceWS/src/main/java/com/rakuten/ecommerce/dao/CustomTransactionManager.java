package com.rakuten.ecommerce.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Repository
/**
 * @author Kshitiz Garg
 * This Tx manager could be used to manage transactions
 * Spring's transaction annotation can't be applied to private methods
 * so it comes handy at those places
 */
public class CustomTransactionManager {
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	public TransactionStatus begin(String txName){
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		transactionDefinition.setTimeout(60);
		transactionDefinition.setName(txName);
		return platformTransactionManager.getTransaction(transactionDefinition);
	}
	
	public boolean commit(TransactionStatus transactionStatus){
		platformTransactionManager.commit(transactionStatus);
		return true;
	}
	
	public boolean rollback(TransactionStatus transactionStatus){
		platformTransactionManager.rollback(transactionStatus);
		return true;
	}
	
}