/*
 * Copyright 2008-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.integration;

import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.jpa.domain.sample.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link JpaRepository}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Jens Schauder
 * @author Greg Turnquist
 * @author Krzysztof Krason
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:infrastructure.xml" })
@Transactional
class IntegrationTest {

	@PersistenceContext EntityManager em;

	private QuerydslJpaRepository<User, Integer> repository;
	private QUser user = new QUser("user");
	private User dave;
	private User carter;
	private User oliver;
	private Role adminRole;

	@BeforeEach
	void setUp() {
		JpaEntityInformation<User, Integer> information = new JpaMetamodelEntityInformation<>(User.class, em.getMetamodel(),
				em.getEntityManagerFactory().getPersistenceUnitUtil());

		repository = new QuerydslJpaRepository<>(information, em);
		dave = repository.save(new User("Dave", "Matthews", "dave@matthews.com"));
		carter = repository.save(new User("Carter", "Beauford", "carter@beauford.com"));
		oliver = repository.save(new User("Oliver", "matthews", "oliver@matthews.com"));
		adminRole = em.merge(new Role("admin"));
	}

	@Test
	void executesPredicatesCorrectly() {

		BooleanExpression isCalledDave = user.firstname.eq("Dave");
		BooleanExpression isBeauford = user.lastname.eq("Beauford");

		List<User> result = repository.findAll(isCalledDave.or(isBeauford));

		assertThat(result).containsExactlyInAnyOrder(carter, dave);
	}


}
