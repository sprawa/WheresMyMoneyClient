package ms.wmm.client.bo;

import java.math.BigDecimal;
import java.util.List;

public class Summary {

	private String user;
	private BigDecimal value;
	private List<Transaction> transactions;

	public Summary(String user) {
		this.user = user;
		this.value = BigDecimal.ZERO;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public BigDecimal getValue() {
		return value;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void add(BigDecimal augend) {
		this.value = value.add(augend);
	}

	public void subtract(BigDecimal subtrahend) {
		this.value = value.subtract(subtrahend);
	}
}
