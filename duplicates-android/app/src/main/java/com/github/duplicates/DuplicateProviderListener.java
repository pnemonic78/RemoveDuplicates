/*
 * Copyright 2016, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.duplicates;

/**
 * Listener for provider events.
 *
 * @author moshe.w
 */
public interface DuplicateProviderListener<I extends DuplicateItem, P extends DuplicateProvider<I>> {

    /**
     * Notification that the provider has fetched an item.
     *
     * @param provider the provider.
     * @param count    the number of items fetched.
     * @param item     the item.
     */
    void onItemFetched(P provider, int count, I item);

    /**
     * Notification that the provider has deleted an item.
     *
     * @param provider the provider.
     * @param count    the number of items deleted.
     * @param item     the item.
     */
    void onItemDeleted(P provider, int count, I item);

    /**
     * Notification that the provider has deleted an item.
     *
     * @param provider the provider.
     * @param count    the number of items deleted.
     * @param pair     the items pair.
     */
    void onPairDeleted(P provider, int count, DuplicateItemPair<I> pair);
}
