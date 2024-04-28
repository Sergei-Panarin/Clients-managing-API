package com.example.demo.specification

import com.example.demo.model.Client
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.util.*

class ClientSpecificationAdvanced(private val searchString: String) : Specification<Client> {

    override fun toPredicate(root: Root<Client>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        if (searchString.isBlank()) {
            return null
        }

        val words = searchString.split(" ").map { it.lowercase(Locale.getDefault()) }
        val predicates = mutableListOf<Predicate>()

        for (word in words) {
            val wordPredicate = "%$word%"
            val firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), wordPredicate)
            val lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), wordPredicate)
            predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate))
        }

        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}
