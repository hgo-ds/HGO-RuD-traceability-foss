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

import {Injectable} from '@angular/core';
import {Pagination} from '@core/model/pagination.model';
import {PartsState} from '@page/parts/core/parts.state';
import {AssetAsBuiltFilter, AssetAsPlannedFilter, Part} from '@page/parts/model/parts.model';
import {TableHeaderSort} from '@shared/components/table/table.model';
import {View} from '@shared/model/view.model';
import {PartsService} from '@shared/service/parts.service';
import {Observable, Subject, Subscription} from 'rxjs';

@Injectable()
export class PartsFacade {
    private partsAsBuiltSubscription: Subscription;
    private partsAsPlannedSubscription: Subscription;
    private readonly unsubscribeTrigger = new Subject<void>();

    constructor(private readonly partsService: PartsService, private readonly partsState: PartsState) {
    }

    public get partsAsBuilt$(): Observable<View<Pagination<Part>>> {
        return this.partsState.partsAsBuilt$;
    }

    public get partsAsPlanned$(): Observable<View<Pagination<Part>>> {
        return this.partsState.partsAsPlanned$;
    }

    public setPartsAsBuilt(page = 0, pageSize = 50, sorting: TableHeaderSort[] = []): void {
        this.partsAsBuiltSubscription?.unsubscribe();
        this.partsAsBuiltSubscription = this.partsService.getPartsAsBuilt(page, pageSize, sorting).subscribe({
            next: data => (this.partsState.partsAsBuilt = {data}),
            error: error => (this.partsState.partsAsBuilt = {error}),
        });
    }

    public setPartsAsBuiltWithFilter(page = 0, pageSize = 50, sorting: TableHeaderSort[] = [], assetAsBuiltFilter: AssetAsBuiltFilter): void {
        this.partsAsBuiltSubscription?.unsubscribe();
        this.partsAsBuiltSubscription = this.partsService.getPartsAsBuiltWithFilter(page, pageSize, sorting, assetAsBuiltFilter).subscribe({
            next: data => (this.partsState.partsAsBuilt = {data}),
            error: error => (this.partsState.partsAsBuilt = {error}),
        });
    }

    public setPartsAsPlanned(page = 0, pageSize = 50, sorting: TableHeaderSort[] = []): void {
        this.partsAsPlannedSubscription?.unsubscribe();
        this.partsAsPlannedSubscription = this.partsService.getPartsAsPlanned(page, pageSize, sorting).subscribe({
            next: data => (this.partsState.partsAsPlanned = {data}),
            error: error => (this.partsState.partsAsPlanned = {error}),
        });
    }

    public setPartsAsPlannedWithFilter(page = 0, pageSize = 50, sorting: TableHeaderSort[] = [], assetAsPlannedFilter: AssetAsPlannedFilter): void {
        this.partsAsPlannedSubscription?.unsubscribe();
        this.partsAsPlannedSubscription = this.partsService.getPartsAsPlannedWithFilter(page, pageSize, sorting, assetAsPlannedFilter).subscribe({
            next: data => (this.partsState.partsAsPlanned = {data}),
            error: error => (this.partsState.partsAsPlanned = {error}),
        });
    }


    public unsubscribeParts(): void {
        this.partsAsBuiltSubscription?.unsubscribe();
        this.partsAsPlannedSubscription?.unsubscribe();
        this.unsubscribeTrigger.next();
    }
}
