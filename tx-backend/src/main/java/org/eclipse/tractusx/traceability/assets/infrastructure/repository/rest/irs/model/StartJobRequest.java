/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.assets.infrastructure.repository.rest.irs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StartJobRequest(
        List<String> aspects,
        String globalAssetId,
        boolean collectAspects,
        BomLifecycle bomLifecycle,
        boolean lookupBPNs,
        int depth,
        Direction direction
) {
    public static StartJobRequest buildJobRequest(String globalAssetId, Direction direction, List<String> aspects) {
        return new StartJobRequest(aspects, globalAssetId, true, BomLifecycle.AS_BUILT, true, DEFAULT_DEPTH, direction);
    }

    public static final int DEFAULT_DEPTH = 2;
}


enum BomLifecycle {
    @JsonProperty("asBuilt")
    AS_BUILT,
    @JsonProperty("asPlanned")
    AS_PLANNED
}
