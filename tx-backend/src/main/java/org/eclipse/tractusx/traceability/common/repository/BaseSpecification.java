/********************************************************************************
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.traceability.common.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.eclipse.tractusx.traceability.common.model.SearchCriteria;
import org.eclipse.tractusx.traceability.common.model.SearchOperation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class BaseSpecification {

    protected Predicate createPredicate(SearchCriteria criteria, Root<?> root, CriteriaBuilder builder) {
        if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
            return builder.equal(
                    root.<String>get(criteria.getKey()).as(String.class),
                    criteria.getValue());
        }
        if (criteria.getOperation().equals(SearchOperation.STARTS_WITH)) {
            return builder.like(
                    root.get(criteria.getKey()),
                    criteria.getValue() + "%");
        }
        if (criteria.getOperation().equals(SearchOperation.AT_LOCAL_DATE)) {
            final LocalDate localDate = LocalDate.parse(criteria.getValue());
            Predicate startingFrom = builder.greaterThanOrEqualTo(root.get(criteria.getKey()),
                    LocalDateTime.of(localDate, LocalTime.MIN));
            Predicate endingAt = builder.lessThanOrEqualTo(root.get(criteria.getKey()),
                    LocalDateTime.of(localDate, LocalTime.MAX));
            return builder.and(startingFrom, endingAt);
        }
        return null;
    }
}
