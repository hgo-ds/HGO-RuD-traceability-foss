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

package org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.semanticdatamodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.domain.base.model.*;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
public class SemanticDataModel {

    PartTypeInformation partTypeInformation;
    ManufacturingInformation manufacturingInformation;
    List<LocalId> localIdentifiers;
    ValidityPeriod validityPeriod;
    List<Site> sites;
    String aspectType;
    private String catenaXId;

    public SemanticDataModel(
            String catenaXId,
            PartTypeInformation partTypeInformation,
            ManufacturingInformation manufacturingInformation,
            List<LocalId> localIdentifiers,
            ValidityPeriod validityPeriod,
            List<Site> sites,
            String aspectType) {
        this.catenaXId = catenaXId;
        this.partTypeInformation = partTypeInformation;
        this.manufacturingInformation = manufacturingInformation;
        this.localIdentifiers = Objects.requireNonNullElse(localIdentifiers, Collections.emptyList());
        this.validityPeriod = validityPeriod;
        this.sites = Objects.requireNonNullElse(sites, Collections.emptyList());
        this.aspectType = aspectType;
    }

    public Optional<String> getLocalId(LocalIdKey key) {
        return emptyIfNull(localIdentifiers).stream()
                .filter(localId -> localId.key() == key)
                .findFirst()
                .map(LocalId::value);
    }

    public Optional<String> getLocalIdByInput(LocalIdKey key, List<LocalId> localIds) {
        return localIds.stream()
                .filter(localId -> localId.key() == key)
                .findFirst()
                .map(LocalId::value);
    }

    public AssetBase toDomain(List<LocalId> localIds, Map<String, String> shortIds, Owner owner, Map<String, String> bpns, List<Descriptions> parentRelations, List<Descriptions> childRelations) {
        final String manufacturerName = bpns.get(manufacturerId());

        final AtomicReference<String> semanticModelId = new AtomicReference<>();
        final AtomicReference<org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel> semanticDataModel = new AtomicReference<>();

        getLocalIdByInput(LocalIdKey.PART_INSTANCE_ID, localIds).ifPresent(s -> {
            semanticModelId.set(s);
            semanticDataModel.set(org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.SERIALPART);
        });

        getLocalIdByInput(LocalIdKey.BATCH_ID, localIds).ifPresent(s -> {
            semanticModelId.set(s);
            semanticDataModel.set(org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.BATCH);
        });

        getLocalIdByInput(LocalIdKey.JIS_NUMBER, localIds).ifPresent(s -> {
            semanticModelId.set(s);
            semanticDataModel.set(org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.JUSTINSEQUENCE);
        });


        if (semanticDataModel.get() == null) {
            semanticDataModel.set(org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.UNKNOWN);
        }

        return AssetBase.builder()
                .id(catenaXId())
                .idShort(defaultValue(shortIds.get(catenaXId())))
                .semanticModelId(semanticModelId.get())
                .semanticModel(SemanticModel.from(partTypeInformation, manufacturingInformation))
                .manufacturerId(manufacturerId())
                .manufacturerName(defaultValue(manufacturerName))
                .parentRelations(parentRelations)
                .childRelations(childRelations)
                .owner(owner)
                .activeAlert(false)
                .underInvestigation(false)
                .classification(partTypeInformation.classification())
                .qualityType(QualityType.OK)
                .semanticDataModel(semanticDataModel.get())
                .van(van())
                .build();
    }

    public AssetBase toDomainAsPlanned(Map<String, String> shortIds, Owner owner, Map<String, String> bpns, List<Descriptions> parentRelations, List<Descriptions> childRelations) {
        final String manufacturerName = bpns.get(manufacturerId());
        final String[] manufacturerId = {"--"};
        bpns.values().stream().filter(s -> s.equals(manufacturerName)).findFirst().ifPresent(s -> manufacturerId[0] = s);

        return AssetBase.builder()
                .id(catenaXId())
                .idShort(defaultValue(shortIds.get(catenaXId())))
                .semanticModel(SemanticModel.from(partTypeInformation))
                .manufacturerId(manufacturerId[0])
                .manufacturerName(defaultValue(manufacturerName))
                .parentRelations(parentRelations)
                .childRelations(childRelations)
                .owner(owner)
                .activeAlert(false)
                .classification(partTypeInformation.classification())
                .underInvestigation(false)
                .qualityType(QualityType.OK)
                .semanticDataModel(org.eclipse.tractusx.traceability.assets.domain.base.model.SemanticDataModel.PARTASPLANNED)
                .van(van())
                .build();
    }

    private String manufacturerId() {
        return getLocalId(LocalIdKey.MANUFACTURER_ID)
                .orElse("--");
    }


    private String defaultValue(String value) {
        final String EMPTY_TEXT = "--";
        if (!StringUtils.hasText(value)) {
            return EMPTY_TEXT;
        }
        return value;
    }

    private String van() {
        final String EMPTY_TEXT = "--";
        return getLocalId(LocalIdKey.VAN)
                .orElse(EMPTY_TEXT);
    }

    public String catenaXId() {
        return catenaXId;
    }

    public PartTypeInformation partTypeInformation() {
        return partTypeInformation;
    }

    public ManufacturingInformation manufacturingInformation() {
        return manufacturingInformation;
    }

    public List<LocalId> localIdentifiers() {
        return localIdentifiers;
    }

    public List<Site> sites() {
        return sites;
    }

    public String aspectType() {
        return aspectType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SemanticDataModel) obj;
        return Objects.equals(this.catenaXId, that.catenaXId) &&
                Objects.equals(this.partTypeInformation, that.partTypeInformation) &&
                Objects.equals(this.manufacturingInformation, that.manufacturingInformation) &&
                Objects.equals(this.localIdentifiers, that.localIdentifiers) &&
                Objects.equals(this.validityPeriod, that.validityPeriod) &&
                Objects.equals(this.sites, that.sites) &&
                Objects.equals(this.aspectType, that.aspectType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catenaXId, partTypeInformation, manufacturingInformation, localIdentifiers, validityPeriod, sites, aspectType);
    }

    @Override
    public String toString() {
        return "SemanticDataModel[" +
                "catenaXId=" + catenaXId + ", " +
                "partTypeInformation=" + partTypeInformation + ", " +
                "manufacturingInformation=" + manufacturingInformation + ", " +
                "localIdentifiers=" + localIdentifiers + ", " +
                "validityPeriod=" + validityPeriod + ", " +
                "sites=" + sites + ", " +
                "aspectType=" + aspectType + ']';
    }

    public boolean isAsPlanned() {
        return aspectType.contains("AsPlanned");
    }

    public boolean isAsBuilt() {
        return !aspectType.contains("AsPlanned");
    }


}

