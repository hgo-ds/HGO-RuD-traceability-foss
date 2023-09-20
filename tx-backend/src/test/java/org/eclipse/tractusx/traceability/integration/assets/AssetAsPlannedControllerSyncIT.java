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
package org.eclipse.tractusx.traceability.integration.assets;

import io.restassured.http.ContentType;
import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.integration.common.support.AssetsSupport;
import org.eclipse.tractusx.traceability.integration.common.support.IrsApiSupport;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;

class AssetAsPlannedControllerSyncIT extends IntegrationTestSpecification {

    @Autowired
    IrsApiSupport irsApiSupport;

    @Autowired
    AssetsSupport assetsSupport;

    @Test
    void shouldSynchronizeAssets() throws JoseException, InterruptedException {
        //GIVEN
        oAuth2ApiSupport.oauth2ApiReturnsTechnicalUserToken();
        irsApiSupport.irsApiTriggerJobAsPlanned();
        irsApiSupport.irsApiReturnsJobDetailsAsPlannedDownward();

        //WHEN
        given()
                .contentType(ContentType.JSON)
                .body(
                        asJson(Map.of("globalAssetIds", List.of("urn:uuid:0733946c-59c6-41ae-9570-cb43a6e4da01"))
                        )
                )
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .when()
                .post("/api/assets/as-planned/sync")
                .then()
                .statusCode(200);

        //THEN
        eventually(() -> {
            try {
                assetsSupport.assertAssetAsPlannedSize(2);
                assetsSupport.assertHasAsPlannedChildCount("urn:uuid:0733946c-59c6-41ae-9570-cb43a6e4da01", 1);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        });

    }

    @Test
    void shouldSynchronizeAssetsUsingRetry() throws JoseException, InterruptedException {
        //GIVEN
        oAuth2ApiSupport.oauth2ApiReturnsTechnicalUserToken();
        irsApiSupport.irsApiTriggerJobAsPlanned();
        irsApiSupport.irsApiReturnsJobDetailsAsPlannedDownwardEmptyFirst();

        //WHEN
        given()
                .contentType(ContentType.JSON)
                .body(
                        asJson(Map.of("globalAssetIds", List.of("urn:uuid:0733946c-59c6-41ae-9570-cb43a6e4da01"))
                        )
                )
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .when()
                .post("/api/assets/as-planned/sync")
                .then()
                .statusCode(200);

        //THEN
        eventually(() -> {
            try {
                assetsSupport.assertAssetAsPlannedSize(2);
                irsApiSupport.verifyIrsApiTriggerJobCalledTimes(1);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Test
    void shouldNotSynchronizeAssetsWhenIrsFailedToReturnJobDetails() throws JoseException, InterruptedException {
        //GIVEN
        oAuth2ApiSupport.oauth2ApiReturnsTechnicalUserToken();
        irsApiSupport.irsApiTriggerJob();
        irsApiSupport.irsJobDetailsApiFailed();

        //WHEN
        given()
                .contentType(ContentType.JSON)
                .body(
                        asJson(Map.of("globalAssetIds", List.of("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb"))
                        )
                )
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .when()
                .post("/api/assets/as-planned/sync")
                .then()
                .statusCode(200);

        //THEN
        eventually(() -> {
            assetsSupport.assertNoAssetsStored();
            return true;
        });
    }

    @Test
    void shouldNotSynchronizeAssetsWhenIrsKeepsReturningJobInRunningState() throws JoseException, InterruptedException {
        //GIVEN
        oAuth2ApiSupport.oauth2ApiReturnsTechnicalUserToken();
        irsApiSupport.irsApiTriggerJob();
        irsApiSupport.irsApiReturnsJobInRunningState();

        //WHEN
        given()
                .contentType(ContentType.JSON)
                .body(
                        asJson(Map.of("globalAssetIds", List.of("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb"))
                        )
                )
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .when()
                .post("/api/assets/as-planned/sync")
                .then()
                .statusCode(200);

        //THEN
        eventually(() -> {
            assetsSupport.assertNoAssetsStored();
            return true;
        });
    }
}