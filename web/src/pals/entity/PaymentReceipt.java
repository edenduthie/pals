package pals.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.braintreegateway.Transaction;

@Entity
public class PaymentReceipt 
{
	@Id @GeneratedValue
	private Integer id;

	private String transactionId;
	private BigDecimal amount;
	private String customerId;
	private String creditCardToken;
	private Long timestamp;
	
	public PaymentReceipt() {}
	
	public PaymentReceipt(Transaction transaction) 
	{
		setTransactionId(transaction.getId());
		setAmount(transaction.getAmount());
		setCustomerId(transaction.getCustomer().getId());
		setCreditCardToken(transaction.getCreditCard().getToken());
		setTimestamp(transaction.getUpdatedAt().getTimeInMillis());
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCreditCardToken() {
		return creditCardToken;
	}

	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
