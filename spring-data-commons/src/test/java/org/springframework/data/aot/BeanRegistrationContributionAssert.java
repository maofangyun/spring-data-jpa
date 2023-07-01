/*
 * Copyright 2022-2023 the original author or authors.
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
package org.springframework.data.aot;

import static org.mockito.Mockito.*;

import java.util.function.Consumer;

import org.assertj.core.api.AbstractAssert;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.aot.test.generate.TestGenerationContext;
import org.springframework.beans.factory.aot.BeanRegistrationAotContribution;
import org.springframework.beans.factory.aot.BeanRegistrationCode;

/**
 * @author Christoph Strobl
 */
public class BeanRegistrationContributionAssert
		extends AbstractAssert<BeanRegistrationContributionAssert, BeanRegistrationAotContribution> {

	protected BeanRegistrationContributionAssert(BeanRegistrationAotContribution beanRegistrationAotContribution) {
		super(beanRegistrationAotContribution, BeanRegistrationContributionAssert.class);
	}

	public static BeanRegistrationContributionAssert assertThatAotContribution(BeanRegistrationAotContribution actual) {
		return new BeanRegistrationContributionAssert(actual);
	}

	public BeanRegistrationContributionAssert codeContributionSatisfies(Consumer<CodeContributionAssert> assertWith) {

		BeanRegistrationCode mockBeanRegistrationCode = mock(BeanRegistrationCode.class);

		GenerationContext generationContext = new TestGenerationContext(Object.class);

		this.actual.applyTo(generationContext, mockBeanRegistrationCode);

		assertWith.accept(new CodeContributionAssert(generationContext));

		return this;
	}
}
