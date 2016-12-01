/*
 * Source file of the Remove Duplicates project.
 * Copyright (c) 2016. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/2.0
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * https://github.com/pnemonic78/RemoveDuplicates
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
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
     * @param item     the item.
     */
    void onItemFetched(P provider, I item);
}
