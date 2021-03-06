/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.gateway.security.authentication;

import static org.eclipse.ditto.model.base.common.ConditionChecker.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.model.base.auth.AuthorizationContext;

/**
 * Default implementation of {@link AbstractAuthenticationResult}.
 */
@NotThreadSafe
public final class DefaultAuthenticationResult extends AbstractAuthenticationResult<AuthorizationContext> {

    private DefaultAuthenticationResult(@Nullable final AuthorizationContext authorizationContext,
            @Nullable final Throwable reasonOfFailure) {

        super(authorizationContext, reasonOfFailure);
    }

    /**
     * Initializes a successful authentication result with a found {@link AuthorizationContext}.
     *
     * @param authorizationContext the authorization context found by authentication.
     * @return a successfully completed authentication result containing the {@code given authorizationContext}.
     * @throws NullPointerException if {@code authorizationContext} is {@code null}.
     */
    public static DefaultAuthenticationResult successful(final AuthorizationContext authorizationContext) {
        return new DefaultAuthenticationResult(checkNotNull(authorizationContext, "AuthorizationContext"), null);
    }

    /**
     * Initializes a result of a failed authentication.
     *
     * @param reasonOfFailure the reason of the authentication failure.
     * @return a failed authentication result containing the {@code given reasonOfFailure}.
     * @throws NullPointerException if {@code reasonOfFailure} is {@code null}.
     */
    public static DefaultAuthenticationResult failed(final Throwable reasonOfFailure) {
        return new DefaultAuthenticationResult(null, checkNotNull(reasonOfFailure, "reason of failure"));
    }

}
