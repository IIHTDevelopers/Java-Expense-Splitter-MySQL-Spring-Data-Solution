package com.expensesplitter.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expensesplitter.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByPaidById(Long userId);

	List<Expense> findBySharedWithId(Long userId);

	@Query("SELECT e FROM Expense e WHERE e.paidBy.id = :payerId OR e.id IN (SELECT e2.id FROM Expense e2 JOIN e2.sharedWith u WHERE u.id = :participantId)")
	List<Expense> findExpensesInvolvingUsers(Long payerId, Long participantId);

	// write custom query to fetch list of expenses by user id
	@Query("SELECT e FROM Expense e WHERE e.paidBy.id = :userId OR :userId IN (SELECT u.id FROM e.sharedWith u)")
	List<Expense> findExpensesByUserId(@Param("userId") Long userId);

	// dynamic query to find net balance between 2 users
	@Query(value = "SELECT SUM(case when e.paid_by_user_id = :userId then amount else 0 end) - SUM(case when e.paid_by_user_id = :otherUserId then amount else 0 end) "
			+ "FROM expenses e " + "JOIN expense_shared es ON e.id = es.expense_id "
			+ "WHERE es.user_id = :userId OR es.user_id = :otherUserId OR e.paid_by_user_id = :userId OR e.paid_by_user_id = :otherUserId", nativeQuery = true)
	Double findNetBalanceBetweenUsers(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);

}
