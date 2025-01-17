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
package bpn.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@ArraySchema(arraySchema = @Schema(description = "BPN Mappings", additionalProperties = Schema.AdditionalPropertiesValue.FALSE), maxItems = Integer.MAX_VALUE)
public record BpnEdcMappingResponse(@ApiModelProperty(example = "BPNL00000003CSGV") String bpn, @ApiModelProperty(example = "https://trace-x-test-edc.dev.demo.catena-x.net/a1") String url) {

    public String getBpn() {
        return bpn;
    }

    public String getUrl() {
        return url;
    }
}
