package com.example.demo.specification

import com.example.demo.model.Client
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.util.*

class ClientSpecification(private val searchMap: Map<String, String>) : Specification<Client> {

    override fun toPredicate(root: Root<Client>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        if (searchMap.isEmpty()) {
            return null
        }

        val predicates = mutableListOf<Predicate>()

        for ((key, value) in searchMap) {
            val field = root.get<String>(key)
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(field), "%${value.lowercase(Locale.getDefault())}%"))
        }

        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}