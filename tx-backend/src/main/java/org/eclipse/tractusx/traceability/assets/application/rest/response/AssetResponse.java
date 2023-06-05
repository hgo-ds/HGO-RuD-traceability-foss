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

package org.eclipse.tractusx.traceability.assets.application.rest.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.domain.model.Asset;
import org.eclipse.tractusx.traceability.common.model.PageResult;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Builder
@Data
@ArraySchema(arraySchema = @Schema(description = "Assets"), maxItems = Integer.MAX_VALUE)
public class AssetResponse {
    @ApiModelProperty(example = "urn:uuid:ceb6b964-5779-49c1-b5e9-0ee70528fcbd")
    private String id;
    @ApiModelProperty(example = "--")
    private String idShort;
    @ApiModelProperty(example = "--")
    private String semanticModelId;
    @ApiModelProperty(example = "BPNL00000003CSGV")
    private String manufacturerId;
    @ApiModelProperty(example = "Tier C")
    private String manufacturerName;
    @ApiModelProperty(example = "CUSTOMER")
    private OwnerResponse owner;
    @ArraySchema(arraySchema = @Schema(description = "Child relationships"), maxItems = Integer.MAX_VALUE)
    private List<DescriptionsResponse> childRelations;
    @ArraySchema(arraySchema = @Schema(description = "Parent relationships"), maxItems = Integer.MAX_VALUE)
    private List<DescriptionsResponse> parentRelations;

    // here latest work - check why there is no activeAlert inside of the response

    @ApiModelProperty(example = "false")
    private boolean underInvestigation;
    @ApiModelProperty(example = "Ok")
    private QualityTypeResponse qualityType;
    @ApiModelProperty(example = "--")
    private String van;

    public static AssetResponse from(final Asset asset) {
        return AssetResponse.builder()
                .id(asset.getId())
                .idShort(asset.getIdShort())
                .nameAtManufacturer(asset.getNameAtManufacturer())
                .manufacturerPartId(asset.getManufacturerPartId())
                .partInstanceId(asset.getPartInstanceId())
                .manufacturerId(asset.getManufacturerId())
                .batchId(asset.getBatchId())
                .manufacturerName(asset.getManufacturerName())
                .nameAtCustomer(asset.getNameAtCustomer())
                .customerPartId(asset.getCustomerPartId())
                .manufacturingDate(asset.getManufacturingDate())
                .manufacturingCountry(asset.getManufacturingCountry())
                .owner(OwnerResponse.from(asset.getOwner()))
                .childDescriptions(
                        asset.getChildDescriptions().stream()
                                .map(DescriptionsResponse::from)
                                .toList())
                .parentDescriptions(
                        asset.getParentDescriptions().stream()
                                .map(DescriptionsResponse::from)
                                .toList())
                .underInvestigation(asset.isUnderInvestigation())
                .qualityType(
                        QualityTypeResponse.from(asset.getQualityType())
                )
                .van(asset.getVan())
                .build();
    }

    public static PageResult<AssetResponse> from(final PageResult<Asset> assetPageResult) {
        return new PageResult<>(
                assetPageResult.content().stream()
                        .map(AssetResponse::from).toList(),
                assetPageResult.page(),
                assetPageResult.pageCount(),
                assetPageResult.pageSize(),
                assetPageResult.totalItems()
        );
    }

    public static List<AssetResponse> from(final List<Asset> assets) {
        return assets.stream()
                .map(AssetResponse::from)
                .toList();
    }
}
