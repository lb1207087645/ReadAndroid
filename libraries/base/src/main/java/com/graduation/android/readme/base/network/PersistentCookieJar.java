/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.graduation.android.readme.base.network;


import com.graduation.android.readme.base.core.ClearableCookieJar;
import com.graduation.android.readme.base.core.CookieCache;
import com.graduation.android.readme.base.core.CookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import static okhttp3.internal.Util.verifyAsIpAddress;

public class PersistentCookieJar implements ClearableCookieJar {

    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;

        this.cache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cache.addAll(cookies);
        persistor.saveAll(filterPersistentCookies(cookies));
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
        List<Cookie> persistentCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            if (cookie.persistent()) {
                persistentCookies.add(cookie);
            }
        }
        return persistentCookies;
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookiesToRemove = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie);
                it.remove();

            } else if (matches(currentCookie, url)) {
                validCookies.add(currentCookie);
            }
        }

        persistor.removeAll(cookiesToRemove);

        return validCookies;
    }

    private boolean matches(Cookie currentCookie, HttpUrl url) {
        boolean domainMatch = currentCookie.hostOnly()
                ? url.host().equals(currentCookie.domain())
                : domainMatch(url.host(), currentCookie.domain());
        if (!domainMatch) {
            return false;
        }
        return pathMatch(url, currentCookie.path());
    }

    private boolean domainMatch(String urlHost, String domain) {
        if (urlHost.equals(domain)) {
            return true; // As in 'example.com' matching 'example.com'.
        }

        if (urlHost.endsWith(domain)
                && urlHost.charAt(urlHost.length() - domain.length() - 1) == '.'
                && !verifyAsIpAddress(urlHost)) {
            return true; // As in 'example.com' matching 'www.example.com'.
        }

        return false;
    }

    private boolean pathMatch(HttpUrl url, String path) {
        String urlPath = url.encodedPath();

        if (urlPath.equals(path)) {
            return true; // As in '/foo' matching '/foo'.
        }

        if (urlPath.startsWith(path)) {
            if (path.endsWith("/")) {
                return true; // As in '/' matching '/foo'.
            }
            return urlPath.charAt(path.length()) == '/';
        }
        return false;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    synchronized public void clearSession() {
        cache.clear();
        cache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void clear() {
        cache.clear();
        persistor.clear();
    }
}
