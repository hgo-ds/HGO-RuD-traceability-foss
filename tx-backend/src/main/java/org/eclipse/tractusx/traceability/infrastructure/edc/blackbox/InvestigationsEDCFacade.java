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
package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.eclipse.edc.catalog.spi.CatalogRequest;
import org.eclipse.edc.spi.query.Criterion;
import org.eclipse.edc.spi.query.QuerySpec;
import org.eclipse.edc.spi.types.domain.edr.EndpointDataReference;
import org.eclipse.tractusx.irs.edc.client.ContractNegotiationService;
import org.eclipse.tractusx.irs.edc.client.EDCCatalogFacade;
import org.eclipse.tractusx.irs.edc.client.EndpointDataReferenceStorage;
import org.eclipse.tractusx.irs.edc.client.model.CatalogItem;
import org.eclipse.tractusx.irs.edc.client.policy.PolicyCheckerService;
import org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model.EDCNotification;
import org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model.EDCNotificationFactory;
import org.eclipse.tractusx.traceability.infrastructure.edc.properties.EdcProperties;
import org.eclipse.tractusx.traceability.qualitynotification.domain.model.QualityNotificationMessage;
import org.eclipse.tractusx.traceability.qualitynotification.domain.model.QualityNotificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.configuration.JsonLdConfigurationTraceX.NAMESPACE_EDC;
import static org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.transferprocess.TransferProcessRequest.DEFAULT_PROTOCOL;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvestigationsEDCFacade {

    private static final MediaType JSON = MediaType.get("application/json");

    private final HttpCallService httpCallService;

    private final ObjectMapper objectMapper;

    private final EdcProperties edcProperties;

    private final EDCCatalogFacade edcCatalogFacade;
    private final ContractNegotiationService contractNegotiationService;
    private final EndpointDataReferenceStorage endpointDataReferenceStorage;
    private final PolicyCheckerService policyCheckerService;

    public static final String ASSET_VALUE_QUALITY_INVESTIGATION = "qualityinvestigation";
    public static final String ASSET_VALUE_QUALITY_ALERT = "qualityalert";
    private static final String ASSET_VALUE_NOTIFICATION_METHOD_UPDATE = "update";
    private static final String ASSET_VALUE_NOTIFICATION_METHOD_RECEIVE = "receive";

    public void startEdcTransferNew(
            final QualityNotificationMessage notification,
            final String receiverEdcUrl,
            final String senderEdcUrl) {

        CatalogItem catalogItem = getCatalogItem(notification, receiverEdcUrl);

        String contractAgreementId = negotiateContractAgreement(receiverEdcUrl, catalogItem);

        final EndpointDataReference dataReference = endpointDataReferenceStorage.remove(contractAgreementId)
                .orElseThrow(() -> new NoEndpointDataReferenceException("No EndpointDataReference was found"));

        notification.setContractAgreementId(contractAgreementId);
        notification.setEdcUrl(receiverEdcUrl);

        try {
            Request notificationRequest = buildNotificationRequestNew(notification, senderEdcUrl, dataReference);
            httpCallService.sendRequest(notificationRequest);
        } catch (Exception e) {
            throw new SendNotificationException("Failed to send notification.", e);
        }
    }

    private String negotiateContractAgreement(final String receiverEdcUrl, final CatalogItem catalogItem) {
        try {
            return Optional.ofNullable(contractNegotiationService.negotiate(receiverEdcUrl + edcProperties.getIdsPath(), catalogItem))
                    .orElseThrow(() -> new ContractNegotiationException("Failed to negotiate contract agreement. Negotiation response is null"))
                    .getContractAgreementId();
        } catch (Exception e) {
            throw new ContractNegotiationException("Failed to negotiate contract agreement. ", e);
        }
    }

    private CatalogItem getCatalogItem(final QualityNotificationMessage notification, final String receiverEdcUrl) {
        try {
            final String propertyNotificationTypeValue = QualityNotificationType.ALERT.equals(notification.getType()) ? ASSET_VALUE_QUALITY_ALERT : ASSET_VALUE_QUALITY_INVESTIGATION;
            final String propertyMethodValue = Boolean.TRUE.equals(notification.getIsInitial()) ? ASSET_VALUE_NOTIFICATION_METHOD_RECEIVE : ASSET_VALUE_NOTIFICATION_METHOD_UPDATE;
            return edcCatalogFacade.fetchCatalogItems(
                            CatalogRequest.Builder.newInstance()
                                    .protocol(DEFAULT_PROTOCOL)
                                    .providerUrl(receiverEdcUrl + edcProperties.getIdsPath())
                                    .querySpec(QuerySpec.Builder.newInstance()
                                            .filter(
                                                    List.of(new Criterion(NAMESPACE_EDC + "notificationtype", "=", propertyNotificationTypeValue),
                                                            new Criterion(NAMESPACE_EDC + "notificationmethod", "=", propertyMethodValue))
                                            )
                                            .build())
                                    .build()
                    ).stream()
                    .filter(catalogItem -> policyCheckerService.isValid(catalogItem.getPolicy()))
                    .findFirst()
                    .orElseThrow(NoCatalogItemException::new);
        } catch (Exception e) {
            log.error("Exception was thrown while requesting catalog items from Lib", e);
            throw new NoCatalogItemException(e);
        }
    }

    private Request buildNotificationRequestNew(
            final QualityNotificationMessage notification,
            final String senderEdcUrl,
            final EndpointDataReference dataReference
    ) throws JsonProcessingException {
        EDCNotification edcNotification = EDCNotificationFactory.createEdcNotification(senderEdcUrl, notification);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = objectMapper.writeValueAsString(edcNotification);
        HttpUrl url = httpCallService.getUrl(dataReference.getEndpoint(), null, null);
        log.info(":::: Send notification Data  body :{}, dataReferenceEndpoint :{}", body, dataReference.getEndpoint());
        return new Request.Builder()
                .url(url)
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode())
                .addHeader("Content-Type", JSON.type())
                .post(RequestBody.create(body, JSON))
                .build();
    }

}
