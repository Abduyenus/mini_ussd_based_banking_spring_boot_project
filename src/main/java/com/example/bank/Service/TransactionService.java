package com.example.bank.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.bank.Model.Account;
import com.example.bank.Model.Customer;
import com.example.bank.Model.History;
import com.example.bank.Model.MobileBankingUser;
import com.example.bank.Model.TopUp;
import com.example.bank.Model.Transaction;
import com.example.bank.Model.Enumeration.CustomerProfile;
import com.example.bank.Model.Enumeration.ResponseCode;
import com.example.bank.Model.Enumeration.Side;
import com.example.bank.Model.Enumeration.TransactionCode;
import com.example.bank.Repository.AccountRepository;
import com.example.bank.Repository.CustomerRepository;
import com.example.bank.Repository.HistoryRepository;
import com.example.bank.Repository.MobileBankingUserRepository;
import com.example.bank.Repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private HistoryService historyService;

	@Autowired
	MobileBankingUserService mobileBankingUserService;

	@Autowired
	private MobileBankingUserRepository mobileBankingUserRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private WebClient webClientForTopUp;

	// New method for OTP generation
	private String generateOTP() {
		// Generate a random 6-digit OTP
		int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
		return String.valueOf(otp);
	}

	// to retrieve all transaction
	public Iterable<Transaction> allTransaction() {
		return transactionRepository.findAll();
	}

	// to generate random rrn
	public static Long generateRRN() {
		// Use the current timestamp as the base
		long timestamp = System.currentTimeMillis();

		// Generate a random number
		Random random = new Random();
		int randomPart = random.nextInt(10000);

		// Combine the timestamp and random number to create a unique RRN
		return Long.parseLong(timestamp + String.format("%04d", randomPart));
	}

	// to insert transaction records into transaction table based on the passed
	// values
	Transaction createTransaction(int accountNumber, Side side, BigDecimal amount, TransactionCode transactionCode,
			ResponseCode responseCode) {
		System.out.println("transaction code ==================>" + transactionCode);
		System.out.println("response code ==================>" + responseCode);
		Transaction transaction = new Transaction();
		transaction.setRrn(generateRRN());
		transaction.setTransactionCode(transactionCode);
		transaction.setAccountNumber(accountNumber);
		transaction.setSide(side);
		transaction.setAmount(amount);
		transaction.setResponseCode(responseCode);
		transaction.setTransactionDate(LocalDateTime.now());
		return transactionRepository.save(transaction);
	}

	// to show available balance
	public ResponseEntity<String> getBalanceByAccountNumber(int accountNumber) {
		BigDecimal balance = accountRepository.findBalanceByAccountNumber(accountNumber);
		if (balance != null) {

			// Create History record for balance inquiry
			History depositHistory = historyService.createHistory(generateRRN(), null, null,
					TransactionCode.BALANCE_INQUIRY, ResponseCode.SUCCESS);
			return ResponseEntity
					.ok("Available Balance for Account Number " + accountNumber + ": " + balance + " Birr");
		} else {
			return ResponseEntity.badRequest().body("Account not found.");
		}
	}

	// to get 5 last balances
	public ResponseEntity<List<Transaction>> getLast5Transactions(int accountNumber) {
		List<Transaction> transactions = transactionRepository.findLast5Transactions(accountNumber);

		if (transactions.isEmpty()) {
			// Create History record for statement
			historyService.createHistory(generateRRN(), null, null, TransactionCode.SHORT_STATEMENT,
					ResponseCode.SUCCESS);
			return ResponseEntity.ok(Collections.emptyList());
		}

		return ResponseEntity.ok(transactions);
	}

	// to deposit into account
	@Transactional
	public ResponseEntity<String> depositToAccount(int accountNumber, BigDecimal amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));

		// Create Transaction record for deposit
		Transaction depositTransaction = transactionService.createTransaction(account.getAccountNumber(), Side.CREDIT,
				amount, TransactionCode.CASH_DEPOSIT, ResponseCode.SUCCESS);

		// Create History record for deposit
		History depositHistory = historyService.createHistory(depositTransaction.getRrn(), Side.CREDIT, amount,
				TransactionCode.CASH_DEPOSIT, ResponseCode.SUCCESS);

		// Perform the deposit
		account.setBalance(account.getBalance().add(amount));
		account.setUpdatedDate(LocalDateTime.now());
		accountRepository.save(account);

		return ResponseEntity.ok("Deposit of " + amount + " to account " + accountNumber + " successfully processed.");
	}

	// to withdraw from account
	@Transactional
	public ResponseEntity<String> withdrawFromAccount(int accountNumber, BigDecimal amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));

		// Check if sufficient funds for withdrawal
		if (account.getBalance().compareTo(amount) < 0) {
			return ResponseEntity.badRequest().body("Insufficient funds for withdrawal");
		}

		// Create Transaction record for withdrawal
		Transaction withdrawalTransaction = transactionService.createTransaction(account.getAccountNumber(), Side.DEBIT,
				amount, TransactionCode.CASH_WITHDRAWAL, ResponseCode.SUCCESS);

		// Create History record for withdrawal
		History withdrawalHistory = historyService.createHistory(withdrawalTransaction.getRrn(), Side.DEBIT, amount,
				TransactionCode.CASH_WITHDRAWAL, ResponseCode.SUCCESS);

		// Perform the withdrawal
		account.setBalance(account.getBalance().subtract(amount));
		account.setUpdatedDate(LocalDateTime.now());
		accountRepository.save(account);

		return ResponseEntity
				.ok("Withdrawal of " + amount + " from account " + accountNumber + " successfully processed.");
	}

	// to transfer money
	@Transactional
	public ResponseEntity<String> transferMoney(int senderAccountNumber, int receiverAccountNumber, BigDecimal amount) {
		// Fetch sender account
		Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

		// Fetch receiver account
		Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

		// Check if sender has sufficient funds
		if (senderAccount.getBalance().compareTo(amount) < 0) {
			return ResponseEntity.badRequest().body("Insufficient funds for the transfer");
		}

		// Create Transaction records
		Transaction senderTransaction = transactionService.createTransaction(senderAccount.getAccountNumber(),
				Side.DEBIT, amount, TransactionCode.TRANSFER, ResponseCode.SUCCESS);
		Transaction receiverTransaction = transactionService.createTransaction(receiverAccount.getAccountNumber(),
				Side.CREDIT, amount, TransactionCode.TRANSFER, ResponseCode.SUCCESS);

		// Create History records
		History senderHistory = historyService.createHistory(senderTransaction.getRrn(), Side.DEBIT, amount,
				TransactionCode.TRANSFER, ResponseCode.SUCCESS);
		History receiverHistory = historyService.createHistory(receiverTransaction.getRrn(), Side.CREDIT, amount,
				TransactionCode.TRANSFER, ResponseCode.SUCCESS);

		// Subtract the transfer amount from the sender's account
		senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

		// Add the transfer amount to the receiver's account
		receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

		// Save the updated accounts
		accountRepository.save(senderAccount);
		accountRepository.save(receiverAccount);

		return ResponseEntity.ok("Transfer of " + amount + " Birr from account " + senderAccountNumber + " to account "
				+ receiverAccountNumber + " successfully processed.");
	}

	// initiate Cash Deposit with OTP
	@Transactional
	public ResponseEntity<String> depositWithOTP(int accountNumber, BigDecimal amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));

		// Generate OTP
		String otp = generateOTP();

		// Create Transaction record for deposit with pending status and OTP
		Transaction depositTransaction = transactionService.createTransaction(account.getAccountNumber(), Side.CREDIT,
				amount, TransactionCode.CASH_DEPOSIT, ResponseCode.PENDING);
//	        depositTransaction.setTransactionCode("PENDING");
		depositTransaction.setOtp(otp);

//	        transactionRepository.save(depositTransaction);

		// Create History record for deposit
		History depositHistory = historyService.createHistory(depositTransaction.getRrn(), Side.CREDIT, amount,
				TransactionCode.CASH_DEPOSIT, ResponseCode.PENDING);

		// Return OTP for customer reference
		return ResponseEntity.ok("Cash deposit initiated. OTP: " + otp);
	}

	// initiate Withdrawal with OTP
	@Transactional
	public ResponseEntity<String> withdrawWithOTP(int accountNumber, BigDecimal amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));

		// Generate OTP
		String otp = generateOTP();

		// Create Transaction record for withdrawal with pending status and OTP
		Transaction withdrawalTransaction = transactionService.createTransaction(account.getAccountNumber(), Side.DEBIT,
				amount, TransactionCode.CASH_WITHDRAWAL, ResponseCode.PENDING);
		withdrawalTransaction.setOtp(otp);

		// Create History record for withdrawal
		History withdrawalHistory = historyService.createHistory(withdrawalTransaction.getRrn(), Side.DEBIT, amount,
				TransactionCode.CASH_WITHDRAWAL, ResponseCode.PENDING);

		// Return OTP for customer reference
		return ResponseEntity.ok("Cash withdrawal initiated. OTP: " + otp);
	}

	// complete Cash Withdrawal with OTP
	@Transactional
	public ResponseEntity<String> completeWithdrawalWithOTP(String phoneNumber, String otp, int accountNumber) {
		// Check if the phone number exists in MobileBankingUser table
		Optional<MobileBankingUser> mobileBankingUserOptional = mobileBankingUserRepository
				.findByPhoneNumber(phoneNumber);
		if (mobileBankingUserOptional.isEmpty()) {
			return ResponseEntity.badRequest().body("Merchant with phone number " + phoneNumber + " does not exist.");
		}

		// Retrieve the account using the account number
		Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
		if (accountOptional.isEmpty()) {
			return ResponseEntity.badRequest().body("Account not found.");
		}
		Account customerAccount = accountOptional.get();

		// Check if the customer profile is "Merchant"
		if (mobileBankingUserOptional.get().getCustomerProfile() == CustomerProfile.MERCHANT) {

			// Use the CustomerService to get the customer id based on phone number
			List<Long> customerIdList = customerService.findCustomerIdByPhoneNumber(phoneNumber);

			if (!customerIdList.isEmpty()) {
				Long customerId = customerIdList.get(0);

				Optional<Integer> merchantAccountNumberOptional = accountRepository
						.findAccountNumberByCustomerId(customerId);
				System.out.println("=============================>" + customerId);
				if (merchantAccountNumberOptional.isPresent()) {
					Integer merchantAccountNumber = merchantAccountNumberOptional.get();

					// Retrieve the amount from the Transaction based on the otp
					Optional<Transaction> transactionOptional = transactionRepository
							.findPendingDepositTransactionWithOTP(accountNumber, otp);
					if (transactionOptional.isPresent()) {

						LocalDateTime otpCreationTime = transactionOptional.get().getTransactionDate();
						LocalDateTime currentTimestamp = LocalDateTime.now();
						long minutesElapsed = Duration.between(otpCreationTime, currentTimestamp).toMinutes();

						// Check if the OTP has expired (assuming a 30-minute limit)
						if (minutesElapsed > 1) {
							// Create history record with fail status
							historyService.createHistory(transactionOptional.get().getRrn(),
									transactionOptional.get().getSide(), transactionOptional.get().getAmount(),
									transactionOptional.get().getTransactionCode(), ResponseCode.FAIL);

							// Update the transaction record with fail status
							Transaction depositTransaction = transactionOptional.get();
							depositTransaction.setResponseCode(ResponseCode.FAIL);
							transactionRepository.save(depositTransaction);

							return ResponseEntity.badRequest().body("The OTP has expired.");
						}

						// Continue with the rest of the code for processing the withdrawal with OTP

						BigDecimal amount = transactionOptional.get().getAmount();
						// Deduct the amount from the customer account
						customerAccount.setBalance(customerAccount.getBalance().subtract(amount));
						accountRepository.save(customerAccount);

						// Retrieve the merchant account
						Optional<Account> merchantAccountOptional = accountRepository
								.findByAccountNumber(merchantAccountNumber);
						if (merchantAccountOptional.isPresent()) {
							Account merchantAccount = merchantAccountOptional.get();

							System.out.println("Merchant Account Number withdraw =============> "
									+ merchantAccount.getAccountNumber());
							// Add the amount to the merchant account
							merchantAccount.setBalance(merchantAccount.getBalance().add(amount));
							accountRepository.save(merchantAccount);
							// Update the transaction status to success and clear OTP
							Transaction depositTransaction = transactionOptional.get();
							depositTransaction.setResponseCode(ResponseCode.SUCCESS);
							depositTransaction.setOtp(null);
							transactionRepository.save(depositTransaction);

							// Update the history record
							History depositHistory = historyRepository.findByRRN(depositTransaction.getRrn())
									.orElseThrow(() -> new IllegalArgumentException("History record not found"));
							depositHistory.setResponseCode(ResponseCode.SUCCESS);
							historyRepository.save(depositHistory);

							// Create Transaction record for merchant account when withdraw with OTP
							// completed
							Transaction withdrawalTransaction = transactionService.createTransaction(
									merchantAccount.getAccountNumber(), Side.CREDIT, amount,
									TransactionCode.CASH_DEPOSIT, ResponseCode.SUCCESS);

							withdrawalTransaction.setRrn(depositHistory.getRrn());
							transactionRepository.save(withdrawalTransaction);

							// Create History record for merchant account when withdraw with OTP completed
							History withdrawalHistory = historyService.createHistory(depositHistory.getRrn(),
									Side.CREDIT, amount, TransactionCode.CASH_DEPOSIT, ResponseCode.SUCCESS);
							return ResponseEntity.ok("Withdrawal with OTP completed successfully.");
						} else {
							// Handle case where merchant account is not found
							return ResponseEntity.badRequest().body("Merchant account not found.");
						}
					} else {
						// Handle case where transaction with the given otp is not found
						return ResponseEntity.badRequest().body("Transaction with OTP " + otp + " not found.");
					}
				} else {
					// Handle case where merchant account number is not found
					return ResponseEntity.badRequest().body("Merchant account number not found.");
				}
			} else {
				// Handle case where customer IDs are not found
				return ResponseEntity.badRequest().body("Customer IDs not found.");
			}
		} else {
			// For default profile, perform the withdrawal with OTP using the retrieved customer account number
			return ResponseEntity.badRequest().body("Default profile not supported for this operation.");
		}
	}

	// complete Cash Deposit with OTP
	@Transactional
	public ResponseEntity<String> completeDepositWithOTP(String phoneNumber, String otp, int accountNumber) {
		// Check if the phone number exists in MobileBankingUser table
		Optional<MobileBankingUser> mobileBankingUserOptional = mobileBankingUserRepository
				.findByPhoneNumber(phoneNumber);
		if (mobileBankingUserOptional.isEmpty()) {
			return ResponseEntity.badRequest()
					.body("Mobile Banking User with phone number " + phoneNumber + " does not exist.");
		}

		// Retrieve the account using the account number
		Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
		if (accountOptional.isEmpty()) {
			return ResponseEntity.badRequest().body("Account not found.");
		}
		Account customerAccount = accountOptional.get();

		// Check if the customer profile is "Merchant"
		if (mobileBankingUserOptional.get().getCustomerProfile() == CustomerProfile.MERCHANT) {
			// Use the CustomerService to get the customer id based on phone number
			List<Long> customerIdList = customerService.findCustomerIdByPhoneNumber(phoneNumber);

			if (!customerIdList.isEmpty()) {
				Long customerId = customerIdList.get(0);

				Optional<Integer> merchantAccountNumberOptional = accountRepository
						.findAccountNumberByCustomerId(customerId);
				System.out.println("=============================>" + customerId);
				if (merchantAccountNumberOptional.isPresent()) {
					Integer merchantAccountNumber = merchantAccountNumberOptional.get();
					// Retrieve the amount from the Transaction based on the otp
					Optional<Transaction> transactionOptional = transactionRepository
							.findPendingDepositTransactionWithOTP(accountNumber, otp);
					if (transactionOptional.isPresent()) {
						LocalDateTime otpCreationTime = transactionOptional.get().getTransactionDate();
						LocalDateTime currentTimestamp = LocalDateTime.now();
						long minutesElapsed = Duration.between(otpCreationTime, currentTimestamp).toMinutes();

						// Check if the OTP has expired (assuming a 30-minute limit)
						if (minutesElapsed > 1) {
							// Handle the case where the OTP has expired

							// Create history record with fail status
							historyService.createHistory(transactionOptional.get().getRrn(),
									transactionOptional.get().getSide(), transactionOptional.get().getAmount(),
									transactionOptional.get().getTransactionCode(), ResponseCode.FAIL);

							// Update the transaction record with fail status
							Transaction depositTransaction = transactionOptional.get();
							depositTransaction.setResponseCode(ResponseCode.FAIL);
							transactionRepository.save(depositTransaction);

							return ResponseEntity.badRequest().body("The OTP has expired.");
						}

						BigDecimal amount = transactionOptional.get().getAmount();

						// Add the amount to the customer account
						customerAccount.setBalance(customerAccount.getBalance().add(amount));
						accountRepository.save(customerAccount);

						// Retrieve the merchant account
						Optional<Account> merchantAccountOptional = accountRepository
								.findByAccountNumber(merchantAccountNumber);
						if (merchantAccountOptional.isPresent()) {
							Account merchantAccount = merchantAccountOptional.get();

							System.out.println("Merchant Account Number deposit =============> "
									+ merchantAccount.getAccountNumber());
							// Deduct the amount from the merchant account
							merchantAccount.setBalance(merchantAccount.getBalance().subtract(amount));
							accountRepository.save(merchantAccount);
							// Update the transaction status to success and clear OTP
							Transaction depositTransaction = transactionOptional.get();
							depositTransaction.setResponseCode(ResponseCode.SUCCESS);
							depositTransaction.setOtp(null);
							transactionRepository.save(depositTransaction);

							// Update the history record
							History depositHistory = historyRepository.findByRRN(depositTransaction.getRrn())
									.orElseThrow(() -> new IllegalArgumentException("History record not found"));
							depositHistory.setResponseCode(ResponseCode.SUCCESS);
							historyRepository.save(depositHistory);

							// Create Transaction record for merchant account when withdraw with OTP
							// completed
							Transaction withdrawalTransaction = transactionService.createTransaction(
									merchantAccount.getAccountNumber(), Side.DEBIT, amount,
									TransactionCode.CASH_DEPOSIT, ResponseCode.SUCCESS);

							withdrawalTransaction.setRrn(depositHistory.getRrn());
							transactionRepository.save(withdrawalTransaction);

							// Create History record for merchant account when withdraw with OTP completed
							History withdrawalHistory = historyService.createHistory(depositHistory.getRrn(),
									Side.DEBIT, amount, TransactionCode.CASH_DEPOSIT, ResponseCode.SUCCESS);

							return ResponseEntity.ok("Deposit with OTP completed successfully.");
						} else {
							// Handle case where merchant account is not found
							return ResponseEntity.badRequest().body("Merchant account not found.");
						}
					} else {
						// Handle case where transaction with the given otp is not found
						return ResponseEntity.badRequest().body("Transaction with OTP " + otp + " not found.");
					}
				} else {
					// Handle case where merchant account number is not found
					return ResponseEntity.badRequest().body("Merchant account number not found.");
				}
			} else {
				// Handle case where customer IDs are not found
				return ResponseEntity.badRequest().body("Customer IDs not found.");
			}
		} else {
			// For default profile, perform the deposit with OTP using the retrieved
			// customer account number
			return ResponseEntity.badRequest().body("Default profile not supported for this operation.");
		}
	}

	// to buy mobile card / top up
	public ResponseEntity<String> topUpfetch(int accountNumber, int amount) {
		// Allowed values
		List<Integer> allowedValues = Arrays.asList(5, 10, 15, 25, 50, 100, 300, 500, 1000);

		// Check if paramValue is in the allowed values
		if (!allowedValues.contains(amount)) {
			// Handle the case where paramValue is not allowed
			return ResponseEntity.badRequest().body("Invalid Value! Allowed values are: " + allowedValues);
		}

		// Retrieve the account using the account number
		Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();

			// Check if the account has enough money to buy
			BigDecimal requiredAmount = new BigDecimal(amount);
			if (account.getBalance().compareTo(requiredAmount) < 0) {
				return ResponseEntity.badRequest().body("Insufficient funds in the account to make the purchase.");
			}

			// Make the API call if paramValue is allowed
			TopUp jsonResponse = webClientForTopUp.get().uri("topup/" + amount).retrieve().bodyToMono(TopUp.class)
					.block();

			// Check if jsonResponse is not null
			if (jsonResponse != null) {
				// Extract values from the response
				String senum = jsonResponse.getSenum();
				String scnum = jsonResponse.getScnum();
				String expDate = jsonResponse.getExpDate();

				// Deduct the amount from the account
				account.setBalance(account.getBalance().subtract(requiredAmount));
				accountRepository.save(account);

				// Record Transaction and History for customer account
				Transaction customerTransaction = createTransaction(accountNumber, Side.DEBIT, requiredAmount,
						TransactionCode.TOP_UP, ResponseCode.SUCCESS);
				transactionRepository.save(customerTransaction);

				History customerHistory = historyService.createHistory(customerTransaction.getRrn(), Side.DEBIT,
						requiredAmount, TransactionCode.TOP_UP, ResponseCode.SUCCESS);
				historyRepository.save(customerHistory);

				// Return the extracted values
				return ResponseEntity.ok("senum: " + senum + "\nscnum: " + scnum + "\nexpDate: " + expDate
						+ "\nAmount deducted from account: " + requiredAmount);
			} else {
				// Handle the case where the response is null
				return ResponseEntity.badRequest().body("Unable to retrieve values from the response.");
			}
		} else {
			// Handle the case where the account is not found
			return ResponseEntity.badRequest().body("Account not found.");
		}
	}

	public Optional<Long> getAccountNumberByRRN(Long rrn) {
		return transactionRepository.findAccountNumberByRRN(rrn);
	}

	public String getCustomerMobileNumberByRRN(Long rrn) {
		Long accountId = transactionRepository.findAccountNumberByRRN(rrn).orElseThrow();
		Long customerId = accountRepository.findCustomerIdByAccountNumber(accountId).orElseThrow();
		return customerRepository.findMobilePhoneByCustomerId(customerId).orElse(null);
	}

}
