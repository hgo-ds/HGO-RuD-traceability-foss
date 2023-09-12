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

package org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.model.AssetAsBuiltEntity;
import org.eclipse.tractusx.traceability.common.model.SearchCriteria;
import org.eclipse.tractusx.traceability.common.model.SearchOperation;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
public class AssetAsBuildSpecification implements Specification<AssetAsBuiltEntity> {

    private SearchCriteria criteria;

    @Override // TODO: try to generify code
    public Predicate toPredicate(Root<AssetAsBuiltEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
            return builder.equal(
                    root.<String>get(criteria.getKey()).as(String.class),
                    (String) criteria.getValue());
        }
        if (criteria.getOperation().equals(SearchOperation.STARTS_WITH)) {
            return builder.like(
                    root.<String>get(criteria.getKey()),
                    criteria.getValue() + "%");
        }
        if (criteria.getOperation().equals(SearchOperation.AT_LOCAL_DATE)) {
            final LocalDate localDate = LocalDate.parse((String) criteria.getValue());
            Predicate startingFrom =  builder.greaterThanOrEqualTo(root.<LocalDateTime>get(criteria.getKey()),
                    LocalDateTime.of(localDate, LocalTime.MIN));
            Predicate endingAt =  builder.lessThanOrEqualTo(root.<LocalDateTime>get(criteria.getKey()),
                    LocalDateTime.of(localDate, LocalTime.MAX));
            return builder.and(startingFrom, endingAt);
        }
        return null;
    }

    public static Specification<AssetAsBuiltEntity> toSpecification(final List<AssetAsBuildSpecification> allSpecifications) {
        var specifications = Lists.newArrayList(allSpecifications);
        if(specifications.isEmpty()){
            return null;
        }
        Specification<AssetAsBuiltEntity> result = specifications.remove(0);
        specifications.forEach(result::and);

        return result;
    }
}
