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

package org.eclipse.tractusx.traceability.test.tooling.rest.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@SuperBuilder
@Jacksonized
public class QualityNotificationResponse {
    private Long id;

    private QualityNotificationStatusResponse status;

    private String description;
    private String createdBy;

    private String createdByName;
    private String createdDate;

    private List<String> assetIds;

    private QualityNotificationSideResponse channel;

    private QualityNotificationReasonResponse reason;

    private String sendTo;

    private String sendToName;

    private QualityNotificationSeverityResponse severity;

    private String targetDate;
}
